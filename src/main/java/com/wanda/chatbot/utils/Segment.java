package com.wanda.chatbot.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;


public class Segment {
	private static final Logger log = Logger.getLogger(Segment.class);
	private static TrieTree analyzer = null;
	static{
		try {
			Set<String> words = ResourceTool.readString("./data/cnCi.txt" , "\t" , 0 , Charset.forName("utf-8"));
			Set<String> extWords = ResourceTool.readString("./data/date.dic" , "\t" , 0 , Charset.forName("utf-8"));
			Set<String> nodes = new HashSet<String>();
			words.addAll(extWords);
			for(String term : words){
				nodes.add(StringTool.reverse(term));
			}
			analyzer  = new TrieTree(nodes , false);
		} catch (Exception e) {
			log.error(" " , e);
		}
	}

	/**
	 * @param input
	 * @return
	 */
	public static List<String> seg(String input){
		String text = StringTool.reverse(input);
		Map<String, List<Integer>> segMap = analyzer.seg(text);
		List<Pair<Integer , String>> listSort = new ArrayList<Pair<Integer , String>>();
		for(Entry<String , List<Integer>> en : segMap.entrySet()){
			for(int ndx : en.getValue()){
				listSort.add(new Pair<Integer , String>(ndx , en.getKey()));
			}
		}
		Collections.sort(listSort, new Comparator<Pair<Integer , String>>(){
			@Override
			public int compare(Pair<Integer, String> o1, Pair<Integer, String> o2) {
				return o2.getFirst().compareTo(o1.getFirst());
			}
		});
		ArrayList<String> retList = new ArrayList<String>();
		for(Pair<Integer , String> pair : listSort){
			retList.add(StringTool.reverse(pair.getSecond()));
		}
		System.out.println();
		return retList;
	}
	public static void main(String[] args) {
		try{
			String str = "我大大后天中文分词的测试！";
			List<String> list = Segment.seg(str);
			System.out.println(list.toString());
			
			str = "这是第二个中文分词的测试！";
			list = Segment.seg(str);
			System.out.println(list.toString());
		}catch(Exception e){
			
		}
	}
}
