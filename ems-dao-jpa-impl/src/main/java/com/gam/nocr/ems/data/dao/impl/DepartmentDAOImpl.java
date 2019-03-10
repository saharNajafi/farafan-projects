package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "DepartmentDAO")
@Local(DepartmentDAOLocal.class)
@Remote(DepartmentDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DepartmentDAOImpl extends EmsBaseDAOImpl<DepartmentTO> implements DepartmentDAOLocal, DepartmentDAORemote {

    private static final String UNIQUE_KEY_DEP_CODE = "AK_DEP_CODE";
    private static final String UNIQUE_KEY_DEP_DN_NAME = "AK_DEP_DN_NAME";
    private static final String UNIQUE_KEY_DEP_NAME = "AK_DEP_NAME";
    private static final String FOREIGN_KEY_DEP_LOCATION_PRV_ID = "FK_DEP_LOCATION_PRV_ID";
    private static final String FOREIGN_KEY_DEP_PARENT_DEP_ID = "FK_DEP_PARENT_DEP_ID";

    private static final String UNIQUE_KEY_ENROLL_OFC_PER_ID = "AK_ENROLL_OFC_PER_ID";
    private static final String FOREIGN_KEY_ENROLL_OFC_DEP_ID = "FK_ENROLL_OFC_DEP_ID";
    private static final String FOREIGN_KEY_ENROLL_OFC_PER_ID = "FK_ENROLL_OFC_PER_ID";
    private static final String FOREIGN_KEY_ENROLL_OFC_RAT_ID = "FK_ENROLL_OFC_RAT_ID";
    private static final String INTEGRITY_CONSTRAINT_EOF_TYPE = "CKC_EOF_TYPE";

    private static final String FOREIGN_KEY_BOX_NXT_REC_DEP_ID = "FK_BOX_NXT_REC_DEP_ID";
    private static final String FOREIGN_KEY_BATCH_NXT_REC_DEP_ID = "FK_BATCH_NXT_REC_DEP_ID";
    private static final String FOREIGN_KEY_DEP_PROFILE_DEP_ID = "FK_DEP_PROFILE_DEP_ID";
    private static final String FOREIGN_KEY_PER_DEP_ID = "FK_PER_DEP_ID";
    private static final String FOREIGN_KEY_WORKST_ENROLL_OFC_ID = "FK_WORKST_ENROLL_OFC_ID";
    private static final String FOREIGN_KEY_NET_TKN_ENR_OFC_ID = "FK_NET_TKN_ENR_OFC_ID";
    private static final String FOREIGN_KEY_RESERV_ENR_OFC_ID = "FK_RESERV_ENR_OFC_ID";
    private static final String FOREIGN_KEY_CARD_REQ_ENROLL_OFC_ID = "FK_CARD_REQ_ENROLL_OFC_ID";
    private static final String FOREIGN_KEY_EOF_SUPERIOR_OFFICE = "FK_EOF_SUPERIOR_OFFICE";
    private static final String FOREIGN_KEY_CRQ_ORG_ENROLL_OFFICE_ID = "FK_CRQ_ORG_ENROLL_OFFICE_ID";
    private static final String FOREIGN_KEY_MESS_OFFICE_FK = "EMST_MESS_OFFICE_FK";

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The create method, handles all the save operations for all the classes which are extended from EntityTO
     *
     * @param departmentTO - the object of type EntityTO to create
     * @return the object of type EntityTo
     */
    @Override
    public DepartmentTO create(DepartmentTO departmentTO) throws BaseException {
        try {
            DepartmentTO to = super.create(departmentTO);
            em.flush();
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_DEP_CODE))
                throw new DAOException(DataExceptionCode.DDI_004, DataExceptionCode.DDI_004_MSG, e);
            if (err.contains(UNIQUE_KEY_DEP_DN_NAME))
                throw new DAOException(DataExceptionCode.DDI_005, DataExceptionCode.DDI_005_MSG, e);
            if (err.contains(UNIQUE_KEY_DEP_NAME))
                throw new DAOException(DataExceptionCode.DDI_006, DataExceptionCode.DDI_006_MSG, e);
            if (err.contains(FOREIGN_KEY_DEP_LOCATION_PRV_ID))
                throw new DAOException(DataExceptionCode.DDI_025, DataExceptionCode.DDI_025_MSG, e, new Long[]{departmentTO.getLocation().getId()});
            if (err.contains(FOREIGN_KEY_DEP_PARENT_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_026, DataExceptionCode.DDI_026_MSG, e, new Long[]{departmentTO.getParentDepartment().getId()});
            if (departmentTO instanceof EnrollmentOfficeTO) {
                EnrollmentOfficeTO eof = (EnrollmentOfficeTO) departmentTO;
                if (err.contains(UNIQUE_KEY_ENROLL_OFC_PER_ID))
                    throw new DAOException(DataExceptionCode.DDI_049, DataExceptionCode.DDI_049_MSG, e, new Long[]{eof.getId()});
                if (err.contains(FOREIGN_KEY_ENROLL_OFC_DEP_ID))
                    throw new DAOException(DataExceptionCode.DDI_027, DataExceptionCode.DDI_027_MSG, e, new Long[]{eof.getId()});
                if (err.contains(FOREIGN_KEY_ENROLL_OFC_PER_ID))
                    throw new DAOException(DataExceptionCode.DDI_028, DataExceptionCode.DDI_028_MSG, e, new Long[]{eof.getManager().getId()});
                if (err.contains(FOREIGN_KEY_ENROLL_OFC_RAT_ID))
                    throw new DAOException(DataExceptionCode.DDI_029, DataExceptionCode.DDI_029_MSG, e, new Long[]{eof.getRatingInfo().getId()});
                if (err.contains(INTEGRITY_CONSTRAINT_EOF_TYPE))
                    throw new DAOException(DataExceptionCode.DDI_048, DataExceptionCode.DDI_048_MSG, e, new Long[]{eof.getRatingInfo().getId()});

            }
            throw new DAOException(DataExceptionCode.DDI_030, DataExceptionCode.DDI_030_MSG, e);
        }
    }

    /**
     * The Update method, handles the update operations for all the classes which are extended from EntityTO.
     *
     * @param departmentTO - the object of type EntityTO to create
     * @return the object which of type EntityTO, or null if the object is not found
     */
    @Override
    public DepartmentTO update(DepartmentTO departmentTO) throws BaseException {
        try {
            DepartmentTO to = super.update(departmentTO);
            em.flush();
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_DEP_CODE))
                throw new DAOException(DataExceptionCode.DDI_004, DataExceptionCode.DDI_004_MSG, e);
            if (err.contains(UNIQUE_KEY_DEP_DN_NAME))
                throw new DAOException(DataExceptionCode.DDI_005, DataExceptionCode.DDI_005_MSG, e);
            if (err.contains(UNIQUE_KEY_DEP_NAME))
                throw new DAOException(DataExceptionCode.DDI_006, DataExceptionCode.DDI_006_MSG, e);
            if (err.contains(FOREIGN_KEY_DEP_LOCATION_PRV_ID))
                throw new DAOException(DataExceptionCode.DDI_031, DataExceptionCode.DDI_031_MSG, e, new Long[]{departmentTO.getLocation().getId()});
            if (err.contains(FOREIGN_KEY_DEP_PARENT_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_032, DataExceptionCode.DDI_032_MSG, e, new Long[]{departmentTO.getParentDepartment().getId()});
            if (departmentTO instanceof EnrollmentOfficeTO) {
                EnrollmentOfficeTO eof = (EnrollmentOfficeTO) departmentTO;
                if (err.contains(UNIQUE_KEY_ENROLL_OFC_PER_ID))
                    throw new DAOException(DataExceptionCode.DDI_050, DataExceptionCode.DDI_049_MSG, e, new Long[]{eof.getId()});
                if (err.contains(FOREIGN_KEY_ENROLL_OFC_DEP_ID))
                    throw new DAOException(DataExceptionCode.DDI_033, DataExceptionCode.DDI_033_MSG, e, new Long[]{eof.getId()});
                if (err.contains(FOREIGN_KEY_ENROLL_OFC_PER_ID))
                    throw new DAOException(DataExceptionCode.DDI_034, DataExceptionCode.DDI_034_MSG, e, new Long[]{eof.getManager().getId()});
                if (err.contains(FOREIGN_KEY_ENROLL_OFC_RAT_ID))
                    throw new DAOException(DataExceptionCode.DDI_035, DataExceptionCode.DDI_035_MSG, e, new Long[]{eof.getRatingInfo().getId()});

            }
            throw new DAOException(DataExceptionCode.DDI_007, DataExceptionCode.DDI_007_MSG, e);
        }
    }

    /**
     * The find method, handles the find operation on the classes which are extended from EntityTO.
     *
     * @param type - the class type of an instance of the class
     * @param id   - the id number of an instance of the class
     * @return the object which of type EntityTO, or null if the object is not found
     */
    @Override
    public DepartmentTO find(Class type, Object id) throws BaseException {
        DepartmentTO department = null;
        try {
            department = super.find(type, id);
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DDI_001, DataExceptionCode.GLB_003_MSG, e);
        }
        if (department != null)
            return department;
        else
            throw new DAOException(DataExceptionCode.DDI_008, DataExceptionCode.DDI_008_MSG);
    }

    @Override
    public boolean removeDepartments(String departmentIds) throws BaseException {
        List<Long> depIds = new ArrayList<Long>();
        try {
            for (String id : departmentIds.split(","))
                depIds.add(Long.parseLong(id.trim()));
        } catch (NumberFormatException e) {
            throw new DAOException(DataExceptionCode.DDI_047, DataExceptionCode.DDI_047_MSG, e);
        }
        int depsToDelete = depIds.size();
        int depsDeleted = 0;
        try {
            /*depsDeleted = em.createQuery("delete from DepartmentTO dep " +
                       "where dep.id in (:depIds)")
                       .setParameter("depIds", depIds)
                       .executeUpdate();*/
            DepartmentTO dep;
            int cache = 0;
            for (Long id : depIds) {
                em.createQuery("delete OfficeActiveShiftTO activeShift " +
                        "where activeShift.enrollmentOffice.id=:enrollmentOfficeId")
                        .setParameter("enrollmentOfficeId", id)
                        .executeUpdate();
                em.createQuery("delete OfficeCapacityTO capacity " +
                        "where capacity.enrollmentOffice.id=:enrollmentOfficeId")
                        .setParameter("enrollmentOfficeId", id)
                        .executeUpdate();
                dep = em.find(DepartmentTO.class, id);
                em.remove(dep);
                depsDeleted++;
                if (++cache % 10 == 0) {
                    em.flush();
                }
            }
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(FOREIGN_KEY_BOX_NXT_REC_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_036, DataExceptionCode.DDI_036_MSG, e);
            if (err.contains(FOREIGN_KEY_BATCH_NXT_REC_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_037, DataExceptionCode.DDI_037_MSG, e);
            if (err.contains(FOREIGN_KEY_DEP_PROFILE_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_038, DataExceptionCode.DDI_038_MSG, e);
            if (err.contains(FOREIGN_KEY_PER_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_039, DataExceptionCode.DDI_039_MSG, e);
            if (err.contains(FOREIGN_KEY_ENROLL_OFC_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_040, DataExceptionCode.DDI_040_MSG, e);
            if (err.contains(FOREIGN_KEY_WORKST_ENROLL_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_041, DataExceptionCode.DDI_041_MSG, e);
            if (err.contains(FOREIGN_KEY_NET_TKN_ENR_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_042, DataExceptionCode.DDI_042_MSG, e);
            if (err.contains(FOREIGN_KEY_RESERV_ENR_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_043, DataExceptionCode.DDI_043_MSG, e);
            if (err.contains(FOREIGN_KEY_CARD_REQ_ENROLL_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_044, DataExceptionCode.DDI_044_MSG, e);
            if (err.contains(FOREIGN_KEY_EOF_SUPERIOR_OFFICE))
                throw new DAOException(DataExceptionCode.DDI_052, DataExceptionCode.DDI_052_MSG, e);
            if (err.contains(FOREIGN_KEY_CRQ_ORG_ENROLL_OFFICE_ID))
                throw new DAOException(DataExceptionCode.DDI_053, DataExceptionCode.DDI_053_MSG, e);
            if (err.contains(FOREIGN_KEY_MESS_OFFICE_FK))
                throw new DAOException(DataExceptionCode.DDI_054, DataExceptionCode.DDI_054_MSG, e);
            if (err.contains("integrity constraint"))
                throw new DAOException(DataExceptionCode.DDI_045, DataExceptionCode.DDI_045_MSG, e);
            throw new DAOException(DataExceptionCode.DDI_046, DataExceptionCode.DDI_046_MSG, e);
        }
        return depsToDelete == depsDeleted;
    }

    @Override
    public boolean removePersons(String departmentIds) throws BaseException {

        List<Long> ids = new ArrayList<Long>();
        for (String id : departmentIds.split(","))
            ids.add(Long.parseLong(id.trim()));

        try {
            em.createQuery(" DELETE FROM DepartmentTO ED " +
                    " where ED.id in (:ids) ")
                    .setParameter("ids", ids)
                    .executeUpdate();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(FOREIGN_KEY_DEP_PARENT_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_002, DataExceptionCode.DDI_002_MSG, e);
            if (err.contains(FOREIGN_KEY_PER_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_009, DataExceptionCode.DDI_009_MSG, e);
            if (err.contains(FOREIGN_KEY_BOX_NXT_REC_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_010, DataExceptionCode.DDI_010_MSG, e);
            if (err.contains(FOREIGN_KEY_BATCH_NXT_REC_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_011, DataExceptionCode.DDI_011_MSG, e);
            if (err.contains(FOREIGN_KEY_ENROLL_OFC_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_012, DataExceptionCode.DDI_012_MSG, e);
            if (err.contains(FOREIGN_KEY_DEP_PROFILE_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_013, DataExceptionCode.DDI_013_MSG, e);
            if (err.contains(FOREIGN_KEY_CARD_REQ_ENROLL_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_014, DataExceptionCode.DDI_014_MSG, e);
            if (err.contains(FOREIGN_KEY_NET_TKN_ENR_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_015, DataExceptionCode.DDI_015_MSG, e);
            if (err.contains(FOREIGN_KEY_WORKST_ENROLL_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_016, DataExceptionCode.DDI_016_MSG, e);
            if (err.contains(FOREIGN_KEY_RESERV_ENR_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_023, DataExceptionCode.DDI_023_MSG, e);
            else
                throw new DAOException(DataExceptionCode.DDI_003, DataExceptionCode.DDI_003_MSG, e);
        }
        return true;
    }

    @Override
    public DepartmentTO fetchDepartment(Long deptId) throws BaseException {
        try {
            return super.find(DepartmentTO.class, deptId);
        } catch (IllegalArgumentException e) {
            throw new DAOException(DataExceptionCode.DDI_018, DataExceptionCode.DDI_018_MSG, e, new Long[]{deptId});
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DDI_017, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public List<DepartmentDispatchSendType> fetchChildDispatchType(DepartmentTO parent) throws BaseException {
        List<DepartmentDispatchSendType> dispatchSendTypes = null;

        try {
            dispatchSendTypes = em.createQuery(" SELECT ED.dispatchSendType FROM DepartmentTO ED " +
                    " WHERE ED.parentDepartment = :parent ", DepartmentDispatchSendType.class)
                    .setParameter("parent", parent)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DDI_019, DataExceptionCode.GLB_005_MSG, e);
        }
        /*if(dispatchSendTypes == null || dispatchSendTypes.size() == 0) {
              throw new DAOException(DataExceptionCode.DDI_020, DataExceptionCode.DDI_020_MSG, new Long[]{parent.getId()});
          }*/
        return dispatchSendTypes;
    }

    /**
     * The method updateSyncDateByCurrentDate is used to update the lastSyncDate field by sysdate
     *
     * @param departmentIds is a list of type {@link Long} which represents the specified ids that belongs to
     *                      {@link DepartmentTO}
     * @throws BaseException
     */
    @Override
    public void updateSyncDateByCurrentDate(List<Long> departmentIds) throws BaseException {

        //edited by Madanipour
        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("UPDATE EMST_DEPARTMENT DEP " +
                "SET DEP.DEP_LAST_SYNC_DATE = sysdate " +
                "WHERE DEP.DEP_ID IN (");


        if (departmentIds.size() < 1000) {
            String ids = "";
            for (Long id : departmentIds) {
                ids = ids.concat("," + (String.valueOf(id)));
            }

            queryBuffer.append(ids.substring(1) + ") ");
        } else {

            Integer loopVar = (departmentIds.size() / 1000);
            for (Integer i = 0; i <= loopVar; i++) {
                Integer subListToIndex = i.equals(loopVar) ? departmentIds.size() : (i + 1) * 1000 - 1;
                Integer subListFromIndex = i * 1000;
                String ids = "";
                for (Long id : departmentIds.subList(subListFromIndex, subListToIndex)) {
                    ids = ids.concat("," + (String.valueOf(id)));
                }

                if (i.equals(0)) {
                    queryBuffer.append(ids.substring(1) + ")");
                } else {
                    queryBuffer.append("OR DEP.DEP_ID IN (" + ids.substring(1) + ") ");
                }
            }
        }

        try {
            em.createNativeQuery(queryBuffer.toString())
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DDI_051, DataExceptionCode.GLB_006_MSG, e);
        }
        //commented by Madanipour
//        String ids = "";
//        for (Long id : departmentIds) {
//            ids = ids.concat("," + (String.valueOf(id)));
//        }
//        try {
//            em.createNativeQuery("UPDATE EMST_DEPARTMENT DEP " +
//                    "SET DEP.DEP_LAST_SYNC_DATE = sysdate " +
//                    "WHERE DEP.DEP_ID IN (" + ids.substring(1) + ")")
//                    .executeUpdate();
//            em.flush();
//        } catch (Exception e) {
//            throw new DAOException(DataExceptionCode.DDI_051, DataExceptionCode.GLB_006_MSG, e);
//        }
    }
}