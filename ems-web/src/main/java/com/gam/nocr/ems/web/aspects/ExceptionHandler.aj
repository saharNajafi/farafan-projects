/**
 *
 */
package com.gam.nocr.ems.web.aspects;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.BaseRuntimeException;
import com.gam.commons.core.biz.BusinessLogException;
import com.gam.commons.core.web.struts2.extJsController.BaseController;
import com.gam.commons.core.web.struts2.extJsController.ExtController;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.web.ws.EMSWebServiceFault;
import com.gam.nocr.ems.web.ws.InternalException;
import com.gam.commons.core.biz.service.Internal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;

/**
 * General exception handler of the project. I listens for all unhandled/rethrown exceptions in project, catches them
 * and generates an appropriate exception and error message base on their error code. If given exception is not handled
 * by lower layers (so it's not a subclass of custom exception classes in project and therefore has no error code)
 * considers a general error code and its corresponding error message for them
 *
 * @author Sina Golesorkhi
 *
 */
@Aspect
public class ExceptionHandler {

    static final Logger logger = BaseLog.getLogger(ExceptionHandler.class);
    static final Logger wsLogger = BaseLog.getLogger("WebServiceLogger");

    private static final String PROJECT_RESOURCENAME_CONST = "Ems.ErrorCode.";
    private static final String MESSAGE_ERROR_CONST = "";

    /**
     * Inspects all action classes and tries to catch any exception thrown from them
     *
     * @param action        The action class to inspect
     * @param thisJoinPoint A reference to the exact join point that is being inspected
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws Throwable
     */
    @Around("execution(public String *(..)) && (within (com.gam.nocr.ems.web.action.*)) && this(action) && !execution(* main(..)) && !execution(* get*(..))")
    public String methodExecution(ExtController action, ProceedingJoinPoint thisJoinPoint)
            throws Throwable {
        try {
            thisJoinPoint.proceed();
        } catch (Exception e) {
            String fullActionClassName = action.getClass().toString(); // e.g. com.gam.nocr.ems.web.action.PersonAction
            String actionClassName = fullActionClassName.substring(fullActionClassName
                    .lastIndexOf(".") + 1); // e.g. PersonAction
            String pureClassName = actionClassName.substring(0, actionClassName.indexOf("Action")); // e.g. Person
            String lowerCasedPureClassName = pureClassName.toLowerCase(); // e.g. person

            BaseException be = null;

            if (!(e instanceof BaseException) && e.getCause() instanceof BaseException) {
                e = (Exception) e.getCause();
            }

            //  Handle generated exception base on RuntimeExceptions or NonRuntimeExceptions
            if (e instanceof BaseException) {
                be = (BaseException) e;
                Object[] args = be.getArgs();
                if (be instanceof com.gam.commons.security.SecurityException) {
                    lowerCasedPureClassName = "security";
                }
                if (be instanceof BusinessLogException) {
                    lowerCasedPureClassName = "businessLog";
                }

                //  Generate an error message like Ems.ErrorCode.person.EMS_S_PSI_007 that would be evaluated in client
                //  side as a JavaScript statement. So it's actually pointing to a constant named EMS_A_EXH in an
                //  object named ErrorCode of in Ems namespace (see webapp/app/locale/ErrCodeFa.js)
                if (args != null) {
                    //  An exception may have some arguments to be replaced in its message. e.g. in a message may be a
                    //  placeholder like {} that would be replaced with arguments in order
                    String argumentedMessage = replaceArguments(be);
                    logger.error(argumentedMessage, args, be);
                    action.fail(PROJECT_RESOURCENAME_CONST + lowerCasedPureClassName + "."
                            + MESSAGE_ERROR_CONST + be.getExceptionCode(), be.getExceptionCode());
                    return BaseController.FAILURE_RESULT;
                } else {
                    logger.error(be.getMessage(), be);
                    action.fail(PROJECT_RESOURCENAME_CONST + lowerCasedPureClassName + "."
                            + MESSAGE_ERROR_CONST + be.getExceptionCode(), be.getExceptionCode());
                    return BaseController.FAILURE_RESULT;
                }
            } else if (e instanceof BaseRuntimeException) {
                BaseRuntimeException bre = (BaseRuntimeException) e;
                Object[] args = bre.getArgs();
                if (args != null) {
                    logger.error(bre.getMessage(), args, bre);
                    action.fail(PROJECT_RESOURCENAME_CONST + lowerCasedPureClassName + "."
                            + MESSAGE_ERROR_CONST + bre.getExceptionCode(), (String[]) null);
                    return BaseController.FAILURE_RESULT;
                } else {

                    logger.error(bre.getMessage(), bre);
                    action.fail(PROJECT_RESOURCENAME_CONST + lowerCasedPureClassName + "."
                            + MESSAGE_ERROR_CONST + bre.getExceptionCode(), (String[]) null);
                    return BaseController.FAILURE_RESULT;
                }
            } else {
                //  This is an unhandled exception and is not extended from custom exception classes. So it has no error
                //  code. This kind of errors will be reported as a general EMS_A_EXH_001 error code with its
                //  corresponding message to be displayed in 3S GUI
                logger.error(
                        WebExceptionCode.EXH_001 + WebExceptionCode.GLB_003_MSG + e.getMessage(), e);
                action.fail(PROJECT_RESOURCENAME_CONST + "general." + MESSAGE_ERROR_CONST
                        + WebExceptionCode.EXH_001, WebExceptionCode.EXH_001);

                return BaseController.FAILURE_RESULT;
            }
        }
        return BaseController.SUCCESS_RESULT;
    }

    /**
     * This method is used to replace all exception message arguments with the provided arguments
     *
     * @param be    The exception that has happened
     * @return      The error message with its argument parameters replaces
     */
    private String replaceArguments(BaseException be) {
        String message = be.getMessage();
        Object[] args = be.getArgs();
        String result = be.getMessage();
        if (be.getMessage().contains("{")) {
            String[] splitted = message.split("\\{");
            if ((splitted.length - 1) == args.length) {
                for (int i = 0; i < splitted.length - 1; i++) {
                    if (i == 0) {
                        result = splitted[i] + "{" + args[i];
                    } else {
                        result = result + splitted[i] + "{" + args[i];
                    }
                }
                result = result + splitted[splitted.length - 1];
            } else {
                result = be.getMessage();
            }
        }
        return result;
    }

    /**
     * Catches all exceptions of classes with @Internal annotation that has void return value
     *
     * @param thisJoinPoint    The web service class to inspect
     * @throws Throwable
     */
    @Around("execution(void *(..)) && @within(Internal)")
    public void methodExecutionInWebServices(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        try {
            thisJoinPoint.proceed();
        } catch (Exception e) {
            if (e instanceof InternalException) {
                //  It's a handled exception. So just log the error message and throw it to the CCOS
                InternalException internalException = (InternalException) e;
                logger.error(internalException.getMessage(), internalException.getFaultInfo()
                        .getCode(), e);
                wsLogger.error(internalException.getMessage(), internalException.getFaultInfo()
                        .getCode(), e);
                throw (InternalException) e;
            } else if (e instanceof Exception) {
                //  It's an unhandled exception. So just log the error message and and throw it with a default error
                //  code
                logger.error(
                        WebExceptionCode.EXH_002 + WebExceptionCode.GLB_003_MSG + e.getMessage(), e);
                wsLogger.error(
                        WebExceptionCode.EXH_002 + WebExceptionCode.GLB_003_MSG + e.getMessage(), e);
                throw new InternalException(WebExceptionCode.EXH_002, new EMSWebServiceFault(
                        WebExceptionCode.EXH_002, 0, ""), e);
            }
        }
    }

    /**
     * Catches all exceptions of classes with @Internal annotation that have some return value
     *
     * @param thisJoinPoint    The web service class to inspect
     * @return                 The return result of the service
     * @throws Throwable
     */
    @Around("execution(!void *(..)) && @within(Internal) && !execution(* convertReason(..))")
    public Object methodExecutionInWebServiceWithReturnValue(ProceedingJoinPoint thisJoinPoint)
            throws Throwable {
        Object returnValueObject = null;
        try {
            returnValueObject = thisJoinPoint.proceed();
        } catch (Exception e) {
            if (e instanceof InternalException) {
                //  It's a handled exception. So just log the error message and throw it to the CCOS
                InternalException internalException = (InternalException) e;
                logger.error(internalException.getMessage(), internalException.getFaultInfo()
                        .getCode(), e);
                wsLogger.error(internalException.getMessage(), internalException.getFaultInfo()
                        .getCode(), e);
                throw (InternalException) e;
            } else if (e instanceof Exception) {
                //  It's an unhandled exception. So just log the error message and and throw it with a default error
                logger.error(
                        WebExceptionCode.EXH_002 + WebExceptionCode.GLB_003_MSG + e.getMessage(), e);
                wsLogger.error(
                        WebExceptionCode.EXH_002 + WebExceptionCode.GLB_003_MSG + e.getMessage(), e);

                throw new InternalException(WebExceptionCode.EXH_002, new EMSWebServiceFault(
                        WebExceptionCode.EXH_002, 0, ""), e);
            }
        }
        return returnValueObject;
    }
}
