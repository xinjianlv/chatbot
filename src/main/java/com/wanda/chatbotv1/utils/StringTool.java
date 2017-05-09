package com.wanda.chatbotv1.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;



public class StringTool {
	/**
	 * @function 如果是null，或者长度为0,或者是hive中的\N（表示空）均返回true
	 * @param str 待判断的字符串
	 * @return
	 */
	public static boolean isNull(String str){
		if(str == null || str.equals("\\N")||str.trim().length() == 0)
			return true;
		return false;
	}
	/**
	 * 移除字符串中的所有空字符
	 */
	public static String removeAllSpace(String strin){
		StringBuilder strBuilder = new StringBuilder(strin.length());
		if (strin.length() == 0) {
			return "";
		}
		for (int iChar = 0; iChar < strin.length(); iChar++) {
			char tmpChar = strin.charAt(iChar);
			if (!Character.isSpaceChar(tmpChar)) {
				strBuilder.append(tmpChar);
			}
		}
		return strBuilder.toString();
	}
	
	/**
	 * 从一个字符串中，移除所有在两个指定的字符串之间的内容，包括这连个字串
	 */
	public static String removeTextBetwenTwoStr(String strContent,String strBegin,String strEnd){
		StringBuilder strBuilder = new StringBuilder(strContent.length());
		int iBegin = -1;
		int iEnd  = 0;
		int iTmpBegin= -1;
		int iTmpEnd = -1;
		while (true) {
			iTmpBegin = strContent.indexOf(strBegin,iEnd);
			if (-1 == iTmpBegin) {
				break;
			}
			iBegin = iTmpBegin;
			strBuilder.append(strContent.substring(iEnd,iBegin));
			iTmpEnd = strContent.indexOf(strEnd,iBegin+strBegin.length());
			if (-1 == iTmpEnd ) {
				break;
			}	
			iEnd = iTmpEnd + strEnd.length();
		}
		if (iEnd < strContent.length()) {
			strBuilder.append(strContent.substring(iEnd));
		}
		return strBuilder.toString();
	}
	/**
	 * 把字符串中之指定的连续的字符，替换成单个字符
	 * @param str
	 * @param c
	 * @return
	 */
	public static String repalceSequenceChar(String str,char c){
		StringBuilder strBuilder = new StringBuilder(str.length());
		if (str.length() == 0) {
			return "";
		}
		boolean isFirstChar = true;
		for (int iChar = 0; iChar < str.length(); iChar++) {
			char tmpChar = str.charAt(iChar);
			if (tmpChar == c ) {
				if (isFirstChar) {
					strBuilder.append(tmpChar);
					isFirstChar  =false;
				}		
			}else {
				strBuilder.append(tmpChar);
				if (!isFirstChar) {
					isFirstChar = true;
				}
			}
		}
		return strBuilder.toString();
	}
	
	/**
	 * 计算连个字符串的最大公共子序列(不要求连续)
	 */	
	public static String getLCS(String strOne,String strOhter){
		
		char[] a = strOne.toCharArray();  
		char[] b = strOhter.toCharArray();  
		int a_length = a.length;  
		int b_length = b.length;  
		int[][] lcs = new int[a_length + 1][b_length + 1];  
		// 初始化数组  
		for (int i = 0; i <= b_length; i++) {  
			for (int j = 0; j <= a_length; j++) {  
				lcs[j][i] = 0;  
			}  
		}  
		for (int i = 1; i <= a_length; i++) {  
			for (int j = 1; j <= b_length; j++) {  
				if (a[i - 1] == b[j - 1]) {  
					lcs[i][j] = lcs[i - 1][j - 1] + 1;  
				}  
				if (a[i - 1] != b[j - 1]) {  
					lcs[i][j] = lcs[i][j - 1] > lcs[i - 1][j] ? lcs[i][j - 1]  
					                                                   : lcs[i - 1][j];  
				}  
			}  
		}  
		// 由数组构造最小公共字符串  
		int max_length = lcs[a_length][b_length];  
		char[] comStr = new char[max_length];  
		int i =a_length, j =b_length;  
		while(max_length>0){  
			if(lcs[i][j]!=lcs[i-1][j-1]){  
				if(lcs[i-1][j]==lcs[i][j-1]){//两字符相等，为公共字符  
					comStr[max_length-1]=a[i-1];  
					max_length--;  
					i--;j--;  
				}else{//取两者中较长者作为A和B的最长公共子序列  
					if(lcs[i-1][j]>lcs[i][j-1]){  
						i--;  
					}else{  
						j--;  
					}  
				}  
			}else{  
				i--;
				j--;  
			}  
		}  
		return new String(comStr);  
	}
	
	/**
	 * 判断一个unicode字符是否是中文
	 */	
	public static boolean isChineseChar(char charTest){
		if ((int)charTest > 0x4E00 && (int)charTest < 0x9FA5) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断一个unicode字符是否是中日韩三国文字中的标点符号
	 */	
	public static boolean isCJKPunctuation(char charTest){
		if ((int)charTest > 0x3000 && (int)charTest < 0x303F) {
			return true;
		}
		return false;
	}
	public static void saveStringToFile(String str , String filepath , boolean app)
	{
		try{
			String encoding = "UTF-8"; 
//			File f = new 
			File file = new File(filepath);  
			if(true){		//file.isFile() && file.exists()
				OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file ,app) , encoding);
				BufferedWriter bufferedWriter = new BufferedWriter(write);				
				bufferedWriter.write(str);				
				bufferedWriter.close();
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static String[] split(String source, String div, boolean include) {

		StringTokenizer tokens = new StringTokenizer(source, div, include);
		String[] result = new String[tokens.countTokens()];
		int i = 0;
		while (tokens.hasMoreTokens()) {
			result[i++] = tokens.nextToken();
		}
		return result;

	}
	/**
	 * 计算两个字符串的相似度
	 * @param strA
	 * @param strB
	 * @return 使两个字符串相同的最小修改次数（Hamming Distance）
	 */
	 public static int  calculateStringDistance(String strA, String strB)
	 {
		int lenA = (int)strA.length();
		int lenB = (int)strB.length();
		int [][] c = new int [lenA+1][lenB+1]; 
		for(int i = 0; i < lenA; i++) c[i][lenB] = lenA - i;
		for(int j = 0; j < lenB; j++) c[lenA][j] = lenB - j;
		c[lenA][lenB] = 0;
		for(int i = lenA-1; i >= 0; i--)
			for(int j = lenB-1; j >= 0; j--)
				{
				if(strB.charAt(j) == strA.charAt(i))
					c[i][j] = c[i+1][j+1];
				else
					c[i][j] = minValue(c[i][j+1], c[i+1][j], c[i+1][j+1]) + 1;
			}
		
		return c[0][0];
	 }
	 public static int minValue(int a, int b, int c)
	 {
		if(a < b && a < c)
			return a;
		else if(b < a && b < c) 
			return b;
		else 
			return c;
	 }
	 
	 public static String getFirstNumericFromString(String str){
		 boolean begin = false;
		 StringBuilder sb = new StringBuilder();
		 for(char ch : str.toCharArray()){
			 if(Character.isDigit(ch)){
				 begin = true;
				 sb.append(ch);
			 }else{
				 if(begin)
					 return sb.toString();
			 }
		 }
		 return sb.toString();
	 }
	 public static List<String> getNumericFromString(String str){
		 boolean begin = false;
		 StringBuilder sb = new StringBuilder();
		 List<String> list = new ArrayList<String>();
		 for(char ch : str.toCharArray()){
			 if(Character.isDigit(ch)){
				 begin = true;
				 sb.append(ch);
			 }else{
				 if(begin){
					 list.add(sb.toString());
					 sb = new StringBuilder();
					 begin = false;
				 }
			 }
		 }
		 if(sb.length() > 0){
			 list.add(sb.toString());
		 }
		 return list;
	 }
	 
	 public static String removeBaskets(String str){
		 try{
			if((str.contains("（") || str.contains("(")) && (str.contains("）")||str.contains(")"))){
				return str.replaceAll("[\\(（][\\s\\S]*[\\)）]", "");
			}else
				return str;
		 }catch(Exception e){
			 e.printStackTrace();
			 return null;
		 } 
	 }
//	 public static boolean isNull(String str){
//		 if(str == null)
//			 return false;
//		 if(str.length() == 0)
//			 return false;
//		 if(str.equals())
//	 }
	public static void main(String[] args) {
		System.out.println( "里脊肉8斤".replaceAll("(?<=\\().*(?=\\))", ""));
		String str = "酸菜（同力）50斤";
		System.out.println(removeBaskets(str));
		System.out.println( "酸菜（同力）50斤".replaceAll("[\\(（][\\s\\S]*[\\)）]", ""));
//		String string = "   zjho   gahgon神风       怪盗 围观    ";		
//		System.out.println(repalceSequenceChar(string, ' '));
		String b="ABCBDAB";
		String a="BDCABA";
		String sub = getLCS(a, b);
		System.out.println(sub);
//		String result = repalceSequenceChar("true     false" , ' ');
//		System.out.println(result);
		
//		String subString = getFirstNumericFromString("1");
//		System.out.println(subString);
//		System.out.println("(lsdfjl)lsdf".replaceAll("\\(|\\)|\\（|\\）", ""));
	}
}
