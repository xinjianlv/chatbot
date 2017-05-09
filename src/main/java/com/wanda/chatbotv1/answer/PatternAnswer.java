package com.wanda.chatbotv1.answer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.wanda.chatbotv1.pojo.AnswerList;
import com.wanda.chatbotv1.utils.FileTool;
import com.wanda.chatbotv1.utils.Filters;
import com.wanda.chatbotv1.utils.StrUtils;



public class PatternAnswer extends TimerTask{
	private Logger log = Logger.getLogger(PatternAnswer.class);
	private static HashMap<String , Integer> questionIndesx = null;
	private static HashMap<Integer ,AnswerList> answer = null;
	public void loadPattern(){
		try {
			log.info("加载模板：pattern.dic");
			List<String> lines = FileTool.LoadListFromFile("./pattern.dic", 0, Charset.forName("utf-8"));
			String [][] matrix = parser(lines);
			HashMap<String , Integer> questionMap = getQuestionMap(matrix);
			HashMap<Integer , List<String>> answerMap = getAnswerMap(matrix);
			
			questionIndesx = new HashMap<String , Integer>();
			answer = new HashMap<Integer,AnswerList>();
			questionIndesx.putAll(questionMap);
			for(Entry<Integer , List<String>> en : answerMap.entrySet()){
				AnswerList a = new AnswerList();
				a.init(en.getValue());
				answer.put(en.getKey(), a);
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
		new Timer().schedule(new PatternAnswer(), 1 , per);
	}
	private HashMap<String , Integer> getQuestionMap(String [][] matrix){
		HashMap<String , Integer> question = new HashMap<String , Integer>();
		for(int i = 0 ; i < matrix.length ; i++){
			String q = matrix[i][0];
			int index = Integer.parseInt(matrix[i][1]);
			question.put(q, index);
		}
		return question;
	}
	private HashMap<Integer , List<String>> getAnswerMap(String [][] matrix){
		HashMap<Integer , List<String>> answer = new HashMap<Integer , List<String>>();
		for(int i = 0 ; i < matrix.length ; i++){
			int index = Integer.parseInt(matrix[i][1]);
			String a = matrix[i][2];
			List<String> list = new ArrayList<String>();
			if(answer.containsKey(index))
				list =answer.get(index);
			list.add(a);
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
	public String getAnswer(String question){
		String qs = Filters.filterPunSym(question);
		String an = "";
		if(questionIndesx.containsKey(qs)){
			int index = questionIndesx.get(qs);
			an = answer.get(index).getRandomAnswer();
		}
		return an;
	}
	
	public static void main(String[] args) {
		PatternAnswer pattern = new PatternAnswer();
		String an = pattern.getAnswer("test");
		System.out.println(an);
	}
}
