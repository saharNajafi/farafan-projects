package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class MessageDestinationVTO extends ExtEntityTO {

	private Long id;
	private String messageType;

	private String provinceId;
	private String provinceName;
	private String departmentId;
	private String departmentName;
	private String officeId;
	private String officeName;
	private String nocrofficId;
	private String nocrofficName;
	private String personId;
	private String personName;

	public MessageDestinationVTO() {
		// TODO Auto-generated constructor stub
	}

	public MessageDestinationVTO(Long id, String messageType,
			String provinceName, String departmentName, String officeName,
			String nocrofficName, String personName) {

		this.id = id;
		this.provinceName = provinceName;
		this.departmentName = departmentName;
		this.nocrofficName = nocrofficName;
		this.officeName = officeName;
		this.personName = personName;

	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getNocrofficId() {
		return nocrofficId;
	}

	public void setNocrofficId(String nocrofficId) {
		this.nocrofficId = nocrofficId;
	}

	public String getNocrofficName() {
		return nocrofficName;
	}

	public void setNocrofficName(String nocrofficName) {
		this.nocrofficName = nocrofficName;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
