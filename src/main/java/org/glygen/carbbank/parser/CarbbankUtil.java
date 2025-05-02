package org.glygen.carbbank.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CarbbankUtil {
	
	public static List<Map<String, String>> parseFile(String filePath) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        Map<String, String> currentRecord = null;
        String currentKey = null;
        StringBuilder currentValue = new StringBuilder();
        Map<String, Integer> subKeyIndexMap = new HashMap<>();
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("; start of record")) {
                currentRecord = new HashMap<>();
                for (String key: subKeyIndexMap.keySet()) {
                	subKeyIndexMap.put(key, -1);
                }
            } else if (line.startsWith("================end of record")) {
                if (currentRecord != null && currentKey != null) {
                    currentRecord.put(currentKey, currentValue.toString().trim());
                }
                if (currentRecord != null) {
                    records.add(currentRecord);
                }
                currentKey = null;
                currentValue.setLength(0);
            } else if (currentRecord != null) {
            	int colonIndex = line.indexOf(':');
            	boolean spaceFound = false;
            	if (colonIndex != -1 && colonIndex == 2) {
            		String space = line.substring(colonIndex+1, colonIndex+2);
            		if (space.equals(" ")) spaceFound = true;
            	}
            	
            	if (line.startsWith("structure:"))
            		spaceFound = true;
            	
            	if (colonIndex != -1 && (colonIndex == 2 || line.startsWith("structure:")) 
            			&& spaceFound) {
                    if (currentKey != null) {
                        currentRecord.put(currentKey, currentValue.toString().trim());  
                    }
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2 && (parts[0].trim().equals("structure") || parts[0].trim().length() == 2)) {
                        currentKey = parts[0].trim();
                        if (subKeyIndexMap.get(currentKey) == null) {
                        	subKeyIndexMap.put(currentKey, -1);
                        }
                        currentValue.setLength(0);
                        currentValue.append(parts[1].trim());
                        if (currentKey.startsWith("BS")) {
                    		parseMultiField(currentRecord, currentKey, currentValue.toString(), subKeyIndexMap.get("BS"));
	                        currentKey = null;
	                        currentValue.setLength(0);
	                        if (subKeyIndexMap.get("BS") == -1) subKeyIndexMap.put ("BS", 2);
	                		else subKeyIndexMap.put("BS", subKeyIndexMap.get("BS") + 1);
                    	} 
                        else {
                        	if (currentRecord.get(currentKey) != null) {
                        		if (subKeyIndexMap.get(currentKey) == -1) subKeyIndexMap.put (currentKey, 2);
    	                		else subKeyIndexMap.put(currentKey, subKeyIndexMap.get(currentKey) + 1);
                        		currentKey += subKeyIndexMap.get(currentKey);
                            }
                        }
                    }
                } else if (currentKey != null) {
                	if (!line.contains("------")) {
                		currentValue.append("\n").append(line);
                	} 
                }
            }
        }
        reader.close();
        return records;
    }

    
    private static void parseMultiField(Map<String, String> record, String key, String value, Integer subKeyIndex) {
    	String[] subEntries = value.split(", ");
    	for (String subEntry : subEntries) {
    		int startIndex = subEntry.indexOf('(');
    		int endIndex = subEntry.indexOf(')');
    		if (startIndex != -1 && endIndex != -1) {
    			String sub2 = subEntry.substring(startIndex + 1, endIndex);
    			if (sub2.equals("*")) {
    				sub2 = "star";
    			}
    			String subKey = key + (subKeyIndex == -1 ? "1": subKeyIndex) + "_" + sub2.toLowerCase() ;
    			String subValue = subEntry.substring(endIndex + 1).trim();
    			if (record.get(subKey) != null) {
    				record.put(subKey, value);
    			} else {
    				record.put(subKey, subValue);
    			}
    		}
    	}
    }
    
    
    public static void main(String[] args) {
        String filePath = "/Users/sena/Downloads/carbbank.txt"; 
        Set<String> patterns = new TreeSet<>();
        try {
            List<Map<String, String>> records = parseFile(filePath);
            
            filePath = "/Users/sena/Downloads/carbbank-processed.txt";
	        
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
				for (Map<String, String> record : records) {
					writer.append("Record:\n");
	                for (Map.Entry<String, String> entry : record.entrySet()) {
	                	String pattern = entry.getKey();
	                	if (!pattern.startsWith("AG") &&
		                		!pattern.startsWith("AN") &&
		                		!pattern.startsWith("PA") &&
		                		!pattern.startsWith("SC") &&
		                		!pattern.startsWith("VR") &&
		                		!pattern.startsWith("ST") &&
		                		!pattern.startsWith("TN") &&
		                		!pattern.startsWith("PM") &&
		                		!pattern.startsWith("NT") &&
		                		!pattern.startsWith("NC") &&
		                		!pattern.startsWith("MT") &&
		                		!pattern.startsWith("DB") &&
		                		!pattern.startsWith("AM")) {
	                		patterns.add(pattern);
	                	}
	                    writer.append(entry.getKey() + ": " + entry.getValue() + "\n");
	                }
	                writer.append("----------------\n");
	            }
			    System.out.println("Successfully wrote to the file.");
			} catch (IOException e) {
			     e.printStackTrace();
			}
			
            System.out.println ("List of patterns");
            for (String pattern: patterns) {
            	if (!pattern.startsWith("BS")) {
            		System.out.println (pattern);
            	}
            }
            
            Set<String> bsPatterns = new TreeSet<>();
            for (String pattern: patterns) {
            	if (pattern.startsWith("BS")) {
            		bsPatterns.add(pattern.substring(pattern.indexOf("_")+1));
            	}
            }
            
            System.out.println ("List of BS patterns");
            for (String pattern: bsPatterns) {
            	System.out.println (pattern);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
