package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.ReligionTO;

/**
 * Created by IntelliJ IDEA.
 * User: jalilian
 * Date: 8/21/13
 * Time: 10:09 AM
 */
public interface ReligionDAO extends EmsBaseDAO<ReligionTO> {

    /**
     * The method findByName is used to find a specified religion by name
     *
     * @param name is an instance of type {@link String}, which represents a specified religion
     * @return an instance of type {@link ReligionTO} or null
     * @throws BaseException
     */
    ReligionTO findByName(String name) throws BaseException;

}
