package org.glygen.carbbank.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.glygen.carbbank.dao.BSRepository;
import org.glygen.carbbank.dao.CarbbankRepository;
import org.glygen.carbbank.dao.MappingCNRepository;
import org.glygen.carbbank.dao.MappingCelllineRepository;
import org.glygen.carbbank.dao.MappingDiseaseRepository;
import org.glygen.carbbank.dao.MappingGSRepository;
import org.glygen.carbbank.dao.MappingOTRepository;
import org.glygen.carbbank.dao.PublicationRepository;
import org.glygen.carbbank.dao2.RemoteStructureRepository;
import org.glygen.carbbank.glycomedb.GlycoCTStructure;
import org.glygen.carbbank.glycomedb.RemoteStructure;
import org.glygen.carbbank.model.CarbbankRecord;
import org.glygen.carbbank.model.mapping.Publication;
import org.glygen.carbbank.model.tablemaker.CollectionType;
import org.glygen.carbbank.model.tablemaker.CollectionView;
import org.glygen.carbbank.model.tablemaker.DatasetInputView;
import org.glygen.carbbank.model.tablemaker.Datatype;
import org.glygen.carbbank.model.tablemaker.Glycan;
import org.glygen.carbbank.model.tablemaker.Grant;
import org.glygen.carbbank.model.tablemaker.License;
import org.glygen.carbbank.model.tablemaker.Metadata;
import org.glygen.carbbank.model.tablemaker.PublicationView;
import org.glygen.carbbank.parser.PubmedUtil;
import org.glygen.carbbank.util.TableMakerAPI;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    
    TableMakerAPI tablemaker;
	
	static Logger logger = org.slf4j.LoggerFactory.getLogger(CarbbankToTablemakerService.class);
	
	String contributorString = "createdBy:Sena Arpinar (University of Georgia)|"
			+ "curatedBy:Aise Arpinar|"
			+ "curatedBy:Nahom Abel|"
			+ "curatedBy:Harivinay Prasad Reddy Gujjula (George Washington University)|"
			+ "contributedBy:Rene Ranzinger (University of Georgia)|"
			+ "createdWith:GlyTableMaker (https://glygen.ccrc.uga.edu/tablemaker)|"
			+ "retrievedFrom:Consortium for Functional Glycomics (https://pubmed.ncbi.nlm.nih.gov/16478800/)";
	
	Map<String, String> carbIdErrorMap = new HashMap<>();
 	
	final CarbbankRepository structureRepository;
	final BSRepository biologicalRepository;
	final PublicationRepository publicationRepository;
	final RemoteStructureRepository remoteRepository;
	
	final MappingDiseaseRepository mappingDiseaseRepository;
	final MappingCelllineRepository mappingCelllineRepository;
	private final MappingOTRepository mappingTissueRepository;
	private final MappingGSRepository mappingGSRepository;
	private final MappingCNRepository mappingCNRepository;
	
	
	public CarbbankToTablemakerService(CarbbankRepository structureRepository, PublicationRepository publicationRepository, 
			BSRepository biologicalRepository, RemoteStructureRepository remoteRepository, 
			MappingOTRepository mappingTissueRepository, MappingGSRepository mappingSpeciesRepository, 
			MappingCNRepository mappingCNRepository, MappingDiseaseRepository mappingDiseaseRepository, 
			MappingCelllineRepository mappingCelllineRepository) {
		this.structureRepository = structureRepository;
		this.biologicalRepository = biologicalRepository;
		this.publicationRepository = publicationRepository;
		this.remoteRepository = remoteRepository;
		this.mappingDiseaseRepository = mappingDiseaseRepository;
		this.mappingCelllineRepository = mappingCelllineRepository;
		this.mappingTissueRepository = mappingTissueRepository;
		this.mappingGSRepository = mappingSpeciesRepository;
		this.mappingCNRepository = mappingCNRepository;
	}
	
	public void createGlycans () {
		this.tablemaker = TableMakerAPI.getInstance();
		this.tablemaker.setApiURL(apiUrl);
		this.tablemaker.setUserName(userId);
		this.tablemaker.setPassword(password);
		List<CarbbankRecord> structures = structureRepository.findAll();
		StringBuffer notes = new StringBuffer();
		int count = 0;
		int totalProcessed = 0;
		int notFoundinGlytoucan = 0;
		for (CarbbankRecord str: structures) {
			String carbId = str.getCC().substring(5);

			Optional<RemoteStructure>  handle = remoteRepository.findByResourceIdWithStructures(carbId);
			if (handle.isPresent() && !handle.get().getStructures().isEmpty()) {
				count++;
				if (count % 100 == 0) {
					logger.info (count + ": adding glycan " + carbId);
				}
				GlycoCTStructure structure = handle.get().getStructures().iterator().next();
				try {
					String glytoucanId = this.tablemaker.addGlycanGlycoCT(structure.getGlycoCT());
					totalProcessed++;
					if (glytoucanId != null && glytoucanId.length() < 15) {
						//str.setGlytoucanId(glytoucanId);
						logger.debug(str.getCC() + " already in glytoucan");
						//structureRepository.save(str);
					} else {
						notFoundinGlytoucan++;
					}
				} catch (Exception e) {
					logger.error("could not add glycan with glycoCT: " + structure.getGlycoCT(), e);
				}
			} else {
				logger.info ("carbId is not found in glycomedb");
				notes.append("carbId is not found in glycomedb");
				String seq = str.getStructure();
				if (seq == null) {
					notes.append("no sequence for: " + str.getCC());
					continue;
				}
				//TODO figure out how to parse carbbank structure
				/*try {
					String glytoucanId = this.tablemaker.addGlycan(seq);
					totalProcessed++;
					if (glytoucanId != null && glytoucanId.length() < 15) {
						//str.setGlytoucanId(glytoucanId);
						//structureRepository.save(str);
						logger.debug(str.getCC() + " already in glytoucan");
					} else {
						notFoundinGlytoucan++;
					}
				} catch (Exception e) {
					// catch DuplicateException
					carbIdErrorMap.put(carbId, "Sequence cannot be found in converted list and cannot be coverted to GlycoCT");
					logger.error("could not convert sequence to GlycoCT: " + seq);
				}*/
			}
		}
		logger.info ("Glycan results:" + notes);
		logger.info ("Processed total: " + totalProcessed);
		logger.info ("Structures with existing glycoCT:" + count);
		logger.info ("Not found in GlyTouCan: " + notFoundinGlytoucan);
		
		 
        try {
        	String filePath = "glycan_errorlog.txt"; 
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
			writer.write(notes.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public void createCollectionsAndPublishDataset () {
		this.tablemaker = TableMakerAPI.getInstance();
		this.tablemaker.setApiURL(apiUrl);
		this.tablemaker.setUserName(userId);
		this.tablemaker.setPassword(password);
		PubmedUtil util = new PubmedUtil(apiKey);
		List<Structures> structures = structureRepository.findAll();
		List<CollectionView> addedCollections = new ArrayList<CollectionView>();
		List<PublicationView> publications = new ArrayList<>();
		StringBuffer notes = new StringBuffer();
		for (Structures str: structures) {
			String glytoucanId = str.getGlytoucanId();
			if (glytoucanId == null || glytoucanId.equalsIgnoreCase("null")) {
				logger.error("No glytoucanId for " + str.getCarb_id());
				continue;
			}
			// find the glycan and use the id
			Long glycanId = null;
			try {
				glycanId = this.tablemaker.retrieveGlycanByGlytoucanId(glytoucanId);
			} catch (Exception e) {
				logger.error("Exception retrieving glycan " + glytoucanId, e);
			}
			if (glycanId == null) {
				logger.error("Cannot find the glycan " + glytoucanId + " in tablemaker ");
				carbIdErrorMap.put(str.getCarb_id(), "Cannot find the glycan " + glytoucanId + " in tablemaker. Please create glycans first!");
				continue;
			}
			
			Glycan glycan = new Glycan();
			glycan.setGlycanId(glycanId);
			glycan.setGlytoucanID(glytoucanId);
			
			if (str.getCarb_key() != null) {
				List<Publication> pubs = publicationRepository.findByCarbKey(str.getCarb_key());
				for (Publication pub: pubs) {
					if (pub.getPmid() == null && pub.getDoiId() == null) {
						logger.info("There is no pmid for this publication " + pub.getId());
						notes.append("Not including " + pub.getCarbKey() + " since there is no pmid or doi\n");
						carbIdErrorMap.put(str.getCarb_id(), "Not including " + pub.getCarbKey() + " since there is no pmid or doi");
						continue;
					}
					// create a collection for each glycan-paper pair
					CollectionView collection = new CollectionView();
					collection.setName(str.getCarb_id() + "-" + glytoucanId + "-" + pub.getId());
					collection.setType(CollectionType.GLYCAN);
					collection.setGlycans(new ArrayList<>());
					
					collection.getGlycans().add(glycan);
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
					collection.setMetadata(new ArrayList<Metadata>());
					collection.getMetadata().add(metadata);
					Metadata contributor = new Metadata();
					datatype = new Datatype();
					datatype.setDatatypeId(16L);
					contributor.setType(datatype);
					contributor.setValue(contributorString);
					collection.getMetadata().add(contributor);
					String id = this.tablemaker.addCollection (collection);
					if (id != null) collection.setCollectionId(Long.parseLong(id));
					else {
						logger.error("failed to create collection " + collection.getName());
					}
					addedCollections.add(collection);
				}
			}
				
			// add a collection for each biological entry
			List<Biological> bList = biologicalRepository.findByCarbId(str.getCarb_id());
			for (Biological bio: bList) {
				CollectionView collection = new CollectionView();
				collection.setName(str.getCarb_id() + "-" + glytoucanId + "-" + bio.getId());
				collection.setType(CollectionType.GLYCAN);
				collection.setGlycans(new ArrayList<>());
				collection.getGlycans().add(glycan);
				collection.setMetadata(new ArrayList<Metadata>());
				if (bio.getScientificname() != null && !bio.getScientificname().equalsIgnoreCase("unknown")) {
					// find the mapping
					Optional<MappingScientificName> mapping = mappingSpeciesRepository.findByNameIgnoreCase(bio.getScientificname());
					if (mapping.isPresent()) {
						String namespaceId = mapping.get().getNamespaceId();
						if (namespaceId != null) {
							Metadata metadata = new Metadata();
							Datatype datatype = new Datatype();
							datatype.setDatatypeId(3L);
							metadata.setType(datatype);
							metadata.setValueId(namespaceId);
							metadata.setValue(mapping.get().getNamespaceName());
							collection.getMetadata().add(metadata);
						}
					} else {
						notes.append("Not including " + str.getCarb_id()  + " with biological entry " + bio.getId() + " since there is no species information");
						carbIdErrorMap.put(str.getCarb_id(), "Not including " + str.getCarb_id()  + " with biological entry " + bio.getId() + " since there is no species information");
						continue;
					}
				} else {
					notes.append("Not including " + str.getCarb_id()  + " with biological entry " + bio.getId() + " since there is no species information");
					carbIdErrorMap.put(str.getCarb_id(), "Not including " + str.getCarb_id()  + " with biological entry " + bio.getId() + " since there is no species information");
					continue;
				}
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
				boolean tissueAdded = false;
				if (bio.getTissue() != null) {
					// can have multiple values
					String tissue = bio.getTissue();
					String[] tissues = tissue.split(",");
					boolean first = true;
					for (String t: tissues) {
						// find the mapping
						Optional<MappingTissue> mapping = mappingTissueRepository.findByNameIgnoreCase(t.trim());
						if (mapping.isPresent()) {
							String namespaceId = mapping.get().getNamespaceId();
							if (namespaceId != null) {
								if (first) {
									collection.setName(str.getCarb_id() + "-" + glytoucanId + "-" + bio.getId() + "-" + t.trim());
									Metadata metadata = new Metadata();
									Datatype datatype = new Datatype();
									datatype.setDatatypeId(5L);
									metadata.setType(datatype);
									metadata.setValueId(namespaceId);
									metadata.setValue(mapping.get().getNamespaceName());
									collection.getMetadata().add(metadata);
									tissueAdded = true;
									Metadata contributor = new Metadata();
									datatype = new Datatype();
									datatype.setDatatypeId(16L);
									contributor.setType(datatype);
									contributor.setValue(contributorString);
									collection.getMetadata().add(contributor);
									String id = this.tablemaker.addCollection (collection);
									collection.setCollectionId(Long.parseLong(id));
									addedCollections.add(collection);
									first = false;
								} else {
									CollectionView collectionCopy = new CollectionView();
									collectionCopy.setName(str.getCarb_id() + "-" + glytoucanId + "-" + bio.getId() + "-" + t.trim());
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
				
				if (!tissueAdded && bio.getOrgan() != null) {
					// can have multiple values
					String organ = bio.getOrgan();
					String[] organs = organ.split(",");
					boolean first = true;
					for (String o: organs) {
						// find the mapping
						Optional<MappingOrgan> mapping = mappingOrganRepository.findByNameIgnoreCase(o.trim());
						if (mapping.isPresent()) {
							String namespaceId = mapping.get().getNamespaceId();
							if (namespaceId != null) {
								if (first) {
									collection.setName(str.getCarb_id() + "-" + glytoucanId + "-" + bio.getId() + "-" + o.trim());
									Metadata metadata = new Metadata();
									Datatype datatype = new Datatype();
									datatype.setDatatypeId(5L);
									metadata.setType(datatype);
									metadata.setValueId(namespaceId);
									metadata.setValue(mapping.get().getNamespaceName());
									collection.getMetadata().add(metadata);
									tissueAdded = true;
									Metadata contributor = new Metadata();
									datatype = new Datatype();
									datatype.setDatatypeId(16L);
									contributor.setType(datatype);
									contributor.setValue(contributorString);
									collection.getMetadata().add(contributor);
									String id = this.tablemaker.addCollection (collection);
									collection.setCollectionId(Long.parseLong(id));
									addedCollections.add(collection);
									first = false;
								} else {
									CollectionView collectionCopy = new CollectionView();
									collectionCopy.setName(str.getCarb_id() + "-" + glytoucanId + "-" + bio.getId() + "-" + o.trim());
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
				
				if (collection.getMetadata().isEmpty()) {
					logger.info ("No metadata for " + str.getCarb_id());
					notes.append("Not including " +  str.getCarb_key() + " since there is no metadata");
					carbIdErrorMap.put(str.getCarb_id(), "Not including " +  str.getCarb_key() + " since there is no metadata");
				} else {
					if (!addedCollections.contains(collection)) {
						Metadata metadata = new Metadata();
						Datatype datatype = new Datatype();
						datatype.setDatatypeId(16L);
						metadata.setType(datatype);
						metadata.setValue(contributorString);
						collection.getMetadata().add(metadata);
						String id = this.tablemaker.addCollection (collection);
						collection.setCollectionId(Long.parseLong(id));
						addedCollections.add(collection);
					}
				}

			}
		}
		
		if (!addedCollections.isEmpty()) {
			DatasetInputView dataset = new DatasetInputView();
			dataset.setName("CFG Glycomics Data");
			dataset.setDescription("The glycomics data generated by the Consortium for Functional Glycomics "
					+ "(CFG) encompasses a broad spectrum of experimental and curated datasets "
					+ "aimed at understanding the roles of glycans in biology. CFG has produced "
					+ "glycan datasets from glycan profiling, glycan array and phenotyping of glycogene "
					+ "mouse strains. The CFG also maintained detailed Molecule Pages that catalog "
					+ "glycan structures, glycan-binding proteins, and glycosyltransferases, providing a "
					+ "rich resource for glycoinformatics and structural glycobiology. The glycan data, "
					+ "including glycan structures, their biological annotation and literature references "
					+ "are archived in this GlyTableMaker dataset.");
			dataset.setLicense(new License());
			dataset.getLicense().setId(2L);
			dataset.getLicense().setName("CC BY 4.0");
			dataset.getLicense().setUrl("https://creativecommons.org/licenses/by/4.0/");
			dataset.getLicense().setCommercialUse(true);
			dataset.getLicense().setAttribution("You must give appropriate credit , provide a link to the license, and indicate if changes were made . You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.");
			dataset.getLicense().setDistribution("No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.");
			//dataset.setNotes(notes.toString());  // it is too long
			dataset.setCollections(addedCollections);
			dataset.setPublications(publications);
			dataset.setGrants(new ArrayList<>());
			Grant grant = new Grant();
			grant.setFundingOrganization("NIH");
			grant.setIdentifier("U54 GM62113");
			grant.setTitle("Consortium for Functional Glycomics (CFG)");
			dataset.getGrants().add(grant);
			
			
			try {
				Publication associatedPaper = util.getPublicatonByPMID("16478800");
				dataset.setAssociatedPapers(new ArrayList<>());
				dataset.getAssociatedPapers().add(createPublicationView(associatedPaper, util));
			} catch (Exception ie) {
				logger.error("Could not find publication from PubMed ", ie);
			}
			
			this.tablemaker.publishDataset(dataset);
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

		
	}*/

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
	        Thread.sleep(100); // wait 100 milliseconds between requests
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // restore interrupted status
	    }
		return view;
	}

}
