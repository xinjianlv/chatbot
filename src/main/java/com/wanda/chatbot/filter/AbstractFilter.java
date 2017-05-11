package com.wanda.chatbot.filter;

import com.wanda.chatbot.pojo.AnswerFilter;

public abstract class AbstractFilter {
	protected int level;

	// 责任链中的下一个元素
	protected AbstractFilter nextFilter;

	public void setNextFilter(AbstractFilter nextFilter) {
		this.nextFilter = nextFilter;
	}

	public void process(String message, AnswerFilter answer) {
		// level越小优先级越高，才能覆盖之前的答案
		if (this.level < answer.getLevel()) {
			filter(message, answer);
		}
		if (nextFilter != null) {
			nextFilter.process(message, answer);
		}
	}

	abstract protected void filter(String message, AnswerFilter answer);
}
