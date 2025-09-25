package org.glygen.carbbank;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.glygen.carbbank.model.mapping.Mapping;
import org.glygen.carbbank.parser.CarbbankUtil;
import org.glygen.carbbank.parser.ComparisonUtil;
import org.glygen.carbbank.service.CarbbankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.ulisesbocchio.jasyptspringboot.environment.StandardEncryptableEnvironment;

@SpringBootApplication
public class CarbbankApplication {
	
	@Autowired
	CarbbankService service;
	
	public static void main(String[] args) {
		new SpringApplicationBuilder()
	    .environment(new StandardEncryptableEnvironment())
	    .sources(CarbbankApplication.class).run(args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup(ApplicationReadyEvent event) {
		ApplicationArguments args = event.getApplicationContext().getBean(ApplicationArguments.class);
		NamespaceHandler.loadNamespaces();
		if (args.containsOption("compare")) {
			List<String> tablenames = args.getOptionValues("compare");
			List<String> filenames = args.getOptionValues("file");
			List<Mapping> mappings = new ComparisonUtil().compareFiles(filenames, tablenames.get(0));
			//service.updateMappings(mappings, tablenames.get(0));
		} else if (args.containsOption("publication")) {
			List<String> filenames = args.getOptionValues("file");
			for (String filename: filenames) {
				service.addPublicationsFromFile(filename);
			}
		} else if (args.containsOption("file")) {
	    	List<String> carbbankFile = args.getOptionValues("file");
	    	if (!carbbankFile.isEmpty()) {
	    		try {
	    			List<Map<String, String>> records = CarbbankUtil.parseFile(carbbankFile.get(0));
	    			service.saveRecords(records);
	    	//		service.createMappingTables();
	    	//		service.addBSInformation();
	    	//		service.addPMIDs();
	    	//		service.generateExcelFiles();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    } else {
	    	// do not parse the file, only work on the mappings
	    	//service.createMappingTables();
	    	//service.addBSInformation();
	    	//service.findConflictsInSpecies();
	    	//service.addPMIDs();
	    	//service.generateExcelFiles();
	    	service.generateMappedExcel();
	    }
	}

}
