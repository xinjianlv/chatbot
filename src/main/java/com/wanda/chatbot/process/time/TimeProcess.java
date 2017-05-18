package com.wanda.chatbot.process.time;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wanda.chatbot.pojo.Answer;
import com.wanda.chatbot.pojo.WeekDateEnum;
import com.wanda.chatbot.process.ProcessInterface;
import com.wanda.chatbot.process.time.helper.DateLetterCaseNormalize;
import com.wanda.chatbot.utils.DateUtil;
import com.wanda.chatbot.utils.ResourceTool;
import com.wanda.chatbot.utils.Segment;


public class TimeProcess implements ProcessInterface {

	static Logger log = Logger.getLogger(TimeProcess.class);
	private static HashMap<String , Integer> dateMap = new HashMap<String , Integer>();
	private static  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private Set<String> dateSet = new HashSet<String>();
	private static DateLetterCaseNormalize letterCaseNormalize = null;
	static{
		List<String> datelist = ResourceTool.readResourceFile("./data/date.dic" , Charset.forName("utf-8"));
		letterCaseNormalize =  new DateLetterCaseNormalize();
		for(String t : datelist){
			String [] ts = t.split("\t");
			dateMap.put(ts[0].trim() , Integer.parseInt(ts[1]));
		}

	}

	public List<String> recognition(String context) {
		dateSet.clear();
		List<String> terms = Segment.seg(context);
		for(String t : terms){
			if(dateMap.containsKey(t)){
				dateSet.add(t);
			}
		}
		return null;
	}
	
//	@Override
//	public List<String> recognition(String context) {
//		dateSet.clear();
//		Result result = DicAnalysis.parse(context);
//		List<Term> terms = result.getTerms();
//		for(Term t : terms){
//			if(dateMap.containsKey(t.getName())){
//				dateSet.add(t.getName());
//			}
//		}
//		return null;
//	}
//	
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
 		for(String t : dateSet){
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
			TimeProcess rdr = new TimeProcess();
			String context = "本周一北京天气";
//			String context = "明天下午18:30在%20万达";
			rdr.recognition(context);
			String date =rdr.getDate(context);
			System.out.println(date);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Answer getAnswer(String question) {
		// TODO Auto-generated method stub
		return null;
	}
}
