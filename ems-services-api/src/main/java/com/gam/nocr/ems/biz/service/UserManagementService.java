package com.gam.nocr.ems.biz.service;

import java.sql.SQLException;
import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.domain.vol.UserVTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public interface UserManagementService extends Service {

	public void changePassword(UserVTO userVTO) throws BaseException;

	public String fetchUserInfo() throws BaseException;

	public CertificateTO getCertificateByUsage(CertificateUsage ccosCer) throws BaseException;

	/**
	 * The method checkUserPermission is used to check a specified permission for the given username
	 *
	 * @param username   is an instance of type {@link String}, which represents a specified username
	 * @param permission is an instance of type {@link String}, which represents a specified permission
	 * @return true or false
	 * @throws BaseException
	 */
	boolean checkUserPermission(String username,
								String permission) throws BaseException;

	/**
	 * @author ganjyar
	 * @throws SQLException 
	 */
	public List<String> getUserAccess(UserProfileTO userProfileTO) throws BaseException;

	//Anbari - userPerm-commented
	//public Boolean hasAccess(String username, String permission) throws BaseException;

	//Anbari - userPerm-commented
	//public void updatePermissionCache(Long id) throws BaseException;

	//Anbari
	//public void populatePermissionCache() throws BaseException;
}
