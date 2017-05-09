package com.wanda.chatbotv1.utils;
import java.util.HashSet;

public class Filters {
	private static HashSet<Character> set = null;
	static{
		set = new HashSet<>();
		set.add('*');
		set.add('(');
		set.add(')');
		set.add('[');
		set.add(']');
		set.add('{');
		set.add('-');
	}
	public static String filter(String input){
		StringBuilder sb = new StringBuilder();
		for(char ch : input.toCharArray()){
			if(set.contains(ch))
				sb.append(' ');
			else
				sb.append(ch);
		}
		return sb.toString().trim();
	}
	public static String filterPunSym(String input){
		StringBuilder sb = new StringBuilder();
		for(char ch : input.toCharArray()){
			if(TextTool.isPunctuation(ch) || TextTool.isSymbol(ch))
				sb.append(' ');
			else
				sb.append(ch);
		}
		return sb.toString().trim();
	}
}