package com.gam.nocr.ems.data.mapper.tomapper;

import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.vol.DepartmentVTO;
import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public class DepartementMapper {

    public static DepartmentTO convert(DepartmentVTO vto) {
        DepartmentTO to = new DepartmentTO();
        to.setId(vto.getId());
        to.setCode(vto.getCode());
        to.setName(vto.getName());
        to.setAddress(vto.getAddress());
        to.setPostalCode(vto.getPostalCode());
        to.setDn(vto.getDn());
        to.setParentDN(vto.getParentDN());
//		to.setSendType(vto.getSendType());
        to.setDispatchSendType(DepartmentDispatchSendType.BATCH);
        to.setParentName(vto.getParentName());
        to.setParentId(vto.getParentId());
        to.setLocName(vto.getLocName());
        to.setLocId(vto.getLocId());
        return to;
    }

    public static DepartmentVTO convert(DepartmentTO to) {
        DepartmentVTO vto = new DepartmentVTO();
        vto.setId(to.getId());
        vto.setCode(to.getCode());
        vto.setName(to.getName());
        vto.setAddress(to.getAddress());
        vto.setPostalCode(to.getPostalCode());
        vto.setDn(to.getDn());
        vto.setParentDN(to.getParentDN());
        vto.setSendType(to.getSendType());
        vto.setParentName(to.getParentName());
        vto.setParentId(to.getParentId());
        vto.setLocName(to.getLocName());
        vto.setLocId(to.getLocId());
        return vto;
    }
}
