package com.gam.nocr.ems.data.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * <p>
 * TODO -- Explain this class
 * </p>
 * 
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Entity
@Table(name = "EMST_HELP")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_HELP", allocationSize = 1)
public class HelpTO extends ExtEntityTO {

	private byte[] helpFile;
	private Date createDate;
	private PersonTO creator;
	private String title;
	private String description;
	private String contentType;

	public HelpTO() {
	}

	public HelpTO(Long id) {
		this.setId(id);
	}

	@Column(name = "HLP_DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "HLP_CONTENT_TYPE", nullable = false)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Id
	@Override
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "HLP_ID")
	public Long getId() {
		return super.getId();
	}

	@Lob
	@Column(name = "HLP_FILE", nullable = false)
	public byte[] getHelpFile() {
		return helpFile;
	}

	public void setHelpFile(byte[] helpFile) {
		this.helpFile = helpFile;
	}

	@Column(name = "HLP_TITLE", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HLP_PERSON_ID", nullable = false)
	public PersonTO getCreator() {
		return creator;
	}

	public void setCreator(PersonTO creator) {
		this.creator = creator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HLP_CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
