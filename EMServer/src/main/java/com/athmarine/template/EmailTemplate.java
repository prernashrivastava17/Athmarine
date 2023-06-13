package com.athmarine.template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;


public class EmailTemplate {

		
		private String template;
		//private Map<String, String> replacementParams;
		public EmailTemplate(String customtemplate) { 
			
		    try {
			   this.template = loadTemplate(customtemplate);
			} catch (Exception e) {
			   this.template = "Empty";
			}
		
	}
	private String loadTemplate(String customtemplate) throws Exception {
		
			
			File file = new ClassPathResource(customtemplate).getFile();
			
			String content = "Empty";
			try {
				content = new String(Files.readAllBytes(file.toPath()));
			} catch (IOException e) {
				throw new Exception("Could not read template  = " + customtemplate+e.getMessage());
			}
			return content;
			
	}
	public String getTemplate(Map<String, String> replacements) {
		
			String cTemplate = this.template;
			//Replace the String 
			for (Map.Entry<String, String> entry : replacements.entrySet()) {
				cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
			}
			return cTemplate;
	    }
	}


