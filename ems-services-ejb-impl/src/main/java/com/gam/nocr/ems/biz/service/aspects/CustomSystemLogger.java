package com.gam.nocr.ems.biz.service.aspects;

import com.farafan.customLog.entities.CustomLogTo;
import com.farafan.customLog.enums.CustomLogAction;
import com.farafan.customLog.enums.CustomLogEntity;
import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.EntityTO;
import com.gam.nocr.ems.biz.service.CustomLogService;
import com.gam.nocr.ems.biz.service.annotations.CustomLoggable;
import com.gam.nocr.ems.biz.service.annotations.ExcludeFromCustomLogging;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.util.EmsUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;

import javax.ejb.EJBTransactionRolledbackException;
import java.lang.annotation.Annotation;
import java.sql.Timestamp;
import java.util.Date;

/**
 * An aspect which logs the method annotated with @CustomLoggable to the DB using
 *
 * @author Mazaher Namjoofar
 */
@Aspect
public class CustomSystemLogger {

    private static final Logger logger = BaseLog.getLogger("CustomSystemLogger");
    private static final String DEFAULT_ENTITY_ID = " ";
    private static final String INPUT_VALUE = "input:";
    private static final String OUTPUT_VALUE = "output:";
    private static final String NULL_VALUE = "null";

    /**
     * This advice logs the method annotated with @CustomLoggable when it returns normally without any exception
     */
    @AfterReturning(pointcut = "execution(@CustomLoggable * *(..)) && @annotation(logData)", returning = "returnValue")
    public void customLogAfterReturning(CustomLoggable logData,
                                        JoinPoint thisJoinPoint,
                                        Object returnValue) {
        //Object returnValue) throws BaseException {
        try {
            CustomLogTo customLogTo = prepareLogFields(logData, thisJoinPoint);
            String additionalData = customLogTo.getAdditionalData();
            if (returnValue == null) {
                additionalData += "\n{" + OUTPUT_VALUE + NULL_VALUE + "}";
                if (customLogTo.getEntityID() == null || customLogTo.getEntityID().isEmpty()) {
                    customLogTo.setEntityID(DEFAULT_ENTITY_ID);
                }
            } else {
                additionalData += "\n{" + OUTPUT_VALUE + returnValue.toString() + "}";

                if (customLogTo.getEntityID() == null || customLogTo.getEntityID().isEmpty()) {
                    Long entityId = null;
                    try {
                        entityId = (Long) returnValue;
                        customLogTo.setEntityID(entityId.toString());
                    } catch (Exception e) {
                        try {
                            entityId = ((EntityTO) returnValue).getId();
                        } catch (Exception e1) {
                            //                        DO nothing
                        }
                    }

                    try {
                        customLogTo.setEntityID(entityId.toString());
                    } catch (Exception e) {
                        customLogTo.setEntityID(DEFAULT_ENTITY_ID);
                    }
                }
            }
            logger.debug(additionalData);
            customLogTo.setActionSuccess(Boolean.TRUE);
            customLogTo.setAdditionalData(additionalData);
            doCallLogService(customLogTo);
        } catch (Exception e) {
            //todo: this exception doesn't throw and logs here!
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * This advice logs the method annotated with @CustomLoggable when it raises an Exception.
     */
    @AfterThrowing(pointcut = "execution(@CustomLoggable * *(..)) && @annotation(logData)", throwing = "e")
    public void customLoggerAfterThrowing(CustomLoggable logData,
                                          JoinPoint thisJoinPoint,
                                          Throwable e) {
        try {
            CustomLogTo customLogTo = inputParamExtractor(logData, thisJoinPoint);
            String additionalData = customLogTo.getAdditionalData();

            if (e instanceof BaseException) {
                additionalData += "\nOutput: " + e.getMessage();
            }
            logger.debug(additionalData);
            customLogTo.setActionSuccess(Boolean.FALSE);
            customLogTo.setAdditionalData(additionalData);
            if (customLogTo.getEntityID() == null || customLogTo.getEntityID().isEmpty()) {
                customLogTo.setEntityID(DEFAULT_ENTITY_ID);
            }

            doCallLogService(customLogTo);
        } catch (BaseException e1) {
            //todo: this exception doesn't throw and logs here!
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * @param customLogTo
     * @throws BaseException
     */
    private void doCallLogService(CustomLogTo customLogTo) throws BaseException {
        try {
            CustomLogService customLogService = ServiceFactoryProvider.getServiceFactory()
                    .getService(
                            EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_CUSTOM_LOG), null);
            customLogService.insertLog(customLogTo);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            if (e instanceof EJBTransactionRolledbackException)
                logger.debug("No CustomLog inserted.", e);
            else
                throw new BaseException(BizExceptionCode.CST_004, e);
        }
    }

    /**
     * Extracts the input parameter names and values of each method
     *
     * @param logData
     * @param thisJoinPoint
     * @return
     * @throws BaseException
     */
    private CustomLogTo inputParamExtractor(CustomLoggable logData, JoinPoint thisJoinPoint) throws BaseException {
        Object[] parameterValues;
        String[] parameterNames;
        MethodSignature signature;
        String additionalData = "InputParams:";

        String logEntityName = logData.logEntityName();
        String logAction = logData.logAction();
        String logActor = logData.logActor();

        CustomLogTo customLogTo = new CustomLogTo();
        customLogTo.setAction(actionMapper(logAction));
        customLogTo.setEntityName(entityMapper(logEntityName));

        // for customLogTo actor
        String username = null;
        try {
            username = (((AbstractService) thisJoinPoint.getTarget()).getUserProfileTO().getUserName());
        } catch (Exception ex) {//TODO ???
        }
        if (username == null || username.trim().length() == 0) {
            if (logActor == null || logActor.trim().length() == 0) {
                customLogTo.setActor("Unspecified");
            } else {
                customLogTo.setActor(logActor);
            }
        } else {
            customLogTo.setActor(username);
        }

        customLogTo.setDate(new Timestamp(new Date().getTime()));

        //customLogTo.setEntityID("" + 1);
        parameterValues = thisJoinPoint.getArgs();
        signature = (MethodSignature) thisJoinPoint.getSignature();
        parameterNames = signature.getParameterNames();
        if (parameterValues != null)
            for (int i = 0; i < parameterValues.length; i++) {
                Long id = null;
                if (parameterValues[i] instanceof EntityTO) {
                    id = ((EntityTO) parameterValues[i]).getId();
                    if (id != null && id > 0) {
                        customLogTo.setEntityID("" + id);
                    }

                } else {
                    try {
                        id = Long.parseLong(parameterValues[i].toString());
                        customLogTo.setEntityID("" + id);
                    } catch (Exception e) {
//                        DO nothing
                    }
                }

                additionalData += "\n" + i + ":" + parameterNames[i] + "=" + parameterValues[i];
            }
        customLogTo.setAdditionalData(additionalData);
        return customLogTo;
    }

    /**
     * The method prepareLogFields is used to prepare the necessary business log attributes depends on data,
     * which has received from the method
     *
     * @param logData       is an instance of type {@link CustomLoggable}
     * @param thisJoinPoint is an instance of {@link JoinPoint}
     * @return an instance of type {@link CustomLogTo}
     * @throws BaseException
     */
    private CustomLogTo prepareLogFields(CustomLoggable logData,
                                         JoinPoint thisJoinPoint) throws BaseException {
        Object[] parameterValues;
        String[] parameterNames;
        MethodSignature signature;
//        String additionalData = "InputParams:";
        String additionalData = "{" + INPUT_VALUE;

        String logEntityName = logData.logEntityName();
        String logAction = logData.logAction();
        String logActor = logData.logActor();

        CustomLogTo customLogTo = new CustomLogTo();
        customLogTo.setAction(actionMapper(logAction));
        customLogTo.setEntityName(entityMapper(logEntityName));
        customLogTo.setDate(new Timestamp(new Date().getTime()));

        // for customLogTo actor
        String username = null;
        try {
            username = (((AbstractService) thisJoinPoint.getTarget()).getUserProfileTO().getUserName());
        } catch (Exception ex) {
            //TODO ???
        }
        if (username == null || username.trim().length() == 0) {
            if (logActor == null || logActor.trim().length() == 0) {
                customLogTo.setActor("Unspecified");
            } else {
                customLogTo.setActor(logActor);
            }
        } else {
            customLogTo.setActor(username);
        }

        Annotation[][] anns = ((MethodSignature) thisJoinPoint.getSignature()).getMethod().getParameterAnnotations();

        parameterValues = thisJoinPoint.getArgs();
        signature = (MethodSignature) thisJoinPoint.getSignature();
        parameterNames = signature.getParameterNames();
        if (parameterValues != null) {
            boolean entityIdAssignedFlag = false;
            for (int i = 0; i < parameterValues.length; i++) {

                boolean shouldBeExcluded = false;
                for (Annotation annotation : anns[i]) {
                    if (annotation instanceof ExcludeFromCustomLogging) {
                        shouldBeExcluded = true;
                        break;
                    }
                }
                if (shouldBeExcluded) {
                    //System.out.println("should be excluded===>"+parameterNames[i]);
                    continue;
                }

                additionalData += "{ " + parameterNames[i] + ": " + EmsUtil.toJSON(parameterValues[i]) + "} ";
                Long id;
                if (!entityIdAssignedFlag) {
                    if (parameterValues[i] instanceof EntityTO) {
                        entityIdAssignedFlag = true;
                        id = ((EntityTO) parameterValues[i]).getId();
                        if (id != null && id > 0) {
                            customLogTo.setEntityID("" + id);
                        }

                    } else {
                        entityIdAssignedFlag = true;
                        try {
                            id = Long.parseLong(parameterValues[i].toString());
                            customLogTo.setEntityID("" + id);
                        } catch (Exception e) {
                            // DO nothing
                        }
                    }
                }

//                additionalData += "\n" + i + ":" + parameterNames[i] + "=" + parameterValues[i];
            }
        }
        additionalData += "}";
        customLogTo.setAdditionalData(additionalData);
        return customLogTo;
    }

    /**
     * Finds the enum type from @CustomLogEntity corresponding to the logAction
     *
     * @param logEntityName
     * @return
     * @throws BaseException
     */
    private CustomLogEntity entityMapper(String logEntityName) throws BaseException {
        try {
            return CustomLogEntity.valueOf(logEntityName);
        } catch (Exception ex) {
            Object[] args = {logEntityName};
            throw new BaseException(BizExceptionCode.CST_005, BizExceptionCode.CST_005_MSG, args);
        }
    }

    /**
     * Finds the enum type from @CustomLogAction corresponding to the logAction
     *
     * @param logAction
     * @return
     * @throws BaseException
     */
    private CustomLogAction actionMapper(String logAction) throws BaseException {
        try {
            return CustomLogAction.valueOf(logAction);
        } catch (Exception ex) {
            Object[] args = {logAction};
            throw new BaseException(BizExceptionCode.CST_006, BizExceptionCode.CST_006_MSG, args);
        }
    }
}
