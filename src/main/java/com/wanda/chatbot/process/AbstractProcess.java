package com.wanda.chatbot.process;

import com.wanda.chatbot.pojo.Answer;

public abstract interface AbstractProcess {
	public abstract Answer getAnswer(String question);
}
