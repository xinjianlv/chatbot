package com.wanda.chatbot.process;

import java.util.ArrayList;
import java.util.List;

import com.wanda.chatbot.pojo.Answer;
import com.wanda.qa.datasource.BaiduDataSource;
import com.wanda.qa.datasource.DataSource;
import com.wanda.qa.files.FilesConfig;
import com.wanda.qa.model.CandidateAnswer;
import com.wanda.qa.model.Question;
import com.wanda.qa.model.QuestionType;
import com.wanda.qa.system.CommonQuestionAnsweringSystem;
import com.wanda.qa.system.QuestionAnsweringSystem;

public class InternetProcess implements ProcessInterface{

	private static QuestionAnsweringSystem questionAnsweringSystem = null;
	public InternetProcess(){
		List<String> files = new ArrayList<>();

		files.add(FilesConfig.personNameQuestions);
//		files.add(FilesConfig.locationNameQuestions);
//		files.add(FilesConfig.organizationNameQuestions);
//		files.add(FilesConfig.numberQuestions);
//		files.add(FilesConfig.timeQuestions);
		DataSource dataSource = new BaiduDataSource(files);
		questionAnsweringSystem = new CommonQuestionAnsweringSystem();
		questionAnsweringSystem.setDataSource(dataSource);
	}
	
	public Answer getAnswer(String question){
		Question qa = questionAnsweringSystem.answerQuestion(question);
		if (qa.getQuestionType() != QuestionType.NULL)
	        for(CandidateAnswer canswer :  qa.getAllCandidateAnswer()){
	        	return new Answer(canswer.getAnswer() , InternetProcess.class.getSimpleName());
	        }
        return null;
	}
}
