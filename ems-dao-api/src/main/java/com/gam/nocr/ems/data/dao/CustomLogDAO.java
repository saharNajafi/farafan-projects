package com.gam.nocr.ems.data.dao;

import com.farafan.customLog.entities.CustomLogTo;
import com.gam.commons.core.BaseException;

public interface CustomLogDAO extends EmsBaseDAO<CustomLogTo> {

    public CustomLogTo insertLog(CustomLogTo to) throws BaseException;

}
