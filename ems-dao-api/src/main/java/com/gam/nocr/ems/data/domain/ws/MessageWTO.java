package com.gam.nocr.ems.data.domain.ws;

import java.sql.Timestamp;

import com.gam.nocr.ems.util.EmsUtil;

//messageResultsList
public class MessageWTO {

	private Long id;
	private String msgUnread;// R U
	private String msgSubject;
	private String msgContent;
	private String msgDownload; // 0 1
	private Timestamp msgDate;
	private String msgType;
	private String msgExtention;
	private byte[] attachFile;
	private String senderUsername;
	private int pageSize;
	private int pageNo;

	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	public byte[] getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(byte[] attachFile) {
		this.attachFile = attachFile;
	}

	public String getMsgSubject() {
		return msgSubject;
	}

	public MessageWTO(Long id, String msgSubject, String msgContent,
			Timestamp msgDate, String msgPriority, String msgUnread) {

		this.id = id;
		this.msgSubject = msgSubject;
		this.msgContent = msgContent;
		this.msgDate = msgDate;
		this.msgUnread = msgUnread;

	}

	public MessageWTO() {
	}

	public MessageWTO(Timestamp msgDate, String msgType, int pageSize,
			int pageNo) {
		super();
		this.msgDate = msgDate;
		this.msgType = msgType;
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}

	public void setMsgSubject(String msgSubject) {
		this.msgSubject = msgSubject;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Timestamp getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(Timestamp msgDate) {
		this.msgDate = msgDate;
	}

	public Long getId() {
		return id;
	}

	public String getMsgUnread() {
		return msgUnread;
	}

	public void setMsgUnread(String msgUnread) {
		this.msgUnread = msgUnread;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsgDownload() {
		return msgDownload;
	}

	public void setMsgDownload(String msgDownload) {
		this.msgDownload = msgDownload;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgExtention() {
		return msgExtention;
	}

	public void setMsgExtention(String msgExtention) {
		this.msgExtention = msgExtention;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}

}
