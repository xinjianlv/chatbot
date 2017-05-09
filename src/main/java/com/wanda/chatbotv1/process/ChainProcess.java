package com.wanda.chatbotv1.process;

public class ChainProcess {
	public static AbstractAnswerExtractor getChainOfProcess() {

		AbstractAnswerExtractor pattern = new PatternAnswerExtractor(1);
		AbstractAnswerExtractor internet = new InternetAnswerExtractor(2);
		AbstractAnswerExtractor index = new IndexAnswerExtractor(3);

		pattern.setNextAnswerExtractor(internet);
		internet.setNextAnswerExtractor(index);

		return pattern;
	}
}
