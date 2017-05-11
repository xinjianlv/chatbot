package com.wanda.chatbot.filter;

import java.nio.charset.Charset;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wanda.chatbot.pojo.AnswerFilter;
import com.wanda.chatbot.utils.FileTool;
import com.wanda.chatbot.utils.TrieTree;

public class SpamFilter extends AbstractFilter {
	Logger log = Logger.getLogger(SpamFilter.class);
	private TrieTree dic = null;

	public SpamFilter() {
		try {
			Set<String> nodes = FileTool.LoadDictKeysFromFile("./outer_dic/spam.dic", 0, Charset.forName("utf-8"));
			dic = new TrieTree(nodes, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void filter(String message, AnswerFilter answer) {
		Set<String> set = dic.detectString(answer.getAnswer());
		if (set.size() > 0) {
			log.info(" contain spam word : " + "[" + answer.getAnswer() + "]");
			answer.setMatchFilterTrue();
		}
	}

}
