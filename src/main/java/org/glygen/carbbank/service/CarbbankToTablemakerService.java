package org.glygen.carbbank.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.glygen.carbbank.dao.BSRepository;
import org.glygen.carbbank.dao.CarbbankRepository;
import org.glygen.carbbank.dao.MappingCNRepository;
import org.glygen.carbbank.dao.MappingCRepository;
import org.glygen.carbbank.dao.MappingCelllineRepository;
import org.glygen.carbbank.dao.MappingDiseaseRepository;
import org.glygen.carbbank.dao.MappingDomainRepository;
import org.glygen.carbbank.dao.MappingFRepository;
import org.glygen.carbbank.dao.MappingGSRepository;
import org.glygen.carbbank.dao.MappingKRepository;
import org.glygen.carbbank.dao.MappingORepository;
import org.glygen.carbbank.dao.MappingOTRepository;
import org.glygen.carbbank.dao.MappingP_DRepository;
import org.glygen.carbbank.dao.PublicationRepository;
import org.glygen.carbbank.dao2.RemoteStructureRepository;
import org.glygen.carbbank.glycomedb.GlycoCTStructure;
import org.glygen.carbbank.glycomedb.RemoteStructure;
import org.glygen.carbbank.model.BS;
import org.glygen.carbbank.model.CarbbankGlycan;
import org.glygen.carbbank.model.CarbbankRecord;
import org.glygen.carbbank.model.Species;
import org.glygen.carbbank.model.mapping.MappingBS_C;
import org.glygen.carbbank.model.mapping.MappingCN;
import org.glygen.carbbank.model.mapping.MappingCellLine;
import org.glygen.carbbank.model.mapping.MappingDisease;
import org.glygen.carbbank.model.mapping.MappingDomain;
import org.glygen.carbbank.model.mapping.MappingF;
import org.glygen.carbbank.model.mapping.MappingGS;
import org.glygen.carbbank.model.mapping.MappingK;
import org.glygen.carbbank.model.mapping.MappingO;
import org.glygen.carbbank.model.mapping.MappingOT;
import org.glygen.carbbank.model.mapping.MappingP_D;
import org.glygen.carbbank.model.mapping.Publication;
import org.glygen.carbbank.model.tablemaker.CollectionType;
import org.glygen.carbbank.model.tablemaker.CollectionView;
import org.glygen.carbbank.model.tablemaker.DatasetInputView;
import org.glygen.carbbank.model.tablemaker.Datatype;
import org.glygen.carbbank.model.tablemaker.Glycan;
import org.glygen.carbbank.model.tablemaker.License;
import org.glygen.carbbank.model.tablemaker.Metadata;
import org.glygen.carbbank.model.tablemaker.PublicationView;
import org.glygen.carbbank.parser.PubmedUtil;
import org.glygen.carbbank.util.TableMakerAPI;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CarbbankToTablemakerService {
	
	@Value("${ncbi.api-key}")
	String apiKey;
	
	@Value("${tablemaker.api-url}")
    String apiUrl;
    
	@Value("${tablemaker.password}")
    String password;
    
    @Value("${tablemaker.user-id}")
    String userId;
    
    @Value("${tablemaker.version}")
    String version;
    
    TableMakerAPI tablemaker;
    
	static Logger logger = org.slf4j.LoggerFactory.getLogger(CarbbankToTablemakerService.class);
	
	String contributorString = "createdBy:Sena Arpinar (University of Georgia)|"
			+ "curatedBy:Aise Arpinar|"
			+ "curatedBy:Nahom Abel|"
			+ "curatedBy:Harivinay Prasad Reddy Gujjula (George Washington University)|"
			+ "contributedBy:Rene Ranzinger (University of Georgia)|"
			+ "createdWith:GlyTableMaker (https://glygen.ccrc.uga.edu/tablemaker)";
	
	Map<String, String> carbIdErrorMap = new HashMap<>();
	
	//List<Publication> allPublications = null;
 	
	final CarbbankRepository structureRepository;
	final BSRepository biologicalRepository;
	final PublicationRepository publicationRepository;
	final RemoteStructureRepository remoteRepository;
	
	final MappingDiseaseRepository mappingDiseaseRepository;
	final MappingCelllineRepository mappingCelllineRepository;
	private final MappingOTRepository mappingTissueRepository;
	private final MappingGSRepository mappingGSRepository;
	private final MappingCNRepository mappingCNRepository;
	private final MappingFRepository mappingFRepository;
	private final MappingORepository mappingORepository;
	private final MappingCRepository mappingCRepository;
	private final MappingP_DRepository mappingP_DRepository;
	private final MappingKRepository mappingKRepository;
	private final MappingDomainRepository mappingDomainRepository;
	
	
	public CarbbankToTablemakerService(CarbbankRepository structureRepository, PublicationRepository publicationRepository, 
			BSRepository biologicalRepository, RemoteStructureRepository remoteRepository, 
			MappingOTRepository mappingTissueRepository, MappingGSRepository mappingSpeciesRepository, 
			MappingCNRepository mappingCNRepository, MappingDiseaseRepository mappingDiseaseRepository, 
			MappingCelllineRepository mappingCelllineRepository, MappingP_DRepository mappingP_DRepository, 
			MappingORepository mappingORepository, MappingKRepository mappingKRepository, 
			MappingFRepository mappingFRepository, MappingCRepository mappingCRepository, MappingDomainRepository mappingDomainRepository) {
		this.structureRepository = structureRepository;
		this.biologicalRepository = biologicalRepository;
		this.publicationRepository = publicationRepository;
		this.remoteRepository = remoteRepository;
		this.mappingDiseaseRepository = mappingDiseaseRepository;
		this.mappingCelllineRepository = mappingCelllineRepository;
		this.mappingTissueRepository = mappingTissueRepository;
		this.mappingGSRepository = mappingSpeciesRepository;
		this.mappingCNRepository = mappingCNRepository;
		this.mappingFRepository = mappingFRepository;
		this.mappingORepository = mappingORepository;
		this.mappingCRepository = mappingCRepository;
		this.mappingP_DRepository = mappingP_DRepository;
		this.mappingKRepository = mappingKRepository;
		this.mappingDomainRepository = mappingDomainRepository;
	}
	
	public void createGlycans () {
		this.tablemaker = TableMakerAPI.getInstance();
		this.tablemaker.setApiURL(apiUrl);
		this.tablemaker.setUserName(userId);
 		this.tablemaker.setPassword(password);
		List<CarbbankRecord> structures = structureRepository.findAll();
		Set<String> notAdded = new HashSet<>();
		int count = 0;
		int totalProcessed = 0;
		int notFoundinGlytoucan = 0;
		Set<String> resourceNotFound = new HashSet<>();
		Map<String, String> processed = new HashMap<>();
		for (CarbbankRecord str: structures) {
			boolean updated = false;
			String carbId = str.getCC().substring(5);
			count++;
			if (count % 100 == 0) {
				logger.info ("processing :" + count);
			}
			try {
				Optional<RemoteStructure>  handle = remoteRepository.findByResourceIdWithStructures(carbId.trim());
				if (handle.isPresent() && !handle.get().getStructures().isEmpty()) {
					totalProcessed++;
					RemoteStructure remote = handle.get();
					
					if (str.getGlycans() == null || str.getGlycans().isEmpty()) {
						updated = true;
						str.setGlycans(new ArrayList<>());
						for (GlycoCTStructure structure: remote.getStructures()) {
							CarbbankGlycan glycan = new CarbbankGlycan();
							glycan.setGlycoCT(structure.getGlycoCT());
							glycan.setRecord(str);
							if (!str.getGlycans().contains(glycan)) {
								str.getGlycans().add(glycan);
								if (processed.get(structure.getGlycoCT()) != null) {
									glycan.setGlytoucanId(processed.get(structure.getGlycoCT()));
								}
							}
						}
					}
					for (CarbbankGlycan glycan: str.getGlycans()) {
						if (processed.get(glycan.getGlycoCT()) == null) {	
							try {
								String glytoucanId = this.tablemaker.addGlycanGlycoCT(glycan.getGlycoCT(), glycan.getGlytoucanId());
								if (glytoucanId != null && glytoucanId.length() < 15) {
									processed.put(glycan.getGlycoCT(), glytoucanId);
									if (glycan.getGlytoucanId() == null) {
										updated = true;
										glycan.setGlytoucanId(glytoucanId);
									}
									logger.debug(str.getCC() + " already in glytoucan");
								} else {
									notFoundinGlytoucan++;
								}
							} catch (Exception e) {
								if (e instanceof HttpClientErrorException) {
									if (((HttpClientErrorException) e).getStatusCode() == HttpStatus.NOT_FOUND) {
										logger.error("glycan cannot be found in tablemaker although it was reported as a duplicate: " + e);
									} else {
										notAdded.add(str.getCC() + ":" + e.getMessage() + "\n" + glycan.getGlycoCT());
										logger.error("could not add glycan: " + str.getCC() + " Reason: " + e);
									}
								} else {
									notAdded.add(str.getCC() + ":" + e.getMessage() + "\n" + glycan.getGlycoCT());
									logger.error("could not add glycan: " + str.getCC() + " Reason: " + e);
								}
								
							}
						}
					}
					if (updated) structureRepository.save(str);
				} else {
					resourceNotFound.add(str.getCC());
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(carbId + " failed. ");
			}
		}
		logger.info ("Total records: " + count );
		logger.info ("Structures with existing glycoCT:" + totalProcessed);
		logger.info ("No structure info found in GlycomeDB: " + resourceNotFound.size());
		logger.info ("Not found in GlyTouCan: " + notFoundinGlytoucan);
		logger.info ("Error adding glycan: " + notAdded.size());
		logger.info ("Processed unique total: " + processed.size());
		 
        try {
        	String filePath = "glycan_errorlog.txt"; 
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
			writer.write(String.join("\n", notAdded));
			writer.close();
			
			filePath = "resourceNotFound.txt"; 
			writer = new BufferedWriter(new FileWriter(filePath, false));
			writer.write(String.join("\n", resourceNotFound));
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createCollections () {
		this.tablemaker = TableMakerAPI.getInstance();
		this.tablemaker.setApiURL(apiUrl);
		this.tablemaker.setUserName(userId);
		this.tablemaker.setPassword(password);
		PubmedUtil util = new PubmedUtil(apiKey);
		Map<String, String> speciesConflicts = new HashMap<>();
		List<CarbbankRecord> structures = structureRepository.findAll();
		List<CollectionView> addedCollections = new ArrayList<CollectionView>();
		List<PublicationView> publications = new ArrayList<>();
		StringBuffer notes = new StringBuffer();
		
		int count=0;
		for (CarbbankRecord str: structures) {
			if (str.getProcessed() != null && str.getProcessed()) {   
				//skip, already processed
				continue;
			}
			String carbId = str.getCC().substring(5);
			str.setProcessed(true);
			count++;
			if (count % 100 == 0) {
				logger.info ("processing :" + count);
			}
			for (CarbbankGlycan cb: str.getGlycans()) {
				if (cb.getGlytoucanId() == null || cb.getGlytoucanId().equalsIgnoreCase("null")) {
					logger.error("No glytoucanId for " + str.getCC());
					continue;
				}
				// find the glycan and use the id
				Long glycanId = null;
				try {
					glycanId = this.tablemaker.retrieveGlycanByGlytoucanId(cb.getGlytoucanId());
				} catch (Exception e) {
					logger.error("Exception retrieving glycan " + cb.getGlytoucanId(), e);
				}
				if (glycanId == null) {
					logger.error("Cannot find the glycan " + cb.getGlytoucanId() + " in tablemaker ");
					carbIdErrorMap.put(str.getCC(), "Cannot find the glycan " + cb.getGlytoucanId() + " in tablemaker. Please create glycans first!");
					continue;
				}
				
				Glycan glycan = new Glycan();
				glycan.setGlycanId(glycanId);
				glycan.setGlytoucanID(cb.getGlytoucanId());
				
				String contributor = contributorString;
				
				if (str.getSB() != null && !str.getSB().equalsIgnoreCase("unverified")) {
					String[] sbList = str.getSB().split(";");
					String additional = "";
					for (String cont: sbList) {
						String name = cont;
						if (cont.contains(",")) {
							name = cont.substring(0, cont.lastIndexOf(","));
						}
						additional += "curatedBy:" + name + "|";
					}
					contributor = additional + contributorString;
				}
				
				if (str.getBsList() == null || str.getBsList().isEmpty()) {
					CollectionView collection = new CollectionView();
					collection.setName(version+ str.getCC() + "-" + cb.getGlytoucanId());
					collection.setType(CollectionType.GLYCAN);
					collection.setGlycans(new ArrayList<>());
					collection.getGlycans().add(glycan);
					collection.setMetadata(new ArrayList<Metadata>());
					// only add publication/contributor and other top level metadata
					try {
						addEvidence(str, collection, publications, util);
						if (collection.getMetadata().isEmpty()) {
							logger.info ("No metadata for " + str.getCC());
							notes.append("Not including " +  str.getCC() + " since there is no metadata");
							carbIdErrorMap.put(str.getCC(), "There is no metadata");
							//str.setProcessed(false);
						} else {
							if (!addedCollections.contains(collection)) {
								String comment = "https://www.genome.jp/entry/ccsd+" + carbId; 
								addContributor(collection, contributor);
								addComment(collection, comment);
								String id = this.tablemaker.addCollection (collection);
								collection.setCollectionId(Long.parseLong(id));
								addedCollections.add(collection);
							}
						}
					} catch (Exception e) {
						carbIdErrorMap.put(str.getCC(), "Could not locate publication in database");
						str.setProcessed(false);
					}
					
				} else {
					try {
						// add a collection for each BS entry
						for (BS bio: str.getBsList()) {
							String comment = "https://www.genome.jp/entry/ccsd+" + carbId; 
							CollectionView collection = new CollectionView();
							collection.setName(version+ str.getCC() + "-" + cb.getGlytoucanId() + "-" + bio.getId());
							collection.setType(CollectionType.GLYCAN);
							collection.setGlycans(new ArrayList<>());
							collection.getGlycans().add(glycan);
							collection.setMetadata(new ArrayList<Metadata>());
							
							addMostSpecificSpeciesEntry (str, bio, collection, util, speciesConflicts);
							
							comment = addBSLine (bio, comment);
							
							if (bio.getDisease() != null) {
								// can have multiple values
								String disease = bio.getDisease();
								String[] diseases = disease.split(",");
								for (String d: diseases) {
									// find the mapping
									Optional<MappingDisease> mapping = mappingDiseaseRepository.findByNameIgnoreCase(d.trim());
									if (mapping.isPresent()) {
										String namespaceId = mapping.get().getNamespaceId();
										if (namespaceId != null) {
											Metadata metadata = new Metadata();
											Datatype datatype = new Datatype();
											datatype.setDatatypeId(7L);
											metadata.setType(datatype);
											metadata.setValueId(namespaceId);
											metadata.setValue(mapping.get().getNamespaceName());
											collection.getMetadata().add(metadata);
										}
									}
								}
							}
							
							if (bio.getOt() != null && bio.getCellline() != null) {
								//ignore these entries for now
							} else if (bio.getOt() != null) {
								// can have multiple values
								String tissue = bio.getOt();
								String[] tissues = tissue.split(",");
								boolean first = true;
								for (String t: tissues) {
									// find the mapping
									Optional<MappingOT> mapping = mappingTissueRepository.findByNameIgnoreCase(t.trim());
									if (mapping.isPresent()) {
										String namespaceId = mapping.get().getNamespaceId();
										if (namespaceId != null) {
											if (first) {
												collection.setName(version + str.getCC() + "-" + cb.getGlytoucanId() + "-" + bio.getId() + "-" + t.trim());
												Metadata metadata = new Metadata();
												Datatype datatype = new Datatype();
												datatype.setDatatypeId(5L);
												metadata.setType(datatype);
												metadata.setValueId(namespaceId);
												metadata.setValue(mapping.get().getNamespaceName());
												collection.getMetadata().add(metadata);
												try {
													addEvidence(str, collection, publications, util);
												} catch (Exception e) {
													carbIdErrorMap.put(str.getCC(), "Could not locate publication in database");
													str.setProcessed(false);
												}
												addContributor(collection, contributor);
												addComment(collection, comment);
												String id = this.tablemaker.addCollection (collection);
												collection.setCollectionId(Long.parseLong(id));
												addedCollections.add(collection);
												first = false;
												//addCelllines (collection, addedCollections, str, publications, contributor, util, bio.getCellline());
											} else {
												CollectionView collectionCopy = new CollectionView();
												collectionCopy.setName(version + str.getCC() + "-" + cb.getGlytoucanId() + "-" + bio.getId() + "-" + t.trim());
												collectionCopy.setType(CollectionType.GLYCAN);
												collectionCopy.setGlycans(collection.getGlycans());
												collectionCopy.setMetadata(new ArrayList<>());
												for (Metadata m: collection.getMetadata()) {
													if (m.getType().getDatatypeId() != 5L) {  // copy everything other than tissue
														collectionCopy.getMetadata().add(m);
													}
												}
												Metadata metadata = new Metadata();
												Datatype datatype = new Datatype();
												datatype.setDatatypeId(5L);
												metadata.setType(datatype);
												metadata.setValueId(namespaceId);
												metadata.setValue(mapping.get().getNamespaceName());
												collectionCopy.getMetadata().add(metadata);
												String id = this.tablemaker.addCollection (collectionCopy);
												collectionCopy.setCollectionId(Long.parseLong(id));
												addedCollections.add(collectionCopy);
												
												//addCelllines (collectionCopy, addedCollections, str, publications, contributor, util, bio.getCellline());
											}
										}
									}
								}
							} else if (bio.getCellline() != null) {
								try {
									addCelllines (collection, addedCollections, str, publications, contributor, util, bio.getCellline(), comment);
								} catch (Exception e) {
									carbIdErrorMap.put(str.getCC(), "Could not locate publication in database");
									str.setProcessed(false);
								}
							}
						
						
							if (collection.getMetadata().isEmpty()) {
								logger.info ("No metadata for " + str.getCC());
								notes.append("Not including " +  str.getCC() + " since there is no metadata");
								carbIdErrorMap.put(str.getCC(), "There is no metadata");
							} else {
								if (!addedCollections.contains(collection)) {
									addContributor(collection, contributor);
									addComment(collection, comment);
									try {
										addEvidence(str, collection, publications, util);
									} catch (Exception e) {
										carbIdErrorMap.put(str.getCC(), "Could not locate publication in database");
										str.setProcessed(false);
									}
									String id = this.tablemaker.addCollection (collection);
									collection.setCollectionId(Long.parseLong(id));
									addedCollections.add(collection);
								}
							}
						}
					} catch (IOException e) {
						logger.error(e.getMessage());
						carbIdErrorMap.put(str.getCC(), "Error getting info from NCBI. Reason: " + e.getMessage());
						str.setProcessed(false);
					}
				}

			}
			structureRepository.save(str);
		}
		
		// write carbIdErrorMap to a csv file
        String filePath = "excluded-list-withreasons.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("CarbID,Reason\n");
            for (Map.Entry<String, String> entry : carbIdErrorMap.entrySet()) {
                writer.append(entry.getKey())
                      .append(',')
                      .append(entry.getValue())
                      .append('\n');
            }
        } catch (IOException e) {
            logger.error("Error writing CSV file: " + e.getMessage());
        }	
        
        // write species conflicts to a csv file
        filePath = "speciesconflicts.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("BS Id,Conflict\n");
            for (Map.Entry<String, String> entry : speciesConflicts.entrySet()) {
                writer.append(entry.getKey())
                      .append(',')
                      .append(entry.getValue())
                      .append('\n');
            }
        } catch (IOException e) {
            logger.error("Error writing CSV file: " + e.getMessage());
        }	
	}
	
	public void createDataset () {
		// retrieve all collections created for user carbbank for the given version
		// retrieve all relevant publications from the collections
		// create the dataset
		this.tablemaker = TableMakerAPI.getInstance();
		this.tablemaker.setApiURL(apiUrl);
		this.tablemaker.setUserName(userId);
		this.tablemaker.setPassword(password);
		PubmedUtil util = new PubmedUtil(apiKey);
		List<PublicationView> publications = new ArrayList<>();
		List<CollectionView> collections = new ArrayList<>();
		List <String> processed = new ArrayList<>();
		try {
			for (int i=0; i < 45; i++) {
				List<CollectionView> pageCollections = tablemaker.getCollections(version, i, 1000);
				logger.info ("retrieved page " + i + " size: " + pageCollections.size());
				for (CollectionView col: pageCollections) {
					List<Metadata> metadata = col.getMetadata();
					for (Metadata meta: metadata) {
						if (meta.getType().getDatatypeId() == 2L) {   //evidence
							if (meta.getValue() != null) {
								if (!processed.contains(meta.getValue())) {
									processed.add(meta.getValue());
									Publication pub = new Publication();
									try {
										Long.parseLong(meta.getValue());
										pub.setPmid(meta.getValue());
									} catch (NumberFormatException e) {
										pub.setDoiId(meta.getValue());
									}
									try {
										PublicationView view = createPublicationView(pub, util);
										if (!publications.contains(view)) {
											publications.add(view);
										}
									} catch (Exception e) {
										logger.error("Could not retrieve publication for collection " + col.getName() + " Reason " + e);
										//try one more time
										try {
									        Thread.sleep(200); // wait 100 milliseconds between requests
									    } catch (InterruptedException ie) {
									        Thread.currentThread().interrupt(); // restore interrupted status
									    }
										try {
											PublicationView view = createPublicationView(pub, util);
											if (!publications.contains(view)) {
												publications.add(view);
											}
										} catch (Exception e1) {
											logger.error("Second try: could not retrieve publication for collection " + col.getName() + " Reason " + e1);
										}
									}
								}
								
							}
						}
					}
				}
				collections.addAll(pageCollections);
				logger.info("Processed collections page " + i);
			}
			logger.info ("Creating Carbbank dataset with " + collections.size() + " collections and " + publications.size() + " publications");
			createDataset(collections, publications, util);
		} catch (Exception e) {
			logger.error("error getting collections", e);
		}
	}
	
	
	void createDataset (List<CollectionView> collections, List<PublicationView> publications, PubmedUtil util) {
		if (!collections.isEmpty()) {
			DatasetInputView dataset = new DatasetInputView();
			dataset.setName("Carbbank Glycomics Data");
			//TODO get the description for Carbbank
			dataset.setDescription("The Complex Carbohydrate Structure Database (CCSD) and CarbBank "
					+ "were created to provide an information system to meet the needs of people interested in carbohydrate science. "
					+ "The CCSD aimed to collect all of the published structures of carbohydrates larger than disaccharides. "
					+ "The glycan data, "
					+ "including glycan structures, their biological annotation and literature references "
					+ "are archived in this GlyTableMaker dataset.");
			/*dataset.setDescription("The glycomics data generated by the Consortium for Functional Glycomics "
					+ "(CFG) encompasses a broad spectrum of experimental and curated datasets "
					+ "aimed at understanding the roles of glycans in biology. CFG has produced "
					+ "glycan datasets from glycan profiling, glycan array and phenotyping of glycogene "
					+ "mouse strains. The CFG also maintained detailed Molecule Pages that catalog "
					+ "glycan structures, glycan-binding proteins, and glycosyltransferases, providing a "
					+ "rich resource for glycoinformatics and structural glycobiology. The glycan data, "
					+ "including glycan structures, their biological annotation and literature references "
					+ "are archived in this GlyTableMaker dataset.");*/
			dataset.setLicense(new License());
			dataset.getLicense().setId(2L);
			dataset.getLicense().setName("CC BY 4.0");
			dataset.getLicense().setUrl("https://creativecommons.org/licenses/by/4.0/");
			dataset.getLicense().setCommercialUse(true);
			dataset.getLicense().setAttribution("You must give appropriate credit , provide a link to the license, and indicate if changes were made . You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.");
			dataset.getLicense().setDistribution("No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.");
			dataset.setCollections(collections);
			dataset.setPublications(publications);
			
			try {
				Publication associatedPaper = util.getPublicatonByPMID("1472756");
				dataset.setAssociatedPapers(new ArrayList<>());
				dataset.getAssociatedPapers().add(createPublicationView(associatedPaper, util));
				associatedPaper = util.getPublicatonByPMID("2623761");
				dataset.getAssociatedPapers().add(createPublicationView(associatedPaper, util));
			} catch (Exception ie) {
				logger.error("Could not find publication from PubMed ", ie);
			}
			
			this.tablemaker.publishDataset(dataset);
		}
		
	}
	
	private String addBSLine(BS bio, String comment) {
		comment += "\nBS:";
		if (bio.getBs() != null) {
			comment += " (BS) " + bio.getBs();
		}
		if (bio.getGs() != null) {
			comment += " (GS) " + bio.getGs();
		}
		if (bio.getC() != null) {
			comment += " (c) " + bio.getC();
		}
		if (bio.getCellline() != null) {
			comment += " (cell line) " + bio.getCellline();
		}
		if (bio.getCn() != null) {
			comment += " (CN) " + bio.getCn();
		}
		if (bio.getDisease() != null) {
			comment += " (disease) " + bio.getDisease();
		}
		if (bio.getDomain() != null) {
			comment += " (domain) " + bio.getDomain();
		}
		if (bio.getOt() != null) {
			comment += " (OT) " + bio.getOt();
		}
		if (bio.getO() != null) {
			comment += " (o) " + bio.getO();
		}
		if (bio.getF() != null) {
			comment += " (f) " + bio.getF();
		}
		if (bio.getK() != null) {
			comment += " (k) " + bio.getK();
		}
		if (bio.getPd() != null) {
			comment += " (p/d) " + bio.getPd();
		}
		if (bio.getStar() != null) {
			comment += " (*) " + bio.getStar();
		}
		if (bio.getGt() != null) {
			comment += " (GT) " + bio.getGt();
		}
		if (bio.getLs() != null) {
			comment += " (LS) " + bio.getLs();
		}
		return comment;
	}

	private void addComment(CollectionView collection, String comment) {
		Metadata metadata = new Metadata();
		Datatype datatype = new Datatype();
		datatype.setDatatypeId(17L);
		metadata.setType(datatype);
		metadata.setValue(comment);
		collection.getMetadata().add(metadata);
	}

	private void addCelllines(CollectionView collection, List<CollectionView> addedCollections, CarbbankRecord str, 
			List<PublicationView> publications, String contributor, PubmedUtil util, String cellline, String comment) throws Exception {
		// can have multiple values
		String[] celllines = cellline.split(",");
		boolean first = true;
		for (String t: celllines) {
			// find the mapping
			Optional<MappingCellLine> mapping = mappingCelllineRepository.findByNameIgnoreCase(t.trim());
			if (mapping.isPresent()) {
				String namespaceId = mapping.get().getNamespaceId();
				if (namespaceId != null) {
					if (first) {
						collection.setName(collection.getName() +  "-" + t.trim());
						Metadata metadata = new Metadata();
						Datatype datatype = new Datatype();
						datatype.setDatatypeId(5L);
						metadata.setType(datatype);
						metadata.setValueId(namespaceId);
						metadata.setValue(mapping.get().getNamespaceName());
						collection.getMetadata().add(metadata);
						addEvidence(str, collection, publications, util);
						addContributor(collection, contributor);
						addComment(collection, comment);
						String id = this.tablemaker.addCollection (collection);
						collection.setCollectionId(Long.parseLong(id));
						addedCollections.add(collection);
						first = false;
					} else {
						CollectionView collectionCopy = new CollectionView();
						collectionCopy.setName(collection.getName() + "-" + t.trim());
						collectionCopy.setType(CollectionType.GLYCAN);
						collectionCopy.setGlycans(collection.getGlycans());
						collectionCopy.setMetadata(new ArrayList<>());
						for (Metadata m: collection.getMetadata()) {
							if (m.getType().getDatatypeId() != 5L) {  // copy everything other than tissue
								collectionCopy.getMetadata().add(m);
							}
						}
						Metadata metadata = new Metadata();
						Datatype datatype = new Datatype();
						datatype.setDatatypeId(5L);
						metadata.setType(datatype);
						metadata.setValueId(namespaceId);
						metadata.setValue(mapping.get().getNamespaceName());
						collectionCopy.getMetadata().add(metadata);
						String id = this.tablemaker.addCollection (collectionCopy);
						collectionCopy.setCollectionId(Long.parseLong(id));
						addedCollections.add(collectionCopy);
						
					}
				}
			}
		}
	}

	private void addMostSpecificSpeciesEntry(CarbbankRecord str, BS bs, CollectionView collection, PubmedUtil util, Map<String, String> speciesConflicts) throws IOException {
		Species species = null;
		
		try {
			List<String> others = new ArrayList<>();
			if (bs.getDomain() != null) {
				Optional<MappingDomain> mapping = mappingDomainRepository.findByNameIgnoreCase(bs.getDomain());
				if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
					others.add(mapping.get().getNamespaceId());
				}
			}
			if (bs.getK() != null) {
				Optional<MappingK> mapping = mappingKRepository.findByNameIgnoreCase(bs.getK());
				if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
					others.add(mapping.get().getNamespaceId());
				}
			}
			if (bs.getPd() != null) {
				Optional<MappingP_D> mapping = mappingP_DRepository.findByNameIgnoreCase(bs.getPd());
				if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
					others.add(mapping.get().getNamespaceId());
				}
			}
			if (bs.getC() != null) {
				Optional<MappingBS_C> mapping = mappingCRepository.findByNameIgnoreCase(bs.getC());
				if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
					others.add(mapping.get().getNamespaceId());
				}
			}
			if (bs.getO() != null) {
				Optional<MappingO> mapping = mappingORepository.findByNameIgnoreCase(bs.getO());
				if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
					if (!mapping.get().getNamespaceName().equalsIgnoreCase("seed"))
						others.add(mapping.get().getNamespaceId());
				}
			}
			if (bs.getF() != null) {
				Optional<MappingF> mapping = mappingFRepository.findByNameIgnoreCase(bs.getF());
				if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
					others.add(mapping.get().getNamespaceId());
				}
			}
			if (bs.getGs() != null) {
				List<MappingGS> mapping = mappingGSRepository.findByNameEqualsIgnoreCase(bs.getGs());
				if (!mapping.isEmpty() && mapping.get(0).getNamespaceId() != null) {
					others.add(mapping.get(0).getNamespaceId());
				}
			}
			if (bs.getCn() != null) {
				List<MappingCN> mapping = mappingCNRepository.findByNameEqualsIgnoreCase(bs.getCn());
				if (!mapping.isEmpty() && mapping.get(0).getNamespaceId() != null) {
					others.add(mapping.get(0).getNamespaceId());
				}
			}
			
			boolean conflict = checkSpeciesConflicts(util, others, speciesConflicts, bs.getId(), str.getCC());
			if (conflict) return;
			
			if (bs.getGs() != null && bs.getCn() != null) {
				List<MappingCN> mappingCN = mappingCNRepository.findByNameEqualsIgnoreCase(bs.getCn());
				List<MappingGS> mappingGS = mappingGSRepository.findByNameEqualsIgnoreCase(bs.getGs());
				if (!mappingCN.isEmpty() && !mappingGS.isEmpty()) {
					if (mappingCN.get(0).getNamespaceId() != null && mappingGS.get(0).getNamespaceId() != null) {
						if (!mappingCN.get(0).getNamespaceId().equals(mappingGS.get(0).getNamespaceId())) {
							// we have a conflict
							// find common ancestor if exists
							Species common = util.findCommonAncestor(mappingCN.get(0).getNamespaceId(), mappingGS.get(0).getNamespaceId());
							if (common != null) {
								// log the common species
								logger.info("there is conflict between CN and GS but there is a common ancestor: " + common.getName());
								speciesConflicts.put(bs.getId()+"", "There is conflict between CN and GS for record: " + str.getCC() + " but there is a common ancestor: " + common.getName());
							}
						} else {
							species = util.getSpeciesByID(mappingGS.get(0).getNamespaceId());
						}
					}
				} else if (!mappingGS.isEmpty() && mappingGS.get(0).getNamespaceId() != null) {
					species = util.getSpeciesByID(mappingGS.get(0).getNamespaceId());	
				} else if (!mappingCN.isEmpty() && mappingCN.get(0).getNamespaceId() != null) {
					species = util.getSpeciesByID(mappingCN.get(0).getNamespaceId());
				}
				
			} else if (bs.getGs() != null) {
				List<MappingGS> mappingGS = mappingGSRepository.findByNameEqualsIgnoreCase(bs.getGs());
				if (!mappingGS.isEmpty() && mappingGS.get(0).getNamespaceId() != null) {
					species = util.getSpeciesByID(mappingGS.get(0).getNamespaceId());	
				}
			} else if (bs.getCn() != null) {
				List<MappingCN> mappingCN = mappingCNRepository.findByNameEqualsIgnoreCase(bs.getCn());
				if (!mappingCN.isEmpty() && mappingCN.get(0).getNamespaceId() != null) {
					species = util.getSpeciesByID(mappingCN.get(0).getNamespaceId());
				}
			}
			
			if (species == null && bs.getF() != null) {
				Optional<MappingF> mappingF = mappingFRepository.findByNameIgnoreCase(bs.getF());
				if (mappingF.isPresent() && mappingF.get().getNamespaceId() != null) {
					species = util.getSpeciesByID(mappingF.get().getNamespaceId());
				}
			}
			if (species == null && bs.getO() != null) {
				if (!bs.getO().equalsIgnoreCase("seed")) {
					Optional<MappingO> mapping= mappingORepository.findByNameIgnoreCase(bs.getO());
					if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
						species = util.getSpeciesByID(mapping.get().getNamespaceId());
					}
				}
			}
			if (species == null && bs.getC() != null) {
				Optional<MappingBS_C> mapping= mappingCRepository.findByNameIgnoreCase(bs.getC());
				if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
					species = util.getSpeciesByID(mapping.get().getNamespaceId());
				}
			}
			
			if (species == null && bs.getPd() != null) {
				Optional<MappingP_D> mapping= mappingP_DRepository.findByNameIgnoreCase(bs.getPd());
				if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
					species = util.getSpeciesByID(mapping.get().getNamespaceId());
				}
			}
			if (species == null && bs.getK() != null) {
				Optional<MappingK> mapping= mappingKRepository.findByNameIgnoreCase(bs.getK());
				if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
					species = util.getSpeciesByID(mapping.get().getNamespaceId());
				}
			}
			if (species == null && bs.getDomain() != null) {
				Optional<MappingDomain> mapping= mappingDomainRepository.findByNameIgnoreCase(bs.getDomain());
				if (mapping.isPresent() && mapping.get().getNamespaceId() != null) {
					species = util.getSpeciesByID(mapping.get().getNamespaceId());
				}
			}
		} catch (IOException e) {
			logger.error("could not get species from NCBI", e);
			throw e;
		}
		
		try {
	        Thread.sleep(200); // wait 100 milliseconds between requests
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // restore interrupted status
	    }
		
		if (species != null) {
			Metadata metadata = new Metadata();
			Datatype datatype = new Datatype();
			datatype.setDatatypeId(3L);
			metadata.setType(datatype);
			metadata.setValueId(species.getId());
			metadata.setValue(species.getName());
			collection.getMetadata().add(metadata);
		}
	}
	
	boolean checkSpeciesConflicts (PubmedUtil util, List<String> species, Map<String, String> speciesConflicts, Long bsId, String cc) throws IOException {
		for (int i=0; i < species.size()-1; i++) {
			boolean same = util.checkIfSameHierarchy(species.get(i), species.get(i+1));
			try {
		        Thread.sleep(200); // wait 100 milliseconds between requests
		    } catch (InterruptedException e) {
		        Thread.currentThread().interrupt(); // restore interrupted status
		    }
			if (!same) {
				speciesConflicts.put(bsId+"", species.get(i) + " and " + species.get(i+1) + " are not in the same hierarchy for record: " + cc);
				return true;
			}
		}
		return false;
	}

	void addContributor (CollectionView collection, String value) {
		Metadata contributor = new Metadata();
		Datatype datatype = new Datatype();
		datatype.setDatatypeId(16L);
		contributor.setType(datatype);
		contributor.setValue(value);
		collection.getMetadata().add(contributor);
	}
	
	void addEvidence (CarbbankRecord str, CollectionView collection, List<PublicationView> publications, PubmedUtil util) throws Exception {
		if (str.getTI() != null || str.getAU() != null) {
			Publication probe = new Publication();
			if (str.getAU() != null) probe.setAuthor(str.getAU().trim());
			if (str.getTI() != null) probe.setTitle(str.getTI());
			CarbbankService.extractJournal (probe, str.getCT());
			
			List<Publication> results = publicationRepository.findMatchingPublications(probe);
			if (results.isEmpty()) {
				logger.error("Cannot find the publication for record: " + str.getCC());
				throw new EntityNotFoundException("Cannot find the publication for record: " + str.getCC());
			} else {
				boolean found = false;
				for (Publication pub: results) {
					if (pub.equals(probe)) {
						found = true;
						if (pub.getPmid() != null || pub.getDoiId() != null) {
							Metadata metadata = new Metadata();
							if (pub.getPmid() != null) metadata.setValue(pub.getPmid());
							else metadata.setValue(pub.getDoiId());
							
							try {
								PublicationView view = createPublicationView(pub, util);
								if (!publications.contains(view)) {
									publications.add(view);
								}
							} catch (Exception e) {
								logger.error("Could not retrieve the publication " + pub.getPmid(), e);
								continue;
							}
							
							Datatype datatype = new Datatype();
							datatype.setDatatypeId(2L);
							metadata.setType(datatype);
							collection.getMetadata().add(metadata);
						}
						break;
					}
				}
				if (!found) {
					logger.error("Cannot find the publication for record within the matches: " + str.getCC());
					throw new EntityNotFoundException("Cannot find the publication for record within the matches: " + str.getCC());
				}
			}
			
			/*int found = allPublications.indexOf(probe);
			if (found != -1) {
				Publication pub = allPublications.get(found);
				if (pub.getPmid() != null || pub.getDoiId() != null) {
					Metadata metadata = new Metadata();
					if (pub.getPmid() != null) metadata.setValue(pub.getPmid());
					else metadata.setValue(pub.getDoiId());
					
					try {
						publications.add(createPublicationView(pub, util));
					} catch (Exception e) {
						logger.error("Could not retrieve the publication " + pub.getPmid(), e);
					}
					
					Datatype datatype = new Datatype();
					datatype.setDatatypeId(2L);
					metadata.setType(datatype);
					collection.getMetadata().add(metadata);
				}
			} else {
				// cannot locate the publication
				logger.error("Cannot find the publication for record: " + str.getCC());
			}*/

			/*ExampleMatcher matcher = ExampleMatcher.matchingAll()
			    .withIgnoreNullValues()
			    .withIgnorePaths("id");

			Example<Publication> example = Example.of(probe, matcher);
*/
		/*	Example<Publication> example = Example.of(probe);
			List<Publication> results = publicationRepository.findAll(example);
			for (Publication pub: results) {
				if (pub.getPmid() != null || pub.getDoiId() != null) {
					Metadata metadata = new Metadata();
					if (pub.getPmid() != null) metadata.setValue(pub.getPmid());
					else metadata.setValue(pub.getDoiId());
					
					try {
						publications.add(createPublicationView(pub, util));
					} catch (Exception e) {
						logger.error("Could not retrieve the publication " + pub.getPmid(), e);
						continue;
					}
					
					Datatype datatype = new Datatype();
					datatype.setDatatypeId(2L);
					metadata.setType(datatype);
					collection.getMetadata().add(metadata);
				}
			}
*/	
		}
	}

	private PublicationView createPublicationView(Publication pub, PubmedUtil util) throws Exception {
		// retrieve publication from Pubmed again since the information on CFG database might be incorrect
		Publication retrieved = null;
		if (pub.getPmid() != null) {
			retrieved = util.getPublicatonByPMID(pub.getPmid());
		} else {
			retrieved = util.getPublicationByDOI(pub.getDoiId());
		}
		PublicationView view = new PublicationView();
		view.setPubmedId(pub.getPmid());
		view.setDoiId(pub.getDoiId());
		view.setAuthors(retrieved.getAuthor());
		view.setTitle(retrieved.getTitle());
		view.setJournal(retrieved.getJournalName());
		if (retrieved.getYear() != null) view.setYear(Integer.parseInt(retrieved.getYear()));
		view.setVolume(retrieved.getVolume());
		if (retrieved.getPageRange() != null && retrieved.getPageRange().contains("-")) {
			view.setStartPage(retrieved.getPageRange().substring(0, retrieved.getPageRange().indexOf("-")));
			view.setEndPage(retrieved.getPageRange().substring(retrieved.getPageRange().indexOf("-")+1));
		}
		try {
	        Thread.sleep(200); // wait 100 milliseconds between requests
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // restore interrupted status
	    }
		return view;
	}

}
