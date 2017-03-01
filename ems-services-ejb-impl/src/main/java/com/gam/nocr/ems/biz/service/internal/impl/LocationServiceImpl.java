package com.gam.nocr.ems.biz.service.internal.impl;
import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_LOCATION;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.displaytag.exception.ListHandlerException;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.EMSValueListProvider;
import com.gam.nocr.ems.data.dao.LocationDAO;
import com.gam.nocr.ems.data.domain.ws.ProvinceWTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
@Stateless(name = "LocationService")
@Local(LocationServiceLocal.class)
@Remote(LocationServiceRemote.class)
public class LocationServiceImpl extends EMSAbstractService implements LocationServiceLocal, LocationServiceRemote {

    @Override
    public SearchResult fetchLocations(String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException {
        HashMap param = new HashMap();
        StringBuilder parts = new StringBuilder();

        if (searchString.indexOf(" (") > 0) {
            param.put("name", "%" + searchString.substring(0, searchString.indexOf(" (")) + "%");
        } else {
            param.put("name", "%" + searchString + "%");
        }


        if ((additionalParams != null) && (additionalParams.get("superDepartmentID") != null)) {
            parts.append(",cityAndVillage");
            param.put("superDepartmentID", additionalParams.get("superDepartmentID"));
        } else {
            parts.append(",allCities");
        }

        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("locationAC", ("main" + parts).split(","), ("count" + parts).split(","), param, orderBy, null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.LMS_001, BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.LMS_002, BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.LMS_003, BizExceptionCode.GLB_008_MSG, e);
        }
    }



    @Override
    public SearchResult getAllProvinces(String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException {
        HashMap param = new HashMap();
        StringBuilder parts = new StringBuilder();

        if (searchString.indexOf(" (") > 0) {
            param.put("name", "%" + searchString.substring(0, searchString.indexOf(" (")) + "%");
        } else {
            param.put("name", "%" + searchString + "%");
        }


        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("locationProAC", ("main" + parts).split(","), ("count" + parts).split(","), param, orderBy, null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.LMS_001, BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.LMS_002, BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.LMS_003, BizExceptionCode.GLB_008_MSG, e);
        }
    }
    /**
	 * 
	 * @author ganjyar
	 * @param generalCriteria
	 * @return
	 * @throws BaseException
	 */
	@Override
	public List<ProvinceWTO> fetchLocationLivingList(
			GeneralCriteria generalCriteria) throws BaseException {

		try {
			List<ProvinceWTO> locations = getLocationDAO()
					.fetchLocationLivingList(generalCriteria);
			return locations;
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.CRE_030,
					BizExceptionCode.GLB_008_MSG, e);
		}

	}
	
	/**
	 * @author ganjyar
	 * @return
	 * @throws BaseException
	 */
	private LocationDAO getLocationDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
				EMSLogicalNames.getDaoJNDIName(DAO_LOCATION));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.CRE_031,
					BizExceptionCode.GLB_001_MSG, e, DAO_LOCATION.split(","));
		}
	}

}
