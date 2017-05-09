package com.wanda.chatbotv1.process;

import com.wanda.chatbotv1.answer.PatternAnswer;
import com.wanda.chatbotv1.pojo.Answer;

public class PatternProcess extends AbstractProcess{

	private  PatternAnswer patternAnswer = null;
	public PatternProcess(int level){
		this.level = level;
		patternAnswer = new PatternAnswer();
		patternAnswer.reLoadPattern();
	}
	
	@Override
	protected void findAnswer(String message , Answer answer) {
		String astr =  patternAnswer.getAnswer(message);
		if(this.level > answer.getLevel()){
			answer.setAnswer(astr);
			answer.setLevel(this.level);
		}
	}

}
