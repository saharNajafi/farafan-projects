package com.gam.nocr.ems.data.enums;

public enum SendSmsType {
	
	REFER_TO_CCOS_SMS(1), DELIVERY_SMS(2),RESERVED_SMS(3);
	
	
	private final Integer value;
	private SendSmsType(Integer value){
		this.value = value;
	}
	
	
	public Integer getIntValue(){
		
		return value;
		
	}

}
