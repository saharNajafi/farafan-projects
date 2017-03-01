package com.gam.nocr.ems.data.domain.vol;

import java.sql.Timestamp;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class HelpVTO extends ExtEntityTO {

	private Long id;
	private Timestamp createDateHelp;
	private String title;
	private String description;
	private String contentType;
	private String creator;

	private String uploadPath;
	private byte[] helpFile;

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public HelpVTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreateDateHelp() {
		return createDateHelp;
	}

	public void setCreateDateHelp(Timestamp createDateHelp) {
		this.createDateHelp = createDateHelp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}

	public byte[] getHelpFile() {
		return helpFile;
	}

	public void setHelpFile(byte[] helpFile) {
		this.helpFile = helpFile;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

}
