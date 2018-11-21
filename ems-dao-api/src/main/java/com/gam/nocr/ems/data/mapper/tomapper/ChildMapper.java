package com.gam.nocr.ems.data.mapper.tomapper;

import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;

import com.gam.commons.core.BaseException;
//import com.gam.nocr.ems.biz.service.external.client.portal.ChildVTO;
import com.gam.nocr.ems.config.ConstValues;
import com.gam.nocr.ems.config.MapperExceptionCode;
import com.gam.nocr.ems.data.domain.ChildTO;
import com.gam.nocr.ems.data.domain.ws.ChildrenWTO;
import com.gam.nocr.ems.data.enums.Gender;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class ChildMapper {

	private ChildMapper() {
	}

	public static ChildTO convert(ChildrenWTO wto) throws BaseException {//Does not set CitizenInfo
		if (wto == null) {
			throw new BaseException(MapperExceptionCode.CHM_005, MapperExceptionCode.CHM_005_MSG);
		}
		ChildTO chi = new ChildTO();
		chi.setId(wto.getId());
		chi.setChildFirstNamePersian(wto.getFistNameFA());
		chi.setChildFatherName(wto.getFatherName());
		chi.setChildBirthCertificateId(wto.getBirthCertId());
		chi.setChildBirthDateSolar(wto.getBirthDate());
		chi.setChildDeathDateSolar(wto.getDeathDate());
		chi.setChildNationalID(wto.getNationalId());
		if (wto.getGender() != null) {
			try {
				chi.setChildGender(Gender.valueOf(wto.getGender()));
			} catch (IllegalArgumentException e) {
				throw new BaseException(MapperExceptionCode.CHM_006, MapperExceptionCode.CHM_004_MSG, e, new String[]{wto.getGender()});
			}
		}
		//        chi.setChildBirthCertificateSeries(wto.getBirthCertSeries());
		chi.setChildBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
		return chi;
	}

	public static ChildrenWTO convert(ChildTO chi) throws BaseException {
		if (chi == null) {
			throw new BaseException(MapperExceptionCode.CHM_007, MapperExceptionCode.CHM_007_MSG);
		}
		ChildrenWTO wto = new ChildrenWTO();
		wto.setId(chi.getId());
		if (chi.getCitizenInfo() == null) {
			wto.setCitizenId(null);
		} else {
			wto.setCitizenId(chi.getCitizenInfo().getId());
		}
		wto.setFistNameFA(chi.getChildFirstNamePersian());
		wto.setFatherName(chi.getChildFatherName());
		wto.setBirthCertId(chi.getChildBirthCertificateId());
		wto.setBirthDate(chi.getChildBirthDateSolar());
		wto.setDeathDate(chi.getChildDeathDateSolar());
		wto.setNationalId(chi.getChildNationalID());
		if (chi.getChildGender() != null) {
			wto.setGender(chi.getChildGender().name());
		}
		//        wto.setBirthCertSeries(chi.getChildBirthCertificateSeries());
		wto.setBirthCertSeries(ConstValues.DEFAULT_CERT_SERIAL);
		return wto;
	}

//	public static ChildVTO convertToChildVTO(ChildTO chi) throws BaseException {
//		if (chi == null) {
//			throw new BaseException(MapperExceptionCode.CHM_008, MapperExceptionCode.CHM_008_MSG);
//		}
//		ChildVTO childVTO = new ChildVTO();
//		childVTO.setId(chi.getId());
//		if (chi.getCitizenInfo() == null) {
//			childVTO.setCitizenId(null);
//		} else {
//			childVTO.setCitizenId(chi.getCitizenInfo().getId());
//		}
//		childVTO.setFirstNameFA(chi.getChildFirstNamePersian());
//		childVTO.setFatherName(chi.getChildFatherName());
//		childVTO.setBirthCertId(chi.getChildBirthCertificateId());
//		childVTO.setBirthDate(DateUtil.convert(chi.getChildBirthDateSolar(), DateUtil.JALALI));
//		if (chi.getChildDeathDateSolar() != null) {
//			childVTO.setDeathDate(DateUtil.convert(chi.getChildDeathDateSolar(), DateUtil.JALALI));
//		}
//		childVTO.setNationalId(chi.getChildNationalID());
//		if (chi.getChildGender() != null) {
//			childVTO.setGender(chi.getChildGender().name());
//		}
//		childVTO.setBirthCertSerial(chi.getChildBirthCertificateSeries());
//		return childVTO;
//	}

//	public static ChildTO convert(ChildVTO childVTO) throws BaseException {//Does not set CitizenInfo
//		if (childVTO == null) {
//			throw new BaseException(MapperExceptionCode.CHM_001, MapperExceptionCode.CHM_001_MSG);
//		}
//		ChildTO chi = new ChildTO();
//		//		chi.setId(childVTO.getId());
//		chi.setChildFirstNamePersian(childVTO.getFirstNameFA());
//		chi.setChildFatherName(childVTO.getFatherName());
//		chi.setChildBirthCertificateId(childVTO.getBirthCertId());
//		try {
//			chi.setChildBirthDateSolar(DateUtil.convert(childVTO.getBirthDate(), DateUtil.JALALI));
//		} catch (DateFormatException e) {
//			throw new BaseException(MapperExceptionCode.CHM_002, MapperExceptionCode.GLB_001_MSG, e);
//		}
//		try {
//			if (EmsUtil.checkString(childVTO.getDeathDate()))
//				chi.setChildDeathDateSolar(DateUtil.convert(childVTO.getDeathDate(), DateUtil.JALALI));
//		} catch (DateFormatException e) {
//			throw new BaseException(MapperExceptionCode.CHM_003, MapperExceptionCode.GLB_001_MSG, e);
//		}
//		chi.setChildNationalID(childVTO.getNationalId());
//		if (childVTO.getGender() != null) {
//			try {
//				chi.setChildGender(Gender.valueOf(childVTO.getGender()));
//			} catch (IllegalArgumentException e) {
//				throw new BaseException(MapperExceptionCode.CHM_004, MapperExceptionCode.CHM_004_MSG, e, new String[]{childVTO.getGender()});
//			}
//		}
//		//        chi.setChildBirthCertificateSeries(childVTO.getBirthCertSerial());
//		chi.setChildBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//		return chi;
//	}


	//hossein 8 feature start
	public  static com.gam.nocr.ems.data.domain.vol.ChildVTO convertToEmsVTO(ChildTO to) throws BaseException
	{
		if (to == null) {
			throw new BaseException(MapperExceptionCode.CHM_008, MapperExceptionCode.CHM_008_MSG);
		}
		com.gam.nocr.ems.data.domain.vol.ChildVTO vto = new  com.gam.nocr.ems.data.domain.vol.ChildVTO();
		vto.setFirstName(to.getChildFirstNamePersian());
		vto.setBirthDate(to.getChildBirthDateSolar());
		vto.setNid(to.getChildNationalID());
		if (to.getChildGender() != null) {
			vto.setGender(to.getChildGender().name());
		}
		return vto;
	}
	//hossein 8 feature end

}
