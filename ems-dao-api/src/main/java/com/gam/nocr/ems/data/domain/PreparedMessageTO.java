package com.gam.nocr.ems.data.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.PreparedMessageState;

import flexjson.JSON;

@Entity
@Table(name = "EMST_PREPARED_MESSAGE")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_PRE_MESSAGE")
public class PreparedMessageTO extends ExtEntityTO {

	private String title;
	private String content;
	private Date createDate;
	private String senderUsername;
	private byte[] attachFile;
	private Boolean hasFile;
	private String fileName;
	private String fileType;

	private Boolean isAll;

	private Boolean isManager;

	private Boolean isOffice;

	private Boolean isNocr;

	private List<LocationTO> provinces;

	private List<EnrollmentOfficeTO> offices;

	private List<PersonTO> persons;

	private PreparedMessageState preparedState;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "PRE_MESS_ID")
	public Long getId() {
		return super.getId();
	}

	@Column(name = "PRE_MESS_TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "PRE_MESS_CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "PRE_MESS_CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "PRE_MESS_Sender_Username")
	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "PRE_MESS_FILE", nullable = true)
	public byte[] getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(byte[] attachFile) {
		this.attachFile = attachFile;
	}

	@Column(name = "PRE_MESS_HAS_FILE")
	public Boolean getHasFile() {
		return hasFile;
	}

	public void setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
	}

	@Column(name = "PRE_MESS_isAll")
	public Boolean getIsAll() {
		return isAll;
	}

	public void setIsAll(Boolean isAll) {
		this.isAll = isAll;
	}

	@Column(name = "PRE_MESS_isManager")
	public Boolean getIsManager() {
		return isManager;
	}

	public void setIsManager(Boolean isManager) {
		this.isManager = isManager;
	}

	@Column(name = "PRE_MESS_isOffice")
	public Boolean getIsOffice() {
		return isOffice;
	}

	public void setIsOffice(Boolean isOffice) {
		this.isOffice = isOffice;
	}

	@Column(name = "PRE_MESS_isNocr")
	public Boolean getIsNocr() {
		return isNocr;
	}

	public void setIsNocr(Boolean isNocr) {
		this.isNocr = isNocr;
	}

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "PRE_MESS_LOC", joinColumns = @JoinColumn(name = "PRE_MESS_ID", referencedColumnName = "PRE_MESS_ID"), inverseJoinColumns = @JoinColumn(name = "LOC_ID", referencedColumnName = "LOC_ID"))
	public List<LocationTO> getProvinces() {
		return provinces;
	}

	public void setProvinces(List<LocationTO> provinces) {
		this.provinces = provinces;
	}

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "PRE_MESS_EOF", joinColumns = @JoinColumn(name = "PRE_MESS_ID", referencedColumnName = "PRE_MESS_ID"), inverseJoinColumns = @JoinColumn(name = "EOF_ID", referencedColumnName = "EOF_ID"))
	public List<EnrollmentOfficeTO> getOffices() {
		return offices;
	}

	public void setOffices(List<EnrollmentOfficeTO> offices) {
		this.offices = offices;
	}

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "PRE_MESS_PERSON", joinColumns = @JoinColumn(name = "PRE_MESS_ID", referencedColumnName = "PRE_MESS_ID"), inverseJoinColumns = @JoinColumn(name = "PER_ID", referencedColumnName = "PER_ID"))
	public List<PersonTO> getPersons() {
		return persons;
	}

	public void setPersons(List<PersonTO> persons) {
		this.persons = persons;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "PRE_MESS_STATE", nullable = false)
	@JSON(include = false)
	public PreparedMessageState getPreparedState() {
		return preparedState;
	}

	public void setPreparedState(PreparedMessageState preparedState) {
		this.preparedState = preparedState;
	}

	@Column(name = "PRE_MESS_FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "PRE_MESS_FILE_TYPE")
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}