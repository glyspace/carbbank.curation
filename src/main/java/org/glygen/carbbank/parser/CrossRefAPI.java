package org.glygen.carbbank.parser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.glygen.carbbank.model.mapping.Publication;
import org.json.JSONArray;
import org.json.JSONObject;

public class CrossRefAPI {
	
    String apiUrl = "https://api.crossref.org/works?query.title=";

    public List<Publication> getPublicationByTitle(String title) throws Exception {
    	List<Publication> results = new ArrayList<>();
    	title = title.replaceAll("\n", " ");
    	String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
    	String url = apiUrl + encodedTitle;
    	HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();
        
        JSONObject obj = new JSONObject(json);
        JSONArray items = obj.getJSONObject("message").getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
        	JSONObject item = items.getJSONObject(i);
            String doi = item.getString("DOI");
            System.out.println("DOI: " + doi);
            
            if (item.has("container-title")) {
	            String journalName = item.getJSONArray("container-title").getString(0);
	            System.out.println("Journal: " + journalName);
            }
            
            int year = item.getJSONObject("issued").getJSONArray("date-parts").getJSONArray(0).getInt(0);
            System.out.println("Year: " + year);
            
            if (item.has("volume")) {
            	String volume = item.getString("volume");
            	System.out.println("Volume: " + volume);
            }
            
            if (item.has("author")) {
	            JSONArray authors = item.getJSONArray("author");
	            System.out.print("Authors: ");
	            for (int j = 0; j < authors.length(); j++) {
	            	if (authors.getJSONObject(j).has("given")) {
		                String fullName = authors.getJSONObject(j).getString("given") + " " + authors.getJSONObject(j).getString("family");
		                System.out.print(fullName);
		                if (j < authors.length() - 1) System.out.print(", ");
	            	}
	            }
	            System.out.println();
            }
        }
        
        return results;
    }
    
    public static void main(String[] args) {
    	try {
    		new CrossRefAPI().getPublicationByTitle("Oligosaccharins: Oligosaccharide regulatory molecules");
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	}
}
