package com.eot.banking.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RESTCall {
	
	public static ByteArrayOutputStream sendPOSTRequest(String payload, String mrhUrl) {
		//String httpURL = "http:/167.99.41.15:7070/EOT-Banking-MRH/"+url;
		//String httpURL = "http://localhost:8080/EOT-Banking-MRH/"+url;
		String httpURL = mrhUrl;
		
		//System.out.println(payload);

		HttpURLConnection connection;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			connection = (HttpURLConnection) new URL(httpURL).openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length", "" + payload.length());
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(300000);
			connection.getOutputStream().write(payload.getBytes());
			connection.getOutputStream().flush();
			int byteRead = 0;
			while ((byteRead = connection.getInputStream().read()) != -1) {
				buffer.write(byteRead);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
	public static ByteArrayOutputStream sendGETRequest(String mrhUrl) {
		
		String httpURL = mrhUrl;
		HttpURLConnection connection;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
	try {
		
		connection = (HttpURLConnection) new URL(httpURL).openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(300000);

		int byteRead = 0;
		while ((byteRead = connection.getInputStream().read()) != -1) {
			buffer.write(byteRead);
		}
		
		connection.disconnect();

	  } catch (MalformedURLException e) {

		e.printStackTrace();

	  } catch (IOException e) {

		e.printStackTrace();

	  }
		return buffer;
	}

}
