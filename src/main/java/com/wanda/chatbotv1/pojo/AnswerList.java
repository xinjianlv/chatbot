package com.wanda.chatbotv1.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * @description pattern 模式功能辅助类
 * @author nocml
 */
public class AnswerList {
	Random random = new Random();
	List<String> answerList = new ArrayList<String>();
	public void init(List<String> answerList){
		this.answerList = answerList;
	}
	
	public String getRandomAnswer(){
		int ranIndx = random.nextInt(answerList.size());
		return answerList.get(ranIndx);
	}
}
