package com.wanda.chatbotv1.extractor;

import org.apache.log4j.Logger;

import com.wanda.chatbotv1.pojo.Answer;
import com.wanda.chatbotv1.process.PatternProcess;

public class PatternAnswerExtractor extends AbstractAnswerExtractor{
	Logger log = Logger.getLogger(PatternAnswerExtractor.class);
	private  PatternProcess patternProcess = null;
	public PatternAnswerExtractor(int level){
		this.level = level;
		patternProcess = new PatternProcess();
		patternProcess.reLoadPattern();
	}
	
	@Override
	protected void findAnswer(String message , Answer answer) {
		String astr =  patternProcess.getAnswer(message);
		log.info("find answer:" + astr);
		if(this.level < answer.getLevel() && astr != null && astr.trim().length() > 0){
			log.info("overwrite answer(pattern)");
			answer.setAnswer(astr);
			answer.setLevel(this.level);
		}
	}

}
