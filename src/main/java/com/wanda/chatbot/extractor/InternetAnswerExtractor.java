package com.wanda.chatbot.extractor;

import org.apache.log4j.Logger;

import com.wanda.chatbot.pojo.Answer;
import com.wanda.chatbot.process.InternetProcess;

public class InternetAnswerExtractor extends AbstractAnswerExtractor{
	Logger log = Logger.getLogger(InternetAnswerExtractor.class);
	private  InternetProcess internetProcess = null;
	public InternetAnswerExtractor(int level){
		this.level = level;
		internetProcess = new InternetProcess();
	}
	
	@Override
	protected void findAnswer(String message , Answer answer) {
		Answer as =  internetProcess.getAnswer(message);
		log.info("find answer:" + as);
		if(this.level < answer.getLevel() && as != null && as.getAnswer().trim().length() > 0){
			log.info("overwrite answer(internet)");
			answer.setAnswer(as);
			answer.setLevel(this.level);
		}
	}

}
