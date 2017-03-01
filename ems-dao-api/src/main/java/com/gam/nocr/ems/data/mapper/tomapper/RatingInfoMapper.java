package com.gam.nocr.ems.data.mapper.tomapper;

import java.util.ArrayList;
import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.biz.service.external.client.portal.RatingInfoWTO;
import com.gam.nocr.ems.data.domain.RatingInfoTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class RatingInfoMapper {
    private RatingInfoMapper() {
    }

    public static RatingInfoWTO convert(RatingInfoTO ratingInfoTO) {
        RatingInfoWTO ratingInfoWTO = null;
        if (ratingInfoTO != null) {
            ratingInfoWTO = new RatingInfoWTO();
            ratingInfoWTO.setId(ratingInfoTO.getId());
            ratingInfoWTO.setClazz(ratingInfoTO.getClazz());
            ratingInfoWTO.setSize(ratingInfoTO.getSize());
        }
        return ratingInfoWTO;
    }

    public static List<RatingInfoWTO> convert(List<RatingInfoTO> ratingInfoTOList) throws BaseException {
        List<RatingInfoWTO> ratingInfoWTOList = null;
        if (ratingInfoTOList != null && !ratingInfoTOList.isEmpty()) {
            ratingInfoWTOList = new ArrayList<RatingInfoWTO>();
            for (RatingInfoTO ratingInfoTO : ratingInfoTOList) {
                ratingInfoWTOList.add(convert(ratingInfoTO));
            }
        }
        return ratingInfoWTOList;
    }
}
