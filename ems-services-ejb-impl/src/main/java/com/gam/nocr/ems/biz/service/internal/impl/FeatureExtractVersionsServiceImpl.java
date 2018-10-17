package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSValueListProvider;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import org.displaytag.exception.ListHandlerException;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/13/18.
 */
@Stateless(name = "FeatureExtractVersionsService")
@Local(FeatureExtractVersionsServiceLocal.class)
@Remote(FeatureExtractVersionsServiceRemote.class)
public class FeatureExtractVersionsServiceImpl  extends EMSAbstractService
        implements FeatureExtractVersionsServiceLocal, FeatureExtractVersionsServiceRemote  {
    @Override
    public SearchResult FeatureExtractVersionList(String searchString, int from, int to, String orderBy) throws BaseException {
        try {
            HashMap param = new HashMap();
            StringBuilder parts = new StringBuilder();

            if (searchString.indexOf(" (") > 0) {
                param.put("name", "%" + searchString.substring(0, searchString.indexOf(" (")) + "%");
            } else {
                param.put("name", "%" + searchString + "%");
            }
            try {
                ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
                        "featureExtractVersionsAC", ("main" + parts).split(","), ("count" + parts).split(","), param, orderBy, null);
                List list = vlh.getList(from, to, true);
                return new SearchResult(vlh.size(), list);
            } catch (ListException e) {
                throw new ServiceException(BizExceptionCode.FEV_001, BizExceptionCode.GLB_006_MSG, e);
            } catch (ListHandlerException e) {
                throw new ServiceException(BizExceptionCode.FEV_002, BizExceptionCode.GLB_007_MSG, e);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.FEV_003, BizExceptionCode.GLB_008_MSG, e);
        }
    }

}
