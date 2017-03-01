package com.gam.nocr.ems.biz.service;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.data.domain.vol.PermissionVTO;
import com.gam.nocr.ems.data.domain.vol.PermissionVTOWrapper;
import com.gam.nocr.ems.data.domain.vol.RoleVTO;
import com.gam.nocr.ems.data.domain.vol.RoleVTOWrapper;
import com.gam.nocr.ems.data.domain.vol.SchedulingVTO;
import com.gam.nocr.ems.data.domain.vol.UserInfoVTO;
import com.gam.nocr.ems.data.domain.vol.ValidIPVTO;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface GAASService extends Service {

    public int save(UserInfoVTO userInfoTO) throws BaseException;

    public int update(UserInfoVTO userInfoTO) throws BaseException;

    public void changePassword(Integer userId, String oldpass, String newpass) throws BaseException;

    public void changeState(Integer userId, Boolean state) throws BaseException;

    public void remove(Integer userId) throws BaseException;

    public List<RoleVTO> getRoles(Integer userId, String searchString, int from, int to) throws BaseException;

    public RoleVTOWrapper getRolesWithCount(Integer userId, String searchString, int from, int to) throws BaseException;

    public List<PermissionVTO> getAllPermission(String searchString, int from, int to) throws BaseException;

    public List<PermissionVTO> getDirectUserPermission(Integer userId, String searchString, int from, int to) throws BaseException;

    public List<PermissionVTO> getAllUserPermission(Integer userId, String searchString, int from, int to) throws BaseException;

    public PermissionVTOWrapper getAllUserPermissionWithCount(Integer userId, String searchString, int from, int to) throws BaseException;

    public List<PermissionVTO> getRolePermission(Integer roleId, int from, int to) throws BaseException;

    public List<SchedulingVTO> getScheduling(Integer userId, String searchString, int from, int to) throws BaseException;

    public List<ValidIPVTO> getValidIPs(Integer userId, String searchString, int from, int to) throws BaseException;

    public Boolean hasTokenPermission(Integer userId) throws BaseException;

    public Boolean validateTicket(String username, String ticket) throws BaseException;
    //Anbari
//    public Long getPersonID(UserProfileTO userProfileTO) throws BaseException;

    public void enableUser(Integer userId) throws BaseException;

    public void disableUser(Integer userId) throws BaseException;

    public UserInfoVTO getUser(Integer userId) throws BaseException;
    /**
     * @author: ganjyar
     */
	public List<PermissionVTO> getAllUserAccess(Integer userId, String string,
			int i, int maxValue) throws BaseException;


}
