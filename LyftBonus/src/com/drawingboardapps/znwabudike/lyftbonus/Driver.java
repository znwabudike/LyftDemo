package com.drawingboardapps.znwabudike.lyftbonus;

import com.google.android.gms.maps.model.LatLng;

public class Driver {
	private String[] paths;
	private String name;
	private LatLng a, b, c, d;

	public Driver(String name, 
			LatLng a, 
			LatLng b, 
			LatLng c,
			LatLng d, 
			String[] paths) {
		this.name = name; 
		this.a = a; 
		this.b = b; 
		this.c = c; 
		this.d = d; 
		this.paths = paths;
	}

	public LatLng getStart() {return this.a;}

	public String getName() {return this.name;}

	public LatLng getStop() {return this.b;}

	public LatLng getPoint1() {return this.c;}

	public String getPath(int i) {return paths[i];}

	public LatLng getPoint2() {return this.d;}

}
