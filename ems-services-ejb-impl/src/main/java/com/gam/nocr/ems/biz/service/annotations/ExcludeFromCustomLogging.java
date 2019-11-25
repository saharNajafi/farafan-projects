package com.gam.nocr.ems.biz.service.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Should be used when exclude specific parameter to logging
 * @author Mazaher Namjoofar
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeFromCustomLogging {
}
