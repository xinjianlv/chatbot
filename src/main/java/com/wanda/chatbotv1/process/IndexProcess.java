package com.wanda.chatbotv1.process;

import com.wanda.chatbotv1.answer.IndexAnswer;
import com.wanda.chatbotv1.pojo.Answer;

public class IndexProcess extends AbstractProcess{

	private  IndexAnswer indexAnswer = null;
	public IndexProcess(int level){
		this.level = level;
		indexAnswer = new IndexAnswer();
	}
	
	@Override
	protected void findAnswer(String message , Answer answer) {
		String astr =  indexAnswer.getAnswer(message);
		if(this.level > answer.getLevel()){
			answer.setAnswer(astr);
			answer.setLevel(this.level);
		}
	}

}
