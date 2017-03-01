package com.gam.nocr.ems.data.domain.ws;

public class EstelamImsImageWTO {

	private byte[] nidImage;
    private byte[] certImage;
    
    public EstelamImsImageWTO() {
	}

	public byte[] getNidImage() {
		return nidImage;
	}


	public void setNidImage(byte[] nidImage) {
		this.nidImage = nidImage;
	}


	public byte[] getCertImage() {
		return certImage;
	}


	public void setCertImage(byte[] certImage) {
		this.certImage = certImage;
	}	

}
