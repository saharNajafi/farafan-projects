package com.gam.nocr.ems.data.domain.ws;

public class PhotoVipWTO {

	private Long id;
	private Long cardRequestid;
	private byte[] data;
	private String metaData;

	public PhotoVipWTO() {
	}

	public Long getCardRequestid() {
		return cardRequestid;
	}

	public void setCardRequestid(Long cardRequestid) {
		this.cardRequestid = cardRequestid;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
