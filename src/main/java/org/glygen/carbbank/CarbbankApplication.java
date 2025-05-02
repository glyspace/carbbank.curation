package org.glygen.carbbank;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.glygen.carbbank.parser.CarbbankUtil;
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
	    if (args.containsOption("file")) {
	    	List<String> carbbankFile = args.getOptionValues("file");
	    	if (!carbbankFile.isEmpty()) {
	    		try {
	    			List<Map<String, String>> records = CarbbankUtil.parseFile(carbbankFile.get(0));
	    			service.saveRecords(records);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	}

}
