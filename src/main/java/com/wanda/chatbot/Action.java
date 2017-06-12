package com.wanda.chatbot;

import com.alibaba.fastjson.JSONObject;
import com.wanda.chatbot.extractor.AbstractAnswerExtractor;
import com.wanda.chatbot.extractor.ChainAnswerProcess;
import com.wanda.chatbot.filter.AbstractFilter;
import com.wanda.chatbot.filter.ChainFilter;
import com.wanda.chatbot.pojo.Answer;
import com.wanda.chatbot.pojo.AnswerFilter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Action {


	private static final Logger log = LoggerFactory.getLogger(Action.class);
	private static AbstractAnswerExtractor extractor = null;
	private static AbstractFilter questionFilter = null;
	static {
		try {
			extractor = ChainAnswerProcess.getChainOfProcess();
			questionFilter = ChainFilter.getChainOfQuestionFilter();
		} catch (Exception e) {
			log.error(" ", e);
			System.exit(-1);
		}
	}
	public static void doServlet(FullHttpRequest req, NettyHttpServletResponse res) throws IOException, ParseException {
		log.info("pid : " + Searcher.pid);
		ByteBuf buf = null;
		QueryStringDecoder qsd = new QueryStringDecoder(req.uri());
		Map<String, List<String>> mapParameters = qsd.parameters();
		List<String> l = mapParameters.get("q");
		Answer bestAnswer = new Answer("呵呵");
		if (null != l && l.size() > 0) {
			AnswerFilter answerFilter = new AnswerFilter();
			questionFilter.process(l.get(0), answerFilter);
			String q = answerFilter.getAnswer();
			if (q.trim().length() > 0) {

				log.info("question=" + q);
				Answer answer = new Answer();
				extractor.process(q, answer);
				if(answer.getLevel() > 0)
					bestAnswer.setAnswer(answer);
			}

		}

        JSONObject ret = new JSONObject();
        ret.put("tip", bestAnswer.getAnswer());
        ret.put("status" , bestAnswer.getExtInfo().getPatternFlag());
        log.info("answer info : " + ret.get("tip"));
        log.info("ext_iofo : " + ret.get("status"));
        buf = Unpooled.copiedBuffer(ret.toJSONString().getBytes());
		res.setContent(buf);
	}

}
