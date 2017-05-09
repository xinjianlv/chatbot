package com.wanda.chatbotv1.utils;
/**   
 * @Description: 
 * @author Fang Lei, jsfanglei@gmail.com   
 * @date 2012-7-21 上午11:34:14  
 */


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Pattern;


public class TextTool {
	public static HashSet<String> stopwords;
	public static HashSet<String> commaPuc;
	public static HashSet<String> endPuc;

	static {
		try {
			stopwords = new HashSet<String>();
			InputStream is = TextTool.class.getClassLoader()
					.getResourceAsStream("resource/stopwords.txt");
			BufferedReader fin = new BufferedReader(new InputStreamReader(is,
					"utf8"));
			String line = null;
			while ((line = fin.readLine()) != null) {
				stopwords.add(line);
			}

			commaPuc = new HashSet<String>(Arrays.asList(new String[] { ",",
					";", ":", "，", "；", "：" }));
			endPuc = new HashSet<String>(Arrays.asList(new String[] { ".", "!",
					"?", "。", "！", "？", "…" }));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static boolean IsChWord(String str) {
		if (str.length() == 0) {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			String bb = str.substring(i, i + 1);
			if (!Pattern.matches("[\u4E00-\u9FA5]", bb))
				return false;
		}
		return true;
	}

	public static boolean IsStopWord(String str) {
		if (stopwords.contains(str)) {
			return true;
		}
		return false;
	}
	/**
	 * @function 
	 * @param str
	 * @return
	 */
	public static boolean IsGoodWord(String str) {
		// return !IsStopWord(str) && IsChWord(str);
		return IsChWord(str);
	}


	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9.]*");
		return pattern.matcher(str).matches();
	}
	/**
	 * @function 获取字符串中的非符号、非标点的字符
	 * @param str 待判断字符串
	 * @return
	 */
	static public String containsSymbolOrPunc(String str) {
		char[] arr = str.toCharArray();
		char[] temp = new char[arr.length];
		int ndx = 0;
		for (char a : arr) {
			if (isSymbol(a) || isPunctuation(a))
				continue;
			temp[ndx++] = a;
		}
		return new String(temp);
	}
	/**
	 * @function 判断字符串中是否包含符号和标点
	 * @param str 待判断字符串
	 * @return
	 */
	public static boolean containsSymbolOrPunc1(String str) {
		char[] arr = str.toCharArray();
		for (char a : arr) {
			if (isSymbol(a) || isPunctuation(a))
				return true;
		}
		return false;
	}
	
	
	/**
	 * @function 判断是否是符号
	 * @param ch 待判断字符
	 * @return
	 */
	public static boolean isSymbol(char ch) {
		if (ch == 0xFFFD || ch == 0x002F)
			return true;
		if (ch == 0x3011 || ch == 0x0040)
			return true;
		if (ch == 0x25CE || ch == 0x25CF)
			return true;
		if (ch == 0xFFFD || ch == 0xFF1A)
			return true;
		if (ch == 0x25A0 || ch == 0x3010)
			return true;
	
			if (isCnSymbol(ch))
				return true;
		if (isEnSymbol(ch))
			return true;

		if (0x2010 <= ch && ch <= 0x2017)
			return true;
		if (0x2020 <= ch && ch <= 0x2027)
			return true;
		if (0x2B00 <= ch && ch <= 0x2BFF)
			return true;
		if (0xFF03 <= ch && ch <= 0xFF06)
			return true;
		if (0xFF08 <= ch && ch <= 0xFF0B)
			return true;
		if (ch == 0xFF0D || ch == 0xFF0F)
			return true;
		if (0xFF1C <= ch && ch <= 0xFF1E)
			return true;
		if (ch == 0xFF20 || ch == 0xFF65)
			return true;
		if (0xFF3B <= ch && ch <= 0xFF40)
			return true;
		if (0xFF5B <= ch && ch <= 0xFF60)
			return true;
		if (ch == 0xFF62 || ch == 0xFF63)
			return true;
		if (ch == 0x0032 || ch == 0x3000)
			return true;
		return false;

	}
	/**
	 * @function 判断是否是中文符号
	 * @param ch
	 * @return
	 */
	static boolean isCnSymbol(char ch) {
		if (0x3004 <= ch && ch <= 0x301C)
			return true;
		if (0x3020 <= ch && ch <= 0x303F)
			return true;
		return false;
	}
	/**
	 * @function 判断是否是英文符号
	 * @param ch 待判断字符
	 * @return
	 */
	static boolean isEnSymbol(char ch) {

		if (ch == 0x40)
			return true;
		if (ch == 0x2D || ch == 0x2F)
			return true;
		if (0x23 <= ch && ch <= 0x26)
			return true;
		if (0x28 <= ch && ch <= 0x2B)
			return true;
		if (0x3C <= ch && ch <= 0x3E)
			return true;
		if (0x5B <= ch && ch <= 0x60)
			return true;
		if (0x7B <= ch && ch <= 0x7E)
			return true;

		return false;
	}

	/**
	 * @function 判断是否是标点
	 * @param ch 待判断字符
	 * @return
	 */
	public static boolean isPunctuation(char ch) {		
		
		if (isCjkPunc(ch))
				return true;
		if (isEnPunc(ch))
			return true;

		if (0x2018 <= ch && ch <= 0x201F)
			return true;
		if (ch == 0xFF01 || ch == 0xFF02)
			return true;
		if (ch == 0xFF07 || ch == 0xFF0C)
			return true;
		if (ch == 0xFF1A || ch == 0xFF1B)
			return true;
		if (ch == 0xFF1F || ch == 0xFF61)
			return true;
		if (ch == 0xFF0E)
			return true;
		if (ch == 0xFF65)
			return true;

		return false;
	}
	/**
	 * @function 判断是否是英文标点
	 * @param ch 待判断字符
	 * @return
	 */
	static boolean isEnPunc(char ch) {
		if (0x21 <= ch && ch <= 0x22)
			return true;
		if (ch == 0x27 || ch == 0x2C)
			return true;
		if (ch == 0x2E || ch == 0x3A)
			return true;
		if (ch == 0x3B || ch == 0x3F)
			return true;

		return false;
	}
	/**
	 * @function 判断是否是阿拉伯数字
	 * @param ch 待判断字符
	 * @return
	 */
	static boolean isNumber(char ch)
	{
		if(0x0030 <= ch && ch <= 0x0039)
			return true;
		if(0xA3B0 <= ch && ch <= 0xA3B9)
			return true;
		return false;
	}
	/**
	 * @function 判断是否是英文字符
	 * @param ch 待判断字符
	 * @return
	 */
	static boolean isEnglishChar(char ch)
	{
		if(0x0061 <= ch && ch <= 0x007A)
			return true;
		if(0x0041 <= ch && ch <= 0x005A)
			return true;
		if(0xA3E1 <= ch && ch <= 0xA3FA)
			return true;
		if(0xA3C1 <= ch && ch <= 0xA3DA)
			return true;
		return false;
	}
	/**
	 * @function 判断字符串是否包含阿拉伯数字
	 * @param str 待判断字符串
	 * @return
	 */
	public static boolean containNum(String str)
	{
		if(str == null ||str.trim().length() == 0)
			return false;
		char[] arr = str.toCharArray();
		for (char a : arr) {
			if (isNumber(a))
				return true;
		}
		return false;
	}
	/**
	 * @function 判断字符串是否包含英文字符
	 * @param str 待判断字符串
	 * @return
	 */
	public static boolean containEnChar(String str)
	{
		char[] arr = str.toCharArray();
		for (char a : arr) {
			if (isEnglishChar(a))
				return true;
		}
		return false;
	}
	/**
	 * @function 判断字符串是否全由英文字符组成
	 * @param str 待判断字符串
	 * @return
	 */
	public static boolean isEnString(String str)
	{
		char[] arr = str.toCharArray();
		for (char a : arr) {
			if (!isEnglishChar(a))
				return false;
		}
		return true;
	}
	/**
	 * @function 判断是否是中、日、韩 标点
	 * @param ch 待判断字符
	 * @return
	 */
	static boolean isCjkPunc(char ch) {
		if (0x3001 <= ch && ch <= 0x3003)
			return true;
		if (0x301D <= ch && ch <= 0x301F)
			return true;
		return false;
	}
	
	public static boolean isDigit(String str){
		if(str.isEmpty())
			return false;
		for(char ch : str.toCharArray()){
			if(!Character.isDigit(ch))
				return false;
		}
		return true;
	}
//	public static boolean isCommaSimilar(Word w) {
//		return w.isPunc() && commaPuc.contains(w.str);
//	}
//
//	public static boolean isEndPunctuation(Word w) {
//		return w.isPunc() && endPuc.contains(w.str);
//	}
	

	/**
	 * @Description:
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(isSymbol('+'));

	}
}
