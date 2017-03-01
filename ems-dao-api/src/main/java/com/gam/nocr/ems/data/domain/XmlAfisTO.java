package com.gam.nocr.ems.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

@Entity
@Table(name = "EMST_XMLAFIS")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_XMLAFIS", allocationSize = 1)
public class XmlAfisTO extends ExtEntityTO {

	private String data;
	private Long cardRequest;
	private Date createDate;
	private String errMsg;

	public XmlAfisTO(Long cardRequest, String data, Date createDate,
			String errMsg) {

		this.cardRequest = cardRequest;
		this.data = data;
		this.createDate = createDate;
		this.errMsg = errMsg;

	}

	public XmlAfisTO() {
		// TODO Auto-generated constructor stub
	}

	public XmlAfisTO(Long id) {
		this.setId(id);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "XAF_ID")
	public Long getId() {
		return super.getId();
	}

	@Override
	public void setId(Long id) {
		super.setId(id);
	}

	@Lob
	@Column(name = "XAF_DATA", nullable = false)
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Column(name = "XAF_REQUEST_ID", nullable = false)
	public Long getCardRequest() {
		return cardRequest;
	}

	public void setCardRequest(Long cardRequest) {
		this.cardRequest = cardRequest;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "XAF_CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	@Lob
	@Column(name = "XAF_ERRMSG")
	public String getErrMsg() {
		return errMsg;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
