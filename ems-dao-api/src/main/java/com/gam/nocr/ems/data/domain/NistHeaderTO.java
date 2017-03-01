package com.gam.nocr.ems.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

@Entity
@Table(name = "EMST_NIST_HEADER")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_NIST_HEADER", allocationSize = 1)
public class NistHeaderTO extends ExtEntityTO {

	private byte[] data;
	private CardRequestTO cardRequest;
	private Date createDate;

	public NistHeaderTO(CardRequestTO cardRequest, byte[] data,Date createDate) {

		this.cardRequest = cardRequest;
		this.data = data;
		this.createDate=createDate;

	}

	public NistHeaderTO() {
		// TODO Auto-generated constructor stub
	}

	public NistHeaderTO(Long id) {
		this.setId(id);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "NIS_ID")
	public Long getId() {
		return super.getId();
	}

	@Override
	public void setId(Long id) {
		super.setId(id);
	}

	@Lob
	@Column(name = "NIS_DATA", nullable = false)
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@ManyToOne
	@JoinColumn(name = "NIS_REQUEST_ID", nullable = false)
	public CardRequestTO getCardRequest() {
		return cardRequest;
	}

	public void setCardRequest(CardRequestTO cardRequest) {
		this.cardRequest = cardRequest;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NIS_CREATE_DATE")
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
