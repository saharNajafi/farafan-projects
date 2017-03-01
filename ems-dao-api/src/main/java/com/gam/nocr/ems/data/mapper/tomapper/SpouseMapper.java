package com.gam.nocr.ems.data.mapper.tomapper;

import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.biz.service.external.client.portal.SpouseVTO;
import com.gam.nocr.ems.config.ConstValues;
import com.gam.nocr.ems.config.MapperExceptionCode;
import com.gam.nocr.ems.data.domain.MaritalStatusTO;
import com.gam.nocr.ems.data.domain.SpouseTO;
import com.gam.nocr.ems.data.domain.ws.SpouseWTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class SpouseMapper {

    private SpouseMapper() {
    }

    public static SpouseTO convert(SpouseWTO wto) throws BaseException {//Does not set CitizenInfo
        if (wto == null) {
            throw new BaseException(MapperExceptionCode.SPM_003, MapperExceptionCode.SPM_003_MSG);
        }
        SpouseTO sps = new SpouseTO();
        if ((wto.getId() != null) && (wto.getId() > 0))
            sps.setId(wto.getId());

        sps.setSpouseFirstNamePersian(wto.getFirstNameFA());
        sps.setSpouseSurnamePersian(wto.getSureNameFA());
        sps.setSpouseFatherName(wto.getFatherName());
        sps.setSpouseBirthCertificateId(wto.getBirthCerId());
        sps.setSpouseNationalID(wto.getNationalId());
//        sps.setSpouseBirthCertificateSeries(wto.getBirthCertSeries());
        sps.setSpouseBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//		if(wto.getBirthDate() != null) {
//			try {
//				sps.setSpouseBirthDate(DateUtil.convert(wto.getBirthDate(), DateUtil.JALALI));
//			} catch (Exception e) {
//				throw new BaseException(WebExceptionCode.RSW_015, WebExceptionCode.RSW_015_MSG, e);
//			}
//		}
        sps.setSpouseBirthDate(wto.getBirthDate());
        sps.setSpouseMarriageDate(wto.getMarriageDate());
        if (wto.getMaritalStatusId() != null) {
            sps.setMaritalStatus(new MaritalStatusTO(wto.getMaritalStatusId()));
        }
        return sps;
    }

    public static SpouseWTO convert(SpouseTO sps) throws BaseException {
        if (sps == null) {
            throw new BaseException(MapperExceptionCode.SPM_004, MapperExceptionCode.SPM_004_MSG);
        }
        SpouseWTO wto = new SpouseWTO();
        wto.setId(sps.getId());

        if (sps.getCitizenInfo() == null) {
            wto.setCitizenId(null);
        } else {
            wto.setCitizenId(sps.getCitizenInfo().getId());
        }
        wto.setFirstNameFA(sps.getSpouseFirstNamePersian());
        wto.setSureNameFA(sps.getSpouseSurnamePersian());
        wto.setFatherName(sps.getSpouseFatherName());
        wto.setBirthCerId(sps.getSpouseBirthCertificateId());
        wto.setNationalId(sps.getSpouseNationalID());
//        wto.setBirthCertSeries(sps.getSpouseBirthCertificateSeries());
        wto.setBirthCertSeries(ConstValues.DEFAULT_CERT_SERIAL);
//		if(sps.getSpouseBirthDate()==null || sps.getSpouseBirthDate().trim().equals("")) {
//			wto.setBirthDate(null);
//		} else {
//			try {
//				wto.setBirthDate(DateUtil.convert(sps.getSpouseBirthDate(), DateUtil.JALALI));
//			} catch (DateFormatException e) {
//				throw new BaseException(WebExceptionCode.RSW_049, WebExceptionCode.RSW_049_MSG, e);
//			}
//		}
        wto.setBirthDate(sps.getSpouseBirthDate());
        wto.setMarriageDate(sps.getSpouseMarriageDate());
        if (sps.getSpouseDeathOrDivorceDate() != null)
            wto.setDeathOrDivorceDate(sps.getSpouseDeathOrDivorceDate());
        if (sps.getMaritalStatus() == null) {
            wto.setMaritalStatusId(null);
        } else {
            wto.setMaritalStatusId(sps.getMaritalStatus().getId());
        }
        return wto;
    }

    public static SpouseTO convert(SpouseVTO spouseVTO) throws BaseException {
        if (spouseVTO == null) {
            throw new BaseException(MapperExceptionCode.SPM_001, MapperExceptionCode.SPM_001_MSG);
        }
        SpouseTO sps = new SpouseTO();
//		if ((spouseVTO.getId() != null) && (spouseVTO.getId() > 0))
//			sps.setId(spouseVTO.getId());

        sps.setSpouseFirstNamePersian(spouseVTO.getFirstNameFA());
        sps.setSpouseSurnamePersian(spouseVTO.getSureNameFA());
        sps.setSpouseFatherName(spouseVTO.getFatherName());
        sps.setSpouseBirthCertificateId(spouseVTO.getBirthCertId());
        sps.setSpouseNationalID(spouseVTO.getNationalId());
//        sps.setSpouseBirthCertificateSeries(spouseVTO.getBirthCertSerial());
        sps.setSpouseBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);

        try {
            sps.setSpouseBirthDate(DateUtil.convert(spouseVTO.getBirthDate(), DateUtil.JALALI));
            sps.setSpouseMarriageDate(DateUtil.convert(spouseVTO.getMarriageDate(), DateUtil.JALALI));
            if (EmsUtil.checkString(spouseVTO.getDeathOrDivorceDate()))
                sps.setSpouseDeathOrDivorceDate(DateUtil.convert(spouseVTO.getDeathOrDivorceDate(), DateUtil.JALALI));
        } catch (DateFormatException e) {
            throw new BaseException(MapperExceptionCode.SPM_002, MapperExceptionCode.GLB_001_MSG, e);
        }

        if (spouseVTO.getMaritalStatusId() != null) {
            sps.setMaritalStatus(new MaritalStatusTO(spouseVTO.getMaritalStatusId()));
        }
        return sps;
    }

    public static SpouseVTO convertToSpouseVTO(SpouseTO sps) throws BaseException {
        if (sps == null) {
            throw new BaseException(MapperExceptionCode.SPM_005, MapperExceptionCode.SPM_005_MSG);
        }
        SpouseVTO spouseVTO = new SpouseVTO();
        spouseVTO.setId(sps.getId());

        if (sps.getCitizenInfo() == null) {
            spouseVTO.setCitizenId(null);
        } else {
            spouseVTO.setCitizenId(sps.getCitizenInfo().getId());
        }
        spouseVTO.setFirstNameFA(sps.getSpouseFirstNamePersian());
        spouseVTO.setSureNameFA(sps.getSpouseSurnamePersian());
        spouseVTO.setFatherName(sps.getSpouseFatherName());
        spouseVTO.setBirthCertId(sps.getSpouseBirthCertificateId());
        spouseVTO.setNationalId(sps.getSpouseNationalID());
//        spouseVTO.setBirthCertSerial(sps.getSpouseBirthCertificateSeries());
        spouseVTO.setBirthCertSerial(ConstValues.DEFAULT_CERT_SERIAL);
        spouseVTO.setBirthDate(DateUtil.convert(sps.getSpouseBirthDate(), DateUtil.JALALI));
        if (sps.getMaritalStatus() == null) {
            spouseVTO.setMaritalStatusId(null);
        } else {
            spouseVTO.setMaritalStatusId(sps.getMaritalStatus().getId());
        }
        return spouseVTO;
    }
    
    //hossein 8 feature start
    public static com.gam.nocr.ems.data.domain.vol.SpouseVTO convertToEmsSpouseVTO(SpouseTO to) throws BaseException {
        if (to == null) {
            throw new BaseException(MapperExceptionCode.SPM_005, MapperExceptionCode.SPM_005_MSG);
        }
        com.gam.nocr.ems.data.domain.vol.SpouseVTO vto = new com.gam.nocr.ems.data.domain.vol.SpouseVTO();
        vto.setFirstName(to.getSpouseFirstNamePersian());
        vto.setSureName(to.getSpouseSurnamePersian());
        vto.setNid(to.getSpouseNationalID());
        vto.setDeathDivorceDate(to.getSpouseDeathOrDivorceDate());
        vto.setMarriageDate(to.getSpouseMarriageDate());
        if (to.getMaritalStatus() == null) {
            vto.setMaritalStatusId(null);
        } else {
            vto.setMaritalStatusId(to.getMaritalStatus().getId());
        }

        return vto;
    }
    //hossein 8 feature end


}
