package com.wanda.chatbot.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
	public static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");	
	/**
	 * (1)能匹配的年月日类型有： 2014年4月19日 2014年4月19号 2014-4-19 2014/4/19 2014.4.19
	 * (2)能匹配的时分秒类型有： 15:28:21 15:28 5:28 pm 15点28分21秒 15点28分 15点
	 * (3)能匹配的年月日时分秒类型有： (1)和(2)的任意组合，二者中间可有任意多个空格
	 * 如果dateStr中有多个时间串存在，只会匹配第一个串，其他的串忽略
	 * 
	 * @param text
	 * @return
	 */
	private static String matchDateString(String dateStr) {
		try {
			List<String> matches = null;
			Pattern p = Pattern.compile(
					"(\\d{1,4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)*(\\d{1,2}([点|时])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			Matcher matcher = p.matcher(dateStr);
			if (matcher.find() && matcher.groupCount() >= 1) {
				matches = new ArrayList<String>();
				for (int i = 1; i <= matcher.groupCount(); i++) {
					String temp = matcher.group(i);
					matches.add(temp);
				}
			} else {
				matches = Collections.EMPTY_LIST;
			}
			if (matches.size() > 0) {
				return ((String) matches.get(0)).trim();
			} else {
			}
		} catch (Exception e) {
			return "";
		}

		return dateStr;
	}

	private static Set<Integer> bigMonth = null;
	private static Pattern patternYMD = null;
	private static Pattern patternMD = null;
	private static Pattern patternD = null;
	private static Pattern patternHMS = null;
	static {
		patternYMD = Pattern.compile("(\\d{1,4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)?)",
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		patternMD = Pattern.compile("(\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)?)",
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		patternD = Pattern.compile("(\\d{1,2}([日|号])(\\s)?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		patternHMS = Pattern.compile("\\d{1,2}([点|时]|:|：)(\\d{1,2}([分|:|：])?(\\d{1,2}(秒)?)?)?" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		bigMonth = new HashSet<Integer>();
		bigMonth.add(1);
		bigMonth.add(3);
		bigMonth.add(5);
		bigMonth.add(7);
		bigMonth.add(8);
		bigMonth.add(10);
		bigMonth.add(12);
	}
	public static Calendar parserTime(String dateString) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			if (dateString == null)
				return calendar;
			dateString = dateString.replaceAll("时|点|分|秒|\\:|：", "_split_");
			String[] arrays = dateString.split("_split_");
			if (arrays.length == 3) {
				int hour = Integer.parseInt(arrays[0]);
				int minute = Integer.parseInt(arrays[1]);
				int second = Integer.parseInt(arrays[2]);
				if (DateUtil.isGoodHour(hour) && DateUtil.isGoodMinute(minute) && DateUtil.isGoodSecond(second) ) {
					calendar.set(Calendar.HOUR_OF_DAY, hour);
					calendar.set(Calendar.MINUTE, minute);
					calendar.set(Calendar.SECOND, second);
				} else {
					return null;
				}

			} else if (arrays.length == 2) {
				int hour = Integer.parseInt(arrays[0]);
				int minute = Integer.parseInt(arrays[1]);
				if (DateUtil.isGoodHour(hour) && DateUtil.isGoodMinute(minute) ) {
					calendar.set(Calendar.HOUR_OF_DAY, hour);
					calendar.set(Calendar.MINUTE, minute);
				} else {
					return null;
				}

			} else if (arrays.length == 1) {
				int hour = Integer.parseInt(arrays[0]);
				if (DateUtil.isGoodHour(hour)) {
					calendar.set(Calendar.HOUR_OF_DAY, hour);
				} else {
					return null;
				}
			}
			return calendar;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public static Date parserDate(String dateString) {
		try {
			Calendar calendar = Calendar.getInstance();
			if (dateString == null)
				return null;
			dateString = dateString.replaceAll("\\.|\\/|-|年|月|日|号", "_split_");
			String[] arrays = dateString.split("_split_");
			if (arrays.length == 3) {
				int year = Integer.parseInt(arrays[0]);
				int month = Integer.parseInt(arrays[1]);
				int day = Integer.parseInt(arrays[2]);
				if(year < 100)
					year += 2000;
				if(year > 9999)
					return null;
				if (DateUtil.isGoodMD(month, day, false)) {
					calendar.set(Calendar.YEAR, year);
					calendar.set(Calendar.MONTH, month - 1);
					calendar.set(Calendar.DAY_OF_MONTH, day);
				} else {
					return null;
				}

			} else if (arrays.length == 2) {
				int month = Integer.parseInt(arrays[0]);
				int day = Integer.parseInt(arrays[1]);
				if (DateUtil.isGoodMD(month, day, false)) {
					calendar.set(Calendar.MONTH, month - 1);
					calendar.set(Calendar.DAY_OF_MONTH, day);
				} else {
					return null;
				}

			} else if (arrays.length == 1) {
				int day = Integer.parseInt(arrays[0]);
				if (day > 0 && day < 32)
					calendar.set(Calendar.DAY_OF_MONTH, day);
				else {
					return null;
				}
			}
			return calendar.getTime();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	@SuppressWarnings("null")
	public static List<String> matchHHMMSS(String timeString){
		try{
			try {
				List<String> matches = new ArrayList<String>();

				Matcher m = patternHMS.matcher(timeString);
				while(m.find()) {
					matches.add(m.group());
			      }
				if (matches.size() > 0) {
					return  matches;
				} 
			} catch (Exception e) {
				return null;
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String matchYearMonthDay(String dateStr) {
		try {
			List<String> matches = null;

			Matcher matcher = patternYMD.matcher(dateStr);
			if (matcher.find() && matcher.groupCount() >= 1) {
				matches = new ArrayList<String>();
				for (int i = 1; i <= matcher.groupCount(); i++) {
					String temp = matcher.group(i);
					matches.add(temp);
				}
			} else {
				matches = Collections.EMPTY_LIST;
			}
			if (matches.size() > 0) {
				return ((String) matches.get(0)).trim();
			} 
		} catch (Exception e) {
			return "";
		}

		return null;
	}

	public static String matchMonthDay(String dateStr) {
		try {
			List<String> matches = null;

			Matcher matcher = patternMD.matcher(dateStr);
			if (matcher.find() && matcher.groupCount() >= 1) {
				matches = new ArrayList<String>();
				for (int i = 1; i <= matcher.groupCount(); i++) {
					String temp = matcher.group(i);
					matches.add(temp);
				}
			} else {
				matches = Collections.EMPTY_LIST;
			}
			if (matches.size() > 0) {
				return ((String) matches.get(0)).trim();
			} 
		} catch (Exception e) {
			return "";
		}

		return null;
	}

	public static String matchDay(String dateStr) {
		try {
			List<String> matches = null;

			Matcher matcher = patternD.matcher(dateStr);
			if (matcher.find() && matcher.groupCount() >= 1) {
				matches = new ArrayList<String>();
				for (int i = 1; i <= matcher.groupCount(); i++) {
					String temp = matcher.group(i);
					matches.add(temp);
				}
			} else {
				matches = Collections.EMPTY_LIST;
			}
			if (matches.size() > 0) {
				return ((String) matches.get(0)).trim();
			} else {
			}
		} catch (Exception e) {
			return "";
		}

		return null;
	}

	public static boolean isGoodMD(int month, int day, boolean isLeapYear) {
		if (month < 0 || month > 12)
			return false;
		if (bigMonth.contains(month)) {
			if (day > 0 && day < 32)
				return true;
			else
				return false;
		} else {
			if (isLeapYear) {
				if (month == 2) {
					if (day > 0 && day < 30)
						return true;
					else
						return false;
				}
			}
			if (day > 0 && day < 31)
				return true;
			else
				return false;
		}
	}


	public static boolean isGoodHour(int hour){
		if(hour >= 0 && hour <=24)
			return true;
		return false;
	}
	public static boolean isGoodMinute(int minute){
		if(minute >= 0 && minute < 60)
			return true;
		return false;
	}
	public static boolean isGoodSecond(int second){
		if(second >= 0 && second < 60)
			return true;
		return false;
	}
	
	
	/**
	 * 判断年份是否为润年
	 * 
	 * @param {Number}
	 *            year
	 */
	public static boolean isLeapYear(int year) {
		return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
	}

	static public SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

	public static int getWeek(String time) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(format.parse(time));
			return calendar.get(Calendar.WEEK_OF_YEAR);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String iSaid = "20：14年dfds05月9日23点40分是什么天气23:25和23点25分58秒和2:25:32sdf34点28秒";
		String ss = "2014年5月南宁的天气";
		// 匹配时间串
		String answer = matchHHMMSS(iSaid).get(0);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 输出：
		// 答：2014年4月25 15时36分21秒
		System.out.println("timestring : " + answer);
		System.out.println("答：" + formatter.format(parserTime(answer).getTime()));
	}
}
