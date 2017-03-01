package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.DocTypeService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.vol.DocTypeVTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public class DocTypeDelegator implements Delegator {

    private DocTypeService getService(UserProfileTO userProfileTO) throws BaseException {
        DocTypeService docTypeService = null;
        try {
            docTypeService = ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_DOC_TYPE), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.DTD_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_DOC_TYPE.split(","));
        }
        docTypeService.setUserProfileTO(userProfileTO);
        return docTypeService;
    }

    public DocTypeVTO load(UserProfileTO userProfileTO, long docTypeId) throws BaseException {
        return getService(userProfileTO).load(docTypeId);
    }

    public long save(UserProfileTO userProfileTO, DocTypeVTO to) throws BaseException {
        return getService(userProfileTO).save(to);
    }

    public long update(UserProfileTO userProfileTO, DocTypeVTO to) throws BaseException {
        return getService(userProfileTO).update(to);
    }

    public boolean remove(UserProfileTO userProfileTO, String docTypeIds) throws BaseException {
        return getService(userProfileTO).remove(docTypeIds);
    }

}
