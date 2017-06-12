package com.wanda.chatbot.pojo;

public class Answer {

	protected String answer;
	//leve初始化为最大值，任何规则都可以覆盖
	protected int level = Integer.MAX_VALUE;
	protected ExtraInformation extInfo = new ExtraInformation();
	public String getAnswer() {
		return answer;
	}
	public Answer(){}
	public Answer(String answer){
		this.answer = answer;
	}
	public boolean setAnswer(Answer answer){
		if(answer != null) {
			this.answer = answer.getAnswer();
			this.extInfo.setExtraInformation(answer.getExtInfo());
			return true;
		}
		return false;
	}
	public Answer(String answer , ExtraInformation extInfo){
		this.answer = answer;
		this.extInfo = extInfo;
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
	public ExtraInformation getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(ExtraInformation extInfo) {
		this.extInfo = extInfo;
	}

	@Override
	public String toString() {
		return "Answer{" +
				"answer='" + answer + '\'' +
				", level=" + level +
				", extInfo='" + extInfo + '\'' +
				'}';
	}
}
