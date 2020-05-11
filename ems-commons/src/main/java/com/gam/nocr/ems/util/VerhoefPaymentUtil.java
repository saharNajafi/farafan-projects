package com.gam.nocr.ems.util;

public class VerhoefPaymentUtil {

    public static String generate(String num, String paymentAmount) throws Exception {
        if (!isValidNumber(num)) {
            throw new Exception("it's not valid number:" + num);
        }

        String firstVerhoeff = VerhoefUtil.GenerateVerhoeffDigit(num + org.apache.commons.lang3.StringUtils.leftPad(paymentAmount, 15, "0"));
        String secondVerhoeff = VerhoefUtil.GenerateVerhoeffDigit(reverseString(num)+reverseString(org.apache.commons.lang3.StringUtils.leftPad(paymentAmount, 15, "0")));
        return firstVerhoeff + secondVerhoeff;
    }

    private static String reverseString(String input) {
        return new StringBuilder(input).reverse().toString();
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
                result = VerhoefPaymentUtil.generate(String.valueOf(b));
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
