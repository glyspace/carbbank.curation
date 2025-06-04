package org.glygen.carbbank.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.glygen.carbbank.model.mapping.Mapping;
import org.glygen.carbbank.model.mapping.MappingCN;
import org.glygen.carbbank.model.mapping.MappingCellLine;
import org.glygen.carbbank.model.mapping.MappingDisease;
import org.glygen.carbbank.model.mapping.MappingGS;
import org.glygen.carbbank.model.mapping.MappingOT;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ComparisonUtil {
	
	static Logger logger = org.slf4j.LoggerFactory.getLogger(ComparisonUtil.class);

	public List<Mapping> compareFiles(List<String> fileList, String tablename) {
		
		String first = fileList.get(0);
		File file1 = new File (first);
		try {
			Workbook workbook = WorkbookFactory.create(file1);
			Sheet mappings = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = mappings.iterator();
			int count = 0;
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	            if (count == 0) {
	            	count = 1;
	            	continue;
	            } else {
	            	String id = row.getCell(0).getStringCellValue();
	            	String namespaceName = row.getCell(3).getStringCellValue();
	            	Cell namespaceIdCell = row.getCell(4);
	            	String namespaceId = null;
	            	if (namespaceIdCell.getCellType() == CellType.NUMERIC) {
	            		namespaceId = namespaceIdCell.getNumericCellValue() + "";
	            	} else {
	            		namespaceId = namespaceIdCell.getStringCellValue();
	            	}
	            	if (!namespaceName.isEmpty() || !namespaceId.isEmpty()) {
	            		// check if it agrees with other files
	            		boolean matchedAll = true;
	            		for (int i=1; i < fileList.size(); i++) {
	            			boolean matched = findInFile (fileList.get(i), id, namespaceId, namespaceName);
	            			if (!matched) {
	            				matchedAll = false;
	            			}
	            		}
	            		if (matchedAll) {
            				String mappingName = row.getCell(5).getStringCellValue();
            				String rank = null;
            				if (row.getCell(6) != null && row.getCell(6).getCellType() != CellType.NUMERIC) {
            					rank = row.getCell(6).getStringCellValue();
            				}
            				return updateDatabase (tablename, id, namespaceName, namespaceId, mappingName, rank);
            			} else {
            				// update progress status
            				return updateDatabase (tablename, id, null, null, null, null);
            			}
	            	}
	            }
	        }
		} catch (EncryptedDocumentException | IOException e) {
			logger.error("Error comparing files", e);
		} 
		
		return null;
	}

	private List<Mapping> updateDatabase(String tablename, String id, String namespaceName, String namespaceId,
			String mappingName, String rank) {
		
		List<Mapping> mappingList = new ArrayList<>();
		if (tablename.equalsIgnoreCase("mapping_bs_cn")) {
			Mapping mapping = new MappingCN();
			if (namespaceName == null && namespaceId == null) {
				mapping.setId(Long.parseLong(id));
				mapping.setInProgress(true);
			} else {
				mapping.setNamespaceId(namespaceId);
				mapping.setId(Long.parseLong(id));
				mapping.setNamespaceName(namespaceName);
				mapping.setMappingName(mappingName);
				((MappingCN) mapping).setRank(rank);
			}
			mappingList.add(mapping);
		} else if (tablename.equalsIgnoreCase("mapping_bs_gs")) {
			Mapping mapping = new MappingGS();
			if (namespaceName == null && namespaceId == null) {
				mapping.setId(Long.parseLong(id));
				mapping.setInProgress(true);
			} else {
				mapping.setNamespaceId(namespaceId);
				mapping.setId(Long.parseLong(id));
				mapping.setNamespaceName(namespaceName);
				mapping.setMappingName(mappingName);
				((MappingGS) mapping).setRank(rank);
			}
			mappingList.add(mapping);
		} else if (tablename.equalsIgnoreCase("mapping_bs_disease")) {
			Mapping mapping = new MappingDisease();
			if (namespaceName == null && namespaceId == null) {
				mapping.setId(Long.parseLong(id));
				mapping.setInProgress(true);
			} else {
				mapping.setNamespaceId(namespaceId);
				mapping.setId(Long.parseLong(id));
				mapping.setNamespaceName(namespaceName);
				mapping.setMappingName(mappingName);
			}
			mappingList.add(mapping);
		} else if (tablename.equalsIgnoreCase("mapping_bs_ot")) {
			Mapping mapping = new MappingOT();
			if (namespaceName == null && namespaceId == null) {
				mapping.setId(Long.parseLong(id));
				mapping.setInProgress(true);
			} else {
				mapping.setNamespaceId(namespaceId);
				mapping.setId(Long.parseLong(id));
				mapping.setNamespaceName(namespaceName);
				mapping.setMappingName(mappingName);
			}
			mappingList.add(mapping);
		} else if (tablename.equalsIgnoreCase("mapping_bs_cellline")) {
			Mapping mapping = new MappingCellLine();
			if (namespaceName == null && namespaceId == null) {
				mapping.setId(Long.parseLong(id));
				mapping.setInProgress(true);
			} else {
				mapping.setNamespaceId(namespaceId);
				mapping.setId(Long.parseLong(id));
				mapping.setNamespaceName(namespaceName);
				mapping.setMappingName(mappingName);
			}
			mappingList.add(mapping);
		}
		
		return mappingList;
	}

	private boolean findInFile(String filename, String id, String namespaceId, String namespaceName) throws EncryptedDocumentException, IOException {
		File file = new File (filename);
		
		Workbook workbook = WorkbookFactory.create(file);
		Sheet mappings = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = mappings.iterator();
		int count = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (count == 0) {
            	count = 1;
            	continue;
            } else {
            	String idInFile = row.getCell(0).getStringCellValue();
            	if (idInFile.equalsIgnoreCase(id)) {
            		String namespaceNameInFile = row.getCell(3).getStringCellValue();
	            	Cell namespaceIdCell = row.getCell(4);
	            	String namespaceIdInFile = null;
	            	if (namespaceIdCell.getCellType() == CellType.NUMERIC) {
	            		namespaceIdInFile = namespaceIdCell.getNumericCellValue() + "";
	            	} else {
	            		namespaceIdInFile = namespaceIdCell.getStringCellValue();
	            	}
	            	
	            	if (namespaceName != null && namespaceName.equalsIgnoreCase(namespaceNameInFile)) {
	            		return true;
	            	} 
	            	if (namespaceId != null && namespaceId.equalsIgnoreCase(namespaceIdInFile)) {
	            		return true;
	            	}       	
	            	break;
            	}
            }
        }
		
		return false;
	}
}
