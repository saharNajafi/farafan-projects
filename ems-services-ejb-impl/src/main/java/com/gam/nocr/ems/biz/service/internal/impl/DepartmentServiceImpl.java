package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.EMSValueListProvider;
import com.gam.nocr.ems.data.dao.DepartmentDAO;
import com.gam.nocr.ems.data.dao.LocationDAO;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.LocationTO;
import com.gam.nocr.ems.data.domain.vol.DepartmentVTO;
import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;
import com.gam.nocr.ems.data.mapper.tomapper.DepartementMapper;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.ListMap;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import org.displaytag.exception.ListHandlerException;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "DepartmentService")
@Local(DepartmentServiceLocal.class)
@Remote(DepartmentServiceRemote.class)
public class DepartmentServiceImpl extends EMSAbstractService implements DepartmentServiceLocal, DepartmentServiceRemote {

    private static final String[] BOX_BATCH_ARRAY = new String[]{"BOX", "BATCH"};
    private static final String[] BATCH_CARD_ARRAY = new String[]{"BATCH", "CARD"};
    private static final String[] BOX_ARRAY = new String[]{"BOX"};
    private static final String[] BATCH_ARRAY = new String[]{"BATCH"};
    private static final String[] CARD_ARRAY = new String[]{"CARD"};

    @Override
    @Permissions(value = "ems_editDepartment || ems_addDepartment")
    @BizLoggable(logAction = "INSERT", logEntityName = "DEPARTMENT")
    public Long save(DepartmentVTO vto) throws BaseException {
        try {
            validate(vto);

            DepartmentDAO departmentDAO = getDepartmentDAO();
            DepartmentTO to = DepartementMapper.convert(vto);
            // parent
            DepartmentTO parent = departmentDAO.find(DepartmentTO.class, to.getParentId());
            if (parent == null)
                throw new ServiceException(BizExceptionCode.DSI_022, BizExceptionCode.DSI_022_MSG, new Long[]{to.getParentId()});

            String parentDN = parent.getParentDN();
            if (!EmsUtil.checkString(parentDN))
                to.setParentDN(parent.getDn());
            else
                to.setParentDN(parent.getDn() + "." + parent.getParentDN());
            to.setParentDepartment(parent);
            // dispatch send type
//			try {
//				DepartmentDispatchSendType sendType = DepartmentDispatchSendType.valueOf(to.getSendType());
//				to.setDispatchSendType(sendType);
//			} catch (IllegalArgumentException e) {
//				throw new ServiceException(BizExceptionCode.DSI_024, BizExceptionCode.DSI_024_MSG);
//			}
            // location
            LocationTO location = getLocationDAO().find(LocationTO.class, to.getLocId());
            if (location == null)
                throw new ServiceException(BizExceptionCode.DSI_027, BizExceptionCode.DSI_027_MSG, new Long[]{to.getLocId()});
            to.setLocation(location);

            String[] validDispatchType = getValidSendType(to);
            if (!Arrays.asList(validDispatchType).contains(to.getDispatchSendType().name()))
                throw new ServiceException(BizExceptionCode.DSI_004, BizExceptionCode.DSI_004_MSG);

            departmentDAO.create(to);
            vto.setId(to.getId());
            return to.getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DSI_029, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_editDepartment")
    @BizLoggable(logAction = "UPDATE", logEntityName = "DEPARTMENT")
    public Long update(DepartmentVTO vto) throws BaseException {
        try {
            validate(vto);

            DepartmentDAO departmentDAO = getDepartmentDAO();
            DepartmentTO to = DepartementMapper.convert(vto);
            // parent
            DepartmentTO parent = departmentDAO.find(DepartmentTO.class, to.getParentId());
            if (parent == null)
                throw new ServiceException(BizExceptionCode.DSI_022, BizExceptionCode.DSI_022_MSG, new Long[]{to.getParentId()});

            List<ListMap> list = fetchAllChildByParentId(to.getId());

            for (ListMap child : list) {
                if (child.get("id").toString().equals(to.getParentId().toString()))
                    throw new ServiceException(BizExceptionCode.DSI_039, BizExceptionCode.DSI_039_MSG, new Long[]{to.getParentId()});
            }

            String parentDN = parent.getParentDN();
            if (!EmsUtil.checkString(parentDN))
                to.setParentDN(parent.getDn());
            else
                to.setParentDN(parent.getDn() + "." + parent.getParentDN());
            to.setParentDepartment(parent);
            // dispatch send type
//			try {
//				DepartmentDispatchSendType sendType = DepartmentDispatchSendType.valueOf(to.getSendType());
//				to.setDispatchSendType(sendType);
//			} catch (IllegalArgumentException e) {
//				throw new ServiceException(BizExceptionCode.DSI_024, BizExceptionCode.DSI_024_MSG);
//			}
            // location
//			ProvinceTO location = getProvinceDAO().find(ProvinceTO.class, to.getLocId());
//			if (location == null)
//				throw new ServiceException(BizExceptionCode.DSI_027, BizExceptionCode.DSI_027_MSG, new Long[]{to.getLocId()});
            to.setLocation(new LocationTO(to.getLocId()));

            String[] validDispatchType = getValidSendType(to);
            if (!Arrays.asList(validDispatchType).contains(to.getDispatchSendType().name()))
                throw new ServiceException(BizExceptionCode.DSI_004, BizExceptionCode.DSI_004_MSG);

            departmentDAO.update(to);
            vto.setId(to.getId());
            return to.getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DSI_030, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_viewDepartment")
    @BizLoggable(logAction = "LOAD", logEntityName = "DEPARTMENT")
    public DepartmentVTO load(Long departmentId) throws BaseException {
        try {
            if (departmentId == null)
                throw new ServiceException(BizExceptionCode.DSI_020, BizExceptionCode.DSI_020_MSG);

            DepartmentTO to = getDepartmentDAO().find(DepartmentTO.class, departmentId);
            if (to.getDispatchSendType() != null)
                to.setSendType(to.getDispatchSendType().name());
            if (to.getParentDepartment() != null) {
                to.setParentId(to.getParentDepartment().getId());
                to.setParentName(to.getParentDepartment().getName());
            }
            if (to.getLocation() != null) {
                to.setLocId(to.getLocation().getId());
                to.setLocName(to.getLocation().getName());
            }
            return DepartementMapper.convert(to);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DSI_031, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_removeDepartment")
    @BizLoggable(logAction = "DELETE", logEntityName = "DEPARTMENT")
    public boolean remove(String departmentIds) throws BaseException {
        try {
            if (departmentIds == null || departmentIds.trim().length() == 0) {
                throw new ServiceException(BizExceptionCode.DSI_005, BizExceptionCode.DSI_005_MSG);
            } else {
                for (String departmentId : departmentIds.split(",")) {
                    if ("1".equals(departmentId))
                        throw new ServiceException(BizExceptionCode.DSI_035, BizExceptionCode.DSI_035_MSG);
                }
            }

            return getDepartmentDAO().removeDepartments(departmentIds);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DSI_032, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private String[] getValidSendType(DepartmentTO to) throws BaseException {
        DepartmentTO parent = to.getParentDepartment();
        DepartmentDispatchSendType parentDispatchType = parent.getDispatchSendType();

        if (to.getId() == null) {
            if (parentDispatchType == DepartmentDispatchSendType.BOX)
                return BOX_BATCH_ARRAY;
            else if (parentDispatchType == DepartmentDispatchSendType.BATCH)
                return BATCH_CARD_ARRAY;
            else if (parentDispatchType == DepartmentDispatchSendType.CARD)
                return CARD_ARRAY;
        } else {
            DepartmentDispatchSendType childDispatchSendType = null;
            List<DepartmentDispatchSendType> dispatchTypes = getDepartmentDAO().fetchChildDispatchType(to);
            if (dispatchTypes.contains(DepartmentDispatchSendType.BOX))
                childDispatchSendType = DepartmentDispatchSendType.BOX;
            else if (dispatchTypes.contains(DepartmentDispatchSendType.BATCH))
                childDispatchSendType = DepartmentDispatchSendType.BATCH;
            else if (dispatchTypes.contains(DepartmentDispatchSendType.CARD))
                childDispatchSendType = DepartmentDispatchSendType.CARD;

            if (parentDispatchType == DepartmentDispatchSendType.BOX && childDispatchSendType == DepartmentDispatchSendType.BOX)
                return BOX_ARRAY;
            else if (parentDispatchType == DepartmentDispatchSendType.BOX && childDispatchSendType == DepartmentDispatchSendType.BATCH)
                return BOX_BATCH_ARRAY;
            else if (parentDispatchType == DepartmentDispatchSendType.BOX && childDispatchSendType == DepartmentDispatchSendType.CARD)
                return BATCH_ARRAY;
            else if (parentDispatchType == DepartmentDispatchSendType.BATCH && childDispatchSendType == DepartmentDispatchSendType.BOX)
                throw new ServiceException(BizExceptionCode.DSI_003, BizExceptionCode.DSI_003_MSG);
            else if (parentDispatchType == DepartmentDispatchSendType.BATCH && childDispatchSendType == DepartmentDispatchSendType.BATCH)
                return BATCH_ARRAY;
            else if (parentDispatchType == DepartmentDispatchSendType.BATCH && childDispatchSendType == DepartmentDispatchSendType.CARD)
                return BATCH_CARD_ARRAY;
            else if (parentDispatchType == DepartmentDispatchSendType.CARD && childDispatchSendType == DepartmentDispatchSendType.BOX)
                throw new ServiceException(BizExceptionCode.DSI_003, BizExceptionCode.DSI_003_MSG);
            else if (parentDispatchType == DepartmentDispatchSendType.CARD && childDispatchSendType == DepartmentDispatchSendType.BATCH)
                throw new ServiceException(BizExceptionCode.DSI_003, BizExceptionCode.DSI_003_MSG);
            else if (parentDispatchType == DepartmentDispatchSendType.CARD && childDispatchSendType == DepartmentDispatchSendType.CARD)
                return CARD_ARRAY;
            else if (parentDispatchType == DepartmentDispatchSendType.BOX && dispatchTypes.size() == 0)
                return BOX_BATCH_ARRAY;
            else if (parentDispatchType == DepartmentDispatchSendType.BATCH && dispatchTypes.size() == 0)
                return BATCH_CARD_ARRAY;
            else if (parentDispatchType == DepartmentDispatchSendType.CARD && dispatchTypes.size() == 0)
                return CARD_ARRAY;

        }
        return new String[0];
    }

    @Override
    @Permissions(value = "ems_viewDepartmentList")
    public SearchResult fetchDepartments(String searchString, int from, int to, String orderBy) throws BaseException {
        HashMap param = new HashMap();
        param.put("depName", "%" + searchString + "%");
        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("departmentAC", "main,department".split(","), "count,department".split(","), param, orderBy, null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.DSI_012, BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.DSI_013, BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DSI_033, BizExceptionCode.GLB_008_MSG, e);
        }
    }
    
    //Adldoost
    @Override
    @Permissions(value = "ems_viewDepartmentList")
    public SearchResult fetchNOCR(String searchString, int from, int to, String orderBy) throws BaseException {
        HashMap param = new HashMap();
        param.put("depName", "%" + searchString + "%");
        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("NOCRAC", "main,department".split(","), "count,department".split(","), param, orderBy, null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.DSI_012, BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.DSI_013, BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DSI_033, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * Fetches all departments accessible through given user's department (its own department and all of its subdepartments)
     *
     * @param searchString Search string entered by user
     * @param from         Start index of data collection to be retrieved
     * @param to           Finish index of data collection to be retrieved
     * @param orderBy      Order of result
     * @return
     * @throws BaseException
     */
    @Override
    @Permissions(value = "ems_viewDepartmentList")
    public SearchResult fetchPersonDepartments(String searchString, int from, int to, String orderBy) throws BaseException {
    	Long personID = null;
    	
    	personID = getPersonService().findPersonIdByUsername(userProfileTO.getUserName());
        HashMap param = new HashMap();
        param.put("depName", "%" + searchString + "%");
        param.put("perid", personID);
        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("personDepartmentAC",
                    new String[]{"main"},
                    new String[]{"count"},
                    param,
                    orderBy,
                    null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.DSI_035, BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.DSI_037, BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DSI_038, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_viewDepartmentList")
    public SearchResult fetchNonEnrollmentDepartments(String searchString, int from, int to, String orderBy) throws BaseException {
        HashMap param = new HashMap();
        param.put("depName", "%" + searchString + "%");
        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("departmentAC",
                    "main,nonEnrollment".split(","),
                    "count,nonEnrollment".split(","),
                    param, orderBy, null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.DSI_014, BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.DSI_015, BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DSI_034, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private void validate(DepartmentVTO departmentVTO) throws BaseException {
        if (departmentVTO == null)
            throw new ServiceException(BizExceptionCode.DSI_002, BizExceptionCode.DSI_002_MSG);
//		if (departmentVTO.getParentId() == null)
//			throw new ServiceException(BizExceptionCode.DSI_021, BizExceptionCode.DSI_021_MSG);
//		if (departmentVTO.getSendType() == null || departmentVTO.getSendType().trim().equals(""))
//			throw new ServiceException(BizExceptionCode.DSI_023, BizExceptionCode.DSI_023_MSG);
        if (departmentVTO.getLocId() == null)
            throw new ServiceException(BizExceptionCode.DSI_026, BizExceptionCode.DSI_026_MSG);
        if (departmentVTO.getAddress() == null || departmentVTO.getAddress().trim().length() == 0)
            throw new ServiceException(BizExceptionCode.DSI_006, BizExceptionCode.DSI_006_MSG);
        if (departmentVTO.getCode() == null || departmentVTO.getCode().trim().length() == 0)
            throw new ServiceException(BizExceptionCode.DSI_007, BizExceptionCode.DSI_007_MSG);
        if (departmentVTO.getDn() == null || departmentVTO.getDn().trim().length() == 0)
            throw new ServiceException(BizExceptionCode.DSI_008, BizExceptionCode.DSI_008_MSG);
        if (departmentVTO.getName() == null || departmentVTO.getName().trim().length() == 0)
            throw new ServiceException(BizExceptionCode.DSI_009, BizExceptionCode.DSI_009_MSG);
        if (departmentVTO.getPostalCode() == null || departmentVTO.getPostalCode().trim().length() == 0)
            throw new ServiceException(BizExceptionCode.DSI_010, BizExceptionCode.DSI_010_MSG);
        if (departmentVTO.getPostalCode() != null && departmentVTO.getPostalCode().trim().length() != 0) {
            if (departmentVTO.getPostalCode().length() < 5 || departmentVTO.getPostalCode().length() > 10)
                throw new ServiceException(BizExceptionCode.DSI_011, BizExceptionCode.DSI_011_MSG);
        }
        if (departmentVTO.getAddress() == null || departmentVTO.getAddress().trim().equals(""))
            throw new ServiceException(BizExceptionCode.DSI_028, BizExceptionCode.DSI_028_MSG);
        /*if (departmentVTO instanceof EnrollmentOfficeTO) {
            EnrollmentOfficeTO enrollmentOfficeTO = (EnrollmentOfficeTO) departmentVTO;
			if (enrollmentOfficeTO.getManager().getId() == null)
				throw new ServiceException(BizExceptionCode.DSI_016, BizExceptionCode.DSI_016_MSG);
			if (enrollmentOfficeTO.getArea() != null && enrollmentOfficeTO.getArea() != 0) {
				if (enrollmentOfficeTO.getArea() > 10)
					throw new ServiceException(BizExceptionCode.DSI_017, BizExceptionCode.DSI_017_MSG);
			}
			if (enrollmentOfficeTO.getFax() != null && enrollmentOfficeTO.getFax().trim().length() != 0) {
				if (enrollmentOfficeTO.getFax().length() > 20)
					throw new ServiceException(BizExceptionCode.DSI_018, BizExceptionCode.DSI_018_MSG);
			}
			if (enrollmentOfficeTO.getPhone() != null && enrollmentOfficeTO.getPhone().trim().length() != 0) {
				if (enrollmentOfficeTO.getPhone().length() > 20)
					throw new ServiceException(BizExceptionCode.DSI_019, BizExceptionCode.DSI_019_MSG);
			}
		}*/
    }

    private List<ListMap> fetchAllChildByParentId(Long parentId) throws BaseException {
        HashMap param = new HashMap();
        param.put("depId", parentId.toString());
        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("departmentWithAllChildDepartments",
                    new String[]{"main"},
                    new String[]{"count"},
                    param,
                    null,
                    null);
            return vlh.getList(false);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.DSI_036, BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.DSI_040, BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DSI_041, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    // ================== Accessor =================

    private DepartmentDAO getDepartmentDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_DEPARTMENT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.DSI_001, BizExceptionCode.GLB_001_MSG, e, new String[]{EMSLogicalNames.DAO_DEPARTMENT});
        }
    }

    private LocationDAO getLocationDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_LOCATION));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.DSI_025, BizExceptionCode.GLB_001_MSG, e, new String[]{EMSLogicalNames.DAO_LOCATION});
        }
    }
    @Override
	@Permissions(value = "ems_viewDepartmentList")
	public SearchResult fetchAnyKindDepartments(String searchString, int from, int to,
			String orderBy, Map additionalParams) throws BaseException {

		HashMap param = new HashMap();
		param.put("depName", "%" + searchString + "%");
		StringBuilder parts = new StringBuilder();
		if (additionalParams != null) {

			if (additionalParams.get("locationId") != null) {
				parts.append(",location");
				param.put("location", additionalParams.get("locationId"));
			}
			if (additionalParams.containsKey("nocroffice")) {

				parts.append(",nocroffice");
				param.put("nocrId", additionalParams.get("nocroffice"));

			} else if (additionalParams.containsKey("office")) {
				parts.append(",office");
				param.put("nocrId", additionalParams.get("nocrId"));
			} else if (additionalParams.containsKey("person")) {
				parts.append(",person");

			} else if (additionalParams.containsKey("nonEnrollment")) {
				parts.append(",nonEnrollment");
			}

		}

		try {
			ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
					"departmentBaseOnLocationAC", ("main" + parts).split(","),
					("count" + parts).split(","), param, orderBy, null);
			List list = vlh.getList(from, to, true);
			return new SearchResult(vlh.size(), list);
		} catch (ListException e) {
			throw new ServiceException(BizExceptionCode.DSI_012,
					BizExceptionCode.GLB_006_MSG, e);
		} catch (ListHandlerException e) {
			throw new ServiceException(BizExceptionCode.DSI_013,
					BizExceptionCode.GLB_007_MSG, e);
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.DSI_033,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}
    
    
    @Override
    @Permissions(value = "ems_viewDepartmentList")
    public SearchResult fetchProvinces(String searchString, int from, int to,
    		String orderBy) throws BaseException {
    	 HashMap param = new HashMap();
         param.put("depName", "%" + searchString + "%");
         try {
             ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("departmentAC",
            		 "main,province".split(","),
                     "count,province".split(","),
                     param, orderBy, null);
             List list = vlh.getList(from, to, true);
             return new SearchResult(vlh.size(), list);
         } catch (ListException e) {
             throw new ServiceException(BizExceptionCode.DSI_014, BizExceptionCode.GLB_006_MSG, e);
         } catch (ListHandlerException e) {
             throw new ServiceException(BizExceptionCode.DSI_015, BizExceptionCode.GLB_007_MSG, e);
         } catch (Exception e) {
             throw new ServiceException(BizExceptionCode.DSI_034, BizExceptionCode.GLB_008_MSG, e);
         }
    }

    @Override
   public DepartmentTO fetchDepartment(Long deptId) throws BaseException{
       return getDepartmentDAO().fetchDepartment(deptId);
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
