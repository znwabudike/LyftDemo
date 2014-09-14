package com.drawingboardapps.znwabudike.lyftbonus;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;

import android.os.HandlerThread;
import android.util.Log;

/**

 * @author Zachary Nwabudike 9/13/2014
 *
 */

public class DistanceHelper {

	private static final String TAG = "DistanceHelper";
	private final String KEY_ID = "id";
	private final String KEY_START = "start";
	private final String KEY_DESTINATION = "destination";
	private final String KEY_WAYPOINT_1 = "wp1";
	private final String KEY_WAYPOINT_2 = "wp2";
	private HttpHelper mHttpHelper;

	private ArrayList<HashMap<String, String>> results;

	public void getDetours(String id, 
			LatLng start, 
			LatLng destination, 
			LatLng waypoint_1, 
			LatLng waypoint_2){
		Log.d(TAG, "Starting..");
		mHttpHelper = new HttpHelper();
		HashMap<String, Object> map = initializeMap(id, start, destination, waypoint_1, waypoint_2);
		RouteOptimizer ro = new RouteOptimizer(this,map);
		ro.getDistances();
	}

	public HashMap<String, Object> initializeMap(String id, 
			LatLng start, 
			LatLng destination,
			LatLng waypoint_1, 
			LatLng waypoint_2) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_ID, id);
		map.put(KEY_START, start);
		map.put(KEY_DESTINATION, destination);
		map.put(KEY_WAYPOINT_1, waypoint_1);
		map.put(KEY_WAYPOINT_2, waypoint_2);
		return map;
	}

	public Thread findRoutes(final ArrayList<Driver> drivers, String threadname){
		results = new ArrayList<HashMap<String, String>>();
		Thread thread = new HandlerThread(threadname){
			public void run(){
				Log.d(TAG,"Starting Thread");
				for (Driver driver : drivers){
					results.add(getRoutes(driver));
				}
				setResults(results);
			}


		};
		thread.setContextClassLoader(getClass().getClassLoader());
		return thread;
	}

	public HashMap<String,String> getRoutes(Driver driver) {
		HashMap<String,String> hm = new HashMap<String,String>();
		getDetours(driver.getName(), 
				driver.getStart(), 
				driver.getStop(), 
				driver.getPoint1(),
				driver.getPoint2());
		ArrayList<Double> distances = mHttpHelper.getDistancesResults();
		ArrayList<String> distance_text = mHttpHelper.getDistanceText();
		ArrayList<Double> times = mHttpHelper.getDurations();
		ArrayList<String> times_text = mHttpHelper.getDurationsText();

		int i = 0;
		int j = 0;
		double minDist = Double.MAX_VALUE;
		double minTime = Double.MAX_VALUE;
		Log.d(TAG, distances.size() + "");
		for (int index = 0; index < distances.size() ; index++){
			Double d = distances.get(index);
			Double t = times.get(index);
			if (d < minDist){minDist = d; i++;}
			if (t < minTime){minTime = t; j++;}
		}
		i--; j--;
		String drivername = driver.getName();
		hm.put("drivername", driver.getName());
		hm.put("mindist"+drivername, minDist+"");
		hm.put("mindist_text"+drivername, distance_text.get(i));
		hm.put("mindist_path"+drivername, (driver.getPath(i)));
		hm.put("mintime_text"+drivername, times_text.get(j));
		hm.put("mintime_path"+drivername, (driver.getPath(j)));
		return hm;
	}


	public class RouteOptimizer {
		private static final String TAG = "RouteOptimizerTask";
		private LatLng location;
		private LatLng destination;
		private LatLng wp1;
		private LatLng wp2;
		public RouteOptimizer(DistanceHelper mDistanceTool, HashMap<String,Object> hm){
			this.location = (LatLng) hm.get(KEY_START);
			this.destination = (LatLng) hm.get(KEY_DESTINATION);
			this.wp1 = (LatLng) hm.get(KEY_WAYPOINT_1);
			this.wp2 = (LatLng) hm.get(KEY_WAYPOINT_2);
		}

		public void getDistances(){
			int length = 3;
			String[] URIs = new String[length];
			new ArrayList<Double>();
			try {
				URIs[0] = buildURI(location, wp1, wp2, destination);//A>>C>>D>>B
				URIs[1] = buildURI(location, destination, wp1, wp2);//A>>B>>C>>D
				URIs[2] = buildURI(location, wp1, destination, wp2);//A>>C>>B>>D
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			int tries = 3;
			for (int i = 0; i < length && tries > 0; i++){
				Log.d(TAG,"Request# " + i);
				String response = mHttpHelper.makeDistanceRequest(URIs[i]);
				if (response != null){
					mHttpHelper.decodeResponseForDistances(response);
				}else{
					Log.d(TAG, "Error:  Check Status, retrying: " + tries + "tries remaining");
					i--; tries--;
				}
			}
		}

		private String buildURI(LatLng origin, LatLng waypoint_1, LatLng waypoint_2, LatLng destination) throws UnsupportedEncodingException{
			String URI = "https://maps.googleapis.com/maps/api/directions/json?"
					+ "origin="
					+ URLEncoder.encode(origin.latitude+","+origin.longitude,"UTF-8")
					+ "&destination="
					+ URLEncoder.encode(destination.latitude+","+destination.longitude,"UTF-8")
					+ "&waypoints="
					+ URLEncoder.encode("optimize:false|","UTF-8")
					+ URLEncoder.encode(waypoint_1.latitude+","+waypoint_1.longitude ,"UTF-8")
					+ URLEncoder.encode("|" ,"UTF-8")
					+ URLEncoder.encode(waypoint_2.latitude+","+waypoint_2.longitude,"UTF-8")
					+ "&mode=driving"
					+ "units=" + (Locale.getDefault() == Locale.US ? "imperial" : "metric" );
			return URI;
		}

	}

	public ArrayList<HashMap<String, String>> getResults() {return this.results;
	}private void setResults(ArrayList<HashMap<String, String>> results) {this.results = results;}

}