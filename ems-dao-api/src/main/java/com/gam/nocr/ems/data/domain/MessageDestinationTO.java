package com.gam.nocr.ems.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.DestMessageType;

@Entity
@Table(name = "EMST_MESS_DEST")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_MESS_DEST")
public class MessageDestinationTO extends ExtEntityTO {

	private MessageTO message;
	private DestMessageType destinationType;
	private Long destinationId;

	private String deleteList = " "; // pattern: @id@@id@;

	private String seenList = " "; // pattern: @id@@id@;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "MEDE_ID")
	public Long getId() {
		return super.getId();
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MEDE_MESSAGE_ID")
	public MessageTO getMessage() {
		return message;
	}

	public void setMessage(MessageTO message) {
		this.message = message;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "MEDE_MESSAGE_TYPE")
	public DestMessageType getDestinationType() {
		return destinationType;
	}

	public void setDestinationType(DestMessageType destinationType) {
		this.destinationType = destinationType;
	}

	@Column(name = "MEDE_DESTINATION_ID")
	public Long getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}

	@Column(name = "MEDE_DELETE_LIST")
	public String getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(String deleteList) {
		this.deleteList = deleteList;
	}

	@Column(name = "MEDE_SEEN_LIST")
	public String getSeenList() {
		return seenList;
	}

	public void setSeenList(String seenList) {
		this.seenList = seenList;
	}

	
}