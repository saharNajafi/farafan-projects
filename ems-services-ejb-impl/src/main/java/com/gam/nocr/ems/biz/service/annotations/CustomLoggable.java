package com.gam.nocr.ems.biz.service.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Should be used for the methods, which must be logged using CustomSystemLogger
 * @author Mazaher Namjoofar
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomLoggable {
    String logEntityName();

    String logAction();

    String logActor() default "";
}
