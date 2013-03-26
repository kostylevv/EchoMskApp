package com.unionfind.android.echomskapp;
/**
 * 
 * Class to store flight entries
 *
 */
public class Trip {
	public String getDuration() {
		return duration;
	}

	public String getTakeoff() {
		return takeoff;
	}

	public String getLanding() {
		return landing;
	}

	public String getFlight() {
		return flight;
	}

	public double getPrice() {
		return price;
	}

	private final String duration;
	private final String takeoff;
	private final String landing;
	private final String flight;
	private final double price;

	public Trip(String duration, String takeoff, String landing, String flight, String price) {
		this.duration = duration;
		this.takeoff = takeoff;
		this.landing = landing;
		this.flight = flight;
		this.price = Float.parseFloat(price);

	}
}