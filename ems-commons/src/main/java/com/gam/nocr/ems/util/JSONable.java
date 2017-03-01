package com.gam.nocr.ems.util;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface JSONable {
    /**
     * The method toJSON is used to convert an object to an instance of type {@link String} to build a JSON Object
     *
     * @return an instance of type {@link String}
     */
    String toJSON();
}
