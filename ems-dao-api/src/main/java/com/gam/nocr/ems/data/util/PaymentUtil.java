package com.gam.nocr.ems.data.util;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.ws.PaymentWTO;
import com.gam.nocr.ems.data.domain.ws.RegistrationPaymentWTO;
import com.gam.nocr.ems.data.domain.ws.SinglePreRegistrationWTO;
import com.gam.nocr.ems.data.enums.CardRequestOrigin;
import com.gam.nocr.ems.data.enums.GenderEnum;
import com.gam.nocr.ems.data.enums.IPGProviderEnum;
import com.gam.nocr.ems.util.CalendarUtil;
import com.gam.nocr.ems.util.Configuration;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.NationalIDUtil;
import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by safiary on 8/20/18.
 */
public class PaymentUtil {

    public static RegistrationPaymentTO convertToRegistrationPayment(
            PaymentWTO paymentWTO) throws BaseException {

        try {
            RegistrationPaymentTO registrationPaymentTO = new RegistrationPaymentTO();
            RegistrationPaymentWTO registrationPaymentWTO = paymentWTO.getRegistrationPaymentWTO();
            registrationPaymentTO.setAmountPaid(registrationPaymentWTO.getAmountPaied());
            registrationPaymentTO.setConfirmed(registrationPaymentWTO.isConfirmed());
            registrationPaymentTO.setSucceed(registrationPaymentWTO.isSucceed());
            registrationPaymentTO.setOrderId(registrationPaymentWTO.getOrderId());
            registrationPaymentTO.setPaymentDate(registrationPaymentWTO.getPaymentDate());
            registrationPaymentTO.setPaymentCode(registrationPaymentWTO.getPaymentCode());
            registrationPaymentTO.setPaidBank(registrationPaymentWTO.getPaiedBank());
            registrationPaymentTO.setSystemTraceNo(registrationPaymentWTO.getSystemTraceNo());
            registrationPaymentTO.setResCode(registrationPaymentWTO.getResCode());
            registrationPaymentTO.setRrn(registrationPaymentWTO.getRRN());
            registrationPaymentTO.setDescription(registrationPaymentWTO.getDescription());
            registrationPaymentTO.setMatchFlag((short) 1);
            return registrationPaymentTO;
        } catch (Exception e) {
            throw new BaseException(WebExceptionCode.CPW_016_MSG, WebExceptionCode.CPW_016, e);
        }
    }

    public static ReservationTO convertSingle(SinglePreRegistrationWTO singlePreRegistrationWTO) throws BaseException {
        try {
            checkSinglePreRegistrationTransfer(singlePreRegistrationWTO);
            CitizenTO ctz = new CitizenTO();
            CitizenInfoTO czi = new CitizenInfoTO();
            String doesNotExist = Configuration.getProperty("dont.exit");
            ctz.setFirstNamePersian(doesNotExist);
            ctz.setSurnamePersian(doesNotExist);
            ctz.setNationalID(StringUtils.leftPad(singlePreRegistrationWTO.getNationalId(), 10, "0"));
            CardRequestTO cardRequestTO = new CardRequestTO();
            cardRequestTO.setPortalEnrolledDate(new Date());
            //cardRequestTO.setTrackingID(NationalIDUtil.generateTrackingId(ctz.getNationalID()));
            cardRequestTO.setOrigin(CardRequestOrigin.valueOf(singlePreRegistrationWTO.getOrigin()));
            cardRequestTO.setPaid(Boolean.FALSE);
            String notValue = Configuration.getProperty("not.value");
            czi.setFirstNameEnglish(notValue);
            czi.setSurnameEnglish(notValue);
            czi.setBirthCertificateId(String.valueOf(singlePreRegistrationWTO.getCertSerialNo()));
            String birthDateSolar =
                    CalendarUtil.addSlashToPersianDate(
                            String.valueOf(singlePreRegistrationWTO.getBirthDateSolar()));
            czi.setBirthDateGregorian(DateUtil.convert(birthDateSolar, DateUtil.JALALI));
            czi.setBirthDateSolar(birthDateSolar);
            czi.setBirthDateLunar(singlePreRegistrationWTO.getBirthDateLunar());
            try {
                czi.setGender(GenderEnum.getEMSGender(singlePreRegistrationWTO.getGender()));
            } catch (IllegalArgumentException e) {
                throw new BaseException(MapperExceptionCode.CRM_005, MapperExceptionCode.CRM_005_MSG, e,
                        new String[]{singlePreRegistrationWTO.getGender().toString()});
            }
            czi.setReligion(new ReligionTO(Long.valueOf(singlePreRegistrationWTO.getReligion().getCode())));
            czi.setMobile(singlePreRegistrationWTO.getCellphoneNumber());
            czi.setBirthCertificateSeries(String.valueOf(singlePreRegistrationWTO.getCertSerialNo()));
            try {
                czi.setFatherBirthDateSolar(gampooya.tools.date.DateUtil.convert(ConstValues.DEFAULT_DATE, gampooya.tools.date.DateUtil.JALALI));
            } catch (DateFormatException e) {
                throw new BaseException(MapperExceptionCode.CRM_006, MapperExceptionCode.GLB_001_MSG, e);
            }
            czi.setFatherNationalID("0000000000");
            czi.setMotherNationalID("0000000000");
            czi.setMotherFirstNamePersian(singlePreRegistrationWTO.getMotherName());
            try {
                czi.setMotherBirthDateSolar(gampooya.tools.date.DateUtil.convert(ConstValues.DEFAULT_DATE, gampooya.tools.date.DateUtil.JALALI));
            } catch (DateFormatException e) {
                throw new BaseException(MapperExceptionCode.CRM_007, MapperExceptionCode.GLB_001_MSG, e);
            }
            czi.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
            RegistrationPaymentTO registrationPaymentTO = new RegistrationPaymentTO();
            registrationPaymentTO.setAmountPaid(0);
            registrationPaymentTO.setDescription("");
            registrationPaymentTO.setConfirmed(false);
            registrationPaymentTO.setSucceed(false);
            registrationPaymentTO.setOrderId(EmsUtil.getRandomPaymentOrderId());
            registrationPaymentTO.setPaymentCode(Configuration.getProperty("PAYMENT.FIRST.CARD.CODE"));
            registrationPaymentTO.setPaidBank(IPGProviderEnum.UNDEFINED);
            registrationPaymentTO.setResCode(null);
            registrationPaymentTO.setSystemTraceNo(null);
            registrationPaymentTO.setMatchFlag((short) 1);
            cardRequestTO.setRegistrationPaymentTO(registrationPaymentTO);
            cardRequestTO.setPaid(false);
            ReservationTO reservationTO = new ReservationTO();
            reservationTO.setPaidDate(null);
            reservationTO.setPaid(false);
            ctz.setCitizenInfo(czi);
            czi.setCitizen(ctz);
            cardRequestTO.setCitizen(ctz);
            reservationTO.setCardRequest(cardRequestTO);
            reservationTO.setEnrollmentOffice(new EnrollmentOfficeTO(singlePreRegistrationWTO.getEnrollmentOfficeId()));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date reservationDate = df.parse(df.format(new Date()) + "T00:00:00.000000");
            reservationTO.setDate(reservationDate);
            cardRequestTO.setReservationDate(reservationDate);
            return reservationTO;
        } catch (Exception e) {
            throw new BaseException(WebExceptionCode.CPW_017, WebExceptionCode.CPW_017_MSG, e);
        }
    }

    public static void checkSinglePreRegistrationTransfer(SinglePreRegistrationWTO singlePreRegistrationWTO) throws BaseException {
        if (singlePreRegistrationWTO == null) {
            throw new BaseException(MapperExceptionCode.CPM_001, MapperExceptionCode.CPM_001_MSG);
        }
        if (!NationalIDUtil.checkValidNinStr(singlePreRegistrationWTO.getNationalId())) {
            throw new BaseException(MapperExceptionCode.CPM_002, MapperExceptionCode.CPM_002_MSG, new String[]{String.valueOf(singlePreRegistrationWTO.getTrackingId())});
        }
        /*if (StringUtils.isEmpty(singlePreRegistrationWTO.getTrackingId())) {
            throw new BaseException(MapperExceptionCode.CRM_009, MapperExceptionCode.CRM_009_MSG);
        }*/
        if (StringUtils.isEmpty(singlePreRegistrationWTO.getOrigin())) {
            throw new BaseException(MapperExceptionCode.CPM_003, MapperExceptionCode.CPM_003_MSG, new Object[]{singlePreRegistrationWTO.getNationalId()});
        }
        if (!NationalIDUtil.checkValidCertSerialNo(String.valueOf(singlePreRegistrationWTO.getCertSerialNo()))) {
            throw new BaseException(MapperExceptionCode.CPM_004, MapperExceptionCode.CPM_004_MSG,
                    new Object[]{singlePreRegistrationWTO.getNationalId()});
        }
        if (StringUtils.isEmpty(singlePreRegistrationWTO.getCellphoneNumber())) {
            throw new BaseException(MapperExceptionCode.CPM_005, MapperExceptionCode.CPM_005_MSG,
                    new Object[]{singlePreRegistrationWTO.getNationalId()});
        }
        if (StringUtils.isEmpty(singlePreRegistrationWTO.getMotherName())) {
            throw new BaseException(MapperExceptionCode.CPM_006, MapperExceptionCode.CPM_006_MSG,
                    new Object[]{singlePreRegistrationWTO.getNationalId()});
        }
        if (singlePreRegistrationWTO.getBirthDateGregorian() == null) {
            throw new BaseException(MapperExceptionCode.CPM_007, MapperExceptionCode.CPM_007_MSG,
                    new Object[]{singlePreRegistrationWTO.getNationalId()});
        }
        if (StringUtils.isEmpty(singlePreRegistrationWTO.getBirthDateLunar())) {
            throw new BaseException(MapperExceptionCode.CPM_008, MapperExceptionCode.CPM_008_MSG,
                    new Object[]{singlePreRegistrationWTO.getNationalId()});
        }
        if (singlePreRegistrationWTO.getGender() == null) {
            throw new BaseException(MapperExceptionCode.CPM_009, MapperExceptionCode.CPM_009_MSG,
                    new Object[]{singlePreRegistrationWTO.getNationalId()});
        }
        if (singlePreRegistrationWTO.getReligion() == null) {
            throw new BaseException(MapperExceptionCode.CPM_010, MapperExceptionCode.CPM_010_MSG,
                    new Object[]{singlePreRegistrationWTO.getNationalId()});
        }
    }

}
