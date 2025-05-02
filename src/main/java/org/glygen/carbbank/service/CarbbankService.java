package org.glygen.carbbank.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glygen.carbbank.dao.CarbbankRepository;
import org.glygen.carbbank.model.AG;
import org.glygen.carbbank.model.AM;
import org.glygen.carbbank.model.AN;
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
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CarbbankService {
	
	final private CarbbankRepository carbbankRepository;
	
	String[] excludeList = {"AG", "AM", "AN", "BS", "DB", "MT", "NC", "NT", "PA", "SC", "ST", "TN", "VR"}; 
	
	public CarbbankService(CarbbankRepository carbbankRepository) {
		this.carbbankRepository = carbbankRepository;
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
    			
    			String col = entry.getKey();
    			col = col.replaceAll(" ", "_");
    			col = col.replaceAll("/", "_");
    			
    			if (col.equals("AU")) {
    				carbRecord.setAU(value);
    			} else if (col.equals("BA")) {
    				carbRecord.setBA(value);
    			} else if (col.equals("BA2")) {
    				carbRecord.setBA2(value);
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
    				String number = col.substring(3, col.indexOf("_"));
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
}
