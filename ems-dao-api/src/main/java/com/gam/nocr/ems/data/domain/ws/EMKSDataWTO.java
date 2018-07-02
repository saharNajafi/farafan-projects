package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;

import java.io.Serializable;

public class EMKSDataWTO implements Serializable{
	private String csn;
	private String crn;
	private String cardProvider;
	private String pinKeyVersion;
	private String pinAlgorithmVersion;
	private String sodKeyVersion;
	private String ldsVersion;
	private String dpVersion;
	private String mocAvailable;
	private String bluePart;
	private String dg8;
	private String yellowPart;
	private String yellowPartSignature;
	private String mocHashData;
	private String antiYesPublicKey;
	private String mocSignature;

	public EMKSDataWTO() {
	}

	public String getCsn() {
		return csn;
	}

	public void setCsn(String csn) {
		this.csn = csn;
	}

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getCardProvider() {
		return cardProvider;
	}

	public void setCardProvider(String cardProvider) {
		this.cardProvider = cardProvider;
	}

	public String getPinKeyVersion() {
		return pinKeyVersion;
	}

	public void setPinKeyVersion(String pinKeyVersion) {
		this.pinKeyVersion = pinKeyVersion;
	}

	public String getPinAlgorithmVersion() {
		return pinAlgorithmVersion;
	}

	public void setPinAlgorithmVersion(String pinAlgorithmVersion) {
		this.pinAlgorithmVersion = pinAlgorithmVersion;
	}

	public String getSodKeyVersion() {
		return sodKeyVersion;
	}

	public void setSodKeyVersion(String sodKeyVersion) {
		this.sodKeyVersion = sodKeyVersion;
	}

	public String getLdsVersion() {
		return ldsVersion;
	}

	public void setLdsVersion(String ldsVersion) {
		this.ldsVersion = ldsVersion;
	}

	public String getDpVersion() {
		return dpVersion;
	}

	public void setDpVersion(String dpVersion) {
		this.dpVersion = dpVersion;
	}

	public String getMocAvailable() {
		return mocAvailable;
	}

	public void setMocAvailable(String mocAvailable) {
		this.mocAvailable = mocAvailable;
	}

	public String getBluePart() {
		return bluePart;
	}

	public void setBluePart(String bluePart) {
		this.bluePart = bluePart;
	}

	public String getDg8() {
		return dg8;
	}

	public void setDg8(String dg8) {
		this.dg8 = dg8;
	}

	public String getYellowPart() {
		return yellowPart;
	}

	public void setYellowPart(String yellowPart) {
		this.yellowPart = yellowPart;
	}

	public String getYellowPartSignature() {
		return yellowPartSignature;
	}

	public void setYellowPartSignature(String yellowPartSignature) {
		this.yellowPartSignature = yellowPartSignature;
	}

	public String getMocHashData() {
		return mocHashData;
	}

	public void setMocHashData(String mocHashData) {
		this.mocHashData = mocHashData;
	}

	public String getAntiYesPublicKey() {
		return antiYesPublicKey;
	}

	public void setAntiYesPublicKey(String antiYesPublicKey) {
		this.antiYesPublicKey = antiYesPublicKey;
	}

	public String getMocSignature() {
		return mocSignature;
	}

	public void setMocSignature(String mocSignature) {
		this.mocSignature = mocSignature;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
