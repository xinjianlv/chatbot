package com.wanda.chatbot.bot;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.wanda.chatbot.extractor.AbstractAnswerExtractor;
import com.wanda.chatbot.extractor.ChainAnswerProcess;
import com.wanda.chatbot.pojo.Answer;
import com.wanda.chatbot.process.IndexProcess;
import com.wanda.chatbot.process.InternetProcess;
import com.wanda.chatbot.process.PatternProcess;
import com.wanda.chatbot.utils.FileTool;
import com.wanda.chatbot.utils.Filters;
import com.wanda.chatbot.utils.TrieTree;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class Action {

	private static final Logger log = LoggerFactory.getLogger(Action.class);
	private static AbstractAnswerExtractor process = null;
	private static TrieTree dic = null;
	static {
		try {
			Set<String> nodes = FileTool.LoadDictKeysFromFile("./spam.dic", 0, Charset.forName("utf-8"));
			dic = new TrieTree(nodes, false);
			process = ChainAnswerProcess.getChainOfProcess();
		} catch (Exception e) {
			log.error(" ", e);
			System.exit(-1);
		}
	}

	public static void doServlet(FullHttpRequest req, NettyHttpServletResponse res) throws IOException, ParseException {
		ByteBuf buf = null;
		QueryStringDecoder qsd = new QueryStringDecoder(req.uri());
		Map<String, List<String>> mapParameters = qsd.parameters();
		List<String> l = mapParameters.get("q");
		String bestAnswer = "呵呵";
		if (null != l && l.size() > 0) {
			String q = Filters.filter(l.get(0));
			if (q.trim().length() > 0) {

				log.info("question=" + q);
				Answer answer = new Answer();
				process.process(q, answer);
				if(answer.getLevel() > 0)
					bestAnswer = answer.getAnswer();
				log.info("best answer :" + bestAnswer);
				JSONObject ret = new JSONObject();
				ret.put("tip", bestAnswer);
				buf = Unpooled.copiedBuffer(ret.toJSONString().getBytes());
			} else {
				JSONObject ret = new JSONObject();
				ret.put("tip", bestAnswer);
				buf = Unpooled.copiedBuffer(ret.toJSONString().getBytes());
			}

		} else {
			JSONObject ret = new JSONObject();
			ret.put("tip", "please input a question");
			buf = Unpooled.copiedBuffer("error".getBytes());
		}
		res.setContent(buf);
	}

}
