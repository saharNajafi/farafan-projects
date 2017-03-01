package com.gam.nocr.ems.data.mapper.tomapper;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.config.MapperExceptionCode;
import com.gam.nocr.ems.data.domain.BiometricTO;
import com.gam.nocr.ems.data.domain.ws.BiometricWTO;
import com.gam.nocr.ems.data.domain.ws.ItemWTO;
import com.gam.nocr.ems.data.enums.BiometricType;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class BiometricMapper {

    private BiometricMapper() {
    }

    public static BiometricTO convert(BiometricWTO wto) throws BaseException {
        try {
            if (wto == null) {
                throw new BaseException(MapperExceptionCode.BIM_001, MapperExceptionCode.BIM_001_MSG);
            }
            BiometricTO bio = new BiometricTO();
            bio.setId(wto.getId());
            if (wto.getType() != null) {
                if (BiometricType.toType(wto.getType()) == null) {
                    throw new BaseException(MapperExceptionCode.BIM_002, MapperExceptionCode.BIM_002_MSG);
                }
                bio.setType(BiometricType.toType(wto.getType()));
            }
            bio.setData(wto.getData());
            // create meta data
            if (wto.getMetaData() == null || wto.getMetaData().length == 0) {
                bio.setMetaData(null);
            } else {
                StringBuilder metaData = new StringBuilder();
                ItemWTO item;
                for (int i = 0; i < wto.getMetaData().length; i++) {
                    item = wto.getMetaData()[i];
                    if (item == null) {
                        throw new BaseException(MapperExceptionCode.BIM_006, MapperExceptionCode.BIM_006_MSG);
                    }
                    metaData.append(item.getKey().trim()).append("=").append(item.getValue().trim());
                    if (i < (wto.getMetaData().length - 1)) {
                        metaData.append(",");
                    }
                }
                bio.setMetaData(metaData.toString());
            }

            return bio;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(MapperExceptionCode.BIM_004, MapperExceptionCode.BIM_004_MSG, e);
        }
    }
    public static BiometricWTO convert(BiometricTO bio) throws BaseException {
        try {
            if (bio == null) {
                throw new BaseException(MapperExceptionCode.BIM_003, MapperExceptionCode.BIM_003_MSG);
            }
            BiometricWTO wto = new BiometricWTO();
            wto.setId(bio.getId());
            wto.setType(BiometricType.toWTOString(bio.getType()));
            wto.setData(bio.getData());
            // meta data
            if (bio.getMetaData() == null || bio.getMetaData().trim().length() == 0) {
                wto.setMetaData(null);
            } else {
                String[] items = bio.getMetaData().split(",");
                ItemWTO[] itemWTOs = new ItemWTO[items.length];
                String[] split;
                ItemWTO itemWTO;
                String item;
                for (int i = 0; i < items.length; i++) {
                    item = items[i];
                    split = item.split("=");
                    itemWTO = new ItemWTO(split[0].trim(), split[1].trim());
                    itemWTOs[i] = itemWTO;
                }
                wto.setMetaData(itemWTOs);
            }

            return wto;
        } catch (Exception e) {
            throw new BaseException(MapperExceptionCode.BIM_005, MapperExceptionCode.BIM_005_MSG, e);
        }
    }
}
