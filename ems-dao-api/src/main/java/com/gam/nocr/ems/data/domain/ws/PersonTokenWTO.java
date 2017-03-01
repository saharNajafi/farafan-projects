package com.gam.nocr.ems.data.domain.ws;

import java.sql.Timestamp;

import org.slf4j.Logger;

import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Hamid Adldoost (adldoust@ipreez.ir)
 */
public class PersonTokenWTO {

	private static final Logger logger = BaseLog.getLogger(EmsUtil.class);
	
	private String tokenState;
	private String tokenType;
	private String id;
	private String requestId;
	private Timestamp requestDate;
	private Timestamp issuanceDate;
	private Timestamp deliverDate;
	
	
	public String getTokenState() {
		return tokenState;
	}
	public void setTokenState(String tokenState) {
		this.tokenState = tokenState;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Timestamp getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}
	public Timestamp getIssuanceDate() {
		return issuanceDate;
	}
	public void setIssuanceDate(Timestamp issuanceDate) {
		this.issuanceDate = issuanceDate;
	}
	public Timestamp getDeliverDate() {
		return deliverDate;
	}
	
	public void setDeliverDate(Timestamp deliverDate) {
		this.deliverDate = deliverDate;
	}	
	public static Logger getLogger() {
		return logger;
	}
	
	
}
