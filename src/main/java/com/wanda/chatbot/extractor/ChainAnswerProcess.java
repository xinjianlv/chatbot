package com.wanda.chatbot.extractor;

public class ChainAnswerProcess {
	public static AbstractAnswerExtractor getChainOfProcess() {
		
		//模板匹配
		AbstractAnswerExtractor pattern = new PatternAnswerExtractor(1);
		//语料索引
		AbstractAnswerExtractor index = new IndexAnswerExtractor(4);
		//
		AbstractAnswerExtractor date = new DateAskAnswerExtractor(2);
		//
		AbstractAnswerExtractor internet = new InternetAnswerExtractor(3);
		//
		pattern.setNextAnswerExtractor(date);
		date.setNextAnswerExtractor(internet);
		internet.setNextAnswerExtractor(index);

		return pattern;
	}
}
