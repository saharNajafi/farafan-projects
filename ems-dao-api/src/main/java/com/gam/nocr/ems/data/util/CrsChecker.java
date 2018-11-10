package com.gam.nocr.ems.data.util;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.enums.GenderEnum;
import com.gam.nocr.ems.data.enums.ReligionEnum;
import com.gam.nocr.ems.util.Configuration;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.LangUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Saeid Rastak (saeid.rastak@gmail.com) on 10/24/2017.
 */
public class CrsChecker {

    public static void checkReligion(ReligionEnum religion) throws BaseException {
        if (religion == null) {
            throw new DataException(DataExceptionCode.REG_008, DataExceptionCode.REG_08_MSG);
        }
    }

    public static void checkGender(GenderEnum sex) throws BaseException {
        if (sex == null) {
            throw new DataException(DataExceptionCode.REG_009, DataExceptionCode.REG_09_MSG);
        }
    }

    public static void checkMobileNumber(String cellPhoneNumber) throws BaseException {
        if (!EmsUtil.checkString(cellPhoneNumber)) {
            throw new DataException(DataExceptionCode.REG_007, DataExceptionCode.REG_07_MSG);
        }
        cellPhoneNumber = LangUtil.getEnglishNumber(cellPhoneNumber);
        Pattern pattern = Pattern.compile(Configuration.getProperty("cell.no.constraint"));
        Matcher matcher = pattern.matcher(cellPhoneNumber);

        Pattern pattern2 = Pattern.compile(Configuration.getProperty("cell.no.constraint.length"));
        Matcher matcher2 = pattern2.matcher(cellPhoneNumber);

        if (!matcher.matches() || !matcher2.matches()) {
            throw new DataException(DataExceptionCode.REG_017, DataExceptionCode.REG_17_MSG);

        }
    }

    public static void checkLunarDate(String lunarDate) throws DataException {
        if (!EmsUtil.checkString(lunarDate) || lunarDate.length() != 10) {
            throw new DataException(DataExceptionCode.REG_012, DataExceptionCode.REG_12_MSG);
        }
    }

    public static void checkBirthDateGregorian(Date birthDateGregorian) throws DataException {
        if (birthDateGregorian == null) {
            throw new DataException(DataExceptionCode.REG_011, DataExceptionCode.REG_11_MSG);
        }
    }

    public static void checkBirthDateSolar(int birthDate) throws DataException {
        if (birthDate <= 0) {
            throw new DataException(DataExceptionCode.REG_018, DataExceptionCode.REG_18_MSG);
        }
    }

    public static void checkMotherName(String motherName) throws DataException {
        if (StringUtils.isEmpty(motherName) || motherName.trim().length() == 0) {
            throw new DataException(DataExceptionCode.REG_013, DataExceptionCode.REG_13_MSG);
        }
        Pattern pattern = Pattern.compile(Configuration.getProperty("persian.alpha.constraint"));
        Matcher matcher = pattern.matcher(motherName);
        if (motherName != null && !matcher.matches()) {
            throw new DataException(DataExceptionCode.REG_016, DataExceptionCode.REG_16_MSG);
        }
    }

    public static void checkSerialNumber(int serialNumber) throws DataException {
        String s = String.valueOf(serialNumber);
        Pattern pattern = Pattern.compile(Configuration.getProperty("birth.cert.serial"));
        Matcher matcher = pattern.matcher(s);
        if (s != null && !matcher.matches()) {
            throw new DataException(DataExceptionCode.REG_020, DataExceptionCode.REG_20_MSG);
        }
    }

}
