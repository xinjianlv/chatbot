package com.wanda.chatbot.filter;

public class ChainFilter {
	public static AbstractFilter getChainOfAnswerFilter() {

		AbstractFilter spam = new SpamFilter();
		return spam;
	}
	public static AbstractFilter getChainOfQuestionFilter() {
		
		AbstractFilter symbol = new PunctionSymbolFilter();
		return symbol;
	}
}
