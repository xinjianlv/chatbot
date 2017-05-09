package com.wanda.chatbotv1.bot;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;

import com.alibaba.fastjson.JSONObject;
import com.wanda.chatbotv1.answer.IndexAnswer;
import com.wanda.chatbotv1.answer.PatternAnswer;
import com.wanda.chatbotv1.utils.FileTool;
import com.wanda.chatbotv1.utils.Filters;
import com.wanda.chatbotv1.utils.TrieTree;
import com.wanda.qa.datasource.BaiduDataSource;
import com.wanda.qa.datasource.DataSource;
import com.wanda.qa.files.FilesConfig;
import com.wanda.qa.system.CommonQuestionAnsweringSystem;
import com.wanda.qa.system.QuestionAnsweringSystem;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class Action {

	private static final Logger log = Logger.getLogger(Action.class);

	private static IndexAnswer indexAnswer = null;
	private static TrieTree dic = null;
	private static PatternAnswer patternAnswer = null;
//	private static QuestionAnsweringSystem questionAnsweringSystem = null; 
	static {
		try {
			indexAnswer = new IndexAnswer();
			Set<String> nodes = FileTool.LoadDictKeysFromFile("./spam.dic", 0, Charset.forName("utf-8"));
			dic = new TrieTree(nodes, false);
			patternAnswer = new PatternAnswer();
			patternAnswer.reLoadPattern();
			List<String> files = new ArrayList<>();

			files.add(FilesConfig.personNameQuestions);
//			files.add(FilesConfig.locationNameQuestions);
//			files.add(FilesConfig.organizationNameQuestions);
//			files.add(FilesConfig.numberQuestions);
//			files.add(FilesConfig.timeQuestions);
//			DataSource dataSource = new BaiduDataSource(files);
//			questionAnsweringSystem = new CommonQuestionAnsweringSystem();
//			questionAnsweringSystem.setDataSource(dataSource);
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
				bestAnswer = patternAnswer.getAnswer(q);
				if(bestAnswer == null || bestAnswer.trim().length() == 0)
					bestAnswer = indexAnswer.getAnswer(q);

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
