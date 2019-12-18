package com.gam.nocr.ems.data.mapper.tomapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.config.MapperExceptionCode;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.domain.vol.PersonTokenVTO;
import org.slf4j.Logger;

import java.sql.Timestamp;

public class PersonTokenMapper {
    private static final Logger logger = BaseLog.getLogger(PersonTokenMapper.class);

    private PersonTokenMapper() {
    }

    public static PersonTokenVTO convert(PersonTokenTO personTokenTO) throws BaseException {
        if (personTokenTO == null) {
            throw new BaseException(MapperExceptionCode.PTM_001, MapperExceptionCode.PTM_001_MSG);
        }

        PersonTokenVTO tokenVTO = new PersonTokenVTO();

        tokenVTO.setPerson(PersonMapper.convert(personTokenTO.getPerson(), null));
        tokenVTO.setIssuanceDate(new Timestamp(personTokenTO.getIssuanceDate().getTime()));
        tokenVTO.setRequestDate(new Timestamp(personTokenTO.getRequestDate().getTime()));
        tokenVTO.setTokenState(personTokenTO.getTokenState());
        tokenVTO.setTokenType(personTokenTO.getTokenType());
        tokenVTO.setTokenReason(personTokenTO.getPtReason().getName());
        //added by Madanipour
        tokenVTO.setDeliverDate(new Timestamp(personTokenTO.getDeliverDate().getTime()));


        return tokenVTO;
    }
}
