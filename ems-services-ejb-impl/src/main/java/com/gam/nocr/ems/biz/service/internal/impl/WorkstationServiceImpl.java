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
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.EMSValueListProvider;
import com.gam.nocr.ems.data.dao.DepartmentDAO;
import com.gam.nocr.ems.data.dao.WorkstationDAO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.data.enums.WorkstationState;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.ListMap;
import gampooya.tools.vlp.ValueListHandler;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_DEPARTMENT;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "WorkstationService")
@Local(WorkstationServiceLocal.class)
@Remote(WorkstationServiceRemote.class)
public class WorkstationServiceImpl extends EMSAbstractService implements WorkstationServiceLocal, WorkstationServiceRemote {

    /**
     * ===================
     * Getter for DAOs
     * ===================
     */

    /**
     * getPersonDAO
     *
     * @return an instance of type {@link DepartmentDAO}
     */
    private DepartmentDAO getDepartmentDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_DEPARTMENT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.WSI_021,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_DEPARTMENT});
        }
    }

    /**
     * The method checkDepartment is used to check whether the selected enrollment office id equals to the user department
     * or one of the children departments of current user
     *
     * @param personId                   is an instance of type {@link Long}, which represents an instance of type
     *                                   {@link com.gam.nocr.ems.data.domain.PersonTO} belongs to the current user
     * @param selectedEnrollmentOfficeId is an instance of type {@link Long}, which represents an instance of type {@link EnrollmentOfficeTO}
     * @throws BaseException
     */
    private void checkDepartment(Long personId, Long selectedEnrollmentOfficeId) throws BaseException {

        List<ListMap> depIds;
        try {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("perid", personId.toString());
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("personAllDepartmentWithSubDepartment",
                    "main".split(","), "count".split(","), param, null, null);
            depIds = vlh.getList(false);
            if (!EmsUtil.checkListSize(depIds)) {
                throw new ServiceException(BizExceptionCode.WSI_022, BizExceptionCode.WSI_022_MSG);
            }

            boolean foundDepIdFlag = false;
            for (ListMap result : depIds) {
                if (result.get("id").equals(BigDecimal.valueOf(selectedEnrollmentOfficeId))) {
                    foundDepIdFlag = true;
                    break;
                }
            }
            if (!foundDepIdFlag) {
                throw new ServiceException(BizExceptionCode.WSI_023, BizExceptionCode.WSI_023_MSG);
            }

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.WSI_024, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_editWorkstation || ems_addWorkstation")
    @BizLoggable(logAction = "INSERT", logEntityName = "WORKSTATION")
    public long save(WorkstationTO to) throws BaseException {
        try {
            if (to == null)
                throw new ServiceException(BizExceptionCode.WSI_010, BizExceptionCode.WSI_010_MSG);
            Long personID = getPersonService().findPersonIdByUsername(getUserProfileTO().getUserName());

            checkDepartment(personID,
                    to.getEnrollmentOfficeId() != null ? to.getEnrollmentOfficeId() : to.getEnrollmentOffice().getId());

            if (to.getId() == null) {
                if (to.getState() == null)
                    to.setState(WorkstationState.A);
                if (to.getEnrollmentOffice() == null)
                    to.setEnrollmentOffice(new EnrollmentOfficeTO(to.getEnrollmentOfficeId()));
            } else {
                WorkstationTO workstationTO = getWorkstationDAO().find(WorkstationTO.class, to.getId());
                to.setEnrollmentOffice(new EnrollmentOfficeTO(to.getEnrollmentOfficeId()));
                to.setState(workstationTO.getState());
            }
            validate(to);
            WorkstationTO workstationTO = getWorkstationDAO().create(to);
            return workstationTO.getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.WSI_015, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_viewWorkstation")
    @BizLoggable(logAction = "LOAD", logEntityName = "WORKSTATION")
    public WorkstationTO load(Long workstationId) throws BaseException {
        try {
            if (workstationId == null)
                throw new ServiceException(BizExceptionCode.WSI_009, BizExceptionCode.WSI_009_MSG);

            return getWorkstationDAO().find(WorkstationTO.class, workstationId);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.WSI_016, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_removeWorkstation")
    @BizLoggable(logAction = "DELETE", logEntityName = "WORKSTATION")
    public boolean remove(String workstationIds) throws BaseException {
        try {
            if (workstationIds == null || workstationIds.trim().length() == 0)
                throw new ServiceException(BizExceptionCode.WSI_011, BizExceptionCode.WSI_011_MSG);

            getWorkstationDAO().removeWorkstations(workstationIds);
            return true;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.WSI_017, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public WorkstationTO find(String workstationActivationCode) throws BaseException {
        try {
            if (workstationActivationCode == null || workstationActivationCode.trim().length() == 0)
                throw new ServiceException(BizExceptionCode.WSI_012, BizExceptionCode.WSI_012_MSG);

            return getWorkstationDAO().findByActivationCode(workstationActivationCode);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.WSI_018, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_approveWorkstation")
    @BizLoggable(logAction = "APPROVE", logEntityName = "WORKSTATION")
    public void approveWorkstation(String workstationIds) throws BaseException {
        try {
            if (workstationIds == null || workstationIds.trim().length() == 0)
                throw new ServiceException(BizExceptionCode.WSI_002, BizExceptionCode.WSI_009_MSG);

            getWorkstationDAO().approveWorkstation(workstationIds);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.WSI_019, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_rejectWorkstation")
    @BizLoggable(logAction = "REJECT", logEntityName = "WORKSTATION")
    public void rejectWorkstation(String workstationIds) throws BaseException {
        try {
            if (workstationIds == null || workstationIds.trim().length() == 0)
                throw new ServiceException(BizExceptionCode.WSI_014, BizExceptionCode.WSI_009_MSG);

            getWorkstationDAO().rejectWorkstation(workstationIds);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.WSI_020, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private void validate(WorkstationTO workstationTO) throws BaseException {
        if (!EmsUtil.checkString(workstationTO.getActivationCode()))
            throw new ServiceException(BizExceptionCode.WSI_004, BizExceptionCode.WSI_004_MSG);
        if (!EmsUtil.checkString(workstationTO.getCode()))
            throw new ServiceException(BizExceptionCode.WSI_005, BizExceptionCode.WSI_005_MSG);
        if (workstationTO.getCode() != null && workstationTO.getCode().trim().length() != 0) {
            if (workstationTO.getCode().length() > 100)
                throw new ServiceException(BizExceptionCode.WSI_007, BizExceptionCode.WSI_007_MSG);
        }
        if (workstationTO.getEnrollmentOffice() == null || workstationTO.getEnrollmentOffice().getId() == null)
            throw new ServiceException(BizExceptionCode.WSI_013, BizExceptionCode.WSI_014_MSG);
//		if (workstationTO.getEnrollmentOffice() != null) {
//			if (workstationTO.getEnrollmentOffice().getId() == null)
//				throw new ServiceException(BizExceptionCode.WSI_003, BizExceptionCode.WSI_003_MSG);
//		}
        if (workstationTO.getState() == null)
            throw new ServiceException(BizExceptionCode.WSI_006, BizExceptionCode.WSI_006_MSG);
        if (workstationTO.getState() != null) {
            if (workstationTO.getState().toString().length() != 1)
                throw new ServiceException(BizExceptionCode.WSI_008, BizExceptionCode.WSI_008_MSG);
        }
    }

    // ================== Accessor =================

    public WorkstationDAO getWorkstationDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_WORKSTATION));
        } catch (DAOFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.WSI_001,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_WORKSTATION});
        }
    }

    @Override
    public WorkstationTO findByDepartmentIdAndActivationCode(Long departmentId,
                                                             String workstationActivationCode) throws BaseException {
        return getWorkstationDAO().findByDepartmentIdAndActivationCode(departmentId, workstationActivationCode);
    }
    

	// Anbari
	private PersonManagementService getPersonService() throws BaseException {
		PersonManagementService personManagementService;
		try {
			personManagementService = (PersonManagementService) ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_PERSON),
							null);
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.PDL_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_PERSON.split(","));
		}
		return personManagementService;
	}
}
