/**
 *
 */
package com.gam.nocr.ems.biz.service.aspects;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.BusinessLogException;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.EntityTO;
import com.gam.nocr.ems.biz.service.BusinessLogService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.enums.BusinessLogActionAttitude;
import com.gam.nocr.ems.data.domain.BusinessLogTO;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;
import com.gam.nocr.ems.util.EmsUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;

import javax.ejb.EJBTransactionRolledbackException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * An aspect which logs the method annotated with @BizLoggable to the DB using
 * BusinessLogService
 *
 * @author Sina Golesorkhi
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 *
 */
@Aspect
public class BusinessLogger {

    private static final Logger logger = BaseLog.getLogger(BusinessLogger.class);
    private static final String DEFAULT_ENTITY_ID = " ";
    private static final String INPUT_VALUE = "input:";
    private static final String OUTPUT_VALUE = "output:";
    private static final String NULL_VALUE = "null";

    /**
     * This advice logs the method annotated with @BizLoggable when it returns normally without any exception
     * @throws BaseException
     */
    @AfterReturning(pointcut = "execution(@BizLoggable * *(..)) && @annotation(logData)", returning = "returnValue")
    public void bizLogAfterReturning(BizLoggable logData,
                                     JoinPoint thisJoinPoint,
                                     Object returnValue) throws BaseException {
        BusinessLogTO businessLogTO = prepareBusinessLogFields(logData, thisJoinPoint);
        String additionalData = businessLogTO.getAdditionalData();
        if (returnValue == null) {
            additionalData += "\n{" + OUTPUT_VALUE + NULL_VALUE + "}";
            if (businessLogTO.getEntityID() == null || businessLogTO.getEntityID().isEmpty()) {
                businessLogTO.setEntityID(DEFAULT_ENTITY_ID);
            }
        } else {
            additionalData += "\n{" + OUTPUT_VALUE + returnValue.toString() + "}";

            if (businessLogTO.getEntityID() == null || businessLogTO.getEntityID().isEmpty()) {
                Long entityId = null;
                try {
                    entityId = (Long) returnValue;
                    businessLogTO.setEntityID(entityId.toString());
                } catch (Exception e) {
                    try {
                        entityId = ((EntityTO) returnValue).getId();
                    } catch (Exception e1) {
//                        DO nothing
                    }
                }

                try {
                    businessLogTO.setEntityID(entityId.toString());
                } catch (Exception e) {
                    businessLogTO.setEntityID(DEFAULT_ENTITY_ID);
                }
            }
        }
        logger.debug(additionalData);
        businessLogTO.setActionAttitude(BusinessLogActionAttitude.T);
        businessLogTO.setAdditionalData(additionalData);
        doCallBusinessLogService(businessLogTO);
    }


    /**
     * This advice logs the method annotated with @BizLoggable when it raises an Exception.
     * @throws BaseException
     */
    @AfterThrowing(pointcut = "execution(@BizLoggable * *(..)) && @annotation(logData)", throwing = "e")
    public void bizLoggerAfterThrowing(BizLoggable logData,
                                       JoinPoint thisJoinPoint,
                                       Throwable e) throws BaseException {
        BusinessLogTO businessLogTO = inputParamExtractor(logData, thisJoinPoint);
        String additionalData = businessLogTO.getAdditionalData();

        /**
         * Code begins
         * @author Saeed Jalilian
         */
        if (e instanceof BaseException) {
            additionalData += "\nOutput: " + e.getMessage();
        }
        logger.debug(additionalData);
        businessLogTO.setActionAttitude(BusinessLogActionAttitude.F);
        businessLogTO.setAdditionalData(additionalData);
        if (businessLogTO.getEntityID() == null || businessLogTO.getEntityID().isEmpty()) {
            businessLogTO.setEntityID(DEFAULT_ENTITY_ID);
        }
        /**
         * Code ends
         * @author Saeed Jalilian
         */

        doCallBusinessLogService(businessLogTO);
    }


    /**
     * @param businessLogTO
     * @throws BaseException
     */
    private void doCallBusinessLogService(BusinessLogTO businessLogTO) throws BaseException {
        try {
            BusinessLogService businessLogService = ServiceFactoryProvider.getServiceFactory()
                    .getService(
                            EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), null);
            businessLogService.insertLog(businessLogTO);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            if (e instanceof EJBTransactionRolledbackException)
                logger.debug("No BusinessLog inserted.", e);
            else
                throw new BusinessLogException(BizExceptionCode.BZL_003, e);
        }
    }

    /**
     * Extracts the input parameter names and values of each method
     * @param logData
     * @param thisJoinPoint
     * @return
     * @throws BusinessLogException
     */
    private BusinessLogTO inputParamExtractor(BizLoggable logData, JoinPoint thisJoinPoint) throws BusinessLogException {
        Object[] parameterValues;
        String[] parameterNames;
        MethodSignature signature;
        String additionalData = "InputParams:";

        String logEntityName = logData.logEntityName();
        String logAction = logData.logAction();
        String logActor = logData.logActor();

        BusinessLogTO businessLogTO = new BusinessLogTO();
        businessLogTO.setAction(actionMapper(logAction));
        businessLogTO.setEntityName(entityMapper(logEntityName));

        // for businessLogTO actor
        String username = null;
        try {
            username = (((AbstractService) thisJoinPoint.getTarget()).getUserProfileTO().getUserName());
        } catch (Exception ex) {//TODO ??? 
        }
        if (username == null || username.trim().length() == 0) {
            if (logActor == null || logActor.trim().length() == 0) {
                businessLogTO.setActor("Unspecified");
            } else {
                businessLogTO.setActor(logActor);
            }
        } else {
            businessLogTO.setActor(username);
        }

        businessLogTO.setDate(new Timestamp(new Date().getTime()));
        /**
         * Commented by Saeed Jalilian
         */
//        businessLogTO.setEntityID("" + 1);
        parameterValues = thisJoinPoint.getArgs();
        signature = (MethodSignature) thisJoinPoint.getSignature();
        parameterNames = signature.getParameterNames();
        if (parameterValues != null)
            for (int i = 0; i < parameterValues.length; i++) {
                Long id = null;
                if (parameterValues[i] instanceof EntityTO) {
                    id = ((EntityTO) parameterValues[i]).getId();
                    if (id != null && id > 0) {
                        businessLogTO.setEntityID("" + id);
                    }

                }

                /**
                 * Code begins
                 * @author Saeed Jalilian
                 */
                else {
                    try {
                        id = Long.parseLong(parameterValues[i].toString());
                        businessLogTO.setEntityID("" + id);
                    } catch (Exception e) {
//                        DO nothing
                    }
                }
                /**
                 * Code ends
                 * @author Saeed Jalilian
                 */

                additionalData += "\n" + i + ":" + parameterNames[i] + "=" + parameterValues[i];
            }
        businessLogTO.setAdditionalData(additionalData);
        return businessLogTO;
    }


    /**
     * Method Begins
     * @author Saeed Jalilian
     */

    /**
     * The method prepareBusinessLogFields is used to prepare the necessary business log attributes depends on data,
     * which has received from the method
     * @param logData is an instance of type {@link BizLoggable}
     * @param thisJoinPoint is an instance of {@link JoinPoint}
     * @return an instance of type {@link BusinessLogTO}
     * @throws BusinessLogException
     */
    private BusinessLogTO prepareBusinessLogFields(BizLoggable logData,
                                                   JoinPoint thisJoinPoint) throws BusinessLogException {
        Object[] parameterValues;
        String[] parameterNames;
        MethodSignature signature;
//        String additionalData = "InputParams:";
        String additionalData = "{" + INPUT_VALUE;

        String logEntityName = logData.logEntityName();
        String logAction = logData.logAction();
        String logActor = logData.logActor();

        BusinessLogTO businessLogTO = new BusinessLogTO();
        businessLogTO.setAction(actionMapper(logAction));
        businessLogTO.setEntityName(entityMapper(logEntityName));
        businessLogTO.setDate(new Timestamp(new Date().getTime()));

        // for businessLogTO actor
        String username = null;
        try {
            username = (((AbstractService) thisJoinPoint.getTarget()).getUserProfileTO().getUserName());
        } catch (Exception ex) {
            //TODO ???
        }
        if (username == null || username.trim().length() == 0) {
            if (logActor == null || logActor.trim().length() == 0) {
                businessLogTO.setActor("Unspecified");
            } else {
                businessLogTO.setActor(logActor);
            }
        } else {
            businessLogTO.setActor(username);
        }

        parameterValues = thisJoinPoint.getArgs();
        signature = (MethodSignature) thisJoinPoint.getSignature();
        parameterNames = signature.getParameterNames();
        if (parameterValues != null) {
            boolean entityIdAssignedFlag = false;
            for (int i = 0; i < parameterValues.length; i++) {
                additionalData += "{ " + parameterNames[i] + ": " + EmsUtil.toJSON(parameterValues[i]) + "} ";
                Long id;
                if (!entityIdAssignedFlag) {
                    if (parameterValues[i] instanceof EntityTO) {
                        entityIdAssignedFlag = true;
                        id = ((EntityTO) parameterValues[i]).getId();
                        if (id != null && id > 0) {
                            businessLogTO.setEntityID("" + id);
                        }

                    } else {
                        entityIdAssignedFlag = true;
                        try {
                            id = Long.parseLong(parameterValues[i].toString());
                            businessLogTO.setEntityID("" + id);
                        } catch (Exception e) {
                            // DO nothing
                        }
                    }
                }

//                additionalData += "\n" + i + ":" + parameterNames[i] + "=" + parameterValues[i];
            }
        }
        additionalData += "}";
        businessLogTO.setAdditionalData(additionalData);
        return businessLogTO;
    }
    /**
     * Method ends
     * @author Saeed Jalilian
     */

    /**
     * Finds the enum type from @BusinessLogEntity corresponding to the logAction
     * @param logEntityName
     * @return
     * @throws BusinessLogException
     */
    private BusinessLogEntity entityMapper(String logEntityName) throws BusinessLogException {
        try {
            return BusinessLogEntity.valueOf(logEntityName);
        } catch (Exception ex) {
            Object[] args = {logEntityName};
            throw new BusinessLogException(BizExceptionCode.BZL_001, BizExceptionCode.BZL_001_MSG, args);
        }
    }

    /**
     * Finds the enum type from @BusinessLogAction corresponding to the logAction
     * @param logAction
     * @return
     * @throws BusinessLogException
     */
    private BusinessLogAction actionMapper(String logAction) throws BusinessLogException {
        try {
            return BusinessLogAction.valueOf(logAction);
        } catch (Exception ex) {
            Object[] args = {logAction};
            throw new BusinessLogException(BizExceptionCode.BZL_002, BizExceptionCode.BZL_002_MSG, args);
        }
    }
}
