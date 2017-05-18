package com.wanda.chatbot.pojo;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public enum WeekDateEnum{

	周一 ("周一",Calendar.MONDAY),
	周二 ("周二",Calendar.TUESDAY),
	周三 ("周三",Calendar.WEDNESDAY),
	周四 ("周四",Calendar.THURSDAY),
	周五 ("周五",Calendar.FRIDAY),
	周六 ("周六",Calendar.SATURDAY),
	周末 ("周末",Calendar.SATURDAY),
	周日 ("周日",Calendar.SUNDAY),
	星期一 ("星期一",Calendar.MONDAY),
	星期二 ("星期二",Calendar.TUESDAY),
	星期三 ("星期三",Calendar.WEDNESDAY),
	星期四 ("星期四",Calendar.THURSDAY),
	星期五 ("星期五",Calendar.FRIDAY),
	星期六 ("星期六",Calendar.SATURDAY),
	小礼拜 ("小礼拜",Calendar.SATURDAY),
	大礼拜 ("大礼拜",Calendar.SUNDAY),
	星期日 ("星期日",Calendar.SUNDAY),
	礼拜一 ("礼拜一",Calendar.MONDAY),
	礼拜二 ("礼拜二",Calendar.TUESDAY),
	礼拜三 ("礼拜三",Calendar.WEDNESDAY),
	礼拜四 ("礼拜四",Calendar.THURSDAY),
	礼拜五 ("礼拜五",Calendar.FRIDAY),
	礼拜六 ("礼拜六",Calendar.SATURDAY),
	礼拜天 ("礼拜天",Calendar.SUNDAY),
	礼拜日 ("礼拜日",Calendar.SUNDAY);

	private String name ;
	private int index;
	private WeekDateEnum(String name , int index) {
		this.name = name;
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public static String getName(int index) {
		for (WeekDateEnum c : WeekDateEnum.values()) {
			if (c.getIndex() == index) {
				return c.getName();
			}
		}
		return null;
	}
	public static WeekDateEnum getEnum(String name){
		for (WeekDateEnum c : WeekDateEnum.values()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}
	public static WeekDateEnum getEnum(int index){
		for (WeekDateEnum c : WeekDateEnum.values()) {
			if (c.getIndex() == index) {
				return c;
			}
		}
		return null;
	}
	public static void main(String[] args) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calen = Calendar.getInstance();
		calen.setFirstDayOfWeek(Calendar.MONDAY);
		calen.add(Calendar.WEEK_OF_YEAR, 1);
		calen.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		System.out.println(calen.getFirstDayOfWeek());
		System.out.println(formatter.format(calen.getTime()) + "\t" + calen.get(Calendar.DAY_OF_WEEK));
	}
}
