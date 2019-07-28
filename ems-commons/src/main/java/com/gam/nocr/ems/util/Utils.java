package com.gam.nocr.ems.util;


import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Array;
import java.security.spec.KeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Pattern;


public class Utils {

    public static String FARSI_CHARS = ":اآبپتثجچحخدذرزژسشصضطظعغفقكگلمنوؤهيئءةأللـه";
    public static String LATIN_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    //      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.-/><*";
    public static String FINGER_PRINT_FARSI_CHARS =
            ":اآبپتثجچحخدذرزژسشصضطظعغفقكگلمنوؤهيئءةأللـه";
    public static String NUMBERS = "0123456789";
    public static String FARSI_NOT_JOIN_CHARS = "اآدذرزژوءةأؤ";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    public static final String ENCRYPTION_KEY =
            "ASDF hgty 2970 5298 jklasdf L9pw3O";

    private static KeySpec keySpec;
    private static SecretKeyFactory keyFactory;
    private static Cipher cipher;

    private static final String UNICODE_FORMAT = "UTF8";

    public Utils() {
    }


    public static boolean isValidNumber(String str, int maxLen) {
        if (str == null || (str.length() == 0 || str.length() > maxLen)) {
            return false;
        } else {
            return str.matches("[0-9]*");
        }
    }

    public static boolean isValidNotZeroNumber(String str, int maxLen) {
        if (isValidNumber(str, maxLen)) {
            return !str.matches("0*");
        } else {
            return false;
        }
    }

    public static boolean isValidDesc(String str, int minLen) {
        str = str.trim();
        if (str.length() < minLen) {
            return false;
        } else if (str.equalsIgnoreCase("null")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidNotZeroNumber(String str, int minLen,
                                               int maxLen) {
        if (isValidNumber(str, maxLen)) {
            if (str.length() < minLen) {
                return false;
            } else {
                return !str.matches("0*");
            }
        } else {
            return false;
        }
    }

    public static boolean isValidZipCode(String str) {
        if (isValidNotZeroNumber(str, 10, 10)) {
            return str.matches("[0123456789]*");
        } else {
            return false;
        }
    }

    public static boolean isValidSeri(String seri) {
        return seri.matches("[" + FARSI_CHARS + NUMBERS + "]*");
    }

    public static boolean isValidShenasnameSeri(String seri) {
        return (seri.matches("[" + FARSI_CHARS + "]*") && seri.length() > 1);
    }

    public static boolean isValidUserId(String str) {
        return str.matches("[" + LATIN_CHARS + NUMBERS + "]*");
    }

    public static String genNinCheckDigit(String str) throws Exception {

        if (!isValidNotZeroNumber(str, 7, 9)) {
            throw new Exception("errInvalidNin");
        }

        str = strRight("00".concat(str), 9);

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Integer.parseInt(strMid(str, i, 1)) * (10 - i);
        }

        int checkDigit = sum % 11;
        if (checkDigit > 1) {
            checkDigit = 11 - checkDigit;
        }
        return Integer.toString(checkDigit);
    }

    public static boolean isValidNin(String str) {
        try {
            if (!isValidNotZeroNumber(str, 8, 10)) {
                return false;
            }

            if (genNinCheckDigit(strLeft(str, str.length() - 1)).equalsIgnoreCase(
                    strRight(str, 1))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public static int genReqNoCheckDigit(String str) throws Exception {

        if (!isValidNotZeroNumber(str, 15)) {
            throw new Exception("errInvalidReqNo");
        }

        str = (strRight("00000000000000".concat(str), 14));

        int sum = 0;
        sum += Integer.parseInt(strMid(str, 0, 1)) * 3;
        sum += Integer.parseInt(strMid(str, 1, 1));
        sum += Integer.parseInt(strMid(str, 2, 1)) * 3;
        sum += Integer.parseInt(strMid(str, 3, 1));
        sum += Integer.parseInt(strMid(str, 4, 1)) * 3;
        sum += Integer.parseInt(strMid(str, 5, 1));
        sum += Integer.parseInt(strMid(str, 6, 1)) * 3;
        sum += Integer.parseInt(strMid(str, 7, 1));
        sum += Integer.parseInt(strMid(str, 8, 1)) * 3;
        sum += Integer.parseInt(strMid(str, 9, 1));
        sum += Integer.parseInt(strMid(str, 10, 1)) * 3;
        sum += Integer.parseInt(strMid(str, 11, 1));
        sum += Integer.parseInt(strMid(str, 12, 1)) * 3;
        sum += Integer.parseInt(strMid(str, 13, 1));

        int checkDigit = (10 - (sum % 10)) % 10;
        return checkDigit;
    }

    public static boolean isValidReqNo(String str) {
        try {
            if (!isValidNotZeroNumber(str, 15) || str.length() < 11) {
                return false;
            }

            if (genReqNoCheckDigit(strLeft(str, str.length() - 1)) !=
                    Integer.parseInt(strRight(str, 1))) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public static String strLeft(String str, int len) {
        if (str == null) {
            return null;
        }

        if (len <= 0) {
            return "";
        }

        if (str.length() <= len) {
            return str;
        } else {
            return str.substring(0, len);
        }
    }

    public static String strRight(String str, int len) {
        if (str == null) {
            return null;
        }

        if (len <= 0) {
            return "";
        }

        if (str.length() <= len) {
            return str;
        } else {
            return str.substring(str.length() - len, str.length());
        }
    }

    public static String strMid(String str, int pos) {
        if (str == null) {
            return null;
        }

        if (pos < 0 || pos >= str.length()) {
            return "";
        }
        return str.substring(pos, str.length());
    }

    public static String strMid(String str, int pos, int len) {
        if (str == null) {
            return null;
        }

        if (pos < 0 || pos >= str.length() || len <= 0) {
            return "";
        }

        if (str.length() <= (pos + len)) {
            return strMid(str, pos);
        } else {
            return str.substring(pos, pos + len);
        }
    }

    public static String strAncrMid(String str, int pos, int len) {
        if (str == null) {
            return null;
        }

        if (pos < 0 || pos >= str.length() || len <= 0) {
            return " ";
        }

        if (str.length() <= (pos + len)) {
            return strMid(str, pos);
        } else {
            return str.substring(pos, pos + len);
        }
    }

    public static String nvl(String val) {
        if (val == null) {
            return "";
        } else {
            return val;
        }
    }

    public static String charVal(String str) {
        if (str == null) {
            return "";
        }

        String val = trimConvert(str);
        val = val.replaceAll("[^" + FARSI_CHARS + "]", " ");
        val = removeWithSpaces(val);
        val = ReplaceSpecialCharacter(val);
        return val;
    }

    public static String charVal2(String str) {
        if (str == null) {
            return "";
        }

        String val = trimConvert(str);
        val = removeWithSpaces(val);
        return val;
    }

    public static String charVal3(String str) {
        String val = removeWithSpaces(str);

        if (!(val.matches("[1234567890]*"))) {
            val = "";
        }
        return val;
    }

    public static String ReplaceSpecialCharacter(String str) {
        if (str == null) {
            return "";
        }
        int len;
        int i = 0;
        len = str.length();
        String val = "";
        while (i <= len) {
            if (strMid(str, i, 1).equalsIgnoreCase(":")) {
                /*"cast Null_Space to Space"*/
                //if (strMid(str, i - 1, 1).equals("‌")) {
                if (strMid(str, i - 1, 1).equals(" ")) {
                    /*"cast Null_Space to Space"*/
                    val = strMid(val, 0, val.length() - 2) + "ة";
                } else {
                    val = strMid(val, 0, val.length() - 1) + "ة";
                }
                i = i + 1;
            } else {
                val = val + strMid(str, i, 1);
                i = i + 1;
            }
        }

        return val;
    }

    public static String removeWithSpaces(String str) {
        if (str == null) {
            return "";
        }

        String val = str.trim();
        int len;

        do {
            len = val.length();
            val = val.replaceAll("\\s\\s", " ");
        }
        while (len > val.length());
        /*"cast Null_Space to Space" */
        //val = val.replaceAll("\\s", "‌"); // "" is not empty, it contains nulSpace*/
        /*"cast Null_Space to Space" */
        for (int i = 0; i < FARSI_NOT_JOIN_CHARS.length(); i++) {
            String ch = Utils.strMid(FARSI_NOT_JOIN_CHARS, i, 1);
            /*"cast Null_Space to Space" */
            val = val.replaceAll(ch + "\\s", ch);
            /*"cast Null_Space to Space" */
        }
        return val;

    }

    public static String trimConvert(String str) {
        if (str == null)
            return null;
        str = str.trim();
        str = str.replaceAll("&#1740;", "ي");
        str = str.replace('ى', 'ي');
        str = str.replace('ی', 'ي');
        str = str.replace('ک', 'ك');


        return str;
    }

    public static String getRandomId() {
        int index;
        char ch;
        String randomId = "";
        char[] chArr = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        char IdArr[] = new char[4];
        Random random = new Random();
        index = Math.abs(random.nextInt()) % 35 + 1;
        ch = Array.getChar(chArr, index);
        for (int i = 0; i < 4; i++) {
            Array.setChar(IdArr, i, ch);
            index = Math.abs(random.nextInt()) % 36;
            ch = Array.getChar(chArr, index);
        }
        for (int i = 0; i < 4; i++) {
            randomId += IdArr[i];
        }
        return randomId;
    }


    public static String strLeftTrim(String str, char specifiedChar) {
        if (str == null) {
            return null;
        }
        int i = 0;
        for (i = 0; i < str.length(); i++) {
            if (str.charAt(i) != specifiedChar) {
                break;
            }
        }
        return str.substring(i);
    }

    public static boolean isValidNotZeroNumberLeftZeroTrimed(String str,
                                                             int maxLen) {
        if (isValidNumber(strLeftTrim(str, '0'), maxLen)) {
            return !str.matches("0*");
        } else {
            return false;
        }
    }

    public static boolean isMached(String str) {
        return str.matches("[" + LATIN_CHARS + "]*");
    }

    private static boolean IsLatinChar(String sText, int lPos) {
        boolean result;
        String sValue = "";
        char mad = '~';
        int imad = (int) mad;
        if (lPos < 0) {
            result = false;
        }
        sValue = strMid(sText, lPos, 1);
        int i = 0;
        if (sValue.length() != 0) {
            i = (int) sValue.charAt(0);
        }

        if (isMached(sValue)) {
            result = true;
        } else if (i <= imad) {
            result = IsLatinChar(sText, lPos - 1);
        } else {
            result = false;
        }

        return result;

    }

    public static String ReArrangeString(String sValue) {
        String sResult = "";
        int lStart;
        int lEnd;
        boolean bState;
        boolean isFirstString = true;
        if (sValue.equals("")) {
            return sResult;
        }

        sValue = sValue.trim() + " ";
        lStart = sValue.length() - 1;

        lEnd = lStart;

        while (true) {
            bState = IsLatinChar(sValue, lEnd);
            while (IsLatinChar(sValue, lStart) == bState) {
                lStart = lStart - 1;
                if (lStart < 0) {
                    break;
                }
            }
            sResult = sResult + strMid(sValue, lStart + 1, lEnd - lStart);
            if (lStart < 0) {
                break;
            }
            lEnd = lStart;
        }

        return sResult.trim();
    }

    public static boolean isValidFoPersonalityNo(String str) {
        try {
            if (str.length() > 20 || str.contains(" ") || str.matches("[0]*"))
                return false;
            else
                return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isTimeBetweenTwoTime(String argStartTime,
                                               String argEndTime, String argCurrentTime) throws ParseException {
        boolean valid = false;
        // Start Time
        java.util.Date startTime = new SimpleDateFormat("HH:mm:ss")
                .parse(argStartTime);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);

        // Current Time
        java.util.Date currentTime = new SimpleDateFormat("HH:mm:ss")
                .parse(argCurrentTime);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentTime);

        // End Time
        java.util.Date endTime = new SimpleDateFormat("HH:mm:ss")
                .parse(argEndTime);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);

        //
        if (currentTime.compareTo(endTime) < 0) {

            currentCalendar.add(Calendar.DATE, 1);
            currentTime = currentCalendar.getTime();

        }

        if (startTime.compareTo(endTime) < 0) {

            startCalendar.add(Calendar.DATE, 1);
            startTime = startCalendar.getTime();

        }
        //
        if (currentTime.before(startTime)) {
            valid = false;
        } else {

            if (currentTime.after(endTime)) {
                endCalendar.add(Calendar.DATE, 1);
                endTime = endCalendar.getTime();

            }


            if (currentTime.before(endTime)) {
                valid = true;
            } else {
                valid = false;
            }

        }
        return valid;


    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hMacGeneratorSHA2(String signStr, byte[] key)
            throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] result = mac.doFinal(signStr.getBytes("UTF-8"));
        return result;
    }

    public static byte[] hMacGeneratorMD5(String signStr, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(secretKey);
        byte[] result = mac.doFinal(signStr.getBytes("UTF-8"));
        return result;
    }

    public static boolean isIPValid(final String ip) {
        Pattern pattern = Pattern.compile(
                "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        return pattern.matcher(ip).matches();
    }


}
