package com.heinrich;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class ShaUtils {
	
	public static void main(String args[]) {
		String p_Secret = "p_Secret";
		String p_Method = "GET";
		String p_Endpoint = "/api/resource";
		String p_Body = "";
		
		// Convert DateTime to RFC
		DateTimeFormatter formattedDateTime = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss O");
		String formattedDateTimeWithTimeZone = formattedDateTime.format(ZonedDateTime.now(ZoneOffset.UTC)).toString();
		
		System.out.print(formattedDateTimeWithTimeZone);		
		System.out.print("\n");

		String hash = execute(p_Secret, p_Method, p_Endpoint, formattedDateTimeWithTimeZone, p_Body);
		System.out.println("Hash is: " + hash);
		
		
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://hostname/api/resource"))
                .header("Date", formattedDateTimeWithTimeZone)
                .header("Authorization", "CUAN client_Key:" + hash)
                .build();

        HttpResponse<String> response;
		try {
			response = client.send(request,
			        HttpResponse.BodyHandlers.ofString());
			
			System.out.println(response.body());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        

	}
	
	
	public static String execute(String p_Secret, String p_Method, String p_Endpoint, String p_Date, String p_Body) {
		try {
			
			String message = p_Method + "\n" + p_Date + "\n" + p_Endpoint + "\n" + p_Body;

			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(Base64.decodeBase64(p_Secret), "HmacSHA256");
			sha256_HMAC.init(secret_key);

			return Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
			}
		
		catch (Exception e){
			System.out.println("Error");
			
							}
		return null ;

	}
}
