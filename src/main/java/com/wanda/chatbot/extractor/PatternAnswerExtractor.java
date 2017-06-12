package com.wanda.chatbot.extractor;

import com.wanda.chatbot.pojo.Answer;
import com.wanda.chatbot.process.PatternProcess;
import org.apache.log4j.Logger;

public class PatternAnswerExtractor extends AbstractAnswerExtractor {
	Logger log = Logger.getLogger(PatternAnswerExtractor.class);
	private PatternProcess patternProcess = null;
	public PatternAnswerExtractor(int level){
		this.level = level;
		patternProcess = new PatternProcess();
		patternProcess.reLoadPattern();
	}
	
	@Override
	protected void findAnswer(String message , Answer answer) {
		Answer as =  patternProcess.getAnswer(message);
		log.info("find answer:" + as);
		if(this.level < answer.getLevel() && as != null && as.getAnswer()!= null && as.getAnswer().trim().length() > 0){
			log.info("overwrite answer(pattern)");
			answer.setAnswer(as);
			answer.setLevel(this.level);
		}
	}

}
