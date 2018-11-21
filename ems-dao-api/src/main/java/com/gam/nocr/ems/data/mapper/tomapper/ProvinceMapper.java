package com.gam.nocr.ems.data.mapper.tomapper;

import java.util.ArrayList;
import java.util.List;

//import com.gam.nocr.ems.biz.service.external.client.portal.LocationWTO;
import com.gam.nocr.ems.data.domain.LocationTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ProvinceMapper {

    private ProvinceMapper() {
    }

//    public static LocationWTO convert(LocationTO locationTO) {
//        LocationWTO locationWTO = null;
//        if (locationTO != null) {
//            locationWTO = new LocationWTO();
//            locationWTO.setId(locationTO.getId());
//            LocationTO province = locationTO.getProvince();
//            if (province != null) {
//                locationWTO.setProvinceId(province.getId());
//            }
//            LocationTO county = locationTO.getCounty();
//            if (county != null) {
//                locationWTO.setCountyId(county.getId());
//            }
//            LocationTO township = locationTO.getTownship();
//            if (township != null) {
//                locationWTO.setTownshipId(township.getId());
//            }
//            LocationTO district = locationTO.getDistrict();
//            if (district != null) {
//                locationWTO.setDistrictId(district.getId());
//            }
//            locationWTO.setName(locationTO.getName());
//            locationWTO.setType(locationTO.getType());
//            locationWTO.setState(locationTO.getState());
//        }
//        return locationWTO;
//    }

//    public static List<LocationWTO> convert(List<LocationTO> locationTOList) {
//        List<LocationWTO> locationWTOList = null;
//        if (locationTOList != null && !locationTOList.isEmpty()) {
//            locationWTOList = new ArrayList<LocationWTO>();
//            for (LocationTO locationTO : locationTOList) {
//                locationWTOList.add(convert(locationTO));
//            }
//        }
//        return locationWTOList;
//    }
}
