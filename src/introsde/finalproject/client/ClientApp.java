package introsde.finalproject.client;

import introsde.finalproject.client.util.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	    		System.out.println("1  - Print person information");
	    		System.out.println("2  - Set health profile (and verify if the goal is hit)- sleep");
	    		System.out.println("3  - Set health profile (and verify if the goal is hit) - weight");
	    		System.out.println("4  - Set health profile (and verify if the goal is hit)- water");
	    		System.out.println("5  - Set health profile (and verify if the goal is hit)- steps");
	    		System.out.println("6  - Set goal - hours slept (at least 8 hours recommended per day)");
	    		System.out.println("7  - Set goal - new weight");
	    		System.out.println("8  - Set goal - litres of water (at least 3 litres recommended per day)");
	    		System.out.println("9  - Set goal - number of steps (at least 30000 recommended per day)");
	    		System.out.println("10 - Create measure - water");
	    		System.out.println("11 - Create goal - water");
	    		System.out.println("0  - Exit");
	    		
	    		System.out.print("\nHow do you want to proceed?\n");
	    		operation = Integer.parseInt(input.nextLine());
	    		
	    		if(operation < 0 || operation > 11){
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
	    	    		System.out.println("PERSON's HEALTH PROFILE");
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
	    				System.out.println("SET HEALTH PROFILE - sleeping hours");
	    	    		System.out.println("===============================================\n");
	    				
	    				System.out.println("Insert a new value: ");
	    				inputValueDouble = Double.parseDouble(input.nextLine()); //Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0 || inputValueDouble > 24){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
					//PCS -> PUT checkMeasure(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkMeasure/" + measureName;
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
	    				System.out.println("SET HEALTH PROFILE - weight");
	    	    		System.out.println("===============================================\n");
	    				
	    	    		System.out.println("Insert a new value: ");
	    				inputValueDouble = Double.parseDouble(input.nextLine()); //Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> PUT checkMeasure(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkMeasure/" + measureName;
			    	
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
	    				System.out.println("SET HEALTH PROFILE - water");
	    	    		System.out.println("===============================================\n");
	    				
	    	    		System.out.println("Insert a new value: ");
	    				inputValueDouble = Double.parseDouble(input.nextLine()); //Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> PUT checkMeasure(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkMeasure/" + measureName;
			    	
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
					
					//measureName --> steps
					measureName = "steps";
					inputValueInt = -1;
					
	    			while(inputValueInt < 0){
	    				System.out.println("===============================================");
	    				System.out.println("SET HEALTH PROFILE - steps");
	    	    		System.out.println("===============================================\n");
	    				
	    	    		System.out.println("Insert a new value: ");
	    				inputValueInt = Integer.parseInt(input.nextLine());
	    				if(inputValueInt < 0){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> PUT checkMeasure(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkMeasure/" + measureName;
			    	
			    	ClientConfig clientConfig5 = new ClientConfig();
					Client client5 = ClientBuilder.newClient(clientConfig5);
					
					WebTarget service5 = client5.target(processCentricURL + path);

			    	Response res5 = null;
					String putResp5 = null;
					
			    	String inputMeasureJSON5 ="{"
			        						+ "\"value\":" + inputValueInt
			        						+"}";
			    	
			    	res5 = service5.request(mediaType).put(Entity.json(inputMeasureJSON5));
			    	putResp5 = res5.readEntity(String.class);
		    		
			    	if(res5.getStatus() != 200 ){
			    		System.out.println("ERROR during updating! Please, try again!");
			    		
			    	}else{
			    		obj = new JSONObject(putResp5.toString());
			    		
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
					
					//measureName --> sleep
					measureName = "sleep";
					inputValueDouble = -1;
					
	    			while(inputValueDouble < 0 || inputValueDouble > 24){
	    				System.out.println("===============================================");
	    				System.out.println("SET GOAL - sleeping hours");
	    				System.out.println("===============================================\n");
	    				System.out.println("Insert a new value: ");
	    				inputValueDouble = Double.parseDouble(input.nextLine()); //Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0 || inputValueDouble > 24){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> PUT checkGoal(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkGoal/" + measureName;
					ClientConfig clientConfig6 = new ClientConfig();
					Client client6 = ClientBuilder.newClient(clientConfig6);
					
					WebTarget service6 = client6.target(processCentricURL + path);

			    	Response res6 = null;
					String putResp6 = null;
					
			    	String inputGoalJSON6 ="{"
			        				+ "\"value\": " + inputValueDouble
			        				+"}";
			    	
			    	res6 = service6.request(mediaType).put(Entity.json(inputGoalJSON6));
			    	putResp6 = res6.readEntity(String.class);
			    	
			    	if(res6.getStatus() != 200 ){
			    		System.out.println("ERROR during updating! Please, try again!");
			    		
			    	}else{
			    		System.out.println("Goal updated successfully!");
			    	}
					break;
					
				case 7:
					
					//measureName --> weight
					measureName = "weight";
					inputValueDouble = -1;
					
	    			while(inputValueDouble < 0){
	    				System.out.println("===============================================");
	    				System.out.println("SET GOAL - weight");
	    				System.out.println("===============================================\n");
	    				System.out.println("Insert a new value: ");
	    				inputValueDouble = Double.parseDouble(input.nextLine()); //Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0 || inputValueDouble > 1001){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> PUT checkGoal(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkGoal/" + measureName;
					ClientConfig clientConfig7 = new ClientConfig();
					Client client7 = ClientBuilder.newClient(clientConfig7);
					
					WebTarget service7 = client7.target(processCentricURL + path);

			    	Response res7 = null;
					String putResp7 = null;
					
			    	String inputGoalJSON7 ="{"
			        				+ "\"value\": " + inputValueDouble
			        				+"}";
			    	
			    	res7 = service7.request(mediaType).put(Entity.json(inputGoalJSON7));
			    	putResp7 = res7.readEntity(String.class);
			    	
			    	if(res7.getStatus() != 200 ){
			    		System.out.println("ERROR during updating! Please, try again!");
			    		
			    	}else{
			    		System.out.println("Goal updated successfully!");
			    	}
					break;	
				
				case 8:
					
					//measureName --> water
					measureName = "water";
					inputValueDouble = -1;
					
	    			while(inputValueDouble < 0){
	    				System.out.println("===============================================");
	    				System.out.println("SET GOAL - water");
	    				System.out.println("===============================================\n");
	    				System.out.println("Insert a new value: ");
	    				inputValueDouble = Double.parseDouble(input.nextLine()); //Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0 || inputValueDouble > 100){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> PUT checkGoal(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkGoal/" + measureName;
					ClientConfig clientConfig8 = new ClientConfig();
					Client client8 = ClientBuilder.newClient(clientConfig8);
					
					WebTarget service8 = client8.target(processCentricURL + path);

			    	Response res8 = null;
					String putResp8 = null;
					
			    	String inputGoalJSON8 ="{"
			        				+ "\"value\": " + inputValueDouble
			        				+"}";
			    	
			    	res8 = service8.request(mediaType).put(Entity.json(inputGoalJSON8));
			    	putResp8 = res8.readEntity(String.class);
			    	
			    	if(res8.getStatus() != 200 ){
			    		System.out.println("ERROR during updating! Please, try again!");
			    		
			    	}else{
			    		System.out.println("Goal updated successfully!");
			    	}
					break;
				
				case 9:
					
					//measureName --> steps
					measureName = "steps";
					inputValueInt = -1;
					
	    			while(inputValueInt < 0 || inputValueInt > 100000){
	    				System.out.println("===============================================");
	    				System.out.println("SET GOAL - steps");
	    				System.out.println("===============================================\n");
	    				System.out.println("Insert a new value: ");
	    				inputValueInt = Integer.parseInt(input.nextLine());
	    				if(inputValueInt < 0 || inputValueInt > 100000){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> PUT checkGoal(int idPerson, String measureName)
			    	path = "/person/" + idPerson + "/checkGoal/" + measureName;
					ClientConfig clientConfig9 = new ClientConfig();
					Client client9 = ClientBuilder.newClient(clientConfig9);
					
					WebTarget service9 = client9.target(processCentricURL + path);

			    	Response res9 = null;
					String putResp9 = null;
					
			    	String inputGoalJSON9 ="{"
			        				+ "\"value\": " + inputValueInt
			        				+"}";
			    	
			    	res9 = service9.request(mediaType).put(Entity.json(inputGoalJSON9));
			    	putResp9 = res9.readEntity(String.class);
			    	
			    	if(res9.getStatus() != 200 ){
			    		System.out.println("ERROR during updating! Please, try again!");
			    		
			    	}else{
			    		System.out.println("Goal updated successfully!");
			    	}
					break;
				
				case 10:

					//measureName --> water
					measureName = "water";
					inputValueDouble = -1;
					
	    			while(inputValueDouble < 0){
	    				System.out.println("===============================================");
	    				System.out.println("CREATE MEASURE - water");
	    				System.out.println("===============================================\n");
	    				System.out.println("Insert a new value: ");
	    				inputValueDouble = Double.parseDouble(input.nextLine()); //Integer.parseInt(input.nextLine());
	    				if(inputValueDouble < 0 || inputValueDouble > 50){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> POST insertNewMeasure(int idPerson, String inputMeasureJSON, String measureName)
			    	path = "/person/" + idPerson + "/insertNewMeasure/" + measureName;
					ClientConfig clientConfig10 = new ClientConfig();
					Client client10 = ClientBuilder.newClient(clientConfig10);
					
					WebTarget service10 = client10.target(processCentricURL);

			    	Response res10 = null;
					String postResp10 = null;
					
			    	String inputMeasureJSON10 ="{"
			    					+ "\"name\": " + measureName + ","
			        				+ "\"value\": " + inputValueDouble
			        				+"}";
			    	System.out.println(inputMeasureJSON10);
			    	
			    	res10 = service10.path(path).request().accept(mediaType)
							.post(Entity.entity(inputMeasureJSON10, mediaType),
									Response.class);
			    	postResp10 = res10.readEntity(String.class);
			    	
			    	System.out.println(postResp10.toString());
			    	
			    	if(res10.getStatus() != 200 ){
			    		System.out.println("ERROR during creating! Please, try again!");
			    		
			    	}else{
			    		obj = new JSONObject(postResp10.toString());
			    		
			    		System.out.println("Measure: " + obj.getJSONObject("measure").get("type"));
			    		System.out.println("ID: " + obj.getJSONObject("measure").get("id"));
    	    			System.out.println("Value: " + obj.getJSONObject("measure").get("value"));  		
			    	}
					break;
					
				case 11:
					
					//measureName --> water
					measureName = "water";
					inputValueDouble = -1;
					
					Date startDateGoal = null;
					Date endDateGoal = null;
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
					    
					boolean achieved = false;
					
	    			while(inputValueDouble < 0){
	    				System.out.println("===============================================");
	    				System.out.println("CREATE GOAL - water");
	    				System.out.println("===============================================\n");
	    				
	    				System.out.println("Insert a new value: ");
	    				inputValueDouble = Double.parseDouble(input.nextLine());
	    				
	    				try{
	    					System.out.println("Insert a new startDateGoal: ");
		    				startDateGoal = df.parse(input.nextLine());
					        //String newDateString = df.format(startDate); 
		    				
		    				System.out.println("Insert a new endDateGoal: ");
		    				endDateGoal = df.parse(input.nextLine());
		    				
	    				}catch(ParseException e){
	    					e.printStackTrace();
	    				}
	    				
	    				if(inputValueDouble < 0 || inputValueDouble > 50){
	    					System.out.println("Value not allowed! Please, try again!");
	    				}
	    				if(startDateGoal == null){
	    					System.out.println("StartDateGoal not allowed! Please, try again!");
	    				}
	    				if(endDateGoal == null){
	    					System.out.println("EndDateGoal not allowed! Please, try again!");
	    				}
	    			}
	    				
	    			//PCS -> POST insertNewGoal(int idPerson, String inputGoalJSON, String measureName)
			    	path = "/person/" + idPerson + "/insertNewGoal/" + measureName;
					ClientConfig clientConfig11 = new ClientConfig();
					Client client11 = ClientBuilder.newClient(clientConfig11);
					
					WebTarget service11 = client11.target(processCentricURL);

			    	Response res11 = null;
					String postResp11 = null;
					
			    	String inputGoalJSON11 ="{"
			    					+ "\"type\": " + measureName + ","
			        				+ "\"value\": " + inputValueDouble + ","
			        				+ "\"startDateGoal\": " + df.format(startDateGoal) + ","
			        				+ "\"endDateGoal\": " + df.format(endDateGoal) + ","
			        				+ "\"achieved\": " + achieved
			        				+"}";
			    	
			    	System.out.println(inputGoalJSON11);
			    	
			    	res11 = service11.path(path).request().accept(mediaType)
							.post(Entity.entity(inputGoalJSON11, mediaType),
									Response.class);
			    	postResp11 = res11.readEntity(String.class);
			    	
			    	System.out.println(postResp11.toString());
			    	
			    	if(res11.getStatus() != 200 ){
			    		System.out.println("ERROR during creating! Please, try again!");
			    		
			    	}else{
			    		obj = new JSONObject(postResp11.toString());
			    		
			    		System.out.println("Goal: " + obj.getJSONObject("goal").get("measure"));
			    		System.out.println("ID: " + obj.getJSONObject("goal").get("id"));
    	    			System.out.println("Value: " + obj.getJSONObject("goal").get("value"));
    	    			System.out.println("Achieved: " + obj.getJSONObject("goal").get("achieved"));
			    	}
					break;	
				}
			}
			
		}catch(Exception e){
			System.out.println("{ \n \"error\" : \"Error in Process Centric Services, due to the exception: "
					+ e + "\"}");
		}
	}
}
