package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.data.domain.RatingInfoTO;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface RatingService extends Service {

    public Long save(RatingInfoTO to) throws BaseException;

    public Long update(RatingInfoTO to) throws BaseException;

    public RatingInfoTO load(Long ratingInfoId) throws BaseException;

    public boolean remove(String ratingInfoIds) throws BaseException;

    public SearchResult fetchRatingList(String searchString, int from, int to, String orderBy) throws BaseException;

    /**
     * The method notifyPortalAboutModifiedRatingInfo is used to notify the subsystem 'Portal' about the new or the
     * modified instances of type {@link RatingInfoTO}
     *
     * @throws com.gam.commons.core.BaseException
     *
     */
    void notifyPortalAboutModifiedRatingInfo() throws BaseException;
}
