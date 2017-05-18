package com.wanda.chatbot.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



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
	 
	 public static String getBraketContent(String managers){
	        List<String> ls=new ArrayList<String>();
	        Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
	        Matcher matcher = pattern.matcher(managers);
	        while(matcher.find())
	            ls.add(matcher.group());
	        if(ls.size() > 0)
	        	return ls.get(0);
	        return null;
	    }
		//最长公共子串
		public static List<String> getLCString(char[] str1, char[] str2) {
			List<String> list = new ArrayList<String>();
			int len1, len2;
			len1 = str1.length;
			len2 = str2.length;
			int maxLen = len1 > len2 ? len1 : len2;

			int[] max = new int[maxLen];// 保存最长子串长度的数组
			int[] maxIndex = new int[maxLen];// 保存最长子串长度最大索引的数组
			int[] c = new int[maxLen];

			int i, j;
			for (i = 0; i < len2; i++) {
				for (j = len1 - 1; j >= 0; j--) {
					if (str2[i] == str1[j]) {
						if ((i == 0) || (j == 0))
							c[j] = 1;
						else
							c[j] = c[j - 1] + 1;//此时C[j-1]还是上次循环中的值，因为还没被重新赋值
					} else {
						c[j] = 0;
					}

					// 如果是大于那暂时只有一个是最长的,而且要把后面的清0;
					if (c[j] > max[0]) {
						max[0] = c[j];
						maxIndex[0] = j;

						for (int k = 1; k < maxLen; k++) {
							max[k] = 0;
							maxIndex[k] = 0;
						}
					}
					// 有多个是相同长度的子串
					else if (c[j] == max[0]) {
						for (int k = 1; k < maxLen; k++) {
							if (max[k] == 0) {
								max[k] = c[j];
								maxIndex[k] = j;
								break; // 在后面加一个就要退出循环了
							}
						}
					}
				}
//				for (int temp : c) {
//					System.out.print(temp);
//				}
//				System.out.println();
			}
	        //打印最长子字符串
			for (j = 0; j < maxLen; j++) {
				if (max[j] > 0) {
					StringBuilder sb = new StringBuilder();
					for (i = maxIndex[j] - max[j] + 1; i <= maxIndex[j]; i++){
						sb.append(str1[i]);
					}
					list.add(sb.toString());
				}
			}
			return list;
		}

	public static String reverse(String s) {
		int length = s.length();
		if (length <= 1)
			return s;
		String left = s.substring(0, length / 2);
		String right = s.substring(length / 2, length);
		return reverse(right) + reverse(left);
	}
	public static void main(String[] args) {
		String str = "酸酸菜(同力)50斤";
		System.out.println(getBraketContent(str));
		System.out.println( "酸菜(同力)50斤".replaceAll("[\\(（][\\s\\S]*[\\)）]", ""));
//		String string = "   zjho   gahgon神风       怪盗 围观    ";		
//		System.out.println(repalceSequenceChar(string, ' '));
		String b="ABBC";
		String a="BDCBBC";
		List<String> sub = getLCString(a.toCharArray(), b.toCharArray());
		System.out.println(sub);
//		String result = repalceSequenceChar("true     false" , ' ');
//		System.out.println(result);
		
//		String subString = getFirstNumericFromString("1");
//		System.out.println(subString);
//		System.out.println("(lsdfjl)lsdf".replaceAll("\\(|\\)|\\（|\\）", ""));
	}
}
