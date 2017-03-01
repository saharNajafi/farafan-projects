package com.gam.nocr.ems.web.action;

/**
 * hossein
 */

import gampooya.tools.date.DateUtil;
import gampooya.tools.security.BusinessSecurityException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.PreparedMessageDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.PreparedMessageTO;
import com.gam.nocr.ems.data.enums.PreparedMessageState;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.FileObject;
import com.gam.nocr.ems.util.FileUtil;

public class PreparedMessageAction extends
		ListControllerImpl<PreparedMessageTO> {

	private String id;
	private InputStream inputStream;
	private String downloadFileName;
	private String contentType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public void setRecords(List<PreparedMessageTO> records) {

		this.records = records;

	}

	public String uploadAttachedFile() throws ActionException {
		MultiPartRequestWrapper request2 = (MultiPartRequestWrapper) ServletActionContext
				.getRequest();

		if (request2.getFiles("hfile") == null
				|| request2.getFiles("hfile").length == 0)
			throw new ActionException(WebExceptionCode.HEA_008,
					WebExceptionCode.HEA_008_MSG);

		File files = request2.getFiles("hfile")[0];

		File temp = new File(files.getPath());
		request2.getFiles("hfile")[0].getName();

		String fileNameParts[] = request2.getFileNames("hfile")[0]
				.split(Pattern.quote("."));
		String contentType = fileNameParts[fileNameParts.length - 1];
		if (contentType == null)
			throw new ActionException(WebExceptionCode.HEA_007,
					WebExceptionCode.HEA_007_MSG);

		int length = (int) temp.length();

		if (length > 5242880)
			throw new ActionException(WebExceptionCode.HEA_008,
					WebExceptionCode.HEA_008_MSG);

		byte[] bfile = new byte[(int) temp.length()];

		try {
			FileInputStream fileInputStream = new FileInputStream(temp);
			fileInputStream.read(bfile);
			fileInputStream.close();

		} catch (FileNotFoundException e) {
			throw new ActionException(WebExceptionCode.HEA_006,
					WebExceptionCode.GLB_001_MSG, e);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileObject fileObject = new FileObject();
		fileObject.setFileContent(bfile);
		fileObject.setFileName(fileNameParts[0]);
		fileObject.setFileExtention(contentType);

		FileUtil.putFileToMap(getCurrentUser(), fileObject);

		return SUCCESS_RESULT;
	}

	public String save() {
		try {
			if (EmsUtil.checkListSize(records)) {
				PreparedMessageTO messageTO = records.get(0);
				messageTO.setPreparedState(PreparedMessageState.NEW);
				messageTO.setCreateDate(DateUtil.getDateWithoutTime(new Date()));
				messageTO.setSenderUsername(getCurrentUser());

				FileObject fileObject = FileUtil
						.getFileFromMap(getCurrentUser());
				if (fileObject != null) {
					messageTO.setAttachFile(fileObject.getFileContent());
					messageTO.setFileName(fileObject.getFileName());
					messageTO.setFileType(fileObject.getFileExtention());
					messageTO.setHasFile(true);
				} else
					messageTO.setHasFile(false);

				new PreparedMessageDelegator().save(messageTO);

				FileUtil.removeFileFromMap(getCurrentUser());

				return SUCCESS_RESULT;
			}
		} catch (BaseException e) {
			e.printStackTrace();
			return FAILURE_RESULT;
		}
		return FAILURE_RESULT;
	}

	public String load() throws BaseException {
		try {
			if (ids != null && !"".equals(ids)) {
				String[] id = ids.split(",");
				if (id.length > 0) {
					PreparedMessageTO messageTO = new PreparedMessageDelegator()
							.load(getUserProfile(), id[0]);

					List<PreparedMessageTO> list = new ArrayList<PreparedMessageTO>();
					list.add(messageTO);
					setRecords(list);
				}
			}

			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.HLA_002,
					WebExceptionCode.GLB_001_MSG, e);
		}

	}

	public String delete() throws BaseException {
		try {
			if (ids != null && !"".equals(ids)) {
				String[] id = ids.split(",");
				if (id.length > 0)
					new PreparedMessageDelegator().delete(getUserProfile(),
							id[0]);
			}

			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.HLA_002,
					WebExceptionCode.GLB_001_MSG, e);
		}
	}

	public String downloadAttachedFile() throws BaseException {
		try {
			PreparedMessageDelegator messageDelegator = new PreparedMessageDelegator();

			PreparedMessageTO preparedMessageTO = messageDelegator
					.downloadAttachedFile(getUserProfile(), id);
			ByteArrayInputStream bis = new ByteArrayInputStream(
					preparedMessageTO.getAttachFile());
			setInputStream(bis);
			setDownloadFileName(preparedMessageTO.getFileName() + "."
					+ preparedMessageTO.getFileType());
			setContentType(preparedMessageTO.getFileType());
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.HEA_001,
					WebExceptionCode.GLB_001_MSG, e);
		}
	}
	
	public String clearAttachment(){
		FileUtil.removeFileFromMap(getCurrentUser());
		return SUCCESS_RESULT;
	}

}