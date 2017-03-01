package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.LocationTO;
import com.gam.nocr.ems.data.domain.ws.ProvinceWTO;
import com.gam.nocr.ems.data.enums.LocationType;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

import java.util.List;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface LocationDAO extends EmsBaseDAO<LocationTO> {

    Long fetchModifiedLocationCount(LocationType locationType) throws BaseException;

    /**
     * The method findModifiedProvinces is used to find the new or the modified instances of type {@link com.gam.nocr.ems.data.domain.LocationTO}
     *
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.LocationTO}
     * @throws com.gam.commons.core.BaseException
     *
     */
    List<LocationTO> findModifiedLocationsByType(LocationType locationType, Integer from, Integer to) throws BaseException;

    /**
     * The method updateModifiedFields is used to update the field 'modified' of a list of type {@link
     * com.gam.nocr.ems.data.domain.LocationTO}
     *
     * @param idList is a list of type {@link Long}
     * @param flag   is an instance of type {@link Boolean}
     * @throws com.gam.commons.core.BaseException
     *
     */
    void updateModifiedFields(List<Long> idList, Boolean flag) throws BaseException;
    
    /**
     * @author ganjyar
     * @param generalCriteria
     * @return
     * @throws BaseException
     */
    List<ProvinceWTO> fetchLocationLivingList(GeneralCriteria generalCriteria) throws BaseException;

}
