package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;

import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface CitizenDAO extends EmsBaseDAO<CitizenTO> {

    public List<CitizenTO> findByNID(String nid) throws BaseException;
    
    //Anbari:IMS ( use in load citizen for sodoor mojadad )
    public CitizenTO findByNationalID(String nid) throws BaseException;

    public CitizenTO findByRequestId(long requestId) throws BaseException;

    public CitizenTO findRequestId(Long requestId) throws BaseException;

    public CitizenTO findAllAttributesByRequestId(long requestId) throws BaseException;

    public List<CitizenTO> findByAttributes(String firstName, String lastName, String nationalId) throws BaseException;

    CitizenTO findAllByRequestId(long requestId) throws BaseException;
    
    List<CardRequestTO> checkCitizenHasAnyUnDeliveredRequest(CitizenTO citizen) throws BaseException;

    CitizenTO findByNationalId(String nationalId) throws BaseException;
}
