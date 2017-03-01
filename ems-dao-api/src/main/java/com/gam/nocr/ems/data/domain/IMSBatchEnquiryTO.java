package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;

import javax.persistence.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Entity
@Table(name = "EMST_IMS_BATCH_ENQUIRY")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_IMS_BATCH_ENQUIRY")
public class IMSBatchEnquiryTO extends ExtEntityTO {

	private String enquiryInfo;
	private Boolean replyFlag;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "IBE_ID")
	public Long getId() {
		return super.getId();
	}

	@Lob
	@Column(name = "IBE_Enquiry_INFO")
	public String getEnquiryInfo() {
		return enquiryInfo;
	}

	public void setEnquiryInfo(String enquiryInfo) {
		this.enquiryInfo = enquiryInfo;
	}

	@Column(name = "IBE_REPLY_FLAG")
	public Boolean getReplyFlag() {
		return replyFlag;
	}

	public void setReplyFlag(Boolean replyFlag) {
		this.replyFlag = replyFlag;
	}
}
