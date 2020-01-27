package com.gam.nocr.ems.biz.service.annotations;

import com.farafan.customLog.enums.CustomLogAction;
import com.farafan.customLog.enums.CustomLogEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Should be used for the methods, which must be logged using CustomSystemLogger
 *
 * @author Mazaher Namjoofar
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomLoggable {
    CustomLogEntity logEntityName();

    CustomLogAction logAction();

    String logActor() default "";
}
