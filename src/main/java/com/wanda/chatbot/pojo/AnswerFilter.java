package com.wanda.chatbot.pojo;

public class AnswerFilter extends Answer {
	private boolean matchFilter = false;

	public AnswerFilter(){
	}
	public AnswerFilter(String answer){
		this.answer = answer;
	}
	public AnswerFilter(Answer answer){
		this.level = answer.getLevel();
		this.answer = answer.getAnswer();
	}
	public boolean isMatchFilter() {
		return matchFilter;
	}

	public void setMatchFilterTrue() {
		this.matchFilter = true;
	}
	public void setMatchFiltetFalse() {
		this.matchFilter = false;
	}
}
