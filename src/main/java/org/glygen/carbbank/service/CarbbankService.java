package org.glygen.carbbank.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.glygen.carbbank.dao.AGRepository;
import org.glygen.carbbank.dao.AMRepository;
import org.glygen.carbbank.dao.ANRepository;
import org.glygen.carbbank.dao.BARepository;
import org.glygen.carbbank.dao.BSRepository;
import org.glygen.carbbank.dao.CarbbankRepository;
import org.glygen.carbbank.dao.DBRepository;
import org.glygen.carbbank.dao.MTRepository;
import org.glygen.carbbank.dao.MappingAGRepository;
import org.glygen.carbbank.dao.MappingAMRepository;
import org.glygen.carbbank.dao.MappingANRepository;
import org.glygen.carbbank.dao.MappingBARepository;
import org.glygen.carbbank.dao.MappingBSRepository;
import org.glygen.carbbank.dao.MappingCNRepository;
import org.glygen.carbbank.dao.MappingCRepository;
import org.glygen.carbbank.dao.MappingCelllineRepository;
import org.glygen.carbbank.dao.MappingDBRepository;
import org.glygen.carbbank.dao.MappingDiseaseRepository;
import org.glygen.carbbank.dao.MappingDomainRepository;
import org.glygen.carbbank.dao.MappingFRepository;
import org.glygen.carbbank.dao.MappingGSRepository;
import org.glygen.carbbank.dao.MappingGTRepository;
import org.glygen.carbbank.dao.MappingKRepository;
import org.glygen.carbbank.dao.MappingLSRepository;
import org.glygen.carbbank.dao.MappingMTRepository;
import org.glygen.carbbank.dao.MappingORepository;
import org.glygen.carbbank.dao.MappingOTRepository;
import org.glygen.carbbank.dao.MappingPARepository;
import org.glygen.carbbank.dao.MappingPMRepository;
import org.glygen.carbbank.dao.MappingP_DRepository;
import org.glygen.carbbank.dao.MappingTNRepository;
import org.glygen.carbbank.dao.PARepository;
import org.glygen.carbbank.dao.PMRepository;
import org.glygen.carbbank.dao.PublicationRepository;
import org.glygen.carbbank.dao.TNRepository;
import org.glygen.carbbank.model.AG;
import org.glygen.carbbank.model.AM;
import org.glygen.carbbank.model.AN;
import org.glygen.carbbank.model.BA;
import org.glygen.carbbank.model.BS;
import org.glygen.carbbank.model.CarbbankRecord;
import org.glygen.carbbank.model.DB;
import org.glygen.carbbank.model.MT;
import org.glygen.carbbank.model.NC;
import org.glygen.carbbank.model.NT;
import org.glygen.carbbank.model.PA;
import org.glygen.carbbank.model.PM;
import org.glygen.carbbank.model.SC;
import org.glygen.carbbank.model.ST;
import org.glygen.carbbank.model.TN;
import org.glygen.carbbank.model.VR;
import org.glygen.carbbank.model.mapping.MappingAG;
import org.glygen.carbbank.model.mapping.MappingAM;
import org.glygen.carbbank.model.mapping.MappingAN;
import org.glygen.carbbank.model.mapping.MappingBA;
import org.glygen.carbbank.model.mapping.MappingBS_BS;
import org.glygen.carbbank.model.mapping.MappingBS_C;
import org.glygen.carbbank.model.mapping.MappingCN;
import org.glygen.carbbank.model.mapping.MappingCellLine;
import org.glygen.carbbank.model.mapping.MappingDB;
import org.glygen.carbbank.model.mapping.MappingDisease;
import org.glygen.carbbank.model.mapping.MappingDomain;
import org.glygen.carbbank.model.mapping.MappingF;
import org.glygen.carbbank.model.mapping.MappingGS;
import org.glygen.carbbank.model.mapping.MappingGT;
import org.glygen.carbbank.model.mapping.MappingK;
import org.glygen.carbbank.model.mapping.MappingLS;
import org.glygen.carbbank.model.mapping.MappingMT;
import org.glygen.carbbank.model.mapping.MappingO;
import org.glygen.carbbank.model.mapping.MappingOT;
import org.glygen.carbbank.model.mapping.MappingPA;
import org.glygen.carbbank.model.mapping.MappingPM;
import org.glygen.carbbank.model.mapping.MappingP_D;
import org.glygen.carbbank.model.mapping.MappingTN;
import org.glygen.carbbank.model.mapping.Publication;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CarbbankService {
	
	static Logger logger = org.slf4j.LoggerFactory.getLogger(CarbbankService.class);
	

	String[] aminoAcids = {
		    "alanine", "ala",
		    "arginine", "arg",
		    "asparagine", "asn",
		    "aspartic acid", "asp",
		    "cysteine", "cys",
		    "glutamic acid", "glu",
		    "glutamine", "gln",
		    "glycine", "gly",
		    "histidine", "his",
		    "isoleucine", "ile",
		    "leucine", "leu",
		    "lysine", "lys",
		    "methionine", "met",
		    "phenylalanine", "phe",
		    "proline", "pro",
		    "serine", "ser",
		    "threonine", "thr",
		    "tryptophan", "trp",
		    "tyrosine", "tyr",
		    "valine", "val"
	};

	
	final private CarbbankRepository carbbankRepository;
	final private AGRepository agRepository;
	final private MappingAGRepository mappingAGRepository;
	final private AMRepository amRepository;
	final private MappingAMRepository mappingAMRepository;
	final private ANRepository anRepository;
	final private MappingANRepository mappingANRepository;
	final private MTRepository mtRepository;
	final private MappingMTRepository mappingMTRepository;
	final private PMRepository pmRepository;
	final private MappingPMRepository mappingPMRepository;
	final private TNRepository tnRepository;
	final private MappingTNRepository mappingTNRepository;
	final private BARepository baRepository;
	final private MappingBARepository mappingBARepository;
	final private PARepository paRepository;
	final private MappingPARepository mappingPARepository;
	final private DBRepository dbRepository;
	final private MappingDBRepository mappingDBRepository;
	final private PublicationRepository publicationRepository;
	final private BSRepository bsRepository;
	final private MappingBSRepository mappingBSRepository;
	final private MappingCRepository mappingCRepository;
	final private MappingCelllineRepository mappingCellineRepository;
	final private MappingCNRepository mappingCNRepository;
	final private MappingDiseaseRepository mappingDiseaseRepository;
	final private MappingDomainRepository mappingDomainRepository;
	final private MappingFRepository mappingFRepository;
	final private MappingGSRepository mappingGSRepository;
	final private MappingGTRepository mappingGTRepository;
	final private MappingKRepository mappingKRepository;
	final private MappingLSRepository mappingLSRepository;
	final private MappingORepository mappingORepository;
	final private MappingOTRepository mappingOTRepository;
	final private MappingP_DRepository mappingP_DRepository;
	
	public CarbbankService(CarbbankRepository carbbankRepository, 
			AGRepository agRepository, MappingAGRepository mappingAGRepository, 
			MappingAMRepository mappingAMRepository, 
			AMRepository amRepository, TNRepository tnRepository, 
			PMRepository pmRepository, MTRepository mtRepository, 
			MappingTNRepository mappingTNRepository, MappingPMRepository mappingPMRepository, 
			MappingMTRepository mappingMTRepository, MappingBARepository mappingBARepository, 
			MappingANRepository mappingANRepository, BARepository baRepository, 
			ANRepository anRepository, PARepository paRepository, 
			MappingPARepository mappingPARepository, MappingDBRepository mappingDBRepository, 
			DBRepository dbRepository, PublicationRepository publicationRepository, 
			MappingKRepository mappingKRepository, MappingFRepository mappingFRepository, 
			MappingP_DRepository mappingP_DRepository, 
			MappingOTRepository mappingOTRepository, MappingORepository mappingORepository, 
			MappingLSRepository mappingLSRepository, MappingGTRepository mappingGTRepository, 
			MappingGSRepository mappingGSRepository, MappingDomainRepository mappingDomainRepository, 
			MappingDiseaseRepository mappingDiseaseRepository, 
			MappingCelllineRepository mappingCellineRepository, 
			MappingCRepository mappingCRepository, MappingCNRepository mappingCNRepository, 
			MappingBSRepository mappingBSRepository, BSRepository bsRepository) {
		this.carbbankRepository = carbbankRepository;
		this.agRepository = agRepository;
		this.mappingAGRepository = mappingAGRepository;
		this.amRepository = amRepository;
		this.mappingAMRepository = mappingAMRepository;
		this.anRepository = anRepository;
		this.mappingANRepository = mappingANRepository;
		this.mtRepository = mtRepository;
		this.mappingMTRepository = mappingMTRepository;
		this.pmRepository = pmRepository;
		this.mappingPMRepository = mappingPMRepository;
		this.tnRepository = tnRepository;
		this.mappingTNRepository = mappingTNRepository;
		this.baRepository = baRepository;
		this.mappingBARepository = mappingBARepository;
		this.paRepository = paRepository;
		this.mappingPARepository = mappingPARepository;
		this.dbRepository = dbRepository;
		this.mappingDBRepository = mappingDBRepository;
		this.publicationRepository = publicationRepository;
		this.bsRepository = bsRepository;
		this.mappingBSRepository = mappingBSRepository;
		this.mappingCRepository = mappingCRepository;
		this.mappingCellineRepository = mappingCellineRepository;
		this.mappingCNRepository = mappingCNRepository;
		this.mappingDiseaseRepository = mappingDiseaseRepository;
		this.mappingDomainRepository = mappingDomainRepository;
		this.mappingFRepository = mappingFRepository;
		this.mappingGSRepository = mappingGSRepository;
		this.mappingGTRepository = mappingGTRepository;
		this.mappingKRepository = mappingKRepository;
		this.mappingLSRepository = mappingLSRepository;
		this.mappingORepository = mappingORepository;
		this.mappingOTRepository = mappingOTRepository;
		this.mappingP_DRepository = mappingP_DRepository;
	}
	
	@Transactional
	public void saveRecords (List<Map<String, String>> records) {
		// create the rows
    	for (Map<String, String> record : records) {
    		CarbbankRecord carbRecord = new CarbbankRecord();
    		String structure = record.get("structure");
    		structure = structure.replaceAll("'", "''");
    		carbRecord.setStructure(structure);
    		
    		Map<String, Map<String, String>> bsRecords = new HashMap<>();
    		
    		for (Map.Entry<String, String> entry : record.entrySet()) {
    			String value = entry.getValue(); 
    			value = value.replaceAll("'", "''");
    			value = value.trim();
    			if (value.length() == 0) {
    				continue;    // skip the empty values
    			}
    			String col = entry.getKey();
    			col = col.replaceAll(" ", "_");
    			col = col.replaceAll("/", "_");
    			
    			if (col.equals("AU")) {
    				carbRecord.setAU(value);
    			} else if (col.equals("CC")) {
    				carbRecord.setCC(value);
    			} else if (col.equals("CT")) {
    				carbRecord.setCT(value);
    			} else if (col.equals("DA")) {
    				carbRecord.setDA(value);
    			} else if (col.equals("FC")) {
    				carbRecord.setFC(value);
    			} else if (col.equals("SB")) {
    				carbRecord.setSB(value);
    			} else if (col.equals("SI")) {
    				carbRecord.setSI(value);
    			} else if (col.equals("TI")) {
    				carbRecord.setTI(value);
    			} else if (col.startsWith("AG")) {
    				AG ag = new AG();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getAgList() == null) {
    					carbRecord.setAgList(new ArrayList<>());
    				}
    				carbRecord.getAgList().add(ag);
    			} else if (col.startsWith("AM")) {
    				AM ag = new AM();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getAmList() == null) {
    					carbRecord.setAmList(new ArrayList<>());
    				}
    				carbRecord.getAmList().add(ag);
    			} else if (col.startsWith("AN")) {
    				AN ag = new AN();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getAnList() == null) {
    					carbRecord.setAnList(new ArrayList<>());
    				}
    				carbRecord.getAnList().add(ag);
    			} else if (col.startsWith("BA")) {
    				BA ba = new BA();
    				ba.setRecord(carbRecord);
    				ba.setValue(value);
    				if (carbRecord.getBaList() == null) {
    					carbRecord.setBaList(new ArrayList<>());
    				}
    				carbRecord.getBaList().add(ba);
    			} else if (col.startsWith("DB")) {
    				DB ag = new DB();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getDbList() == null) {
    					carbRecord.setDbList(new ArrayList<>());
    				}
    				carbRecord.getDbList().add(ag);
    			} else if (col.startsWith("MT")) {
    				MT ag = new MT();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getMtList() == null) {
    					carbRecord.setMtList(new ArrayList<>());
    				}
    				carbRecord.getMtList().add(ag);
    			} else if (col.startsWith("NC")) {
    				NC ag = new NC();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getNcList() == null) {
    					carbRecord.setNcList(new ArrayList<>());
    				}
    				carbRecord.getNcList().add(ag);
    			} else if (col.startsWith("NT")) {
    				NT ag = new NT();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getNtList() == null) {
    					carbRecord.setNtList(new ArrayList<>());
    				}
    				carbRecord.getNtList().add(ag);
    			} else if (col.startsWith("PA")) {
    				PA ag = new PA();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getPaList() == null) {
    					carbRecord.setPaList(new ArrayList<>());
    				}
    				carbRecord.getPaList().add(ag);
    			} else if (col.startsWith("PM")) {
    				PM ag = new PM();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getPmList() == null) {
    					carbRecord.setPmList(new ArrayList<>());
    				}
    				carbRecord.getPmList().add(ag);
    			} else if (col.startsWith("SC")) {
    				SC ag = new SC();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getScList() == null) {
    					carbRecord.setScList(new ArrayList<>());
    				}
    				carbRecord.getScList().add(ag);
    			} else if (col.startsWith("ST")) {
    				ST ag = new ST();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getStList() == null) {
    					carbRecord.setStList(new ArrayList<>());
    				}
    				carbRecord.getStList().add(ag);
    			} else if (col.startsWith("TN")) {
    				TN ag = new TN();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getTnList() == null) {
    					carbRecord.setTnList(new ArrayList<>());
    				}
    				carbRecord.getTnList().add(ag);
    			} else if (col.startsWith("VR")) {
    				VR ag = new VR();
    				ag.setRecord(carbRecord);
    				ag.setValue(value);
    				if (carbRecord.getVrList() == null) {
    					carbRecord.setVrList(new ArrayList<>());
    				}
    				carbRecord.getVrList().add(ag);
    			} else if (col.startsWith("BS")) {
    				String number = col.substring(2, col.indexOf("_"));
    				if (bsRecords.get(number) == null) {
    					bsRecords.put(number, new HashMap<>());
    				}
    				String col2 =  col.substring(col.indexOf("_")+1);
    				bsRecords.get(number).put(col2, value);
    			}
    		}
    		
			carbRecord.setBsList(new ArrayList<>());
    		for (Map<String, String> row: bsRecords.values()) {
    			BS bs = new BS();
    			bs.setRecord(carbRecord);
    			for (Map.Entry<String, String> e: row.entrySet()) {
    				if (e.getKey().equalsIgnoreCase("bs")) {
    					bs.setBs(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("c")) {
    					bs.setC(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("cell_line")) {
    					bs.setCellline(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("cn")) {
    					bs.setCn(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("disease")) {
    					bs.setDisease(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("domain")) {
    					bs.setDomain(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("f")) {
    					bs.setF(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("gs")) {
    					bs.setGs(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("gt")) {
    					bs.setGt(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("k")) {
    					bs.setK(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("ls")) {
    					bs.setLs(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("nt")) {
    					bs.setNt(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("o")) {
    					bs.setO(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("ot")) {
    					bs.setOt(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("p_d")) {
    					bs.setPd(e.getValue());
    				} else if (e.getKey().equalsIgnoreCase("star")) {
    					bs.setStar(e.getValue());
    				}
    			}
    			carbRecord.getBsList().add(bs);
    		}
    		
    		carbbankRepository.save(carbRecord);
    	}
	}
	
	@Transactional
	public void createMappingTables () {
		long c = mappingAGRepository.count();
		if (c == 0) {
			List<String> distinctValues = agRepository.findDistinctValue();
			for (String name: distinctValues) {
				long count = agRepository.countByValueIgnoreCase(name);
				MappingAG mapping = new MappingAG();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingAGRepository.save(mapping);
			}
		}
		
		c = mappingAMRepository.count();
		if (c == 0) {
			List<String> distinctValues = amRepository.findDistinctValue();
			for (String name: distinctValues) {
				long count = amRepository.countByValueIgnoreCase(name);
				MappingAM mapping = new MappingAM();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingAMRepository.save(mapping);
			}
		}
		
		c = mappingANRepository.count();
		if (c == 0) {
			List<String> distinctValues = anRepository.findDistinctValue();
			for (String name: distinctValues) {
				long count = anRepository.countByValueIgnoreCase(name);
				MappingAN mapping = new MappingAN();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingANRepository.save(mapping);
			}
		}
		
		c = mappingMTRepository.count();
		if (c == 0) {
			List<String> distinctValues = mtRepository.findDistinctValue();
			for (String name: distinctValues) {
				long count = mtRepository.countByValueIgnoreCase(name);
				MappingMT mapping = new MappingMT();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingMTRepository.save(mapping);
			}
		}
		
		c = mappingPMRepository.count();
		if (c == 0) {
			List<String> distinctValues = pmRepository.findDistinctValue();
			for (String name: distinctValues) {
				long count = pmRepository.countByValueIgnoreCase(name);
				MappingPM mapping = new MappingPM();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingPMRepository.save(mapping);
			}
		}
		
		c = mappingTNRepository.count();
		if (c == 0) {
			List<String> distinctValues = tnRepository.findDistinctValue();
			for (String name: distinctValues) {
				long count = tnRepository.countByValueIgnoreCase(name);
				MappingTN mapping = new MappingTN();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingTNRepository.save(mapping);
			}
		}
		
		c = mappingBARepository.count();
		if (c == 0) {
			List<String> distinctValues = baRepository.findDistinctValue();
			for (String name: distinctValues) {
				long count = baRepository.countByValueIgnoreCase(name);
				MappingBA mapping = new MappingBA();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingBARepository.save(mapping);
			}
		}
		
		c = mappingPARepository.count();
		if (c == 0) {
			Map <String, Integer> counts = new HashMap<>();
			List<String> distinctValues = paRepository.findDistinctValue();
			Set<String> processed = new HashSet<>();
			for (String name: distinctValues) {
				String[] splitted = name.split(",");
				for (String split: splitted) {
					split = split.trim();
					split = split.toLowerCase();
					if (split.contains("-")) {
						continue;   // ignore aminoacid part
					}
					String[] split2 = split.split(" ");
					if (Arrays.asList(aminoAcids).contains(split2[0].toLowerCase())) {
						continue;
					}
					if (!processed.contains(split)) {
						processed.add(split);
						MappingPA mapping = new MappingPA();
						mapping.setName(split);
						mappingPARepository.save(mapping);
					} 
					if (counts.get(split) == null) {
						counts.put(split, 1);
					} else {
						counts.put(split, counts.get(split) + 1);
					}
				}
			}
			
			List<MappingPA> mappings = mappingPARepository.findAll();
			for (MappingPA mapping: mappings) {
				Integer count = counts.get(mapping.getName());
				if (count != null) {
					mapping.setCount(count);
					mappingPARepository.save(mapping);
				}
			}
		}
		
		c = mappingDBRepository.count();
		if (c == 0) {
			List<String> distinctValues = dbRepository.findDistinctValue();
			Set<String> processed = new HashSet<>();
			Map <String, Integer> counts = new HashMap<>();
 			for (String name: distinctValues) {
				String[] splitted = name.split(":");
				long count = dbRepository.countByValueIgnoreCase(name);
				if (!processed.contains(splitted[0])) {
					processed.add(splitted[0]);
					MappingDB mapping = new MappingDB();
					mapping.setCount(Long.valueOf(count).intValue());
					mapping.setName(splitted[0]);
					mappingDBRepository.save(mapping);
				}
				if (counts.get(splitted[0]) == null) {
					counts.put(splitted[0], 1);
				} else {
					counts.put(splitted[0], counts.get(splitted[0]) + 1);
				}
			}
 			
 			List<MappingDB> mappings = mappingDBRepository.findAll();
			for (MappingDB mapping: mappings) {
				Integer count = counts.get(mapping.getName());
				if (count != null) {
					mapping.setCount(count);
					mappingDBRepository.save(mapping);
				}
			}
		}
		
		// create BS mappings
		
		c = mappingBSRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctBS();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByBsIgnoreCase(name);
				MappingBS_BS mapping = new MappingBS_BS();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingBSRepository.save(mapping);
			}
		}
		
		c = mappingCRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctC();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByCIgnoreCase(name);
				MappingBS_C mapping = new MappingBS_C();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingCRepository.save(mapping);
			}
		}
		
		c = mappingCellineRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctCellline();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByCelllineIgnoreCase(name);
				MappingCellLine mapping = new MappingCellLine();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingCellineRepository.save(mapping);
			}
		}
		
		c = mappingCNRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctCN();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByCnIgnoreCase(name);
				MappingCN mapping = new MappingCN();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingCNRepository.save(mapping);
			}
		}
		
		c = mappingDiseaseRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctDisease();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByDiseaseIgnoreCase(name);
				MappingDisease mapping = new MappingDisease();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingDiseaseRepository.save(mapping);
			}
		}
		
		c = mappingDomainRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctDomain();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByDomainIgnoreCase(name);
				MappingDomain mapping = new MappingDomain();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingDomainRepository.save(mapping);
			}
		}
		
		c = mappingFRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctF();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByFIgnoreCase(name);
				MappingF mapping = new MappingF();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingFRepository.save(mapping);
			}
		}
		
		c = mappingGSRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctGS();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByGsIgnoreCase(name);
				MappingGS mapping = new MappingGS();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingGSRepository.save(mapping);
			}
		}
		
		c = mappingGTRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctGT();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByGtIgnoreCase(name);
				MappingGT mapping = new MappingGT();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingGTRepository.save(mapping);
			}
		}
		
		c = mappingKRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctK();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByKIgnoreCase(name);
				MappingK mapping = new MappingK();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingKRepository.save(mapping);
			}
		}
		
		c = mappingLSRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctLS();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByLsIgnoreCase(name);
				MappingLS mapping = new MappingLS();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingLSRepository.save(mapping);
			}
		}
		
		c = mappingORepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctO();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByOIgnoreCase(name);
				MappingO mapping = new MappingO();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingORepository.save(mapping);
			}
		}
		
		c = mappingOTRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctOT();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByOtIgnoreCase(name);
				MappingOT mapping = new MappingOT();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingOTRepository.save(mapping);
			}
		}
		
		c = mappingP_DRepository.count();
		if (c == 0) {
			List<String> distinctValues = bsRepository.findDistinctP_D();
			for (String name: distinctValues) {
				if (name == null || name.isEmpty()) 
					continue;
				long count = bsRepository.countByPdIgnoreCase(name);
				MappingP_D mapping = new MappingP_D();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingP_DRepository.save(mapping);
			}
		}
		
		Set<String> overlap = new HashSet<>();
		long pubCount = publicationRepository.count();
		List<CarbbankRecord> records = carbbankRepository.findAll();
		List <Publication> created = new ArrayList<>();
		for (CarbbankRecord record: records) {
			if (record.getStList() != null && !record.getStList().isEmpty()) {
				if (record.getBsList() != null && !record.getBsList().isEmpty()) {
					overlap.add(record.getCC());
				}
			}
		
			if (pubCount == 0) {
				// create publications
				Publication pub = new Publication();
				pub.setTitle (record.getTI());
				pub.setAuthor(record.getAU());
				pub.setJournal(record.getCT());
				if (!created.contains(pub)) {
					for (DB db: record.getDbList()) {
						if (db.getValue().toLowerCase().startsWith("pmid")) {
							String[] split = db.getValue().split(":");
							if (split.length > 1) {
								pub.setCarbbankPmid(split[1]);
								break;
							}
						}
					}
					created.add(pub);
					publicationRepository.save(pub);
				} 
			}
		}
		if (overlap.size() > 0) {
			logger.error("There is an overlap of ST and BS for records: " + overlap);
		}
	}
}
