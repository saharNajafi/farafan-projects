package com.gam.nocr.ems.data.mapper.xmlmapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.domain.ExtEntityTO;

import java.util.Map;

public interface XMLMapper {
    public byte[] writeXML(ExtEntityTO to, Map<String, String> attributesMap) throws BaseException;

    public ExtEntityTO readXML(String xmlData, ExtEntityTO to) throws BaseException;

    /**
     * The method readXML is used to read the xml data
     *
     * @param xmlData       is an instance of type {@link String} which represents the xml String that has wanted to read
     * @param to            is an instance of type {@link ExtEntityTO}
     * @param attributesMap is a map of type {@link Map<String, String>}
     * @return an instance of type {@link ExtEntityTO}
     * @throws BaseException
     */
    ExtEntityTO readXML(String xmlData,
                        ExtEntityTO to,
                        Map<String, String> attributesMap) throws BaseException;
}
