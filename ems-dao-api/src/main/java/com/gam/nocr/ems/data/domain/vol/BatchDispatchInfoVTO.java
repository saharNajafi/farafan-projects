package com.gam.nocr.ems.data.domain.vol;

import java.sql.Timestamp;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import flexjson.JSON;

public class BatchDispatchInfoVTO extends ExtEntityTO {

	
	private String batchId;
	private String cmsID;
	private String departmentName;
	private Timestamp batchLostDate;
	private String isConfirm;


	@JSON(include = false)
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(String isConfirm) {
		this.isConfirm = isConfirm;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Timestamp getBatchLostDate() {
		return batchLostDate;
	}

	public void setBatchLostDate(Timestamp batchLostDate) {
		this.batchLostDate = batchLostDate;
	}

	public String getCmsID() {
		return cmsID;
	}

	public void setCmsID(String cmsID) {
		this.cmsID = cmsID;
	}


}
