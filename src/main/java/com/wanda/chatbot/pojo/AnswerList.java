package com.wanda.chatbot.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @description pattern 模式功能辅助类
 * @author nocml
 */
public class AnswerList {
	Random random = new Random();
	List<Answer> answerList = new ArrayList<Answer>();
	public void init(List<Answer> answerList){
		this.answerList = answerList;
	}
	
	public Answer getRandomAnswer(){
		int ranIndx = random.nextInt(answerList.size());
		return answerList.get(ranIndx);
	}
}
