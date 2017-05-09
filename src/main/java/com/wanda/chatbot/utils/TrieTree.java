package com.wanda.chatbot.utils;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



public class TrieTree implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 4868555579581414164L;

	private boolean isEnd; // 是否到叶子节点
	private Map<String, TrieTree> children; // 子节点
	private String tag;
	private boolean isSplit = false;

	public String toString() {
		String s = this.tag;
		if (!isEnd)
			for (String ttag : children.keySet()) {
				s += "[" + children.get(ttag) + "]";
			}
		return s;
	}

	TrieTree(boolean isEnd, HashMap<String, TrieTree> children) {
		this.isEnd = isEnd;
		this.children = children;
	}

	private void init(Collection<String> nodes, boolean isSplit) throws Exception {
		this.isEnd = false;
		this.children = new HashMap<String, TrieTree>();
		this.isSplit = isSplit;
		TrieTree root = this;
		for (String node : nodes) {
			if (this.isSplit ){
				if(node.startsWith(" ") || node.endsWith(" "))
					throw new Exception("原始关键词不能以空格开头或者以空格结尾");			
				node = " " + node + " ";
			}
			addWord(root, node, node);
		}
	}
	/**
	 * @Title: TrieTree
	 * @Description: TrieTree构造
	 * @param @param nodes
	 * @param @param isSplit 是否为分词匹配
	 * @return 返回类型
	 * @throws Exception
	 */
	public TrieTree(Collection<String> nodes, boolean isSplit) throws Exception {
		init(nodes, isSplit);
	}
	
	/**
	 * @Title: TrieTree
	 * @Description: TrieTree构造
	 * @param @param nodes
	 * @param @param isSplit 是否为分词匹配
	 * @return 返回类型
	 * @throws Exception
	 */
	public TrieTree(Collection<String> nodes) throws Exception {
		init(nodes, true);
	}

	/**
	 * @Description:添加一个词到trie树，可以包含不同特征词
	 * @param t
	 * @param word
	 *            关键词中剩下未加入树中的部分
	 * @param origin
	 *            完整的关键词
	 */
	public static void addWord(TrieTree t, String word, String origin) {

		if (word.length() == 0) {
			TrieTree leaf = new TrieTree(true, null);
			leaf.tag = origin;
			t.children.put("$end", leaf);
		} else if (t.children.containsKey(word.substring(0, 1))) {
			addWord(t.children.get(word.substring(0, 1)), word.substring(1),
					origin);
		} else {
			TrieTree subt = new TrieTree(false, new HashMap<String, TrieTree>());
			subt.tag = word.substring(0, 1);
			t.children.put(subt.tag, subt);
			addWord(subt, word.substring(1), origin);
		}
	}

	/**
	 * @Description: 从字符串@param str中找到第一个匹配的关键词
	 * @param str
	 *            待检测字符串
	 * @param index
	 *            开始检测的偏移量
	 * @param t
	 *            用于匹配的trie树
	 * @return 匹配的关键词
	 */
	public static String find(String str, int index, TrieTree t) {
		String matchNode = null;
		if (t.children.containsKey("$end")) {
			matchNode = t.children.get("$end").tag;
		}

		if (index == str.length()) { // 到了字符串末尾
			return matchNode;
		} else {
			String tag = str.substring(index, index + 1);
			if (!t.children.containsKey(tag))
				return matchNode;
			else {
				String next = find(str, index + 1, t.children.get(tag));
				if (next != null) {
					matchNode = next;
				}
			}
			return matchNode;
		}
	}

	/**
	 * @Description:匹配输入的字符串，返回每个关键词和匹配上的下标列表，关键词已经去过空格。
	 * @param str
	 *            输入字符串
	 * @param isSplitted
	 *            是否是分词匹配
	 * @return 每个关键词和匹配上的下标列表
	 * @throws Exception
	 *             带分词匹配需要以空格开头，以空格结尾
	 */
	public Map<String, List<Integer>> detect(String str)  {
		int plusLen = 0;
		if (this.isSplit){
			if(!str.startsWith(" ")){
				str = " " + str;
				plusLen = 1;
			}
			if(!str.endsWith(" "))
				str = str + " ";
		}			
		int i = 0;
		// 每个词及其出现的位置
		Map<String, List<Integer>> matchMap = new HashMap<String, List<Integer>>();
		// char[] tmp = str.toCharArray();
		while (i < str.length()) {
			String next = find(str, i, this);
			List<Integer> idx = null;
			if (next != null) {
				String value = "";
				if(this.isSplit)
					//去掉前后空格
					value = next.substring(1, next.length() - 1);
				else
					value = next;
				idx = matchMap.get(value);
				if (idx == null)
					idx = new ArrayList<Integer>();
				if (this.isSplit)// 需要忽略空格
					idx.add(i + 1 - plusLen);
				else
					idx.add(i - plusLen);
				matchMap.put(value, idx);
				i += next.length();
				if (this.isSplit)// 分词匹配需要回退
					i--;
			} else {
				i++;
			}
		}
		return matchMap;
	}
	/**
	 * @Description:匹配输入的字符串，返回每个关键词和匹配上的下标列表，关键词已经去过空格。
	 * @param str
	 *            输入字符串
	 * @param isSplitted
	 *            是否是分词匹配
	 * @return 每个关键词和匹配上的下标列表
	 * @throws Exception
	 *             带分词匹配需要以空格开头，以空格结尾
	 */
	public Map<String, List<Integer>> detectReverse(String str)  {
		int plusLen = 0;
		if (this.isSplit){
			if(!str.startsWith(" ")){
				str = " " + str;
				plusLen = 1;
			}
			if(!str.endsWith(" "))
				str = str + " ";
		}			
		int i = 0;
		// 每个词及其出现的位置
		Map<String, List<Integer>> matchMap = new HashMap<String, List<Integer>>();
		// char[] tmp = str.toCharArray();
		while (i < str.length()) {
			String next = find(str, i, this);
			List<Integer> idx = null;
			if (next != null) {
				String value = "";
				if(this.isSplit)
					//去掉前后空格
					value = next.substring(1, next.length() - 1);
				else
					value = next;
				idx = matchMap.get(value);
				if (idx == null)
					idx = new ArrayList<Integer>();
				if (this.isSplit)// 需要忽略空格
					idx.add(i + 1 - plusLen);
				else
					idx.add(i - plusLen);
				matchMap.put(value, idx);
				i += next.length();
				if (this.isSplit)// 分词匹配需要回退
					i--;
			} else {
				i++;
			}
		}
		return matchMap;
	}
	/**
	 * @Description:正向最大匹配分词
	 * @param str
	 *            输入字符串
	 * @param isSplitted
	 *            是否是分词匹配
	 * @return 每个关键词和匹配上的下标列表
	 * @throws Exception
	 *             带分词匹配需要以空格开头，以空格结尾
	 */
	public Map<String, List<Integer>> seg(String str)  {
		if(str == null)
			return new HashMap<String, List<Integer>>();
		int plusLen = 0;
		if (this.isSplit){
			if(!str.startsWith(" ")){
				str = " " + str;
				plusLen = 1;
			}
			if(!str.endsWith(" "))
				str = str + " ";
		}			
		int i = 0;
		// 每个词及其出现的位置
		Map<String, List<Integer>> matchMap = new HashMap<String, List<Integer>>();
		// char[] tmp = str.toCharArray();
		while (i < str.length()) {
			String next = find(str, i, this);
			List<Integer> idx = null;
			if (next != null) {
				String value = "";
				if(this.isSplit)
					//去掉前后空格
					value = next.substring(1, next.length() - 1);
				else
					value = next;
				idx = matchMap.get(value);
				if (idx == null)
					idx = new ArrayList<Integer>();
				if (this.isSplit)// 需要忽略空格
					idx.add(i + 1 - plusLen);
				else
					idx.add(i - plusLen);
				matchMap.put(value, idx);
				i += next.length();
				if (this.isSplit)// 分词匹配需要回退
					i--;
			} else {
				if (TextTool.isEnglishChar(str.charAt(i)) ) {
					StringBuilder sb = new StringBuilder();				
					while( i < str.length() && (TextTool.isEnglishChar(str.charAt(i)))){
						sb.append(Character.toLowerCase(str.charAt(i++)));
					}
					
					idx = matchMap.get(sb.toString());
					if (idx == null)
						idx = new ArrayList<Integer>();
					if (this.isSplit)// 需要忽略空格
						idx.add(i + 1 - plusLen);
					else
						idx.add(i - sb.toString().length() - plusLen);
					matchMap.put(sb.toString(), idx);
					
				} else if(TextTool.isNumber(str.charAt(i))){
					StringBuilder sb = new StringBuilder();				
					while( i < str.length() && (TextTool.isNumber(str.charAt(i)) || str.charAt(i) == '.')){
						if(str.charAt(i) == '.'){
							if(i + 1 < str.length()){
								if(sb.toString().contains("."))
									break;
								if(!TextTool.isNumber(str.charAt(i+1))){
									break;
								}
							}else
								break;
						}
						sb.append(Character.toLowerCase(str.charAt(i++)));
					}
					
					idx = matchMap.get(sb.toString());
					if (idx == null)
						idx = new ArrayList<Integer>();
					if (this.isSplit)// 需要忽略空格
						idx.add(i + 1 - plusLen);
					else
						idx.add(i - sb.toString().length() - plusLen);
					matchMap.put(sb.toString(), idx);
				}else {
					String value = str.charAt(i) + "";
					idx = matchMap.get(value);
					if (idx == null)
						idx = new ArrayList<Integer>();
					if (this.isSplit)// 需要忽略空格
						idx.add(i + 1 - plusLen);
					else
						idx.add(i - plusLen);
					matchMap.put(value, idx);
					i++;
				}
			}
		}
		return matchMap;
	}
	public static List<Pair<String , Integer>> sort(Map<String, List<Integer>> subquery){
		List<Pair<String , Integer>> ret = new ArrayList<Pair<String , Integer>>();
		for(Entry<String , List<Integer>> en : subquery.entrySet()){
		  for(int i : en.getValue()){
		    Pair<String , Integer> kv = new Pair<String , Integer>(en.getKey() , i);
		    ret.add(kv);
		  }
		}
		Collections.sort(ret, new Comparator<Pair<String , Integer>>(){
			@Override
			public int compare(Pair<String , Integer> o1,
			    Pair<String , Integer> o2) {
				return o1.getSecond() - o2.getSecond();
			}
		});
		return ret;
	}	
	public String segment(String str){
		if(str == null)
			return null;
		Map<String, List<Integer>> info = seg(str);
		List<Pair<String , Integer>> sorted = sort(info);
		StringBuilder sb = new StringBuilder();
		for(Pair<String , Integer> en : sorted){
			sb.append(en.getFirst()).append(" ");
		}
		return sb.toString().trim();
	}
	/**
	 * @Description:匹配输入的字符串，返回每个关键词和匹配次数
	 * @param str
	 *            输入字符串
	 * @return 每个关键词和匹配次数
	 * @throws Exception
	 *             带分词匹配需要以空格开头，以空格结尾
	 */
	public Map<String, Integer> detectCount(String str)  {
		Map<String, List<Integer>> matchMap = detect(str);
		Map<String, Integer> matchCount = new HashMap<String, Integer>();
		for (Entry<String, List<Integer>> en : matchMap.entrySet())
			matchCount.put(en.getKey(), en.getValue().size());
		return matchCount;
	}

	/**
	 * @Description:匹配输入的字符串，返回匹配上的关键词
	 * @param str
	 *            输入字符串
	 * @return 每个关键词和匹配次数
	 * @throws Exception
	 *             带分词匹配需要以空格开头，以空格结尾
	 */
	public Set<String> detectString(String str)  {
		Map<String, List<Integer>> matchMap = detect(str);
		return matchMap.keySet();
	}
	public ArrayList<String> detectString2List(String str)  {
		Map<String, List<Integer>> matchMap = detect(str);
		ArrayList<String> list = new ArrayList<String>();
		for(Entry<String , List<Integer>> en : matchMap.entrySet())
			list.add(en.getKey());
		return list;
	}

	public static void main(String[] args) throws Exception {

		Set<String> starNodes = new HashSet<String>();
		starNodes.add("范冰冰");
//		starNodes = ReadSourceTool.readString("resource/cnCi.txt", "\t", 0);
		TrieTree starTree = new TrieTree(starNodes, false);

		String str = "范冰冰";
		Set<String> weathers = starTree.detectString(str);
		System.out.println(weathers.size());
	}
}
