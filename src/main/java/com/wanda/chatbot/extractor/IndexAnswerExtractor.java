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
		Answer as =  indexProcess.getAnswer(message);
		log.info("find answer:" + as);
		if(this.level < answer.getLevel() && as != null && as.getAnswer().trim().length() > 0){
			log.info("overwrite answer(index)");
			answer.setAnswer(as.getAnswer());
			answer.setLevel(this.level);
		}
	}

}
