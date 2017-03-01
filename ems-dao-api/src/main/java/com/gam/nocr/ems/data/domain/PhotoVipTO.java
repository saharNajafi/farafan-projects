package com.gam.nocr.ems.data.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;

import flexjson.JSON;


@Entity
@Table(name = "EMST_PHOTO_VIP")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_PHOTO_VIP", allocationSize = 1)
public class PhotoVipTO extends ExtEntityTO implements JSONable {

	private CardRequestTO cardRequest;
	private byte[] data;
	private String metaData;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "PHV_ID")
	public Long getId() {
		return super.getId();
	}

	@Lob
	@Column(name = "PHV_DATA", nullable = false)
	@JSON(include = false)
	public byte[] getData() {
		return data == null ? data : data.clone();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHV_CARD_REQUEST_ID")
	@JSON(include = false)
	public CardRequestTO getCardRequest() {
		return cardRequest;
	}

	public void setCardRequest(CardRequestTO cardRequest) {
		this.cardRequest = cardRequest;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Column(name = "PHV_META_DATA", length = 300)
	public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	@Override
	public String toString() {
		return toJSON();
	}

	/**
	 * The method toJSON is used to convert an object to an instance of type
	 * {@link String}
	 * 
	 * @return an instance of type {@link String}
	 */
	@Override
	public String toJSON() {
		String jsonObject = EmsUtil.toJSON(this);
		jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
		if (cardRequest == null) {
			jsonObject += "," + "\"" + "citizenInfoId:" + "\"";
		} else {
			jsonObject += "," + "cardRequestId:" + cardRequest.getId();
		}
		jsonObject += "}";
		return jsonObject;
	}
}