package com.gam.nocr.ems.data.mapper.tomapper;

import com.gam.nocr.ems.data.domain.CitizenInfoTO;
import com.gam.nocr.ems.data.domain.vol.CitizenInfoVTO;
import com.gam.nocr.ems.data.enums.ReligionEnum;

/**
 * Created by hossein on 12/21/2015.
 */
public class CitizenInfoMapper {

    public CitizenInfoMapper(){

    }

    public static CitizenInfoVTO convert(CitizenInfoTO to)
    {
        CitizenInfoVTO vto=new CitizenInfoVTO();
        if (to.getCitizen()!=null) {
            vto.setCitizenFirstName(to.getCitizen().getFirstNamePersian());
            vto.setCitizenNId(to.getCitizen().getNationalID());
            vto.setCitizenSurname(to.getCitizen().getSurnamePersian());
        }
        vto.setCitizenBirthDate(to.getBirthDateGregorian());
        vto.setCitizenBirthDateSOL(to.getBirthDateSolar());
        vto.setCitizenBirthDateLUN(to.getBirthDateLunar());
        vto.setGender(to.getGender());
        vto.setPostalCode(to.getPostcode());
        if (to.getLiving()!=null) {
            vto.setLivingProvinceName(to.getLiving().getName());
        }
        if (to.getLivingCity()!=null) {
            vto.setLivingCityName(to.getLivingCity().getName());
        }
        if (to.getReligion()!=null) {
            vto.setReligion(to.getReligion().getName());
        }else {
            vto.setReligion(ReligionEnum.ISLAM.getCode());
        }
        vto.setAddress(to.getAddress());
        vto.setPhone(to.getPhone());
        vto.setMobile(to.getMobile());
        vto.setBirthCertiIssuancePlace(to.getBirthCertificateIssuancePlace());

        vto.setFatherName(to.getFatherFirstNamePersian());
        vto.setFatherNID(to.getFatherNationalID());
        vto.setFatherBirthCertID(to.getFatherBirthCertificateId());

        vto.setMotherName(to.getMotherFirstNamePersian());
        vto.setMotherNID(to.getMotherNationalID());
        vto.setMotherBirthCertID(to.getMotherBirthCertificateId());
        return  vto;
    }
}
