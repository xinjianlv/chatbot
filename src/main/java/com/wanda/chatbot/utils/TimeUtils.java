package com.wanda.chatbot.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TimeUtils {

//	private static Logger log = Logger.getLogger(TimeUtils.class);
	public static String getToday(){
		try{
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = new Date();
			String m = format1.format(d1);
			return m;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	public static String getWeekTime(String time){
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(paserDate(time));
			int year =  calendar.get(Calendar.YEAR);
			int week = calendar.get(Calendar.WEEK_OF_YEAR);
			if(week < 10)
				return year + "0" + week;
			else
				return year + "" + week;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static String getDate(int offset) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, offset);
		date = calendar.getTime();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		return format1.format(date);
	}
	public static int getHour(){
		Calendar cal=Calendar.getInstance();        
		return cal.get(Calendar.HOUR_OF_DAY);         
	}
	
	public static Date paserDate(String dateTime){
		try {
			SimpleDateFormat sdf = null;
			if (dateTime.length() < "1970-01-06 11:45:55".length())
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			else
				sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			
			return sdf.parse(dateTime.replaceAll(":", "-"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static int getDateOffset(String date1 , String date2){
		try{
			Calendar c1 = Calendar.getInstance();// paserDate(date1);
			Calendar c2 = Calendar.getInstance();// paserDate(date1);
			c1.setTime(paserDate(date1));
			c2.setTime(paserDate(date2));
			long minus = Math.abs(c1.getTimeInMillis() - c2.getTimeInMillis());
			return (int) (minus / (1000 * 60 * 60 * 24));
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	public static String getTimePeriod(int hour){
		if(hour < 0 || hour > 23)
			return "error";
		if(hour >= 22 || hour < 4)
			return "夜间";
		else if(hour >= 4 && hour < 9)
			return "清晨";
		else if(hour >= 9 && hour < 12)
			return "上午";
		else if(hour >= 12 && hour < 14)
			return "中午";
		else if(hour >= 14 && hour < 18)
			return "下午";
		else if(hour >= 18 && hour < 22 )
			return "晚间";
		else
			return "error";
	}
	
	public static String getDateString(Date date ){
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		return format1.format(date);
	}
	
	public static String getDateStringYYYMMDD(Date date ){
		DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		return format1.format(date);
	}
	public static String getDateStringYYYY_MM_DD(Date date) {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		return format1.format(date);
	}
	public static List<String> getDate(String begin, String end) {
		List<String> dateList = new ArrayList<String> ();
		Calendar cBegin = Calendar.getInstance();
		Calendar cEnd = Calendar.getInstance();
		cBegin.setTime(TimeUtils.paserDate(begin));
		cEnd.setTime(TimeUtils.paserDate(end));
		while (cBegin.compareTo(cEnd) < 0) {
			dateList.add(getDateStringYYYY_MM_DD(cBegin.getTime()));
			cBegin.add(6, 1);
		}
		return dateList;
	}

	public static double getArgDay(Collection<Long> collection){
		try{
			List<Long> list = new ArrayList<Long>(collection);
			if(list.size() < 2)
				return -1;
			Collections.sort(list);
			double sum = 0.0;
			for(int i = 0 ; i < list.size() - 1 ; i++){
				System.out.println("timemills:" + list.get(i));
				sum += (list.get(i + 1) - list.get(i));
			}
			System.out.println("sum:" + sum);
			return sum/(list.size() -1);
			
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	/** 
	 * 判断年份是否为润年 
	 *  
	 * @param {Number} year 
	 */ 
	public static boolean isLeapYear(int year) { 
	    return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0); 
	} 
	static public 	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	public static  int getWeek(String time) { 
		try{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(format.parse(time));
	    return calendar.get(Calendar.WEEK_OF_YEAR);
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	        
	} 
	
	public static void main(String[] args) {
		try{
			
			int n = TimeUtils.getDateOffset("2016-01-01" , "2015-01-01");
			System.out.println(n);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
