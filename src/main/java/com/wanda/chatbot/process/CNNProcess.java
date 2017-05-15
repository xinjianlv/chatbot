package com.wanda.chatbot.process;

import java.util.List;

import com.wanda.chatbot.utils.HttpUtil;

public class CNNProcess {
	private final String service = "10.209.20.**";
	public String getAnswer(String question , List<String> answers){
		
		StringBuilder sb = new StringBuilder();
		for(String str : answers){
			sb.append(str).append("_");
		}
		String parameter = "question="+question+"&"+"answer="+sb.substring(0, sb.length() - 1);
		String url = service + "?" + parameter;
		String an = HttpUtil.sendGet(url);
		return an;
	}
	
}
