package com.drawingboardapps.znwabudike.lyftbonus;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpHelper {

	private static final String TAG = null;
	private ArrayList<Double> distances;
	private ArrayList<Double> durations;
	private ArrayList<String> durations_text;
	private ArrayList<String> distances_text;
	
	public HttpHelper(){
		distances = new ArrayList<Double>();
		durations = new ArrayList<Double>();
		durations_text = new ArrayList<String>();
		distances_text = new ArrayList<String>();
	}
	String makeDistanceRequest(String URI) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(URI);
		HttpResponse response = null; 
		String responseString = null;
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity, "UTF-8");
			String status = checkStatus(responseString);
			if (status.compareTo("OK") != 0){
				Log.d(TAG, "Error, status: " + status);
				return null;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseString;
	}
	

	private String checkStatus(String response){
		JSONObject responseJArry = null;
		String status = null;
		try {
			responseJArry = new JSONObject(response);
			status = responseJArry.getString("status");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.d(TAG, "Status = " + status);
		return status;
	}
	
	public void decodeResponseForDistances(String response) {
		JSONObject responseJArry = null;
		String duration_text = null;
		double[] dist_dur = new double[3];
		try {
			responseJArry = new JSONObject(response);
			JSONArray routes = (JSONArray) responseJArry.getJSONArray("routes");
			JSONObject route = routes.getJSONObject(0);
			JSONArray legs = route.getJSONArray("legs");
			JSONObject distanceObj = legs.getJSONObject(0);
			JSONObject durationObj = legs.getJSONObject(1);
			distances.add(Double.parseDouble(distanceObj.getJSONObject("distance").getString("value")));
			durations.add(Double.parseDouble(durationObj.getJSONObject("duration").getString("value")));
			distances_text.add(durationObj.getJSONObject("distance").getString("text"));
			durations_text.add(distanceObj.getJSONObject("duration").getString("text"));


		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
		
	public ArrayList<Double> getDurations(){return durations;}
	public ArrayList<String> getDurationsText(){return durations_text;}
	public ArrayList<String> getDistanceText() {return distances_text;}
	public ArrayList<Double> getDistancesResults(){return distances;}
	void setDurations(ArrayList<Double> durations) {this.durations = durations;}
}
