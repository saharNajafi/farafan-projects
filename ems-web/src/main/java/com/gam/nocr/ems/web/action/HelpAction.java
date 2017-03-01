package com.gam.nocr.ems.web.action;

import gampooya.tools.security.BusinessSecurityException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.HelpDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.HelpTO;
import com.gam.nocr.ems.data.domain.vol.HelpVTO;
import com.gam.nocr.ems.web.ws.EMSWebServiceFault;
import com.gam.nocr.ems.web.ws.InternalException;

public class HelpAction extends ListControllerImpl<HelpVTO> {

	static final Logger logger = BaseLog.getLogger(HelpAction.class);

	private String id;
	private InputStream inputStream;
	private String downoadFileName;
	private String conentType;

	private String title;
	private String desc;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getDownloadFileName() {
		return downoadFileName;
	}

	public void setDownloadFileName(String name) {
		downoadFileName = name;
	}

	public String getContentType() {
		return conentType;
	}

	public void setContentType(String typeName) {
		conentType = typeName;
	}

	@Override
	public void setRecords(List<HelpVTO> records) {
		this.records = records;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String save() throws BaseException {
		try {

			MultiPartRequestWrapper request2 = (MultiPartRequestWrapper) ServletActionContext
					.getRequest();
			
			if(request2.getFiles("hfile")==null || request2.getFiles("hfile").length==0)
				throw new ActionException(WebExceptionCode.HEA_008,
						WebExceptionCode.HEA_008_MSG);

			File files = request2.getFiles("hfile")[0];

			File temp = new File(files.getPath());
			request2.getFiles("hfile")[0].getName();

			
			String fileNameParts[] = request2.getFileNames("hfile")[0]
					.split(Pattern.quote("."));
			String contentType = fileNameParts[fileNameParts.length - 1];
			if(contentType==null)
				throw new ActionException(WebExceptionCode.HEA_007,
						WebExceptionCode.HEA_007_MSG);
			
			if ( !contentType.equals("pdf") && !contentType.equals("txt")
					&& !contentType.equals("docx")
					&& !contentType.equals("xlsx")) {
				throw new ActionException(WebExceptionCode.HEA_007,
						WebExceptionCode.HEA_007_MSG);
			}
			int length =(int) temp.length();
			
			if(length > 5242880)
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
			}
			
			

			HelpVTO helpVTO = new HelpVTO();
			helpVTO.setHelpFile(bfile);
			helpVTO.setTitle(title);
			helpVTO.setDescription(desc);
			
			helpVTO.setContentType(contentType);

			HelpDelegator docTypeDelegator = new HelpDelegator();
			docTypeDelegator.save(getUserProfile(), helpVTO);
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.HEA_005,
					WebExceptionCode.GLB_001_MSG, e);
		} catch (Exception e) {
			String code = e.getMessage();
			if(code.contains("HEA_007"))
			{
				throw new ActionException(WebExceptionCode.HEA_007,
						WebExceptionCode.HEA_007_MSG,e);
			}
			else if(code.contains("HEA_008"))
			{
				throw new ActionException(WebExceptionCode.HEA_008,
						WebExceptionCode.HEA_008_MSG,e);
			}
			else
				throw new ActionException(WebExceptionCode.HEA_007,
						WebExceptionCode.GLB_003_MSG,e);

		}
	}

	public String delete() throws BaseException {
		try {
			HelpDelegator ratingDelegator = new HelpDelegator();

			ratingDelegator.remove(getUserProfile(), ids);

			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.HEA_002,
					WebExceptionCode.GLB_001_MSG, e);
		}
	}

	public String downloadHelpFile() throws BaseException {
		try {
			HelpDelegator ratingDelegator = new HelpDelegator();

			HelpTO helpTO = ratingDelegator.downloadHelpFile(getUserProfile(),
					Long.parseLong(id));
			ByteArrayInputStream bis = new ByteArrayInputStream(
					helpTO.getHelpFile());
			setInputStream(bis);
			setDownloadFileName("Help-" + helpTO.getId() + "."
					+ helpTO.getContentType());
			setContentType("pdf");
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.HEA_001,
					WebExceptionCode.GLB_001_MSG, e);
		}
	}

}
