package com.gam.nocr.ems.biz.service;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface PortalSmsService extends Service {

//    public List<Long> retrieveRequestedSms() throws BaseException;

    public void addRequestedSms(Long portalCardRequestId) throws BaseException;

    public Integer fetchReadyToProcessSms() throws BaseException;

    public Boolean processSms(Integer from, Integer to) throws BaseException;
    
	Integer fetchReferToCCOSProcessSms() throws BaseException;

	Boolean processReferToCCOSSms(Integer from, Integer to)
			throws BaseException;

	public Integer fetchSmsCount(int smsType) throws BaseException;

	public Boolean processSmsToSend(Integer from, int to, int smsType)throws BaseException;

	public void deleteOldRecordsFromMsgt(Integer timeInterval, Integer smsType)throws BaseException;
}
