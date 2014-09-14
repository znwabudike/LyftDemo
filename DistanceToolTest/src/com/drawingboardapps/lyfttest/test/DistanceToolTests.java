package com.drawingboardapps.lyfttest.test;

import java.util.ArrayList;
import java.util.HashMap;

import android.test.AndroidTestCase;
import android.util.Log;

import com.drawingboardapps.znwabudike.lyftbonus.DistanceHelper;
import com.drawingboardapps.znwabudike.lyftbonus.Driver;
import com.google.android.gms.maps.model.LatLng;

public class DistanceToolTests extends AndroidTestCase {

	private static final String TAG = "DistanceToolTest";
	LatLng A, B, C, D;
	DistanceHelper distanceHelper;
	String driver_1_name;
	String driver_2_name;
	String[] paths_d1 = {"A>>C>>D>>B","A>>B>>C>>D","A>>C>>B>>D"};
	String[] paths_d2 = {"C>>A>>B>>D","C>>D>>A>>B","C>>A>>D>>B"};
	private ArrayList<Driver> drivers;
	private ArrayList<Thread> threads;
	private ArrayList<HashMap<String, String>> results;
	/*
	 * Set initialization variables here.
	 */
	private void init() {
		double a_lat = 43.537349;
		double a_lng = -96.711273; // Souix Falls

		double b_lat = 41.637513;
		double b_lng = -93.603516; // Des Moines

		double c_lat = 39.094631;
		double c_lng = -94.581299; // Kansas City

		double d_lat = 40.817804;
		double d_lng = -96.689301; // Lincoln

		A = new LatLng(a_lat, a_lng);
		B = new LatLng(b_lat, b_lng);
		C = new LatLng(c_lat, c_lng);
		D = new LatLng(d_lat, d_lng);
		
		driver_1_name = "Alice";
		driver_2_name = "Bob";
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		long tock = System.currentTimeMillis();
		
		init();
		startDistanceThread();
		Log.d(TAG,"Finished in " + (System.currentTimeMillis()-tock)/1000. + " seconds");
		printResults(results);
		
	}

	private void startDistanceThread() {
		
		DistanceHelper dHelper = new DistanceHelper();
		Thread thread = dHelper.findRoutes(makeDrivers(), "thread");
		thread.start();
		waitForThread(thread);
		results = dHelper.getResults();
		
	}

	private void waitForThread(Thread thread) {
		float timeElapsed = 0;
		float thistime = System.currentTimeMillis();
		while (thread != null && thread.isAlive()){
			//hack haiku to keep the thread alive during JUnit Test
			if (System.currentTimeMillis() < thistime + 1000){
				thistime += 1000;
				timeElapsed += 1000;
				Log.d(TAG, "Why not write this here? " + thistime);
				Log.d(TAG, "It might not get many views, " + thistime);
				Log.d(TAG, "But you're reading it. " + thistime);
			}
		}
		
	}

	public ArrayList<Driver> makeDrivers(){
		drivers = new ArrayList<Driver>();
		drivers.add(new Driver(driver_1_name, A, B, C, D, paths_d1));
		drivers.add(new Driver(driver_2_name, C, D, A, B, paths_d2));
		return drivers;
	}

	private void printResults(ArrayList<HashMap<String, String>> results) {

		for(HashMap<String,String> result : results){
			Log.d(TAG,"Results for " + result.get("drivername"));
			String drivername = result.get("drivername");
			Log.d(TAG, "Minimum Distance = " + result.get("mindist_text"+drivername) + " using path " 
					+  result.get("mindist_path"+drivername));
			Log.d(TAG, "Minimum Time = " + result.get("mintime_text"+drivername) + " using path " 
					+ result.get("mintime_path"+drivername));
		}
	}

}

