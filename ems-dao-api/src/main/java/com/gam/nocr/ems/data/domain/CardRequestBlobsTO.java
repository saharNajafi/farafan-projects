package com.gam.nocr.ems.data.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.JSONable;

import flexjson.JSON;


@Entity
@Table(name = "EMST_CARD_REQUEST_BLOBS")
//@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_CARD_REQUEST_BLOBS", allocationSize = 1)
public class CardRequestBlobsTO extends ExtEntityTO implements Serializable,
		JSONable {

	private byte[] signedReceipt;
	private String receiptText;
	private byte[] officerSign;
	private CardRequestTO cardRequest;

	public CardRequestBlobsTO() {
	}
	

	@Id	
	public Long getId() {
		return super.getId();
	}
	
	public CardRequestBlobsTO(Long id) {
		this.setId(id);
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CRQ_BLOBS_SIGNED_RECEIPT")
	@JSON(include = false)
	public byte[] getSignedReceipt() {
		return signedReceipt == null ? signedReceipt : signedReceipt.clone();
	}

	public void setSignedReceipt(byte[] signedReceipt) {
		this.signedReceipt = signedReceipt;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CRQ_BLOBS_RECEIPT_TEXT")
	@JSON(include = false)
	public String getReceiptText() {
		return receiptText;
	}

	public void setReceiptText(String receiptText) {
		this.receiptText = receiptText;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CRQ_BLOBS_OFFICER_SIGN")
	@JSON(include = false)
	public byte[] getOfficerSign() {
		return officerSign == null ? officerSign : officerSign.clone();
	}

	public void setOfficerSign(byte[] officerSign) {
		this.officerSign = officerSign;
	}

	@MapsId
	@OneToOne
	@JoinColumn(name = "CRQ_BLOBS_ID")
	@JSON(include = false)
	public CardRequestTO getCardRequest() {
		return cardRequest;
	}
	
	

	public void setCardRequest(CardRequestTO cardRequest) {
		this.cardRequest = cardRequest;
	}

	@Override
	public String toJSON() {
		return toJSON();
	}

}
