package com.wanda.chatbot.extractor;

public class ChainAnswerProcess {
	public static AbstractAnswerExtractor getChainOfProcess() {
		
		//模板匹配
		AbstractAnswerExtractor pattern = new PatternAnswerExtractor(1);
		//网络答案
		AbstractAnswerExtractor internet = new InternetAnswerExtractor(2);
		//语料索引
		AbstractAnswerExtractor index = new IndexAnswerExtractor(3);

//		pattern.setNextAnswerExtractor(internet);
//		internet.setNextAnswerExtractor(index);
		
		pattern.setNextAnswerExtractor(index);

		return pattern;
	}
}
