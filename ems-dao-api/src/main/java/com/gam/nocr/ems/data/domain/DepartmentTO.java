package com.gam.nocr.ems.data.domain;

import java.util.Date;

import javax.persistence.*;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;

import flexjson.JSON;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_DEPARTMENT")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_DEPARTMENT", allocationSize = 1)
public class DepartmentTO extends ExtEntityTO implements JSONable {

	private DepartmentTO parentDepartment;
	private String code;
	private String name;
	private String address;
	private String postalCode;
	private String dn;
	private String parentDN;
	private DepartmentDispatchSendType dispatchSendType;
	private LocationTO location;
	private String provinceName;
	private String sendType;
	private String parentName;
	private Long parentId;
	private String locName;
	private Long locId;

	private Date lastSyncDate;
	private Date lastModifiedDate;
	private long depPhoneNumber;

	public DepartmentTO() {
	}

	public DepartmentTO(Long id) {
		this.setId(id);
	}

	/**
	 * maps to the siteId in networkReq.xml
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "DEP_ID")
	public Long getId() {
		return super.getId();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEP_PARENT_DEP_ID")
	@JSON(include = false)
	public DepartmentTO getParentDepartment() {
		return parentDepartment;
	}

	public void setParentDepartment(DepartmentTO parentDepartment) {
		this.parentDepartment = parentDepartment;
	}

	@Column(name = "DEP_CODE", length = 255, nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "DEP_NAME", length = 255, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DEP_ADDRESS", length = 255, nullable = false)
	@JSON(include = false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "DEP_POSTAL_CODE", nullable = false, length = 10)
	public @JSON(include = false)
	String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Column(name = "DEP_DN", length = 255)
	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	@Column(name = "DEP_PARENT_DN", length = 255)
	public String getParentDN() {// role
		return parentDN;
	}

	public void setParentDN(String parentDN) {
		this.parentDN = parentDN;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "DEP_DISPATCH_SEND_TYPE")
	@JSON(include = false)
	public DepartmentDispatchSendType getDispatchSendType() {
		return dispatchSendType;
	}

	public void setDispatchSendType(DepartmentDispatchSendType dispatchSendType) {
		this.dispatchSendType = dispatchSendType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEP_LAST_SYNC_DATE")
	@JSON(include = false)
	public Date getLastSyncDate() {
		return lastSyncDate;
	}

	public void setLastSyncDate(Date lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEP_LAST_MODIFIED_DATE")
	@JSON(include = false)
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * The method returnFQDN prepares the 'FQDN' name which is used for sending
	 * the network token request
	 * 
	 * @return
	 */
	public String returnFQDN() {// organization
		return getDn() + "." + getParentDN();
	}

	@ManyToOne
	@JoinColumn(name = "DEP_LOCATION_ID")
	@JSON(include = false)
	public LocationTO getLocation() {
		return location;
	}

	public void setLocation(LocationTO location) {
		this.location = location;
	}

	@Transient
	@JSON(include = false)
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Transient
	@JSON(include = false)
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Transient
	@JSON(include = false)
	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}

	@Transient
	@JSON(include = false)
	public Long getLocId() {
		return locId;
	}

	public void setLocId(Long locId) {
		this.locId = locId;
	}

	@Transient
	@JSON(include = false)
	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	@Transient
	@JSON(include = false)
	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	@Column(name = "DEP_PHONE_NUMBER")
	public long getDepPhoneNumber() {
		return depPhoneNumber;
	}

	public void setDepPhoneNumber(long depPhoneNumber) {
		this.depPhoneNumber = depPhoneNumber;
	}

	@Override
	public String toString() {
		// String string = "CitizenTO : { " +
		// "id = " + getId() +
		// ", code = " + code +
		// ", name = " + name +
		// ", dn = " + dn +
		// ", parentDN = " + parentDN;
		//
		// if (parentDepartment == null) {
		// string += ", parentDepartment = " + parentDepartment;
		// } else {
		// string += ", parentDepartmentID = " + parentDepartment.getId();
		// }
		//
		// string += " }";
		// return string;
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
		if (parentDepartment == null) {
			jsonObject += ", parentDepartmentId:" + parentDepartment;
		} else {
			jsonObject += ", parentDepartmentId:" + parentDepartment.getId();
		}
		jsonObject += "}";

		return jsonObject;
	}
}
