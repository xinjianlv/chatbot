package com.wanda.chatbot.process.time.helper;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class DateLetterCaseNormalize implements Normalize {
	Logger log = Logger.getLogger(DateLetterCaseNormalize.class);
	private static HashMap<Character , String> tmap = new HashMap<Character , String>();
	private static HashMap<Character , String> fmap = new HashMap<Character , String>();
	static {
		tmap.put('零', "0");
		tmap.put('一', "1");
		tmap.put('二', "2");
		tmap.put('三', "3");
		tmap.put('四', "4");
		tmap.put('五', "5");
		tmap.put('六', "6");
		tmap.put('七', "7");
		tmap.put('八', "8");
		tmap.put('九', "9");
		
		fmap.put('十', "10");
		fmap.put('廿', "20");
		fmap.put('卅', "30");
		
	}
	
	@Override
	public String normaizition(String str) {
		StringBuilder sb = new StringBuilder();
		char [] carray = str.toCharArray();
		boolean isDigit = false;
		for(int i = 0 ; i < carray.length ; i++){
			char ch = carray[i];
			isDigit = false;
			if(tmap.containsKey(ch)){
				sb.append(tmap.get(ch));
				isDigit = true;
			}
			if(fmap.containsKey(ch)){
				isDigit = true;
				if(i > 0 && i < carray.length - 1){
					char pre = carray[i - 1];
					char suf = carray[i + 1];
					if(tmap.containsKey(pre) ){
						if(tmap.containsKey(suf))
							;
						else
							sb.append('0');
					}
					else {
						if(tmap.containsKey(suf)){
							sb.append(fmap.get(ch));
							sb.deleteCharAt(sb.length() - 1);
						}
						else{
							sb.append(fmap.get(ch));
						}
							
					}
						
				}
				else if (i == 0 ){
					if(carray.length > 1){
						if(tmap.containsKey(carray[i + 1])){
							sb.append(fmap.get(ch));
							sb.deleteCharAt(sb.length() - 1);
						}else{
							sb.append(fmap.get(ch));
						}
					}else{
						sb.append(fmap.get(ch));
					}
				}
				else if( i == carray.length - 1){
					if(carray.length > 1){
						if(tmap.containsKey(carray[i - 1])){
							sb.append(fmap.get(ch));
							sb.deleteCharAt(sb.length() - 2);
						}else{
							sb.append(fmap.get(ch));
						}
					}else{
						sb.append(fmap.get(ch));
					}
				}
			}
			if(!isDigit)
				sb.append(carray[i]);
			
		}
		return sb.toString();
	}
	public static void main(String[] args) {
//		System.out.println(Utils.isNumber(''));
		DateLetterCaseNormalize nor = new DateLetterCaseNormalize();
		System.out.println(nor.normaizition("十月一日"));
	}
}
