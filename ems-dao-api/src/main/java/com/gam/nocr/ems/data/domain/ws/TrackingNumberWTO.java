package com.gam.nocr.ems.data.domain.ws;

public class TrackingNumberWTO {
	private String trackingID;
	private String state;

	
	public TrackingNumberWTO() {
	}
	
	public TrackingNumberWTO(String trackingID, String state) {

		this.trackingID = trackingID;
		this.state = state;
	}

	public String getTrackingID() {
		return trackingID;
	}

	public void setTrackingID(String trackingID) {
		this.trackingID = trackingID;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
