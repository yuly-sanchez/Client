package introsde.finalproject.client;

import introsde.finalproject.client.util.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONObject;

public class ClientApp {

	private String errorMessage(Exception e) {
		return "{ \n \"error\" : \"Error in Business Logic Services, due to the exception: "
				+ e + "\"}";
	}
	
	public static void main(String[] args) {
		
		try{
			
			Scanner input = new Scanner(System.in);

			String targetName = "";
			String measureName = "";
			int operation = -1;
			int idPerson = -1;
			boolean targetID = false;
			double inputValueDouble = -1;
			int inputValueInt = -1;
			
			String mediaType = MediaType.APPLICATION_JSON;
			
			UrlInfo urlInfo = new UrlInfo();
			String storageURL = urlInfo.getStorageURL();
			String businessLogicURL = urlInfo.getBusinesslogicURL();
			String processCentricURL = urlInfo.getProcesscentricURL();
			
			System.out.println("\n" + "\t\t\tWelcome to LIFECOACH SYSTEM!" + "\n");
			
			//SS -> GET getPersonList()
			String path = "/person";

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(storageURL + path);
			HttpResponse response = httpClient.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			JSONObject obj = new JSONObject(result.toString());

			while (targetID == false) {
				
				System.out.println("Choose one person from the table below: ");
				
				System.out.println ("---------------------------------------------------------------------------------------");
				System.out.format("%5s%20s%20s%15s", "ID", "First Name", "Last Name", "Birthdate");
				System.out.println ("\n-------------------------------------------------------------------------------------");
				
				if (response.getStatusLine().getStatusCode() == 200) {
					
					for (int i=0; i<obj.getJSONArray("person").length(); i++) {
						System.out.println(String.format("%5d%20s%20s%15s",
								obj.getJSONArray("person").getJSONObject(i).get("pid"),
								obj.getJSONArray("person").getJSONObject(i).get("firstname"),
								obj.getJSONArray("person").getJSONObject(i).get("lastname"),
								obj.getJSONArray("person").getJSONObject(i).get("birthdate")));
					}
				}

				System.out.println("\nChoose a person: ");
				idPerson = Integer.parseInt(input.nextLine());

				for (int i=0; i<obj.getJSONArray("person").length(); i++) {
					if (Integer.parseInt(obj.getJSONArray("person").getJSONObject(i).get("pid").toString()) == idPerson){
						targetID = true;
						targetName = obj.getJSONArray("person").getJSONObject(i).getString("lastname")
									+ " " + obj.getJSONArray("person").getJSONObject(i).getString("firstname");
					}
				}

				if (targetID == false) {
					System.out.println("\nWrong choice! Please, try again!\n");
				}
			}

			System.out.println("Well-done! You have chosen to run the program as "
					+ targetName);
			System.out.println("Loading ... ");

			System.out.println("\n\nWelcome back " + targetName + "!");
			System.out
					.println("Please, choose an operation from the MENU' below to proceed\n");

			while(operation != 0){
				
				System.out.println("\nMENU'\n");
	    		System.out.println("1 - Print person information");
	    		System.out.println("2 - Set currentHealth profile (and verify if the goal is hit)- sleep");
	    		System.out.println("3 - Set currentHealth profile (and verify if the goal is hit) - weight");
	    		System.out.println("4 - Set currentHealth profile (and verify if the goal is hit)- water");
	    		System.out.println("5 - Set currentHealth profile (and verify if the goal is hit)- steps");
	    		System.out.println("6 - Set goal - hours slept the previous night");
	    		System.out.println("7 - Set goal - new weight");
	    		System.out.println("8 - Set goal - litres of water you should drink");
	    		System.out.println("9 - Print currentGoals information");
	    		System.out.println("0 - Exit");
	    		
	    		System.out.print("\nHow do you want to proceed?\n");
	    		operation = Integer.parseInt(input.nextLine());
	    		
	    		if(operation < 0 || operation > 8){
	    			System.out.print("\nOperation not allowed, try again!\n\n");
	    		}
	    		
	    		switch (operation) {
				case 0:
					System.out.println("\nThank you for using this application!");
					break;

				case 1:
					//BLS -> GET readPersonDetails(int idPerson)
					path = "/person/" + idPerson;
	    	    	
	    	    	httpClient = new DefaultHttpClient();
	    	    	request = new HttpGet(businessLogicURL + path);
	    	    	response = httpClient.execute(request);
	    	    	
	    	    	rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	    	    	result = new StringBuffer();
	    	    	line = "";
	    	    	while ((line = rd.readLine()) != null) {
	    	    	    result.append(line);
	    	    	}
	    	    	
	    	    	obj = new JSONObject(result.toString());
	    	    	
	    	    	if(response.getStatusLine().getStatusCode() == 200){
	    	    		
	    	    		System.out.println("===============================================");
	    	    		System.out.println("PERSON's INFORMATION");
	    	    		System.out.println("===============================================");
	    	    		
	    	    		System.out.println("Name: " + obj.getString("firstname"));
	    	    		System.out.println("Surname: " + obj.getString("lastname"));
	    	    		System.out.println("E-mail: " + obj.getString("email"));
	    	    		System.out.println("Birthdate: " + obj.getString("birthdate"));
	    	    		System.out.println("Gender: " + obj.getString("gender"));
	    	    		
	    	    		System.out.println("===============================================");
	    	    		System.out.println("PERSON's CURRENT HEALTH PROFILE");
	    	    		System.out.println("===============================================");
	    	    		
	    	    		for(int i = 0; i < obj.getJSONObject("currentHealth").getJSONArray("measure").length(); i++){
	    	    			System.out.println("Measure: " + obj.getJSONObject("currentHealth").getJSONArray("measure").getJSONObject(i).get("name"));
	    	    			System.out.println("Value: " + obj.getJSONObject("currentHealth").getJSONArray("measure").getJSONObject(i).get("value"));
	        	    		System.out.println();
	    	            }
	    	    		
	    	    		System.out.println("===============================================");
	    	    		System.out.println("PERSONS's GOALS");
	    	    		System.out.println("===============================================");
	    	    		
	    	    		for(int i = 0; i < obj.getJSONObject("goals").getJSONArray("goal").length(); i++){
	    	    			System.out.println("Goal: " + obj.getJSONObject("goals").getJSONArray("goal").getJSONObject(i).get("type"));
	    	    			System.out.println("Value: " + obj.getJSONObject("goals").getJSONArray("goal").getJSONObject(i).get("value"));
	    	    			System.out.println("Achieved: " + obj.getJSONObject("goals").getJSONArray("goal").getJSONObject(i).get("achieved"));
	        	    		System.out.println("");
	    	            }
	    	    		
	    	    	}
					break;
				
				case 2:
					
					//measureName --> sleep
					measureName = "sleep";
					inputValueDouble = -1;
					
	    			while(inputValueDouble < 0 || inputValueDouble > 24){
	    				System.out.println("===============================================");
	    				System.out.println("HEALTH PROFILE - sleeping hours");
	    	    		System.out.println("===============================================\n");
	    				
	    				System.out.println("Insert a new value: ");
	    				inputValueDouble = Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0 || inputValueDouble > 24){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
					//PCS -> PUT checkCurrentHealth(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkCurrentHealth/" + measureName;
			    	ClientConfig clientConfig = new ClientConfig();
					Client client = ClientBuilder.newClient(clientConfig);
					
					WebTarget service = client.target(processCentricURL + path);

			    	Response res = null;
					String putResp = null;
					
			    	String inputMeasureJSON ="{"
			        						+ "\"value\":" + inputValueDouble
			        						+"}";
			    	
			    	res = service.request(mediaType).put(Entity.json(inputMeasureJSON));
			    	putResp = res.readEntity(String.class);
			    	
			    	if(res.getStatus() != 200 ){
			    		System.out.println("ERROR during updating! Please, try again!");
			    		
			    	}else{
			    		obj = new JSONObject(putResp.toString());
			    		
			    		System.out.println("sleep - updated measure => " + obj.getJSONObject("comparisonInformation").get("measureValue"));
	    	    		System.out.println("sleep - goal set => " + obj.getJSONObject("comparisonInformation").get("goalValue"));
	    	    		
	    	    		String resp = obj.getJSONObject("comparisonInformation").getString("result");
	    	    		
	    	    		if(resp.equals("ok")){
	    	    			System.out.println("\nCONGRATULATION! GOAL HIT!!!");
	    	    			String picture_url = obj.getJSONObject("resultInformation").getString("picture_url");
	    	    			System.out.println("Pretty picture for your success: " + picture_url);
	    	    			String motivation = obj.getJSONObject("resultInformation").getString("quote");
	    	    			System.out.println("Motivational phrase: " + motivation);
	    	    		}
	    	    		else{
	    	    			System.out.println("\nGOAL NOT HIT!!!");
	    	    			String picture_url = obj.getJSONObject("resultInformation").getString("picture_url");
	    	    			System.out.println("Pretty picture for your success: " + picture_url);
	    	    			String motivation = obj.getJSONObject("resultInformation").getString("quote");
	    	    			System.out.println("Motivational phrase: " + motivation);
	    	    		}
			    	}
	    	    	break;
					
				case 3:
					
					//measureName --> weight
					measureName = "weight";
					inputValueDouble = -1;
					
	    			while(inputValueDouble < 0){
	    				System.out.println("===============================================");
	    				System.out.println("HEALTH PROFILE - weight");
	    	    		System.out.println("===============================================\n");
	    				
	    	    		System.out.println("Insert a new value: ");
	    				inputValueDouble = Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> PUT checkCurrentHealth(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkCurrentHealth/" + measureName;
			    	
			    	ClientConfig clientConfig3 = new ClientConfig();
					Client client3 = ClientBuilder.newClient(clientConfig3);
					
					WebTarget service3 = client3.target(processCentricURL + path);

			    	Response res3 = null;
					String putResp3 = null;
					
			    	String inputMeasureJSON3 ="{"
			        						+ "\"value\":" + inputValueDouble
			        						+"}";
			    	
			    	res3 = service3.request(mediaType).put(Entity.json(inputMeasureJSON3));
			    	putResp3 = res3.readEntity(String.class);
		    		
			    	if(res3.getStatus() != 200 ){
			    		System.out.println("ERROR during updating! Please, try again!");
			    		
			    	}else{
			    		obj = new JSONObject(putResp3.toString());
			    		
			    		System.out.println("weight - updated measure => " + obj.getJSONObject("comparisonInformation").get("measureValue"));
	    	    		System.out.println("weight - goal set => " + obj.getJSONObject("comparisonInformation").get("goalValue"));

	    	    		String resp = obj.getJSONObject("comparisonInformation").getString("result");
	    	    		
	    	    		if(resp.equals("ok")){
	    	    			System.out.println("\nCONGRATULATION! GOAL HIT!!!");
	    	    			String picture_url = obj.getJSONObject("resultInformation").getString("picture_url");
	    	    			System.out.println("Pretty picture for your success: " + picture_url);
	    	    			String motivation = obj.getJSONObject("resultInformation").getString("quote");
	    	    			System.out.println("Motivational phrase: " + motivation);
	    	    		}
	    	    		else{
	    	    			System.out.println("\nGOAL NOT HIT!!!");
	    	    			String picture_url = obj.getJSONObject("resultInformation").getString("picture_url");
	    	    			System.out.println("Pretty picture for your success: " + picture_url);
	    	    			String motivation = obj.getJSONObject("resultInformation").getString("quote");
	    	    			System.out.println("Motivational phrase: " + motivation);
	    	    		}
			    	}
					break;
					
				case 4:
					
					//measureName --> water
					measureName = "water";
					inputValueDouble = -1;
					
	    			while(inputValueDouble < 0){
	    				System.out.println("===============================================");
	    				System.out.println("HEALTH PROFILE - water");
	    	    		System.out.println("===============================================\n");
	    				
	    	    		System.out.println("Insert a new value: ");
	    				inputValueDouble = Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> PUT checkCurrentHealth(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkCurrentHealth/" + measureName;
			    	
			    	ClientConfig clientConfig4 = new ClientConfig();
					Client client4 = ClientBuilder.newClient(clientConfig4);
					
					WebTarget service4 = client4.target(processCentricURL + path);

			    	Response res4 = null;
					String putResp4 = null;
					
			    	String inputMeasureJSON4 ="{"
			        						+ "\"value\":" + inputValueDouble
			        						+"}";
			    	
			    	res4 = service4.request(mediaType).put(Entity.json(inputMeasureJSON4));
			    	putResp4 = res4.readEntity(String.class);
		    		
			    	if(res4.getStatus() != 200 ){
			    		System.out.println("ERROR during updating! Please, try again!");
			    		
			    	}else{
			    		obj = new JSONObject(putResp4.toString());
			    		
			    		System.out.println("water - updated measure => " + obj.getJSONObject("comparisonInformation").get("measureValue"));
	    	    		System.out.println("water - goal set => " + obj.getJSONObject("comparisonInformation").get("goalValue"));

	    	    		String resp = obj.getJSONObject("comparisonInformation").getString("result");
	    	    		
	    	    		if(resp.equals("ok")){
	    	    			System.out.println("\nCONGRATULATION! GOAL HIT!!!");
	    	    			String picture_url = obj.getJSONObject("resultInformation").getString("picture_url");
	    	    			System.out.println("Pretty picture for your success: " + picture_url);
	    	    			String motivation = obj.getJSONObject("resultInformation").getString("quote");
	    	    			System.out.println("Motivational phrase: " + motivation);
	    	    		}
	    	    		else{
	    	    			System.out.println("\nGOAL NOT HIT!!!");
	    	    			String picture_url = obj.getJSONObject("resultInformation").getString("picture_url");
	    	    			System.out.println("Pretty picture for your success: " + picture_url);
	    	    			String motivation = obj.getJSONObject("resultInformation").getString("quote");
	    	    			System.out.println("Motivational phrase: " + motivation);
	    	    		}
			    	}
					break;
					
				case 5:
					
					/**
					 * while(value3 < 0 || value3 > 1000000){
			  				System.out.println("Goal - steps: ");
			  				System.out.println("Insert new value: ");
			  				value3 = Integer.parseInt(input.nextLine());
			  				if(value3 < 0 || value3 > 1000000){
			  					System.out.println("Value not allowed! Please, try again!");
			  				}
			  			}
					 */
					
					//measureName --> steps
					measureName = "steps";
					inputValueInt = -1;
					
	    			while(inputValueDouble < 0){
	    				System.out.println("===============================================");
	    				System.out.println("HEALTH PROFILE - water");
	    	    		System.out.println("===============================================\n");
	    				
	    	    		System.out.println("Insert a new value: ");
	    				inputValueDouble = Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> PUT checkCurrentHealth(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkCurrentHealth/" + measureName;
			    	
			    	ClientConfig clientConfig4 = new ClientConfig();
					Client client4 = ClientBuilder.newClient(clientConfig4);
					
					WebTarget service4 = client4.target(processCentricURL + path);

			    	Response res4 = null;
					String putResp4 = null;
					
			    	String inputMeasureJSON4 ="{"
			        						+ "\"value\":" + inputValueDouble
			        						+"}";
			    	
			    	res4 = service4.request(mediaType).put(Entity.json(inputMeasureJSON4));
			    	putResp4 = res4.readEntity(String.class);
		    		
			    	if(res4.getStatus() != 200 ){
			    		System.out.println("ERROR during updating! Please, try again!");
			    		
			    	}else{
			    		obj = new JSONObject(putResp4.toString());
			    		
			    		System.out.println("water - updated measure => " + obj.getJSONObject("comparisonInformation").get("measureValue"));
	    	    		System.out.println("water - goal set => " + obj.getJSONObject("comparisonInformation").get("goalValue"));

	    	    		String resp = obj.getJSONObject("comparisonInformation").getString("result");
	    	    		
	    	    		if(resp.equals("ok")){
	    	    			System.out.println("\nCONGRATULATION! GOAL HIT!!!");
	    	    			String picture_url = obj.getJSONObject("resultInformation").getString("picture_url");
	    	    			System.out.println("Pretty picture for your success: " + picture_url);
	    	    			String motivation = obj.getJSONObject("resultInformation").getString("quote");
	    	    			System.out.println("Motivational phrase: " + motivation);
	    	    		}
	    	    		else{
	    	    			System.out.println("\nGOAL NOT HIT!!!");
	    	    			String picture_url = obj.getJSONObject("resultInformation").getString("picture_url");
	    	    			System.out.println("Pretty picture for your success: " + picture_url);
	    	    			String motivation = obj.getJSONObject("resultInformation").getString("quote");
	    	    			System.out.println("Motivational phrase: " + motivation);
	    	    		}
			    	}

					break;
					
				case 6:
					break;
					
				case 7:
					break;	
				
				case 8:
					break;
				}
			}
			
		}catch(Exception e){
			System.out.println("{ \n \"error\" : \"Error in Business Logic Services, due to the exception: "
					+ e + "\"}");
		}
	}
	
	public static String center (String s, int length) {
	    if (s.length() > length) {
	        return s.substring(0, length);
	    } else if (s.length() == length) {
	        return s;
	    } else {
	        int leftPadding = (length - s.length()) / 2; 
	        StringBuilder leftBuilder = new StringBuilder();
	        for (int i = 0; i < leftPadding; i++) {
	            leftBuilder.append(" ");
	        }

	        int rightPadding = length - s.length() - leftPadding;
	        StringBuilder rightBuilder = new StringBuilder();
	        for (int i = 0; i < rightPadding; i++) 
	            rightBuilder.append(" ");

	        return leftBuilder.toString() + s 
	                + rightBuilder.toString();
	    }
	}
}
