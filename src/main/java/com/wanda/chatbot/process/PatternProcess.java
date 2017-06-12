package com.wanda.chatbot.process;

import com.wanda.chatbot.pojo.Answer;
import com.wanda.chatbot.pojo.AnswerList;
import com.wanda.chatbot.pojo.ExtraInformation;
import com.wanda.chatbot.utils.FileTool;
import com.wanda.chatbot.utils.StrUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;


public class PatternProcess extends TimerTask implements ProcessInterface {
	private Logger log = Logger.getLogger(PatternProcess.class);
	private HashMap<String , Integer> questionIndesx = null;
	private HashMap<Integer ,AnswerList> answerinfo = null;
	public void loadPattern(){
		try {
			log.info("加载模板：pattern.dic");
			List<String> lines = FileTool.LoadListFromFile("./outer_dic/pattern.dic", 0, Charset.forName("utf-8"));
			String [][] matrix = parser(lines);
			HashMap<String , Integer> questionMap = getQuestionMap(matrix);
			HashMap<Integer , List<Answer>> answerMap = getAnswerMap(matrix);
			
			questionIndesx = new HashMap<String , Integer>();
			answerinfo = new HashMap<Integer,AnswerList>();
			questionIndesx.putAll(questionMap);
			for(Entry<Integer , List<Answer>> en : answerMap.entrySet()){
				AnswerList a = new AnswerList();
				a.init(en.getValue());
				answerinfo.put(en.getKey(), a);
			}
		} catch (IOException e) {
			log.error(" " , e);
		}
	}
	@Override
	public void run() {
		loadPattern();
	}
	public void reLoadPattern(){
		long per = 1000 * 60 * 10;
		new Timer().schedule(this, 1 , per);
	}
	private HashMap<String , Integer> getQuestionMap(String [][] matrix){
		HashMap<String , Integer> question = new HashMap<String , Integer>();
		for(int i = 0 ; i < matrix.length ; i++){
			int index = Integer.parseInt(matrix[i][0]);
			String q = matrix[i][2];
			question.put(q, index);
		}
		return question;
	}
	private HashMap<Integer , List<Answer>> getAnswerMap(String [][] matrix){
		HashMap<Integer , List<Answer>> answer = new HashMap<Integer , List<Answer>>();
		for(int i = 0 ; i < matrix.length ; i++){
			int index = Integer.parseInt(matrix[i][0]);
			String info = matrix[i][1];
			String a = matrix[i][3];
			List<Answer> list = new ArrayList<Answer>();
			if(answer.containsKey(index))
				list =answer.get(index);
			Answer an = new Answer(a , new ExtraInformation(Integer.parseInt(info.trim())));
			list.add(an);
			answer.put(index, list);
		}
		return answer;
	}
	private String [][] parser(List<String> lines){
		String [][] matrix = new String[lines.size()][3];
		for(int i = 0 ; i < lines.size() ; i++){
			String line = lines.get(i);
			matrix[i] = line.split(StrUtils.tab);
		}
		return matrix;
	}
	public Answer getAnswer(String question){
		Answer an = new Answer();
		if(questionIndesx.containsKey(question)){
			int index = questionIndesx.get(question);
			an = answerinfo.get(index).getRandomAnswer();
		}
		an.getExtInfo().setClassOrigin(PatternProcess.class.getSimpleName());
		return  an;
	}
	
	public static void main(String[] args) {
		PatternProcess pattern = new PatternProcess();
		pattern.loadPattern();
		Answer an = pattern.getAnswer("你好");
		System.out.println(an);
	}
}
