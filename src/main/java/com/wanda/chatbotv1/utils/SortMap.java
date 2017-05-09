package com.wanda.chatbotv1.utils;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

public class SortMap<K , V> {
		
	public List<Entry<K , V>> sortByValue(List<Entry<K , V>> info){	
		Collections.sort(info, new Comparator<Entry<K , V>>(){
		@Override
		public int compare(Entry<K, V> o1,
				Entry<K, V> o2) {
			double v2 = Double.parseDouble(o2.getValue().toString());
			double v1 = Double.parseDouble(o1.getValue().toString());
			if(v2 - v1 > 0)
				return 1;
			else if(v2 - v1 < 0)
				return -1;
			else
				return 0;
			
		}});
		return info;
	}
	public List<Pair<K , V>> sortPairByValue(List<Pair<K , V>> info){	
		Collections.sort(info, new Comparator<Pair<K , V>>(){
			@Override
			public int compare(Pair<K, V> o1,
					Pair<K, V> o2) {
				double v2 = Double.parseDouble(o2.getSecond().toString());
				double v1 = Double.parseDouble(o1.getSecond().toString());
				if(v2 - v1 > 0)
					return 1;
				else if(v2 - v1 < 0)
					return -1;
				else
					return 0;
				
			}});
		return info;
	}
	public List<Entry<K , V>> sortByKey(List<Entry<K , V>> info){	
		Collections.sort(info, new Comparator<Entry<K , V>>(){
			@Override
			public int compare(Entry<K, V> o1,
					Entry<K, V> o2) {
				double v2 = Double.parseDouble(o2.getKey().toString());
				double v1 = Double.parseDouble(o1.getKey().toString());
				if(v2 - v1 > 0)
					return 1;
				else if(v2 - v1 < 0)
					return -1;
				else
					return 0;
				
			}});
		return info;
	}
	public List<Entry<K , V>> sortByKeyAsc(List<Entry<K , V>> info){	
		Collections.sort(info, new Comparator<Entry<K , V>>(){
			@Override
			public int compare(Entry<K, V> o1,
					Entry<K, V> o2) {
				double v2 = Double.parseDouble(o2.getKey().toString());
				double v1 = Double.parseDouble(o1.getKey().toString());
				if(v2 - v1 > 0)
					return -1;
				else if(v2 - v1 < 0)
					return 1;
				else
					return 0;
				
			}});
		return info;
	}
}
