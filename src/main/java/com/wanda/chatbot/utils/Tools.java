package com.wanda.chatbot.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Tools {

//	private static Logger log = Logger.getLogger(Tools.class);
	public static List<Pair<Integer , String>> getListFromString(String data){
		List<Pair<Integer , String>> list = new ArrayList<Pair<Integer , String>>();
		try{
			if(data != null && data.trim().length() > 0){
				String ss = data.replaceAll("\\{|\\}|\\[|\\]", "");
				for(String kv : ss.split(StrUtils.comma)){
					String [] arrays = kv.trim().split(StrUtils.equalSign);
					if(arrays == null || arrays.length < 2){
						System.out.println("error in getMapFromString");
					}else{
						int key = Integer.parseInt(arrays[0]);
						String value = arrays[1];
						Pair<Integer , String> pair = new Pair<Integer , String>(key, value);
						list.add(pair);
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	} 

	
	public static void  normalized(ArrayList<Entry<String , Double>> list){
		try{
			double sum = 0; 
			for(Entry<String , Double> en : list){
				sum += en.getValue();
			}
			Map<String , Double> map = new HashMap<String, Double>();
			if(sum != 0){
				for(Entry<String , Double> en : list){
					map.put(en.getKey()	, en.getValue() / sum);
				}
			}
			list.clear();
			list.addAll(map.entrySet());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void  normalized(Map<String , Double> itemMap){
		try{
			double sum = 0; 
			for(Entry<String , Double> en : itemMap.entrySet()){
				sum += en.getValue();
			}
			Map<String , Double> map = new HashMap<String, Double>();
			if(sum != 0){
				for(Entry<String , Double> en : itemMap.entrySet()){
					map.put(en.getKey()	, en.getValue() / sum);
				}
			}
			itemMap.clear();
			itemMap.putAll(map);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static List<Pair<String , String>> getListFromStringSS(String data){
		List<Pair<String , String>> list = new ArrayList<Pair<String , String>>();
		try{
			if(data != null && data.trim().length() > 0){
				String ss = data.replaceAll("\\{|\\}|\\[|\\]", "");
				for(String kv : ss.split(StrUtils.comma)){
					String [] arrays = kv.trim().split(StrUtils.equalSign);
					if(arrays == null || arrays.length < 2){
						System.out.println("error in getMapFromString");
					}else{
						String key = arrays[0];
						String value = arrays[1];
						Pair<String , String> pair = new Pair<String , String>(key, value);
						list.add(pair);
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	} 
	public static List<Pair<String , Integer>> getListFromStringSI(String data){
		List<Pair<String , Integer>> list = new ArrayList<Pair<String , Integer>>();
		try{
			if(data != null && data.trim().length() > 0){
				String ss = data.replaceAll("\\{|\\}|\\[|\\]", "");
				for(String kv : ss.split(StrUtils.comma)){
					String [] arrays = kv.trim().split(StrUtils.equalSign);
					if(arrays == null || arrays.length < 2){
						System.out.println("error in getMapFromString");
					}else{
						String key = arrays[0].trim();
						int value = Integer.parseInt(arrays[1].trim());
						Pair<String , Integer> pair = new Pair<String , Integer>(key, value);
						list.add(pair);
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	} 
	
	public static void main(String[] args) {
		try{
			Map<String , Double> map = new HashMap<String, Double>();
			map.put("1", 0.5225611627912283);
			map.put("2", 0.47743883720877156);
			System.out.println(map.toString());
			for(Entry<String , Double> en : map.entrySet())
				System.out.println(en.toString());
			List<Pair<String , String>> result = getListFromStringSS(map.toString());
			System.out.println(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
