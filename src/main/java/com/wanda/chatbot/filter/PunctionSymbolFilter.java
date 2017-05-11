package com.wanda.chatbot.filter;

import com.wanda.chatbot.pojo.AnswerFilter;
import com.wanda.chatbot.utils.TextTool;

public class PunctionSymbolFilter extends AbstractFilter{

	
	@Override
	protected void filter(String message, AnswerFilter answer) {
		
		StringBuilder sb = new StringBuilder();
		for(char ch : message.toCharArray()){
			if(TextTool.isPunctuation(ch) || TextTool.isSymbol(ch))
				sb.append(' ');
			else
				sb.append(ch);
		}
		answer.setAnswer(sb.toString());
	}

}
