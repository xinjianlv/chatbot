package com.wanda.chatbot.extractor;

import com.wanda.chatbot.pojo.Answer;

/**
 * @description 答案提取器
 * @author nocml
 *
 */
public abstract class AbstractAnswerExtractor {
	protected int level;
	// 责任链中的下一个元素
	protected AbstractAnswerExtractor nextExtractor;

	public void setNextAnswerExtractor(AbstractAnswerExtractor nextExtractor) {
		this.nextExtractor = nextExtractor;
	}

	public void process(String message, Answer answer) {
		// level越小优先级越高，才能覆盖之前的答案
		if (this.level < answer.getLevel()) {
			findAnswer(message, answer);
		}
		if (nextExtractor != null) {
			nextExtractor.process(message, answer);
		}
	}

	abstract protected void findAnswer(String message, Answer answer);
}