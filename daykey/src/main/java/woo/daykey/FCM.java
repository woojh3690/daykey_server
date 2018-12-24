package woo.daykey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class FCM {

	final static private String FCM_URL = "http://fcm.googleapis.com/fcm/send";

	/**
	 * Method to send push notification to Android FireBased Cloud messaging Server.
	 * 
	 * @param tokenId
	 *            Generated and provided from Android Client Developer
	 * @param server_key
	 *            Key which is Generated in FCM Server
	 * @param message
	 *            which contains actual information. 
	 */

	public void send_FCM_Notification(String server_key, String message, int urlID, String type) {
		System.out.println("message : " + message + "urlID : " + urlID + "type : " + type);

		try {
			URL url = new URL(FCM_URL);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "key=" + server_key);
			conn.setRequestProperty("Content-Type", "application/json");

			// Create JSON Object & pass value
			JSONObject infoJson = new JSONObject();
			infoJson.put("title", "새로운 공지가 있습니다.");
			infoJson.put("message", message);
			infoJson.put("urlID", urlID);
			infoJson.put("type", type);

			JSONObject json = new JSONObject();
			json.put("to","/topics/ALL");
			//json.put("to", "d5EFfqzHews:APA91bH_SRn88nzezlwsX-oEBVt8-shmagd02gR_8sdeHWjwaR4FvGmnJLmCTeaUKCkJK60e4jMUis2rGTxvPw3NpAgImPCypqG-WdVIB_EEi10NMAN9VI3Ie29G5v42qHHLSTR1313g");
			json.put("data", infoJson);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json.toString());
			wr.flush();

			int status = 0;
			if (null != conn) {
				status = conn.getResponseCode();
			}
			
			if (status != 0) {
				if (status == 200) {
					// SUCCESS message
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					System.out.println("Android Notification Response : " + reader.readLine());
				} else if (status == 401) {
					// client side error
					System.out.println("ERROR : 401");
				} else if (status == 501) {
					// server side error
					System.out.println("ERROR : 501");
				} else if (status == 503) {
					// server side error
					System.out.println("ERROR : 503");
				} else {
					System.out.println("ERROR status : " + status);
				}
			} else {
				System.out.println("status : 0");
			}
		} catch (MalformedURLException mlfexception) {
			// Prototcal Error
			mlfexception.printStackTrace();
		} catch (IOException mlfexception) {
			// URL problem
			mlfexception.printStackTrace();
		} catch (JSONException jsonexception) {
			// Message format error
			jsonexception.printStackTrace();
		} catch (Exception exception) {
			// General Error or exception.
			exception.printStackTrace();;
		}
	}
}
