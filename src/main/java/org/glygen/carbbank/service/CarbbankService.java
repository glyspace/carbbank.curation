package org.glygen.carbbank.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.glygen.carbbank.dao.AGRepository;
import org.glygen.carbbank.dao.AMRepository;
import org.glygen.carbbank.dao.ANRepository;
import org.glygen.carbbank.dao.BARepository;
import org.glygen.carbbank.dao.CarbbankRepository;
import org.glygen.carbbank.dao.DBRepository;
import org.glygen.carbbank.dao.MTRepository;
import org.glygen.carbbank.dao.MappingAGRepository;
import org.glygen.carbbank.dao.MappingAMRepository;
import org.glygen.carbbank.dao.MappingANRepository;
import org.glygen.carbbank.dao.MappingBARepository;
import org.glygen.carbbank.dao.MappingDBRepository;
import org.glygen.carbbank.dao.MappingMTRepository;
import org.glygen.carbbank.dao.MappingPARepository;
import org.glygen.carbbank.dao.MappingPMRepository;
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
import org.glygen.carbbank.model.mapping.MappingDB;
import org.glygen.carbbank.model.mapping.MappingMT;
import org.glygen.carbbank.model.mapping.MappingPA;
import org.glygen.carbbank.model.mapping.MappingPM;
import org.glygen.carbbank.model.mapping.MappingTN;
import org.glygen.carbbank.model.mapping.Publication;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CarbbankService {
	
	static Logger logger = org.slf4j.LoggerFactory.getLogger(CarbbankService.class);
	
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
			DBRepository dbRepository, PublicationRepository publicationRepository) {
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
    					bs.setCell_line(e.getValue());
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
    					bs.setP_d(e.getValue());
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
				long count = agRepository.countByValue(name);
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
				long count = amRepository.countByValue(name);
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
				long count = anRepository.countByValue(name);
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
				long count = mtRepository.countByValue(name);
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
				long count = pmRepository.countByValue(name);
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
				long count = tnRepository.countByValue(name);
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
				long count = baRepository.countByValue(name);
				MappingBA mapping = new MappingBA();
				mapping.setCount(Long.valueOf(count).intValue());
				mapping.setName(name);
				
				mappingBARepository.save(mapping);
			}
		}
		
		c = mappingPARepository.count();
		if (c == 0) {
			List<String> distinctValues = paRepository.findDistinctValue();
			Set<String> processed = new HashSet<>();
			for (String name: distinctValues) {
				long count = paRepository.countByValue(name);
				String[] splitted = name.split(",");
				for (String split: splitted) {
					split = split.trim();
					if (split.contains("-")) {
						split = split.substring(0, split.indexOf("-"));
						split = split.trim();
					}
					if (!processed.contains(split)) {
						processed.add(split);
						MappingPA mapping = new MappingPA();
						mapping.setCount(Long.valueOf(count).intValue());
						mapping.setName(split);
						mappingPARepository.save(mapping);
					}
				}
			}
		}
		
		c = mappingDBRepository.count();
		if (c == 0) {
			List<String> distinctValues = dbRepository.findDistinctValue();
			Set<String> processed = new HashSet<>();
 			for (String name: distinctValues) {
				String[] splitted = name.split(":");
				long count = dbRepository.countByValue(name);
				if (!processed.contains(splitted[0])) {
					processed.add(splitted[0]);
					MappingDB mapping = new MappingDB();
					mapping.setCount(Long.valueOf(count).intValue());
					mapping.setName(splitted[0]);
					mappingDBRepository.save(mapping);
				}
			}
		}
		
		Set<String> overlap = new HashSet<>();
		Set<String> mismatch = new HashSet<>();
		long pubCount = publicationRepository.count();
		List<CarbbankRecord> records = carbbankRepository.findAll();
		Map<String, Publication> created = new HashMap<>();
		for (CarbbankRecord record: records) {
			if (record.getStList() != null && !record.getStList().isEmpty()) {
				if (record.getBsList() != null && !record.getBsList().isEmpty()) {
					overlap.add(record.getCC());
				}
			}
		
			if (pubCount == 0) {
				// create publications
				Publication p = created.get(record.getTI());
				if (p == null) {
					Publication pub = new Publication();
					pub.setTitle (record.getTI());
					pub.setAuthor(record.getAU());
					pub.setJournal(record.getCT());
					for (DB db: record.getDbList()) {
						if (db.getValue().toLowerCase().startsWith("pmid")) {
							String[] split = db.getValue().split(":");
							if (split.length > 1) {
								pub.setCarbbankPmid(split[1]);
								break;
							}
						}
					}
					created.put(pub.getTitle(), pub);
					publicationRepository.save(pub);
				} else {
					// check if other fields match
					if ((p.getAuthor() != null && !p.getAuthor().equalsIgnoreCase(record.getAU())) || (p.getJournal() != null && !p.getJournal().equalsIgnoreCase(record.getCT()))) {
						mismatch.add(record.getCC());
					}
					
				}
			}
		}
		if (overlap.size() > 0) {
			logger.error("There is an overlap of ST and BS for records: " + overlap);
		}
		if (mismatch.size() > 0) {
			logger.error("Publication details do not match for records:  " + mismatch);
		}

	}
}
