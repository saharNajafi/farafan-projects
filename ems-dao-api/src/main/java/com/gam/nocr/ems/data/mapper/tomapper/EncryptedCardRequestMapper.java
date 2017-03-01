package com.gam.nocr.ems.data.mapper.tomapper;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.EncryptedCardRequestTO;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public class EncryptedCardRequestMapper {

    private EncryptedCardRequestMapper() {
    }

    public static EncryptedCardRequestTO convert(byte[] citizenInfo, byte[] spouses, byte[] children, byte[] fingers, byte[] faces, byte[] documents) throws BaseException {
        EncryptedCardRequestTO to = new EncryptedCardRequestTO();
        to.setCitizenInfo(citizenInfo);
        to.setSpouses(spouses);
        to.setChildren(children);
        to.setFingers(fingers);
        to.setFaces(faces);
        to.setDocuments(documents);
        return to;
    }

}
