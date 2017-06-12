package com.ml.word2vec;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class Segment {
	private static final Logger log = Logger.getLogger(Segment.class);
	private static Analyzer analyzer = null;
	private static 	TokenStream ts = null;
	static{
		try {
			analyzer  = new IKAnalyzer(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param input
	 * @return
	 */
	/**
	 * @param input
	 * @return
	 */
	/**
	 * @param input
	 * @return
	 */
	public static List<String> seg(String input){
		try {
			ts = analyzer.tokenStream("myfield", new StringReader(input));
			   CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
			    
				ts.reset(); 
				List<String> list = new ArrayList<String>();
				//迭代获取分词结果
				while (ts.incrementToken()) {
					list.add(term.toString());
				}
				//关闭TokenStream（关闭StringReader）
				ts.end();
				return list;
		} catch (Exception e) {
			log.error("segment" ,e);
			log.info("input:"+input );
		}finally{
			if(ts != null){
				try {
					ts.close();
				} catch (IOException e) {
					log.error("" ,e);
				}  
				
			}
		}
		return null;
	}
	public static void main(String[] args) {
		try{
			String str = "这是第一个中文分词的测试！";
			List<String> list = Segment.seg(str);
			System.out.println(list.toString());
			
			str = "这是第二个中文分词的测试！";
			list = Segment.seg(str);
			System.out.println(list.toString());
		}catch(Exception e){
			
		}
	}
}
