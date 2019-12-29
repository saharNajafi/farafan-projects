package com.gam.nocr.ems.util;

import java.math.BigInteger;
import java.util.Random;

public class VerhoeffForPaymentCode {

    public static String generate(String num) throws Exception {
        if (!isValidNumber(num)) {
            throw new Exception("it's not valid number:" + num);
        }

        String firstVerhoeff = Verhoeff.generateVerhoeff(num);
        String secondVerhoeff = Verhoeff.generateVerhoeff(reverseString(num));
        return firstVerhoeff + secondVerhoeff;
    }

    private static String reverseString(String input) {
        byte[] strAsByteArray = input.getBytes();

        byte[] result =
                new byte[strAsByteArray.length];

        for (int i = 0; i < strAsByteArray.length; i++)
            result[i] =
                    strAsByteArray[strAsByteArray.length - i - 1];

        return new String(result);
    }

    private static boolean isValidNumber(String str) {
        if (str == null || (str.length() == 0)) {
            return false;
        } else {
            return str.matches("[0-9]*");
        }
    }

    //for test:
    /*public static void main(String[] args) {
        boolean hasBug = false;
        String result;
        try {
            for (int i = 5000; i < 100000; i++) {
                BigInteger b = new BigInteger(i, new Random());
                result = VerhoeffForPaymentCode.generate(String.valueOf(b));
                if (result.length() != 2) {
                    hasBug = true;
                    break;
                }
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasBug = true;
        }
        if (hasBug) {
            System.err.println("errrrrrrrrrrrrrrrrrrrrrrrrrrrrrror");
            System.err.println("errrrrrrrrrrrrrrrrrrrrrrrrrrrrrror");
            System.err.println("errrrrrrrrrrrrrrrrrrrrrrrrrrrrrror");
        }
    }*/

}
