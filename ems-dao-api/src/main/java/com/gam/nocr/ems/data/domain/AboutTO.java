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
@Table(name = "EMST_ABOUT")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_ABOUT", allocationSize = 1)
public class AboutTO extends ExtEntityTO {

	private String content;
	private PersonTO creator;
	private Date createDate;

	public AboutTO() {
	}

	public AboutTO(Long id) {
		this.setId(id);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "ABT_ID")
	public Long getId() {
		return super.getId();
	}

	@Column(name = "ABT_CONTENT", nullable = false, unique = true)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ABT_PERSON_ID", nullable = false)
	public PersonTO getCreator() {
		return creator;
	}

	public void setCreator(PersonTO creator) {
		this.creator = creator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ABT_CREATE_DATE")
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
