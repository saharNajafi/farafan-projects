//package com.gam.nocr.ems.biz.job;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.InterruptableJob;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.JobKey;
//import org.quartz.PersistJobDataAfterExecution;
//import org.quartz.UnableToInterruptJobException;
//import org.slf4j.Logger;
//
//import com.gam.commons.core.BaseException;
//import com.gam.commons.core.BaseLog;
//import com.gam.commons.core.biz.delegator.DelegatorException;
//import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
//import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
//import com.gam.commons.core.data.domain.UserProfileTO;
//import com.gam.nocr.ems.biz.delegator.PersonDelegator;
//import com.gam.nocr.ems.biz.delegator.UserDelegator;
//import com.gam.nocr.ems.biz.service.PersonManagementService;
//import com.gam.nocr.ems.config.BizExceptionCode;
//import com.gam.nocr.ems.config.EMSLogicalNames;
//import com.gam.nocr.ems.data.domain.ws.PermissionsWTO;
//import com.gam.nocr.ems.util.EmsUtil;
//import com.tangosol.net.CacheFactory;
//import com.tangosol.net.NamedCache;
//
//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
//public class UpdatePermissionCacheJob  extends BaseEmsJob implements InterruptableJob {
//
//	private static final Logger logger = BaseLog
//			.getLogger(UpdatePermissionCacheJob.class);
//
//	private boolean isJobInterrupted = false;
//	private JobKey jobKey = null;
//	//private static String TICKET_CACHE_NAME = "ticketCache";
//	private static String PERMISSION_CACHE_NAME = "cachePermissions";
//
//	@Override
//	public void execute(JobExecutionContext jobExecutionContext)
//			throws JobExecutionException {
//
//		UserDelegator userDelegator = new UserDelegator();
//		PersonDelegator personDelegator = new PersonDelegator();
//
//	//	NamedCache ticketCache = CacheFactory.getCache(TICKET_CACHE_NAME);
//		NamedCache cachePermissions = CacheFactory.getCache(PERMISSION_CACHE_NAME);
//		
//
//		try {
//			jobKey = jobExecutionContext.getJobDetail().getKey();
//			logger.info("Upadet PermissionCache Start!!!");
////			if (ticketCache != null) {
////				Iterator iterator = ticketCache.keySet().iterator();
////				while (iterator.hasNext()) {
////					if (!isJobInterrupted) {
////						Long personID = (Long) iterator.next();
////						if (personID != null) {
////							PermissionsWTO permissionsWTO = (PermissionsWTO) cachePermissions.get(personID);
////							if (permissionsWTO != null)
////								userDelegator.updatePermissionCache(personID);
////						}
////					}
////				}
////			}	
//			
//			List<Long> ids = personDelegator.getAllPersonIds();
//			if (ids != null) {
//				for (Long personID : ids) {
//					if (!isJobInterrupted) {
//						PermissionsWTO permissionsWTO = (PermissionsWTO) cachePermissions.get(personID);
//						if (permissionsWTO != null)
//							userDelegator.updatePermissionCache(personID);
//
//					}
//					else
//						break;
//				}
//			}
//			
//			
//			logger.info("Upadet PermissionCache End!!!");
//
//		} catch (BaseException e) {
//			logger.error("\nRuntime exception in job 'UpdatePermissionCacheJob' : ",e);
//
//		}
//	}
//
//	@Override
//	public void interrupt() throws UnableToInterruptJobException {
//		System.err.println("calling interrupt: jobKey ==> " + jobKey);
//		isJobInterrupted = true;
//	}
//	
//	
//}
