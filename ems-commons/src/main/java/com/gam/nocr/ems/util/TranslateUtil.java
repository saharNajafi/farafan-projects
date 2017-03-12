package com.gam.nocr.ems.util;

public class TranslateUtil {
	
	public static String translate(String word){
		word = word.replaceAll(" ", "");
//		word = word.replaceAll("ث", "س");
//		word = word.replaceAll("ص", "س");
//		word = word.replaceAll("ط", "ت");
//		word = word.replaceAll("ظ", "ز");
//		word = word.replaceAll("ض", "ز");
//		word = word.replaceAll("ذ", "ز");
//		word = word.replaceAll("ح", "ه");
		word = word.replaceAll("ة", "ه");
//		word = word.replaceAll("ع", "ا");
		//word = word.replaceAll("آ", "ا");
		word = word.replaceAll("أ", "ا");
		word = word.replaceAll("إ", "ا");
//		word = word.replaceAll("غ", "ق");
//		word = word.replaceAll("گ", "ک");
		word = word.replaceAll("ك", "ک");
		word = word.replaceAll("ی", "ي");
		word = word.replaceAll("ئ", "ي");
		word = word.replaceAll("ء", "ي");
		word = word.replaceAll("ؤ", "و");
		return word ;
	}

}
