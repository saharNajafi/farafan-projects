package com.gam.nocr.ems.sharedobjects;

import java.util.Map;

import com.gam.commons.core.data.domain.UserProfileTO;

public class GeneralCriteria {

	private Map<String, Object> parameters;
	private String orderBy;
	UserProfileTO userProfileTO;
	private Integer pageSize;
	private Integer pageNo;

	public GeneralCriteria(Map<String, Object> parameters, String orderBy,
			UserProfileTO userProfileTO, Integer pageSize, Integer pageNo) {
		super();
		this.parameters = parameters;
		this.orderBy = orderBy;
		this.userProfileTO = userProfileTO;
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}



	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public UserProfileTO getUserProfileTO() {
		return userProfileTO;
	}

	public void setUserProfileTO(UserProfileTO userProfileTO) {
		this.userProfileTO = userProfileTO;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

}
