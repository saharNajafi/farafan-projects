package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.RatingInfoTO;

import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface RatingInfoDAO extends EmsBaseDAO<RatingInfoTO> {

    public List<RatingInfoTO> findAll() throws BaseException;

    public boolean deleteRatingInfos(List<Long> ratingInfoIds) throws BaseException;

    /**
     * The method findModifiedRatingInfo is used to find the instances of type {@link RatingInfoTO} which are new or
     * modified.
     *
     * @return a list fo type {@link RatingInfoTO}
     * @throws BaseException
     */
    List<RatingInfoTO> findModifiedRatingInfo() throws BaseException;

    /**
     * The method updateModifiedFields is used to update the field 'modified' of a list of type {@link
     * com.gam.nocr.ems.data.domain.RatingInfoTO}
     *
     * @param idList is a list of type {@link Long}
     * @param flag   is an instance of type {@link Boolean}
     * @throws com.gam.commons.core.BaseException
     *
     */
    public void updateModifiedFields(List<Long> idList,
                                     Boolean flag) throws BaseException;
}
