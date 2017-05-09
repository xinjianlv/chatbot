package com.wanda.chatbotv1.extractor;

import org.apache.log4j.Logger;

import com.wanda.chatbotv1.pojo.Answer;
import com.wanda.chatbotv1.process.InternetProcess;

public class InternetAnswerExtractor extends AbstractAnswerExtractor{
	Logger log = Logger.getLogger(InternetAnswerExtractor.class);
	private  InternetProcess internetProcess = null;
	public InternetAnswerExtractor(int level){
		this.level = level;
		internetProcess = new InternetProcess();
	}
	
	@Override
	protected void findAnswer(String message , Answer answer) {
		String astr =  internetProcess.getAnswer(message);
		log.info("find answer:" + astr);
		if(this.level < answer.getLevel() && astr != null && astr.trim().length() > 0){
			log.info("overwrite answer(internet)");
			answer.setAnswer(astr);
			answer.setLevel(this.level);
		}
	}

}