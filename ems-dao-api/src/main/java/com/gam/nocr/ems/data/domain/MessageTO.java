package com.gam.nocr.ems.data.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gam.commons.core.data.domain.ExtEntityTO;

@Entity
@Table(name = "EMST_MESSAGE")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_MESSAGE")
public class MessageTO extends ExtEntityTO {

	private String title;
	private String content;
	private Date createDate;
	private String senderUsername;
	private byte[] attachFile;
	private Boolean hasFile;
	private String fileName;
	private String fileType;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "MESS_ID")
	public Long getId() {
		return super.getId();
	}

	@Column(name = "MESS_TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "MESS_CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "MESS_CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "MESS_Sender_Username")
	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "MESS_FILE", nullable = true)
	public byte[] getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(byte[] attachFile) {
		this.attachFile = attachFile;
	}

	@Column(name = "MESS_Has_File")
	public Boolean getHasFile() {
		return hasFile;
	}

	public void setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
	}

	@Column(name = "MESS_FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "MESS_FILE_TYPE")
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}