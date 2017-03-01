package com.gam.nocr.ems.biz.service.aspects;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.security.SecurityException;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSAccesses;
import gampooya.tools.security.BusinessSecurityException;
import gampooya.tools.security.SecurityContextService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sina Golesorkhi
 *
 */
@Aspect
public class Authorizer {
    // TODO 1- Method calls should be added 2- Syntax should be checked
    // completely using annotation processor
    // http://javasign.blogspot.com/2009/08/annotation-checking-at-compile-time.html?m=1
    // http://stackoverflow.com/questions/1752607/how-to-intentionally-cause-a-custom-java-compiler-warning-message
    static final Logger logger = BaseLog.getLogger(Authorizer.class);

    @Before("execution(@Permissions * * (..)) && @annotation(permissions)")
    public void intercept(Permissions permissions, JoinPoint thisJoinPoint) throws BaseException {
        try {
            boolean result = false;
            UserProfileTO userProfile = new UserProfileTO();
            userProfile.setUserName(((AbstractService) thisJoinPoint.getTarget())
                    .getUserProfileTO().getUserName());
            SecurityContextService securityContextService = new SecurityContextService();

            // If the user is the Admin of the system nothing will be checked because he has all the access privileges
            if (securityContextService.hasAccess(userProfile.getUserName(), (EMSAccesses.IS_ADMIN))) {
                result = true;
            } else {
                String annotationValue = permissions.value();
                // Removing white spaces
                annotationValue = annotationValue.replaceAll("\\s+", "");

                if (annotationValue.contains("&|") || annotationValue.contains("|&")) {
                    throw new RuntimeException(
                            "Remove the &| or |& from your @Permission attribute and use the correct syntax.");
                }
                while (annotationValue.indexOf("&&") > 0) {
                    annotationValue = annotationValue.replace("&&", "&");
                }
                while (annotationValue.indexOf("||") > 0) {
                    annotationValue = annotationValue.replace("||", "|");
                }

                int counter = 0;
                List<String> tokensList = new ArrayList<String>();
                if (annotationValue.contains("(")) {
                    while (annotationValue.contains("(")) {

                        int indexOfOpeningParantheses = annotationValue.lastIndexOf("(");
                        int indexOfClosingParantheses = annotationValue.indexOf(")",
                                indexOfOpeningParantheses);
                        String token = annotationValue.substring(indexOfOpeningParantheses + 1,
                                indexOfClosingParantheses);
                        tokensList.add(token);
                        annotationValue = annotationValue.substring(0, indexOfOpeningParantheses)
                                + "$X" + counter
                                + annotationValue.substring(indexOfClosingParantheses + 1);
                        counter++;
                    }

                } else {
                    tokensList.add(annotationValue);
                }

                for (int i = 0; i < tokensList.size(); i++) {
                    String token = tokensList.get(i);
                    // AND checker
                    String[] andList = null;

                    if (token.contains("&")) {
                        andList = token.split("&");
                    }
                    if (andList != null) {

                        for (String permission : andList) {
                            if (token.contains("$X")) {
                                int xIndex = token.indexOf("X");
                                int indexInTokenList = Integer.parseInt(token.substring(xIndex + 1));
                                if (tokensList.get(indexInTokenList).equals("false")) {
                                    result = false;
                                    break;
                                } else {
                                    result = true;
                                }
                            } else {
                                try {
                                    if (!securityContextService.hasAccess(userProfile.getUserName(),
                                            permission)) {
                                        result = false;
                                        break;
                                    } else {
                                        result = true;
                                    }
                                } catch (BusinessSecurityException e) {
                                    throw new SecurityException(BizExceptionCode.AUT_001, e);
                                }
                            }
                        }

                        if (result) {
                            tokensList.set(i, "true");
                        } else {
                            tokensList.set(i, "false");
                        }
                    }

                    // OR checker
                    String[] orList = null;
                    if (token.contains("|")) {
                        orList = token.split("\\|");
                    }

                    if (orList != null) {
                        for (String permission : orList) {
                            if (permission.contains("$X")) {
                                int xIndex = permission.indexOf("X");
                                int indexInTokenList = Integer.parseInt(permission
                                        .substring(xIndex + 1));
                                if (tokensList.get(indexInTokenList).equals("false")) {
                                    result = false;
                                } else {
                                    result = true;
                                    break;
                                }
                            } else {
                                try {
                                    if (!securityContextService.hasAccess(userProfile.getUserName(),
                                            permission)) {
                                        result = false;
                                    } else {
                                        result = true;
                                        break;
                                    }
                                } catch (BusinessSecurityException e) {
                                    throw new SecurityException(BizExceptionCode.AUT_002, e);
                                }
                            }
                        }
                        if (result) {
                            tokensList.set(i, "true");
                        } else {
                            tokensList.set(i, "false");
                        }
                    }
                    if (andList == null && orList == null) {
                        try {
                            result = securityContextService.hasAccess(userProfile.getUserName(),
                                    tokensList.get(0));
                        } catch (BusinessSecurityException e) {
                            throw new SecurityException(BizExceptionCode.AUT_003, e);
                        }
                    }
                }
            }
            if (result) {
                logger.info("The user has access to the specified permissions' combination: "
                        + permissions.value());
            } else {
                Object[] args = {permissions.value()};
                throw new SecurityException(BizExceptionCode.AUT_004, BizExceptionCode.AUT_004_MSG, args);
            }
        } catch (Exception e) {
            if (e instanceof SecurityException) {
                throw (SecurityException) e;
            } else {
                throw new SecurityException(BizExceptionCode.AUT_005, BizExceptionCode.AUT_005_MSG, e);
            }

        }
    }
}
