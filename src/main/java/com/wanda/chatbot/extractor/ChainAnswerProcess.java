package com.wanda.chatbot.extractor;

public class ChainAnswerProcess {
	public static AbstractAnswerExtractor getChainOfProcess() {
		
		//模板匹配
		AbstractAnswerExtractor pattern = new PatternAnswerExtractor(1);
		//日期询问
		AbstractAnswerExtractor dateAsk = new DateAskAnswerExtractor(2);
		//网络答案
		AbstractAnswerExtractor internet = new InternetAnswerExtractor(3);
		//语料索引
		AbstractAnswerExtractor index = new IndexAnswerExtractor(4);

		pattern.setNextAnswerExtractor(dateAsk);
		dateAsk.setNextAnswerExtractor(internet);
		internet.setNextAnswerExtractor(index);


		return pattern;
	}
}
