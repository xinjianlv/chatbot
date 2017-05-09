package com.wanda.chatbotv1;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
//import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.PriorityQueue;
import org.apdplat.word.segmentation.Word;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanda.chatbotv1.utils.FileTool;
import com.wanda.chatbotv1.utils.StringTool;
import com.wanda.chatbotv1.utils.TrieTree;
import com.wanda.qa.datasource.BaiduDataSource;
import com.wanda.qa.datasource.DataSource;
import com.wanda.qa.files.FilesConfig;
import com.wanda.qa.model.CandidateAnswer;
import com.wanda.qa.model.Question;
import com.wanda.qa.model.QuestionType;
import com.wanda.qa.system.CommonQuestionAnsweringSystem;
import com.wanda.qa.system.QuestionAnsweringSystem;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class Action {
	
	private static final Logger log = Logger.getLogger(Action.class);
	private static final Logger logChat = Logger.getLogger("chat");
	
	private static final int MAX_RESULT = 10;
	private static final int MAX_TOTAL_HITS = 1000000;
	private static final int MAX_RESULT_SEARCH = 100;
	private static IndexSearcher indexSearcher = null;
	private static TrieTree dic = null;
	private static QuestionAnsweringSystem questionAnsweringSystem = null; 
	static {
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get("./index", new String [0])));
			Set<String> nodes = FileTool.LoadDictKeysFromFile("./spam.dic", 0, Charset.forName("utf-8"));
			dic = new TrieTree(nodes, false);
			List<String> files = new ArrayList<>();
			files.add(FilesConfig.personNameQuestions);
//			files.add(FilesConfig.locationNameQuestions);
//			files.add(FilesConfig.organizationNameQuestions);
//			files.add(FilesConfig.numberQuestions);
//			files.add(FilesConfig.timeQuestions);
			DataSource dataSource = new BaiduDataSource(files);

		        questionAnsweringSystem = new CommonQuestionAnsweringSystem();
		        questionAnsweringSystem.setDataSource(dataSource);
		} catch (Exception e) {
			log.error(" " , e);
			System.exit(-1);
		}
		indexSearcher = new IndexSearcher(reader);
	}
	
	public static void doServlet(FullHttpRequest req, NettyHttpServletResponse res) throws IOException, ParseException {
		ByteBuf buf = null;
		QueryStringDecoder qsd = new QueryStringDecoder(req.uri());
		Map<String, List<String>> mapParameters = qsd.parameters();
		List<String> l = mapParameters.get("q");
		
		if (null != l && l.size() > 0) {
			String bestAnswer = "呵呵";
			String q = l.get(0);
			if(q == null || q.trim().length() < 1){
				JSONObject ret = new JSONObject();
				ret.put("answer", bestAnswer);
				buf = Unpooled.copiedBuffer(ret.toJSONString().getBytes());
				res.setContent(buf);
				return;
			}
			
			if ((bestAnswer = getQAnswer(q)) != null) {
				JSONObject ret = new JSONObject();
				ret.put("result", bestAnswer);
				buf = Unpooled.copiedBuffer(ret.toJSONString().getBytes());
				res.setContent(buf);
				return;
			}
			
			log.info("question=" + q);
			List<String> clientIps = mapParameters.get("clientIp");
			String clientIp = "";
			if (null != clientIps && clientIps.size() == 1) {
				clientIp = clientIps.get(0);
				log.info("clientIp=" + clientIp);
			}
			
			Query query = null;
			PriorityQueue<ScoreDoc> pq = new PriorityQueue<ScoreDoc>(MAX_RESULT) {
				
				@Override
				protected boolean lessThan(ScoreDoc a, ScoreDoc b) {
					return a.score < b.score;
				}
			};
			MyCollector collector = new MyCollector(pq);
			
			JSONObject ret = new JSONObject();
			TopDocs topDocs = collector.topDocs();
			Analyzer analyzer = new IKAnalyzer(true);
			QueryParser qp = new QueryParser("question", analyzer);
			if (topDocs.totalHits == 0) {
				qp.setDefaultOperator(Operator.AND);
				query = qp.parse(q);
				log.info("lucene query=" + query.toString());
				topDocs = indexSearcher.search(query, MAX_RESULT_SEARCH);
				log.info("elapse " + collector.getElapse() + " " + collector.getElapse2());
			}
			
			if (topDocs.totalHits == 0) {
				qp.setDefaultOperator(Operator.OR);
				query = qp.parse(q);
				log.info("lucene query=" + query.toString());
				topDocs = indexSearcher.search(query, MAX_RESULT_SEARCH);
				log.info("elapse " + collector.getElapse() + " " + collector.getElapse2());
			}
			
			
			//extract question real word
			String questionMath = "";
			TokenStream ts  = analyzer.tokenStream("myfield", new StringReader(q));
		    //获取词元文本属性
		    CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		    //重置TokenStream（重置StringReader）
			ts.reset(); 
			//迭代获取分词结果
			while (ts.incrementToken()) {
			  questionMath += term.toString();
			}
			//关闭TokenStream（关闭StringReader）
			ts.end();   // Perform end-of-stream operations, e.g. set the final offset.

			
			JSONArray result = new JSONArray();
			String firstAnswer = "";
			double topScore = -1;
			int maxlcs = -1;
		
			for (ScoreDoc d : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(d.doc);
				String question = doc.get("question");
				String answer = doc.get("answer");
				Set<String> set = dic.detectString(answer);
				if(set.size() > 0){
					logChat.info(clientIp  + " contain spam word : "+ "[" + answer + "]");
					continue;
				}
				String lcs = StringTool.getLCS(questionMath, answer);
				JSONObject item = new JSONObject();
				item.put("question", question);
				if (firstAnswer.equals("")) {
					firstAnswer = answer;
				}
				item.put("answer", answer);
				item.put("score", d.score);
				item.put("lcs", lcs.length());
				item.put("doc", d.doc);
				result.add(item);
				
				if (topScore < 0)
					topScore = d.score;
				if (d.score < topScore)
					break;
				if (maxlcs < lcs.length()) {
					maxlcs = lcs.length();
					bestAnswer = answer;
				}
				
			}
			//答案候选集不好，不应变直接是问题的下一句，可能是连在一起的话。建过引时，应该多建几句。
			//现在是测试阶段，如果上线，应该在建索引时就把最好的答案计算出来。
			log.info("best answer :" + bestAnswer);
			ret.put("answer", bestAnswer);
			logChat.info(clientIp + " [" + q + "] [" + firstAnswer + "]");
			buf = Unpooled.copiedBuffer(ret.toJSONString().getBytes());
		} else {
			JSONObject ret = new JSONObject();
			ret.put("answer", "please input a question");
			buf = Unpooled.copiedBuffer(ret.toJSONString().getBytes());
		}
		res.setContent(buf);
	}
	
	
	private static String getQAnswer(String q){
		Question qa = questionAnsweringSystem.answerQuestion(q);
		Set<String> nrset = new HashSet<String>();
		for(Word w : qa.getQuestionWords()){
			if(w.getPartOfSpeech().getPos().equals("nr")){
				nrset.add(w.getText());
			}
		}
		if(qa.getQuestionType() == QuestionType.PERSON_NAME){
			for(CandidateAnswer canswer : qa.getAllCandidateAnswer()){
				if(nrset.contains(canswer.getAnswer())){
					continue;
				}
				return canswer.getAnswer();
			}
		}else{
			return null;
		}
		return null;
	}
	public String filterStopWords(String question){
		String re = "";
		
		return re;
	}
	
	public static class MyCollector extends TopDocsCollector<ScoreDoc> {

		protected Scorer scorer;
		protected int baseDoc;
		protected HashSet<Integer> set = new HashSet<Integer>();
		protected long elapse = 0;
		protected long elapse2 = 0;

		public long getElapse2() {
			return elapse2;
		}

		public void setElapse2(long elapse2) {
			this.elapse2 = elapse2;
		}

		public long getElapse() {
			return elapse;
		}

		public void setElapse(long elapse) {
			this.elapse = elapse;
		}

		protected MyCollector(PriorityQueue<ScoreDoc> pq) {
			super(pq);
		}


		
		private String getAnswer(int doc) throws IOException {
			Document d = indexSearcher.doc(doc);
			return d.get("answer");
		}

		@Override
		public LeafCollector getLeafCollector(LeafReaderContext paramLeafReaderContext) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean needsScores() {
			// TODO Auto-generated method stub
			return false;
		}
	}


}
