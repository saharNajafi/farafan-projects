package com.gam.nocr.ems.data.mapper.tomapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.service.external.client.portal.CardRequestWTO;
import com.gam.nocr.ems.biz.service.external.client.portal.ChildVTO;
import com.gam.nocr.ems.biz.service.external.client.portal.CitizenVTO;
import com.gam.nocr.ems.biz.service.external.client.portal.SpouseVTO;
import com.gam.nocr.ems.config.ConstValues;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.MapperExceptionCode;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.domain.ws.ChildrenWTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.SpouseWTO;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardRequestType;
import com.gam.nocr.ems.data.enums.Gender;
import com.gam.nocr.ems.data.enums.ReligionEnum;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import org.slf4j.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class CardRequestMapper {

    private static final Logger logger = BaseLog.getLogger(CardRequestMapper.class);

    private CardRequestMapper() {
    }

    public static CardRequestTO convert(CitizenWTO wto) throws BaseException {
        if (wto == null) {
            throw new BaseException(MapperExceptionCode.CRM_008, MapperExceptionCode.CRM_008_MSG);
        }

        CardRequestTO crq = new CardRequestTO();
        CitizenTO ctz = new CitizenTO();
        CitizenInfoTO czi = new CitizenInfoTO();
        SpouseTO sps;
        ChildTO chi;

        ctz.setId(wto.getId());
        ctz.setFirstNamePersian(wto.getFirstNameFA());
        ctz.setSurnamePersian(wto.getSureNameFA());
        ctz.setNationalID(wto.getNationalId());

        crq.setId(wto.getRequestId());//5
        if (wto.getType() != null) {
            if (CardRequestType.toType(wto.getType()) == null) {
                throw new BaseException(MapperExceptionCode.CRM_009, MapperExceptionCode.CRM_009_MSG);
            }
            crq.setType(CardRequestType.toType(wto.getType()));
            if (wto.getType().equals(4L)) {
                crq.setReason("DESTROYED");
            } else if (wto.getType().equals(5L)) {
                crq.setReason("IDENTITY_CHANGED");
            }
        }
        crq.setEnrolledDate(wto.getEnrolledDate());
        if (wto.getState() != null) {
            if (CardRequestState.toState(wto.getState()) == null) {
                throw new BaseException(MapperExceptionCode.CRM_010, MapperExceptionCode.CRM_010_MSG);
            }
            crq.setState(CardRequestState.toState(wto.getState()));
        }
        crq.setTrackingID(wto.getTrackingId());
      //************************Anbari:Payment
        crq.setPaid(wto.getIsPaid()== null ? false : wto.getIsPaid().intValue() == 1);
        crq.setPaidDate(wto.getPaidDate());
        //**************************

        czi.setId(wto.getId());//10
        czi.setFirstNameEnglish(wto.getFirstNameEN());
        czi.setSurnameEnglish(wto.getSureNameEN());
        czi.setBirthCertificateId(wto.getBirthCertId());
        czi.setBirthDateGregorian(wto.getBirthDate());
        czi.setBirthDateLunar(wto.getBirthDateHijri());//15
        if (wto.getGender() != null) {
            try {
                czi.setGender(Gender.valueOf(wto.getGender()));
            } catch (IllegalArgumentException e) {
                throw new BaseException(MapperExceptionCode.CRM_011, MapperExceptionCode.CRM_005_MSG, e, new String[]{wto.getGender()});
            }
        }
        if (wto.getReligionId() != null) {
            czi.setReligion(new ReligionTO(wto.getReligionId()));
        }
        else {
            czi.setReligion(new ReligionTO(Long.valueOf(ReligionEnum.ISLAM.getCode())));
        }
        czi.setPostcode(wto.getPostCode());
        czi.setEmail(wto.getEmail());
        /* Decided to use birthCertificateIssuancePlace instead of birthCertPrvId
        if(wto.getBirthCertPrvId() != null) {
			czi.setBirthCertificateIssuancePlaceProvince(new ProvinceTO(wto.getBirthCertPrvId()));//20
		}*/
        czi.setBirthCertificateIssuancePlace(wto.getBirthCertPrvName());
        // set location
        if(wto.getUserCityType() == null)
        	
        	throw new BaseException(MapperExceptionCode.CRM_018, MapperExceptionCode.CRM_018_MSG);
        if(wto.getUserCityType().equals("2")) //is vip
        {
        	czi.setLiving(null);
        	czi.setLivingCity(null);
        }
        else {
        	
        	if (wto.getLivingPrvId() == null || wto.getLivingPrvId()==0) 
        		
        		throw new BaseException(MapperExceptionCode.CRM_017, MapperExceptionCode.CRM_017_MSG);
        	
        	czi.setLiving(new LocationTO(wto.getLivingPrvId()));
        	
        	// 1 -----> shahr
        	// 0 ------> dehestan
        	if (wto.getUserCityType().equals("1")) {
        		if (wto.getLivingCityId() == null || wto.getLivingCityId()==0) 
        			
        			throw new BaseException(MapperExceptionCode.CRM_019, MapperExceptionCode.CRM_019_MSG);
        		czi.setLivingCity(new LocationTO(wto.getLivingCityId()));
        		
        		
        	} else if (wto.getUserCityType().equals("0")) {
        		if (wto.getLivingVillageId() == null || wto.getLivingVillageId()==0) 
        			
        			throw new BaseException(MapperExceptionCode.CRM_020, MapperExceptionCode.CRM_020_MSG);
        		czi.setLivingCity(new LocationTO(wto.getLivingVillageId()));
        		
        	}
        }

        czi.setBirthCertificateSeries(wto.getBirthCertSerial());
        //czi.setBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
        czi.setFatherFirstNamePersian(wto.getFatherFirstNameFA());
        czi.setFatherFirstNameEnglish(wto.getFatherFirstNameEN());//25
        czi.setFatherFatherName(wto.getFatherFatherName());
        czi.setFatherSurname(wto.getFatherSureName());
        czi.setFatherNationalID(wto.getFatherNationalId());
        czi.setFatherBirthDateSolar(wto.getFatherBirthDate());
        czi.setFatherBirthCertificateId(wto.getFatherBirthCertId());//30
//        czi.setFatherBirthCertificateSeries(wto.getFatherBirthCertSeries());
        czi.setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
        czi.setMotherFirstNamePersian(wto.getMotherFirstNameFA());
        czi.setMotherSurname(wto.getMotherSureName());
        czi.setMotherNationalID(wto.getMotherNationalId());
        if (wto.getMotherBirthDate() != null) {
            czi.setMotherBirthDateSolar(new Date(wto.getMotherBirthDate().getTime()));//35
        }
        czi.setMotherFatherName(wto.getMotherFatherName());
        czi.setMotherBirthCertificateId(wto.getMotherBirthCertId());
//        czi.setMotherBirthCertificateSeries(wto.getMotherBirthCertSeries());
        czi.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
        czi.setAddress(wto.getAddress());
        czi.setPhone(wto.getPhone());//40
        czi.setMobile(wto.getMobile());

        if (wto.getSpouses() != null) {
            for (SpouseWTO spouse : wto.getSpouses()) {
                sps = SpouseMapper.convert(spouse);
                sps.setCitizenInfo(czi);
                czi.getSpouses().add(sps);
            }
        }

        if (wto.getChildren() != null) {
            for (ChildrenWTO child : wto.getChildren()) {
                chi = ChildMapper.convert(child);
                chi.setCitizenInfo(czi);
                czi.getChildren().add(chi);
            }
        }

        ctz.setCitizenInfo(czi);
        czi.setCitizen(ctz);
        crq.setCitizen(ctz);
        return crq;
    }

    //hossein 8 feature start
    public static CardRequestVTO convertToVTO(CardRequestTO to) throws BaseException {
        if (to == null) {
            throw new BaseException(MapperExceptionCode.CRM_008, MapperExceptionCode.CRM_008_MSG);
        }
        CardRequestVTO vto = new CardRequestVTO();
        CitizenVTO ctz = new CitizenVTO();
        SpouseVTO sps;
        ChildVTO chi;

        vto.setId(to.getId());
        if (to.getEnrollmentOffice()!=null) {
            vto.setEnrollmentOfficeId(to.getEnrollmentOffice().getId());
            vto.setEnrollmentOfficeName(to.getEnrollmentOffice().getName());
        }
        if (to.getEnrolledDate()!=null){
            vto.setEnrolledDate((Timestamp)to.getEnrolledDate());
        }
        if (to.getPortalEnrolledDate()!=null)
        {
            vto.setPortalEnrolledDate((Timestamp) to.getPortalEnrolledDate());
        }
        if (to.getState()!=null){
               vto.setCardRequestState(to.getState().name());
        }
        if (to.getType()!=null){
            vto.setCardType(to.getType().name());
         }
        if (to.getCard()!=null && to.getCard().getState()!=null)
        vto.setCardState(to.getCard().getState().name());
        vto.setTrackingId(to.getTrackingID());
        if (to.getRequestedAction()!=null) {
            vto.setRequestedAction(to.getRequestedAction().name());
        }
        vto.setRequestOrigin(to.getOrigin().name());
        if (to.getDeliveredOfficeId()!=null) {
            vto.setDeliveredOfficeName(to.getDeliveredOfficeId().toString());
        }
         vto.setReservationDate((Timestamp)to.getReservationDate());

        if (to.getCitizen()!=null )
        {
            if (to.getCitizen().getCitizenInfo()!=null){
                vto.setCitizenInfo(CitizenInfoMapper.convert(to.getCitizen().getCitizenInfo()));
                vto.setSpouses(new ArrayList<com.gam.nocr.ems.data.domain.vol.SpouseVTO>());
                if (EmsUtil.checkListSize(to.getCitizen().getCitizenInfo().getSpouses()) ){
                    for(SpouseTO spouse:to.getCitizen().getCitizenInfo().getSpouses()){
                        vto.getSpouses().add(SpouseMapper.convertToEmsSpouseVTO(spouse));
                    }
                }
                vto.setChildren(new ArrayList<com.gam.nocr.ems.data.domain.vol.ChildVTO>());
                if (EmsUtil.checkListSize(to.getCitizen().getCitizenInfo().getChildren())){
                    for(ChildTO child:to.getCitizen().getCitizenInfo().getChildren()){
                        vto.getChildren().add(ChildMapper.convertToEmsVTO(child));
                    }
                }
            }

        }

        return vto;
    }
    //hossein 8 feature end
    
    public static CitizenWTO convert(CardRequestTO crq, CitizenTO ctz) throws BaseException {
        if (ctz == null) {
            throw new BaseException(MapperExceptionCode.CRM_012, MapperExceptionCode.CRM_012_MSG);
        }
        CitizenInfoTO czi = ctz.getCitizenInfo();
        CitizenWTO wto = new CitizenWTO();
        SpouseWTO spouse;
        ChildrenWTO child;
        SpouseWTO[] spouses;
        ChildrenWTO[] children;

        if (crq != null) {
            wto.setRequestId(crq.getId());
            if (crq.getEnrolledDate() != null) {
                wto.setEnrolledDate(new Timestamp(crq.getEnrolledDate().getTime()));
            }
            wto.setState(CardRequestState.toLong(crq.getState()));//40
            wto.setTrackingId(crq.getTrackingID());
            if (crq.getType() == CardRequestType.REPLACE) {
                if (crq.getReason() == null) {
                    throw new BaseException(MapperExceptionCode.CRM_013, MapperExceptionCode.CRM_013_MSG);
                } else if (crq.getReason().contains("DESTROYED")) {
                    wto.setType(4L);
                } else if (crq.getReason().contains("IDENTITY_CHANGED")) {
                    wto.setType(5L);
                } else {
                    throw new BaseException(MapperExceptionCode.CRM_014, MapperExceptionCode.CRM_014_MSG);
                }
            } else {
                wto.setType(CardRequestType.toLong(crq.getType()));
            }
        }
        wto.setId(ctz.getId());
        wto.setFirstNameFA(ctz.getFirstNamePersian());
        wto.setFirstNameEN(czi.getFirstNameEnglish());
        wto.setSureNameFA(ctz.getSurnamePersian());//5
        wto.setSureNameEN(czi.getSurnameEnglish());
        wto.setBirthCertId(czi.getBirthCertificateId());
        if (czi.getBirthDateGregorian() != null) {
            wto.setBirthDate(new Timestamp(czi.getBirthDateGregorian().getTime()));
        }
        wto.setBirthDateHijri(czi.getBirthDateLunar());
        wto.setNationalId(ctz.getNationalID());//10
        if (czi.getGender() != null) {
            wto.setGender(czi.getGender().name());
        }

        if (czi.getReligion() != null) {
            wto.setReligionId(czi.getReligion().getId());
        }else {
            wto.setReligionId(Long.valueOf(ReligionEnum.ISLAM.getCode()));
        }
        wto.setPostCode(czi.getPostcode());
        wto.setEmail(czi.getEmail());//15
        // Decided to use birthCertificateIssuancePlace instead of birthCertPrvId
        /*if(czi.getBirthCertificateIssuancePlaceProvince() != null) {
            wto.setBirthCertPrvName(czi.getBirthCertificateIssuancePlaceProvince().getName());
			wto.setBirthCertPrvId(czi.getBirthCertificateIssuancePlaceProvince().getId());
		}*/
        wto.setBirthCertPrvName(czi.getBirthCertificateIssuancePlace());

        setGeo(czi, wto);
//        wto.setBirthCertSerial(czi.getBirthCertificateSeries());
        //wto.setBirthCertSerial(ConstValues.DEFAULT_CERT_SERIAL);
        
      //Anbari:Estelam getCertCerial       
        if(czi.getBirthCertificateSeries() != null && czi.getBirthCertificateSeries().length() > 0)
        	wto.setBirthCertSerial(czi.getBirthCertificateSeries());        
        else
        	 wto.setBirthCertSerial(ConstValues.DEFAULT_CERT_SERIAL);
        
        wto.setFatherFirstNameFA(czi.getFatherFirstNamePersian());
        wto.setFatherFirstNameEN(czi.getFatherFirstNameEnglish());
        wto.setFatherFatherName(czi.getFatherFatherName());//25
        wto.setFatherSureName(czi.getFatherSurname());
        wto.setFatherNationalId(czi.getFatherNationalID());
        if (czi.getFatherBirthDateSolar() != null) {
            wto.setFatherBirthDate(new Timestamp(czi.getFatherBirthDateSolar().getTime()));
        }
        wto.setFatherBirthCertId(czi.getFatherBirthCertificateId());
//        wto.setFatherBirthCertSeries(czi.getFatherBirthCertificateSeries());//30
       // wto.setFatherBirthCertSeries(ConstValues.DEFAULT_CERT_SERIAL);//30
        
        //Anbari:Estelam getFather BirthCertCerial
        if(czi.getFatherBirthCertificateSeries() != null && czi.getFatherBirthCertificateSeries().length() > 0)
        	wto.setFatherBirthCertSeries(czi.getBirthCertificateSeries());        
        else
        	 wto.setFatherBirthCertSeries(ConstValues.DEFAULT_CERT_SERIAL);
        
        wto.setMotherFirstNameFA(czi.getMotherFirstNamePersian());
        wto.setMotherSureName(czi.getMotherSurname());
        wto.setMotherNationalId(czi.getMotherNationalID());
        if (czi.getMotherBirthDateSolar() != null) {
            wto.setMotherBirthDate(new Timestamp(czi.getMotherBirthDateSolar().getTime()));
        }
        wto.setMotherFatherName(czi.getMotherFatherName());//35
        wto.setMotherBirthCertId(czi.getMotherBirthCertificateId());
        //wto.setMotherBirthCertSeries(czi.getMotherBirthCertificateSeries());
        
        //Anbari:Estelam getMother BirthCertCerial
        if(czi.getMotherBirthCertificateSeries() != null && czi.getMotherBirthCertificateSeries().length() > 0)
        	wto.setMotherBirthCertSeries(czi.getMotherBirthCertificateSeries());        
        else
        	 wto.setMotherBirthCertSeries(ConstValues.DEFAULT_CERT_SERIAL);
        
        wto.setAddress(czi.getAddress());
        wto.setPhone(czi.getPhone());
        wto.setMobile(czi.getMobile());

        if (czi.getSpouses() != null && !czi.getSpouses().isEmpty()) {
            spouses = new SpouseWTO[czi.getSpouses().size()];
            for (int i = 0; i < czi.getSpouses().size(); i++) {
                spouse = SpouseMapper.convert(czi.getSpouses().get(i));
                spouses[i] = spouse;
            }
            wto.setSpouses(spouses);
        }

        if (czi.getChildren() != null && !czi.getChildren().isEmpty()) {
            children = new ChildrenWTO[czi.getChildren().size()];
            for (int i = 0; i < czi.getChildren().size(); i++) {
                child = ChildMapper.convert(czi.getChildren().get(i));
                children[i] = child;
            }
            wto.setChildren(children);
        }
        return wto;
    }

    public static CardRequestWTO convert(CardRequestTO cardRequestTO) throws BaseException {
        CardRequestWTO cardRequestWTO = new CardRequestWTO();

        if (cardRequestTO.getCitizen() != null) {
            cardRequestWTO.setFirstNamePersian(cardRequestTO.getCitizen().getFirstNamePersian());
            cardRequestWTO.setSurnamePersian(cardRequestTO.getCitizen().getSurnamePersian());
            cardRequestWTO.setNationalID(cardRequestTO.getCitizen().getNationalID());
            cardRequestWTO.setBirthCertificateSerial(cardRequestTO.getCitizen().getCitizenInfo().getBirthCertificateSeries());
            if (cardRequestTO.getCitizen().getCitizenInfo() != null) {
                cardRequestWTO.setBirthDateSolar(cardRequestTO.getCitizen().getCitizenInfo().getBirthDateSolar());
                cardRequestWTO.setBirthDateLunar(cardRequestTO.getCitizen().getCitizenInfo().getBirthDateLunar());
                cardRequestWTO.setGender(cardRequestTO.getCitizen().getCitizenInfo().getGender().toString());
                cardRequestWTO.setReligion(cardRequestTO.getCitizen().getCitizenInfo().getReligion().getId());
                cardRequestWTO.setMotherFirstNamePersian(cardRequestTO.getCitizen().getCitizenInfo().getMotherFirstNamePersian());
                cardRequestWTO.setCellNo(cardRequestTO.getCitizen().getCitizenInfo().getMobile());
            }
        }
        if (cardRequestTO.getState() != null) {
            cardRequestWTO.setCardRequestState(cardRequestTO.getState().name());
        }
        if (cardRequestTO.getEnrolledDate() != null) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(cardRequestTO.getEnrolledDate());
            XMLGregorianCalendar xmlGregorianCalendar = null;
            try {
                xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            } catch (DatatypeConfigurationException e) {
                logger.error(DataExceptionCode.GLB_ERR_MSG, e);
            }
            cardRequestWTO.setEnrolledDate(xmlGregorianCalendar);
        }
        if (cardRequestTO.getType() != null) {
            cardRequestWTO.setCardRequestType(cardRequestTO.getType().name());
        }
        cardRequestWTO.setTrackingID(cardRequestTO.getTrackingID());
        if (cardRequestTO.getEnrollmentOffice() != null) {
            cardRequestWTO.setEnrollmentOfficeId(cardRequestTO.getEnrollmentOffice().getId());
        }

        return cardRequestWTO;
    }

    public static CardRequestTO convert(CitizenVTO citizenVTO) throws BaseException {
        if (citizenVTO == null) {
            throw new BaseException(MapperExceptionCode.CRM_001, MapperExceptionCode.CRM_001_MSG);
        }

        CardRequestTO crq = new CardRequestTO();
        CitizenTO ctz = new CitizenTO();
        CitizenInfoTO czi = new CitizenInfoTO();
        SpouseTO sps;
        ChildTO chi;

//		ctz.setId(citizenVTO.getId());
        ctz.setFirstNamePersian(citizenVTO.getFirstNameFA());
        ctz.setSurnamePersian(citizenVTO.getSureNameFA());
        ctz.setNationalID(citizenVTO.getNationalId());

        crq.setPortalRequestId(citizenVTO.getRequestId());
//        TODO: This part has been commented out to ignite setting type whenever new request or updated one has been transferred from Portal
//        if (citizenVTO.getType() != null) {
//            if (CardRequestType.toType(citizenVTO.getType()) == null) {
//                throw new BaseException(MapperExceptionCode.CRM_002, MapperExceptionCode.CRM_002_MSG);
//            }
//            crq.setType(CardRequestType.toType(citizenVTO.getType()));
//            if (citizenVTO.getType().equals(4L)) {
//                crq.setReason("DESTROYED");
//            } else if (citizenVTO.getType().equals(5L)) {
//                crq.setReason("IDENTITY_CHANGED");
//            }
//        }
        if (citizenVTO.getEnrolledDate() != null) {
            crq.setPortalEnrolledDate(citizenVTO.getEnrolledDate().toGregorianCalendar().getTime());
        }
        if (citizenVTO.getLastModifiedDate() != null)
            crq.setPortalLastModifiedDate(citizenVTO.getLastModifiedDate().toGregorianCalendar().getTime());

        if (citizenVTO.getState() != null) {
            if (CardRequestState.toState(citizenVTO.getState()) == null) {
                throw new BaseException(MapperExceptionCode.CRM_003, MapperExceptionCode.CRM_003_MSG);
            }
            crq.setState(CardRequestState.toState(citizenVTO.getState()));
        }
        crq.setTrackingID(citizenVTO.getTrackingId());

        czi.setFirstNameEnglish(citizenVTO.getFirstNameEN());
        czi.setSurnameEnglish(citizenVTO.getSureNameEN());
        czi.setBirthCertificateId(citizenVTO.getBirthCertId());
        try {
            czi.setBirthDateGregorian(DateUtil.convert(citizenVTO.getBirthDate(), DateUtil.JALALI));
        } catch (DateFormatException e) {
            throw new BaseException(MapperExceptionCode.CRM_004, MapperExceptionCode.GLB_001_MSG, e);
        }
        czi.setBirthDateSolar(citizenVTO.getBirthDate());
        czi.setBirthDateLunar(citizenVTO.getBirthDateHijri());
        if (citizenVTO.getGender() != null) {
            try {
                czi.setGender(Gender.valueOf(citizenVTO.getGender()));
            } catch (IllegalArgumentException e) {
                throw new BaseException(MapperExceptionCode.CRM_005, MapperExceptionCode.CRM_005_MSG, e, new String[]{citizenVTO.getGender()});
            }
        }
        if (citizenVTO.getReligionId() != null) {
            czi.setReligion(new ReligionTO(citizenVTO.getReligionId()));
        }else {
            czi.setReligion(new ReligionTO(Long.valueOf(ReligionEnum.ISLAM.getCode())));
        }
        czi.setMobile(citizenVTO.getCellNo());

        czi.setBirthCertificateIssuancePlace(citizenVTO.getBirthCertPlace());

        if (citizenVTO.getLivingPrvId() != null) {
            czi.setLiving(new LocationTO(citizenVTO.getLivingPrvId()));
        }
        if (citizenVTO.getLivingCityId() != null) {
            czi.setLivingCity(new LocationTO(citizenVTO.getLivingCityId()));
        }

        czi.setBirthCertificateSeries(citizenVTO.getBirthCertSerial());
        //czi.setBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
        czi.setFatherFirstNamePersian(citizenVTO.getFatherFirstNameFA());
        czi.setFatherFirstNameEnglish(citizenVTO.getFatherFirstNameEN());
        czi.setFatherFatherName(citizenVTO.getFatherFatherName());
        czi.setFatherSurname(citizenVTO.getFatherSureName());
        czi.setFatherNationalID(citizenVTO.getFatherNationalId());
        try {
            if (!"NA".equals(citizenVTO.getFatherBirthDate()))
                czi.setFatherBirthDateSolar(DateUtil.convert(citizenVTO.getFatherBirthDate(), DateUtil.JALALI));
            else
                czi.setFatherBirthDateSolar(DateUtil.convert("1080/01/01", DateUtil.JALALI));
        } catch (DateFormatException e) {
            throw new BaseException(MapperExceptionCode.CRM_006, MapperExceptionCode.GLB_001_MSG, e);
        }
        czi.setFatherBirthCertificateId(citizenVTO.getFatherBirthCertId());
//        czi.setFatherBirthCertificateSeries(citizenVTO.getFatherBirthCertSerial());
        czi.setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
        czi.setMotherFirstNamePersian(citizenVTO.getMotherFirstNameFA());
        czi.setMotherSurname(citizenVTO.getMotherSureName());
        czi.setMotherNationalID(citizenVTO.getMotherNationalId());
        if (citizenVTO.getMotherBirthDate() != null) {
            try {
                if (!"NA".equals(citizenVTO.getMotherBirthDate()))
                    czi.setMotherBirthDateSolar(DateUtil.convert(citizenVTO.getMotherBirthDate(), DateUtil.JALALI));
                else
                    czi.setMotherBirthDateSolar(DateUtil.convert("1080/01/01", DateUtil.JALALI));
            } catch (DateFormatException e) {
                throw new BaseException(MapperExceptionCode.CRM_007, MapperExceptionCode.GLB_001_MSG, e);
            }
        }
        czi.setMotherFatherName(citizenVTO.getMotherFatherName());
        czi.setMotherBirthCertificateId(citizenVTO.getMotherBirthCertId());
//        czi.setMotherBirthCertificateSeries(citizenVTO.getMotherBirthCertSerial());
        czi.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
        czi.setAddress(citizenVTO.getAddress());

        List<SpouseVTO> spouseVTOList = citizenVTO.getSpouses();
        if (spouseVTOList != null && !spouseVTOList.isEmpty()) {
            for (SpouseVTO spouse : citizenVTO.getSpouses()) {
                sps = SpouseMapper.convert(spouse);
                sps.setCitizenInfo(czi);
                czi.getSpouses().add(sps);
            }
        }

        List<ChildVTO> childVTOList = citizenVTO.getChildren();
        if (childVTOList != null && !childVTOList.isEmpty()) {
            for (ChildVTO child : citizenVTO.getChildren()) {
                chi = ChildMapper.convert(child);
                chi.setCitizenInfo(czi);
                czi.getChildren().add(chi);
            }
        }

        ctz.setCitizenInfo(czi);
        czi.setCitizen(ctz);
        crq.setCitizen(ctz);
        return crq;
    }

    public static com.gam.nocr.ems.biz.service.external.client.portal.CitizenWTO convertToCitizenWTO(CardRequestTO cardRequestTO) throws BaseException {
        com.gam.nocr.ems.biz.service.external.client.portal.CitizenWTO citizenWTO = new com.gam.nocr.ems.biz.service.external.client.portal.CitizenWTO();

        if (cardRequestTO.getCitizen() != null) {
            citizenWTO.setFirstNameFA(cardRequestTO.getCitizen().getFirstNamePersian());
            citizenWTO.setSureNameFA(cardRequestTO.getCitizen().getSurnamePersian());
            citizenWTO.setNationalId(cardRequestTO.getCitizen().getNationalID());
            if (cardRequestTO.getCitizen().getCitizenInfo() != null) {
                CitizenInfoTO citizenInfoTO = cardRequestTO.getCitizen().getCitizenInfo();
                citizenWTO.setFirstNameEN(citizenInfoTO.getFirstNameEnglish());
                citizenWTO.setSureNameEN(citizenInfoTO.getSurnameEnglish());
                citizenWTO.setBirthCertId(citizenInfoTO.getBirthCertificateId());
                citizenWTO.setBirthCertPlace(citizenInfoTO.getBirthCertificateIssuancePlace());
                if (citizenInfoTO.getBirthCertificateIssuancePlaceProvince() != null) {
                    citizenWTO.setBirthCertPrvId(citizenInfoTO.getBirthCertificateIssuancePlaceProvince().getId());
                }
//				TODO : Check it on future to appreciate Whether the matching bellow is correct or not!!!!!!!!!!
                citizenWTO.setBirthCertPrvName(citizenInfoTO.getBirthCertificateIssuancePlace());
                citizenWTO.setBirthDate(citizenInfoTO.getBirthDateSolar());
                citizenWTO.setBirthDateHijri(citizenInfoTO.getBirthDateLunar());
                citizenWTO.setBirthCertSerial(citizenInfoTO.getBirthCertificateSeries());

                citizenWTO.setLivingPrvId(citizenInfoTO.getLiving().getId());
                citizenWTO.setLivingPrvName(citizenInfoTO.getLiving().getName());
                citizenWTO.setLivingCityId(citizenInfoTO.getLivingCity().getId());
                citizenWTO.setLivingCityName(citizenInfoTO.getLivingCity().getName());
                citizenWTO.setAddress(citizenInfoTO.getAddress());
                citizenWTO.setEmail(citizenInfoTO.getEmail());

                citizenWTO.setFatherBirthCertId(citizenInfoTO.getFatherBirthCertificateId());
                citizenWTO.setFatherBirthCertSerial(citizenInfoTO.getFatherBirthCertificateSeries());
                citizenWTO.setFatherBirthDate(DateUtil.convert(citizenInfoTO.getFatherBirthDateSolar(), DateUtil.JALALI));
                citizenWTO.setFatherFirstNameFA(citizenInfoTO.getFatherFirstNamePersian());
                citizenWTO.setFatherFirstNameEN(citizenInfoTO.getFatherFirstNameEnglish());
                citizenWTO.setFatherSureName(citizenInfoTO.getFatherSurname());
                citizenWTO.setFatherNationalId(citizenInfoTO.getFatherNationalID());
                citizenWTO.setFatherFatherName(citizenInfoTO.getFatherFatherName());

                citizenWTO.setMotherBirthCertId(citizenInfoTO.getMotherBirthCertificateId());
                citizenWTO.setMotherBirthCertSerial(citizenInfoTO.getMotherBirthCertificateSeries());
                citizenWTO.setMotherBirthDate(DateUtil.convert(citizenInfoTO.getMotherBirthDateSolar(), DateUtil.JALALI));
                citizenWTO.setMotherFirstNameFA(citizenInfoTO.getMotherFirstNamePersian());
//					citizenWTO.setMotherFirstNameEN(citizenInfoTO.getMother`);
                citizenWTO.setMotherSureName(citizenInfoTO.getMotherSurname());
                citizenWTO.setMotherNationalId(citizenInfoTO.getMotherNationalID());
                citizenWTO.setMotherFatherName(citizenInfoTO.getMotherFatherName());

                citizenWTO.setGender(citizenInfoTO.getGender().name());
                citizenWTO.setReligionId(citizenInfoTO.getReligion().getId());

                if (EmsUtil.checkListSize(citizenInfoTO.getSpouses())) {
                    for (SpouseTO spouseTO : citizenInfoTO.getSpouses()) {
                        spouseTO.setId(null);
                        citizenWTO.getSpouses().add(SpouseMapper.convertToSpouseVTO(spouseTO));
                    }
                }

                if (EmsUtil.checkListSize(citizenInfoTO.getChildren())) {
                    for (ChildTO childTO : citizenInfoTO.getChildren()) {
                        childTO.setId(null);
                        citizenWTO.getChildren().add(ChildMapper.convertToChildVTO(childTO));
                    }
                }
            }
        }
//		if(cardRequestTO.getId() != null){
//			citizenWTO.setRequestId(cardRequestTO.getId());
//		}

        if (cardRequestTO.getState() != null) {
            citizenWTO.setState(CardRequestState.toLong(cardRequestTO.getState()));
        }
        if (cardRequestTO.getEnrolledDate() != null) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(cardRequestTO.getEnrolledDate());
            XMLGregorianCalendar xmlGregorianCalendar = null;
            try {
                xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            } catch (DatatypeConfigurationException e) {
                logger.error(DataExceptionCode.GLB_ERR_MSG, e);
            }
            citizenWTO.setEnrolledDate(xmlGregorianCalendar);
        }
        if (cardRequestTO.getType() != null) {
            citizenWTO.setType(CardRequestType.toLong(cardRequestTO.getType()));
        }

        citizenWTO.getMetadata().add(cardRequestTO.getMetadata());

        citizenWTO.setTrackingId(cardRequestTO.getTrackingID());

        return citizenWTO;
    }

    
    //Anbari convert citizenWRO to cardRequestTO (for preRegistration VIP in CCOS)
   	public static CardRequestTO convertVIPRequest(CitizenWTO wto) throws BaseException, DateFormatException {
   		
   		if (wto == null) {
            throw new BaseException(MapperExceptionCode.CRM_015, MapperExceptionCode.CRM_008_MSG);
        }


        CardRequestTO crq = new CardRequestTO();
        CitizenTO ctz = new CitizenTO();
        CitizenInfoTO czi = new CitizenInfoTO();
        SpouseTO sps;
        ChildTO chi;

        ctz.setFirstNamePersian(ConstValues.DEFAULT_NAMES_FA);
        ctz.setSurnamePersian(ConstValues.DEFAULT_NAMES_FA);
        ctz.setNationalID(wto.getNationalId());

        
        czi.setFirstNameEnglish(ConstValues.DEFAULT_NAMES_EN);
        czi.setSurnameEnglish(ConstValues.DEFAULT_NAMES_EN);
        czi.setBirthCertificateId(ConstValues.DEFAULT_NUMBER);
        czi.setBirthDateGregorian(wto.getBirthDate());
        czi.setBirthDateLunar(wto.getBirthDateHijri());//15
        if (wto.getGender() != null) {
            try {
                czi.setGender(Gender.valueOf(wto.getGender()));
            } catch (IllegalArgumentException e) {
                throw new BaseException(MapperExceptionCode.CRM_016, MapperExceptionCode.CRM_005_MSG, e, new String[]{wto.getGender()});
            }
        }
        if (wto.getReligionId() != null) {
            czi.setReligion(new ReligionTO(wto.getReligionId()));
        }
        else {
            czi.setReligion(new ReligionTO(Long.valueOf(ReligionEnum.ISLAM.getCode())));
        }
        czi.setPostcode(wto.getPostCode());
        czi.setEmail(ConstValues.DEFAULT_NAMES_EN);
        czi.setBirthCertificateIssuancePlace(wto.getBirthCertPrvName());
        
        czi.setAddress(wto.getAddress());
        czi.setPhone(EmsUtil.checkString(wto.getPhone()) ? wto.getPhone() : ConstValues.DEFAULT_NUMBER);//40
        czi.setMobile(wto.getMobile());
      
        //************************* For 5 Layer Location
        if(wto.getUserCityType() == null)
        	
        	throw new BaseException(MapperExceptionCode.CRM_018, MapperExceptionCode.CRM_018_MSG);
        if(wto.getUserCityType().equals("2")) //is vip
        {
        	czi.setLiving(null);
        	czi.setLivingCity(null);
        }
        else {
        	
        	if (wto.getLivingPrvId() == null || wto.getLivingPrvId()==0) 
        		
        		throw new BaseException(MapperExceptionCode.CRM_017, MapperExceptionCode.CRM_017_MSG);
        	
        	czi.setLiving(new LocationTO(wto.getLivingPrvId()));
        	
        	// 1 -----> shahr
        	// 0 ------> dehestan
        	if (wto.getUserCityType().equals("1")) {
        		if (wto.getLivingCityId() == null || wto.getLivingCityId()==0 ) 
        			
        			throw new BaseException(MapperExceptionCode.CRM_019, MapperExceptionCode.CRM_019_MSG);
        		czi.setLivingCity(new LocationTO(wto.getLivingCityId()));
        		
        		
        	} else if (wto.getUserCityType().equals("0")) {
        		if (wto.getLivingVillageId() == null || wto.getLivingVillageId()==0) 
        			
        			throw new BaseException(MapperExceptionCode.CRM_020, MapperExceptionCode.CRM_020_MSG);
        		czi.setLivingCity(new LocationTO(wto.getLivingVillageId()));
        		
        	}
        }
        //****************************   
        czi.setBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
        
        //father
        czi.setFatherFirstNamePersian(EmsUtil.checkString(wto.getFatherFirstNameFA()) ? wto.getFatherFirstNameFA() : ConstValues.DEFAULT_NAMES_FA);
        czi.setFatherFirstNameEnglish(ConstValues.DEFAULT_NAMES_EN);//25
        czi.setFatherFatherName(ConstValues.DEFAULT_NAMES_FA);
        czi.setFatherSurname(ConstValues.DEFAULT_NAMES_FA);
        czi.setFatherNationalID(EmsUtil.checkString(wto.getFatherNationalId()) ? wto.getFatherNationalId() : ConstValues.DEFAULT_NID);
        czi.setFatherBirthDateSolar(DateUtil.convert(ConstValues.DEFAULT_DATE,DateUtil.JALALI));
        czi.setFatherBirthCertificateId(EmsUtil.checkString(wto.getFatherBirthCertId()) ? wto.getFatherBirthCertId() : ConstValues.DEFAULT_NUMBER);//30
        czi.setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
        
        //mother
        czi.setMotherFirstNamePersian(wto.getMotherFirstNameFA());
        czi.setMotherSurname(ConstValues.DEFAULT_NAMES_FA);
        czi.setMotherNationalID(EmsUtil.checkString(wto.getMotherNationalId()) ? wto.getMotherNationalId() : ConstValues.DEFAULT_NID);
        czi.setMotherBirthDateSolar(DateUtil.convert(ConstValues.DEFAULT_DATE,DateUtil.JALALI));//35
        czi.setMotherFatherName(ConstValues.DEFAULT_NAMES_FA);
        czi.setMotherBirthCertificateId(EmsUtil.checkString(wto.getMotherBirthCertId()) ? wto.getMotherBirthCertId() : ConstValues.DEFAULT_NUMBER);
        czi.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);

        if (wto.getSpouses() != null) {
            for (SpouseWTO spouse : wto.getSpouses()) {
                sps = SpouseMapper.convert(spouse);
                sps.setCitizenInfo(czi);
                czi.getSpouses().add(sps);
            }
        }

        if (wto.getChildren() != null) {
            for (ChildrenWTO child : wto.getChildren()) {
                chi = ChildMapper.convert(child);
                chi.setCitizenInfo(czi);
                czi.getChildren().add(chi);
            }
        }
        
        ctz.setCitizenInfo(czi);
        czi.setCitizen(ctz);
        crq.setCitizen(ctz);
        
        return crq;
   	}
/**
 * this method is used to set geo serial base on living city
 * @author ganjyar
 * @param czi
 * @param wto
 */
	private static void setGeo(CitizenInfoTO czi, CitizenWTO wto) {
		//ostan
        if (czi.getLiving() != null) {
            wto.setLivingPrvName(czi.getLiving().getName());
            wto.setLivingPrvId(czi.getLiving().getId());
        }
        if(czi.getLivingCity()!=null){
       //shahrestan 
        String countyName = null;
        if (czi.getLivingCity().getCounty() != null) {
        	countyName = czi.getLivingCity().getCounty().getName();
        	wto.setLivingStateName(countyName);//20
        	wto.setLivingStateId(czi.getLivingCity().getCounty().getId());
        }
        //bakhsh
        if (czi.getLivingCity().getTownship() != null) {
        	wto.setLivingSectorName(czi.getLivingCity().getTownship().getName());
        	wto.setLivingSectorId(czi.getLivingCity().getTownship().getId());
        }
      
        	// 1 -----> shahr
            // 0 ------> dehestan
        	if( czi.getLivingCity().getType().equals("1"))
        	{
        		//shahr
        		wto.setLivingCityName(czi.getLivingCity().getName());
        		wto.setLivingCityId(czi.getLivingCity().getId());
        		wto.setUserCityType("1");
        	}
        	else if( czi.getLivingCity().getType().equals("2"))
        	{
        		//dehestan
        		wto.setLivingCityName(czi.getLivingCity().getDistrict().getName());//20
        		wto.setLivingCityId(czi.getLivingCity().getDistrict().getId());
        		//rusta
        		wto.setLivingVillageName(czi.getLivingCity().getName());
        		wto.setLivingVillageId(czi.getLivingCity().getId());
        		wto.setUserCityType("0");
        	}

        }
    }
}

