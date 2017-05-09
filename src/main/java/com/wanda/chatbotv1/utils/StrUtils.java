package com.wanda.chatbotv1.utils;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class StrUtils {

	public static String tab = "\t";

	public static String colon = ":";

	public static String semicolon = ";";

	public static String comma = ",";
	
	public static String sharp = "#";
	
	public static String equalSign = "=";
	
	public static String hivePunc ="\001";
	
	public static String space = " ";
	
	public static String endwin = "\r\n";
	
	public static String endl = "\n";
	
	public static String tempDir = "lvxinjian/temp/";
	public static String split = "_split_";
	public static String labelOutPutPath = "/user/dm/lvxinjian/result/tag_bh/";
	public static Timestamp StringToTimestamp(String str) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = Timestamp.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts;
	}

	public static String TimestampToString(Timestamp ts) {
		String tsStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 方法一
			tsStr = sdf.format(ts);
			// 方法二
			// tsStr = ts.toString();
			// System.out.println(tsStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	public static String getFormatStirng(double d , String format){
		try {
			java.text.DecimalFormat df = new java.text.DecimalFormat(format);
			return df.format(d);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
