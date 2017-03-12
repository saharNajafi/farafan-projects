package com.gam.nocr.ems.biz.service.internal.impl;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_PERSON;
import static com.gam.nocr.ems.config.EMSLogicalNames.SRV_GAAS;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;
import static com.gam.nocr.ems.config.EMSLogicalNames.getExternalServiceJNDIName;
import gampooya.tools.security.BusinessSecurityException;
import gampooya.tools.security.SecurityContextService;
import gampooya.tools.util.Base64;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.GAASService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.CertificateDAO;
import com.gam.nocr.ems.data.dao.PersonDAO;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.domain.vol.PermissionVTO;
import com.gam.nocr.ems.data.domain.vol.UserVTO;
import com.gam.nocr.ems.data.domain.ws.PermissionsWTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.util.EmsUtil;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
@Stateless(name = "UserManagementService")
@Local(UserManagementServiceLocal.class)
@Remote(UserManagementServiceRemote.class)
public class UserManagementServiceImpl extends EMSAbstractService implements UserManagementServiceLocal, UserManagementServiceRemote {

	
	@Override
	@BizLoggable(logAction = "CHANGE_PASSWORD", logEntityName = "PERSON")
	public void changePassword(UserVTO userVTO) throws BaseException {
		try {
			if (userVTO == null)
				throw new ServiceException(BizExceptionCode.USI_003, BizExceptionCode.USI_003_MSG);

			validatePassword(userVTO);
			Long personID = getPersonDAO().findPersonIdByUsername(getUserProfileTO().getUserName());

			Integer userId = getPersonDAO().findUserIdByPersonId(personID);
			if (userId == null)
				throw new ServiceException(BizExceptionCode.USI_005, BizExceptionCode.USI_005_MSG);

			try {
				getGaasService().changePassword(
						userId,
						Base64.encode(EmsUtil.MD5Digest(userProfileTO.getUserName() + userVTO.getOldPassword()), "UTF-8"),
						Base64.encode(EmsUtil.MD5Digest(userProfileTO.getUserName() + userVTO.getNewPassword()), "UTF-8")
				);
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(BizExceptionCode.USI_006, BizExceptionCode.USI_006_MSG, e);
			}
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.USI_011, BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
//	@BizLoggable(logAction = "LOGIN", logEntityName = "PERSON")
	public String fetchUserInfo() throws BaseException {
		try {
			Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
			PersonTO personTO = getPersonDAO().find(PersonTO.class, personID);
			if (personTO == null)
				throw new ServiceException(BizExceptionCode.USI_007, BizExceptionCode.USI_007_MSG);

			return personTO.getFirstName() + " " + personTO.getLastName() + " - " + personTO.getDepartment().getName() + " (" + personTO.getUserName() + ")";
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.USI_012, BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	public CertificateTO getCertificateByUsage(CertificateUsage certificateUsage) throws BaseException {
		return getCertificateDAO().findCertificateByUsage(certificateUsage);
	}

	/**
	 * The method checkUserPermission is used to check a specified permission for the given username
	 *
	 * @param username   is an instance of type {@link String}, which represents a specified username
	 * @param permission is an instance of type {@link String}, which represents a specified permission
	 * @return true or false
	 * @throws BaseException
	 */
	
	
	public boolean checkUserPermission(String username,
									   String permission) throws BaseException {
		SecurityContextService securityContextService = new SecurityContextService();
		try {
			return securityContextService.hasAccess(username, permission);
		} catch (BusinessSecurityException e) {
			throw new ServiceException(BizExceptionCode.USI_014, BizExceptionCode.GLB_020_MSG, e);
		}
	}
	
	//********* Anbari - userPerm-commented
	/*public boolean checkUserPermission(String username, String permission)
			throws BaseException {
		return hasAccess(username, permission);
	}*/

	private PersonDAO getPersonDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_PERSON));
		} catch (DAOFactoryException e) {
			throw new ServiceException(
					BizExceptionCode.USI_002,
					BizExceptionCode.GLB_001_MSG,
					e,
					new String[]{EMSLogicalNames.DAO_PERSON});
		}
	}

	private CertificateDAO getCertificateDAO() throws ServiceException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CERTIFICATE));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.USI_013, BizExceptionCode.GLB_001_MSG, e, EMSLogicalNames.DAO_CERTIFICATE.split(","));
		}
	}

	private GAASService getGaasService() throws BaseException {
		GAASService gaasService = null;
		try {
			gaasService = ServiceFactoryProvider.getServiceFactory().getService(getExternalServiceJNDIName(SRV_GAAS), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new ServiceException(
					BizExceptionCode.USI_001,
					BizExceptionCode.GLB_002_MSG,
					e,
					new String[]{EMSLogicalNames.SRV_GAAS});
		}
		gaasService.setUserProfileTO(getUserProfileTO());
		return gaasService;
	}
	
	private void validatePassword(UserVTO userVTO) throws BaseException {
		if (!EmsUtil.checkString(userVTO.getOldPassword()))
			throw new ServiceException(BizExceptionCode.USI_004, BizExceptionCode.USI_004_MSG);
		if (!EmsUtil.checkString(userVTO.getNewPassword()))
			throw new ServiceException(BizExceptionCode.USI_008, BizExceptionCode.USI_008_MSG);
		if (!EmsUtil.checkString(userVTO.getConfirmNewPassword()))
			throw new ServiceException(BizExceptionCode.USI_009, BizExceptionCode.USI_009_MSG);

		if (!userVTO.getNewPassword().equals(userVTO.getConfirmNewPassword()))
			throw new ServiceException(BizExceptionCode.USI_010, BizExceptionCode.USI_010_MSG);
	}
	/**
	 * The method gets all user accesses which contains all direct or all accesses base on role's by username
	 *@author ganjyar
	 */
	@Override
	public List<String> getUserAccess(UserProfileTO userProfileTO)
			throws BaseException {
			List<String> permissions=new ArrayList<String>();
		try {
			Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
			PersonTO person = getPersonDAO().findByPersonId(personID);
			List<PermissionVTO> allUserPermission = getGaasService().getAllUserAccess(person.getUserId(), "", 0, Integer.MAX_VALUE);
			if(EmsUtil.checkListSize(allUserPermission))
			{
			for (PermissionVTO permission : allUserPermission) {

				if(EmsUtil.checkString(permission.getName())) 
				
					permissions.add(permission.getName());
			}	
		}
			return permissions;
			
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.USI_015, BizExceptionCode.GLB_008_MSG, e);
		}
	}
	
	//Anbari - userPerm-commented
	/* 
	private static final String DEFAULT_GAAS_JNDI_NAME = "jdbc/GamNocrEmsOracleDS";
	private static String PERMISSION_CACHE_NAME = "cachePermissions";
    private NamedCache cachePermissions = CacheFactory.getCache(PERMISSION_CACHE_NAME);	
	@Override
	public List<String> getUserAccess(UserProfileTO userProfileTO)	throws BaseException {
			List<String> permissions=new ArrayList<String>();
			String query = "";
			PermissionsWTO permissionsInCache;
			Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
			permissionsInCache = (PermissionsWTO) cachePermissions.get(personID);
			if(permissionsInCache == null)
			{
				PersonTO person = getPersonDAO().findByPersonId(personID);
				if(personID != null)
				{
				
					permissions = geAccessFromGaasJndi(person.getUserId());
					PermissionsWTO permissionsWTO = new PermissionsWTO();
					permissionsWTO.setName(permissions);					
					cachePermissions.put(personID, permissionsWTO);
				}
			}else
			{
				if(EmsUtil.checkListSize(permissionsInCache.getName()))
						return permissionsInCache.getName();
				else
					return new ArrayList<String>();
			}
			return permissions;
			
	}
	*/

	//Anbari - userPerm-commented
	/*
	private List<String> geAccessFromGaasJndi(Integer userID) throws BaseException {
		String query = "";
		List<String> permissions = new ArrayList<String>();
		String gaasJndiName = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_GAAS_JNDI_NAME,DEFAULT_GAAS_JNDI_NAME));
		
		Connection connectionByDataSource = EmsUtil.getConnectionByDataSource(gaasJndiName);
		try {
			//query = " select acc.ACS_NAME from GAST_ACCESS acc join GAST_ROLEACCESS rac on rac.RAC_ACCESSID = acc.ACS_ID join GAST_USERROLE uro on uro.URO_ROLEID = rac.RAC_ROLEID where uro.USR_ID = ? ";
			//query = " select distinct(acc.ACS_NAME) from GAST_USERACCESS uac join GAST_USERROLE uro on uac.USR_ID = uro.USR_ID join GAST_ACCESS acc on acc.ACS_ID = uac.USA_ACCESSID where uro.USR_ID = ? ";
			query = " select DISTINCT(acc.ACS_NAME) from (select rac.RAC_ACCESSID aid from GAST_ROLEACCESS rac join GAST_USERROLE uro on uro.URO_ROLEID = rac.RAC_ROLEID  where uro.USR_ID = ? union select uac.USA_ACCESSID from GAST_USERACCESS uac where uac.USR_ID = ? ) abc join GAST_ACCESS acc on acc.ACS_ID = abc.aid  ";
			PreparedStatement preparedStatement = connectionByDataSource.prepareStatement(query);
			preparedStatement.setLong(1, userID);
			preparedStatement.setLong(2, userID);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				  String str = rs.getString("ACS_NAME");
				  permissions.add(str);
				}
			connectionByDataSource.close();
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return permissions;
	}
	*/
	
	
	//Anbari - userPerm-commented
	/*
	@Override
	public Boolean hasAccess(String username,String permission) throws BaseException
	{
		Long personID = getPersonDAO().findPersonIdByUsername(username);
		List<String> permissionFromGaas =new ArrayList<String>();
		PermissionsWTO permissionsInCache = (PermissionsWTO) cachePermissions.get(personID);
		if(permissionsInCache == null || !EmsUtil.checkListSize(permissionsInCache.getName()))
		{
			PersonTO person = getPersonDAO().findByPersonId(personID);
			if(person != null)
			{
			
				permissionFromGaas = geAccessFromGaasJndi(person.getUserId());				
				PermissionsWTO permissionsWTO = new PermissionsWTO();
				permissionsWTO.setName(permissionFromGaas);					
				cachePermissions.put(personID, permissionsWTO);
				if(permissionFromGaas.contains(permission))
					return true;
			}			
			
		}
		else if(permissionsInCache.getName().contains(permission))
			return true;
		
		return false;
	}
	*/

	//Anbari - userPerm-commented
	/*
	@Override
	public void updatePermissionCache(Long personID) throws BaseException {
		List<String> permissionFromGaas =new ArrayList<String>();
		if(personID != null)
		{
		
			PersonTO person = getPersonDAO().findByPersonId(personID);
			permissionFromGaas = geAccessFromGaasJndi(person.getUserId());				
			PermissionsWTO permissionsWTO = new PermissionsWTO();
			permissionsWTO.setName(permissionFromGaas);					
			cachePermissions.remove(personID);
			cachePermissions.put(personID, permissionsWTO);
		}		
	}	
	*/
	
	//Anbari
//	private static String TICKET_CACHE_NAME = "ticketCache";
//	@Override
//	public void populatePermissionCache() throws BaseException
//	{
//		   NamedCache ticketCache = CacheFactory.getCache(TICKET_CACHE_NAME);
//		   if(ticketCache != null)
//		   {
//			   Set keySet = ticketCache.keySet();
//			   keySet.size();
//			   Iterator iterator = ticketCache.keySet().iterator();
//			   while (iterator.hasNext()) {
//				   	Long personID = (Long) iterator.next();
//				   	if(personID != null)
//				   	{
//					   	PermissionsWTO permissionsWTO = (PermissionsWTO) cachePermissions.get(personID);
//					   	if(permissionsWTO != null)
//					   		updatePermissionCache(personID);
//					}
//			    }			 
//		   }
//			
//	}				  
//		
	
	 
	
	
	
	
}
