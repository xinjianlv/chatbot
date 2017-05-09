package com.wanda.chatbot.extractor;

import org.apache.log4j.Logger;

import com.wanda.chatbot.pojo.Answer;
import com.wanda.chatbot.process.IndexProcess;

public class IndexAnswerExtractor extends AbstractAnswerExtractor{
	Logger log = Logger.getLogger(IndexAnswerExtractor.class);
	private  IndexProcess indexProcess = null;
	public IndexAnswerExtractor(int level){
		this.level = level;
		indexProcess = new IndexProcess();
	}
	
	@Override
	protected void findAnswer(String message , Answer answer) {
		String astr =  indexProcess.getAnswer(message);
		log.info("find answer:" + astr);
		if(this.level < answer.getLevel() && astr != null && astr.trim().length() > 0){
			log.info("overwrite answer(index)");
			answer.setAnswer(astr);
			answer.setLevel(this.level);
		}
	}

}
