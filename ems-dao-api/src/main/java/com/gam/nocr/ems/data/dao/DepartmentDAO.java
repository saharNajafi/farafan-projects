package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;

import java.util.List;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface DepartmentDAO extends EmsBaseDAO<DepartmentTO> {

    DepartmentTO fetchDepartment(Long deptId) throws BaseException;

    List<DepartmentDispatchSendType> fetchChildDispatchType(DepartmentTO parent) throws BaseException;

    boolean removePersons(String departmentIds) throws BaseException;

    boolean removeDepartments(String departmentIds) throws BaseException;

    /**
     * The method updateSyncDateByCurrentDate is used to update the lastSyncDate field by sysdate
     *
     * @param departmentIds is a list of type {@link Long} which represents the specified ids that belongs to
     *                      {@link DepartmentTO}
     * @throws BaseException
     */
    void updateSyncDateByCurrentDate(List<Long> departmentIds) throws BaseException;

}
