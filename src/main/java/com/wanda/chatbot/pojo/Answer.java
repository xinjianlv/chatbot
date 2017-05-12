package com.wanda.chatbot.pojo;

public class Answer {

	protected String answer;
	//leve初始化为最大值，任何规则都可以覆盖
	protected int level = Integer.MAX_VALUE;
	protected String extInfo;
	public String getAnswer() {
		return answer;
	}
	public Answer(){}
	public Answer(String answer){
		this.answer = answer;
	}
	public Answer(String answer , String info){
		this.answer = answer;
		this.extInfo = info;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	
}
