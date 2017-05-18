package com.wanda.chatbot.process.time;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import com.wanda.qa.model.Question;
import com.wanda.qa.model.QuestionType;
import com.wanda.qa.questiontypeanalysis.QuestionClassifier;
import com.wanda.qa.questiontypeanalysis.patternbased.*;
import org.apache.log4j.Logger;

import com.wanda.chatbot.pojo.Answer;
import com.wanda.chatbot.pojo.WeekDateEnum;
import com.wanda.chatbot.process.ProcessInterface;
import com.wanda.chatbot.process.time.helper.DateLetterCaseNormalize;
import com.wanda.chatbot.utils.DateUtil;
import com.wanda.chatbot.utils.ResourceTool;
import com.wanda.chatbot.utils.Segment;


public class DateAskProcess implements ProcessInterface {

	static Logger log = Logger.getLogger(DateAskProcess.class);
	private static HashMap<String , Integer> dateMap = new HashMap<String , Integer>();
	private static  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private List<String> datelist= new ArrayList<String>();
	private static DateLetterCaseNormalize letterCaseNormalize = null;
	private static QuestionClassifier questionClassifier = null;
	static{
		List<String> datelist = ResourceTool.readResourceFile("./data/date.dic" , Charset.forName("utf-8"));
		letterCaseNormalize =  new DateLetterCaseNormalize();
		for(String t : datelist){
			String [] ts = t.split("\t");
			dateMap.put(ts[0].trim() , Integer.parseInt(ts[1]));
		}

		PatternMatchStrategy patternMatchStrategy = new PatternMatchStrategy();
		patternMatchStrategy.addQuestionPattern(QuestionPattern.Question);
		patternMatchStrategy.addQuestionPattern(QuestionPattern.TermWithNatures);
		patternMatchStrategy.addQuestionPattern(QuestionPattern.Natures);
		patternMatchStrategy.addQuestionPattern(QuestionPattern.MainPartPattern);
		patternMatchStrategy.addQuestionPattern(QuestionPattern.MainPartNaturePattern);
		patternMatchStrategy.addQuestionTypePatternFile("QuestionTypePatternsLevel1_true.txt");
		patternMatchStrategy.addQuestionTypePatternFile("QuestionTypePatternsLevel2_true.txt");
		patternMatchStrategy.addQuestionTypePatternFile("QuestionTypePatternsLevel3_true.txt");
		PatternMatchResultSelector patternMatchResultSelector = new DefaultPatternMatchResultSelector();
		questionClassifier = new PatternBasedMultiLevelQuestionClassifier(patternMatchStrategy, patternMatchResultSelector);

	}

	public boolean isDateAskQuestion(String questionStr){
		Question question = new Question();
		question.setQuestion(questionStr);
		question = questionClassifier.classify(question);
		return QuestionType.DATEASK ==  question.getQuestionType();
	}

	public List<String> recognition(String context) {
		datelist.clear();
		List<String> terms = Segment.seg(context);
		for(String t : terms){
			if(dateMap.containsKey(t)){
				if(!datelist.contains(t))
					datelist.add(t);
			}
		}
		return null;
	}
	

	private Calendar getCurrentDate(){
		return Calendar.getInstance();
	}
	
	public String getDate(String context){
		boolean userappoint = false;
		Calendar calen = getCurrentDate();
		calen.setFirstDayOfWeek(Calendar.MONDAY);
		context = letterCaseNormalize.normaizition(context);
		String dateString = DateUtil.matchYearMonthDay(context);
		if(dateString == null)
			dateString = DateUtil.matchMonthDay(context);
		if(dateString == null)
			dateString = DateUtil.matchDay(context);
		Date date = DateUtil.parserDate(dateString);
		if(date != null){
			calen.setTime(date);
			userappoint = true;
		}
 		for(String t : datelist){
 			userappoint = true;
			int offset = dateMap.get(t);
			if(offset < 100){
				calen.add(Calendar.DAY_OF_MONTH, offset);
			}else{
				String term = t;
				if(t.startsWith("下个")){
					calen.add(Calendar.WEEK_OF_YEAR, 1);
					term = t.substring(2, t.length());
				}
				else if(t.startsWith("上个")){
					calen.add(Calendar.WEEK_OF_YEAR, -1);
					term = t.substring(2, t.length());
				}
				else if(t.startsWith("下")){
					calen.add(Calendar.WEEK_OF_YEAR, 1);
					term = t.substring(1, t.length());
				}
				else if(t.startsWith("上")){
					calen.add(Calendar.WEEK_OF_YEAR, -1);
					term = t.substring(1, t.length());
				}else if(t.startsWith("本")){
					term = t.substring(1, t.length());
				}
				log.info("term:" + term);
				
				calen.set(Calendar.DAY_OF_WEEK, WeekDateEnum.getEnum(term).getIndex() );
			}
			
		}
 		if(userappoint)
 			return formatter.format(calen.getTime());
 		else
 			return "";
	}
	public void getDayInWeek(Calendar calen , String time){
	}
	
	public static void main(String[] args) {
		try{
			String str=String.format("Hi,%s", "王力");
			System.out.print(str);
			DateAskProcess rdr = new DateAskProcess();
			String context = "后天是多少号？";
			Answer answer = rdr.getAnswer(context);
			System.out.print(answer.getAnswer());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	String pattern = "%s是%s日";
	@Override
	public Answer getAnswer(String question) {
		Answer answer = null;
	    if(isDateAskQuestion(question)){
	    	log.info("question is date ask.");
			recognition(question);
			String date = getDate(question);
			if (date != null){
				StringBuilder sb = new StringBuilder();
			    for ( String t : datelist)
						sb.append(t);
				String an = String.format(pattern, sb.toString(),date);
				answer = new Answer(an);
			}
		}else
			log.info("question is not date ask.");
		return answer;
	}
}
