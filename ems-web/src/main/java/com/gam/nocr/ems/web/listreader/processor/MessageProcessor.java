package com.gam.nocr.ems.web.listreader.processor;

import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import gampooya.tools.vlp.ValueListHandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ListResult;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.biz.delegator.MessageDelegator;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.MessageDestinationTO;
import com.gam.nocr.ems.data.domain.ws.MessageWTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * The list reader processor class for card dispatch grid
 * 
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class MessageProcessor extends EMSVLPListProcessor {

	private static final Logger logger = BaseLog
			.getLogger(MessageProcessor.class);

	protected ValueListHandler prepareVLH(ParameterProvider paramProvider)
			throws ListReaderException {
		return null;
	}

	@Override
	public ListResult fetchList(ParameterProvider paramProvider)
			throws ListReaderException {

		UserProfileTO userProfileTO = paramProvider.getUserProfileTO();
		String msgType = paramProvider.getParameter("msgType");
		int pageSize = paramProvider.getTo() - paramProvider.getFrom();
		int pageNo = (paramProvider.getTo() / pageSize) - 1; // zero index

		String msgDate = paramProvider.getParameter("msgDate");

		Timestamp time = null;

		if (EmsUtil.checkString(msgDate)) {
			Date convert;
			try {
				convert = DateUtil.convert(msgDate, DateUtil.GREGORIAN);
				time = new Timestamp(DateUtil.getDateWithoutTime(convert).getTime());
			} catch (DateFormatException e) {
				e.printStackTrace();
			}
		}

		MessageWTO messageWTO = new MessageWTO(time, msgType, pageSize, pageNo);
		Long personID = null;

		try {
			List<MessageDestinationTO> list = new MessageDelegator()
					.getMessageByCriteria(userProfileTO, messageWTO);
			
			personID = getPersonService().findPersonIdByUsername(userProfileTO.getUserName());

			List<MessageWTO> wtoList = new ArrayList<MessageWTO>();

			if (EmsUtil.checkListSize(list)) {

				for (MessageDestinationTO messageDestinationTO : list) {

					MessageWTO wto = new MessageWTO();

					wto.setId(messageDestinationTO.getId());

					wto.setMsgDate(new Timestamp(messageDestinationTO
							.getMessage().getCreateDate().getTime()));

					wto.setMsgSubject(messageDestinationTO.getMessage()
							.getTitle());

					wto.setMsgDownload((messageDestinationTO.getMessage()
							.getHasFile() != null || messageDestinationTO
							.getMessage().getHasFile()) ? "1" : "0");

					wto.setSenderUsername(messageDestinationTO.getMessage()
							.getSenderUsername());

					if (messageDestinationTO.getSeenList().indexOf(
							"@" + personID + "@") == -1) {
						wto.setMsgUnread("U");
					} else {
						wto.setMsgUnread("R");
					}
					wtoList.add(wto);
				}

			}

			return new ListResult("messageCcosList", wtoList.size(), wtoList);
		} catch (BaseException e) {
			e.printStackTrace();
		}

		return super.fetchList(paramProvider);
	}
	
	//Anbari
    private PersonManagementService getPersonService() throws BaseException {
        PersonManagementService personManagementService;
        try {
            personManagementService = (PersonManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_PERSON),null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.PDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_PERSON.split(","));
        }
        return personManagementService;
    }
}
