package com.wanda.chatbot.process;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanda.chatbot.filter.AbstractFilter;
import com.wanda.chatbot.filter.ChainFilter;
import com.wanda.chatbot.pojo.Answer;
import com.wanda.chatbot.pojo.AnswerFilter;
import com.wanda.chatbot.pojo.ExtraInformation;
import com.wanda.chatbot.utils.StringTool;
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
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.PriorityQueue;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class IndexProcess implements ProcessInterface {
	private Logger log = Logger.getLogger(IndexProcess.class);
	private static final int MAX_RESULT = 100;
	private static final int MAX_RESULT_SEARCH = 100;
	private static IndexSearcher indexSearcher = null;
	private static AbstractFilter chainFilter = null;
	public IndexProcess() {
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get("./index", new String[0])));
			indexSearcher = new IndexSearcher(reader);
			chainFilter = ChainFilter.getChainOfAnswerFilter();
		} catch (Exception e) {
			log.error(" ", e);
		}
	}
	public Answer getAnswer(String q){
		String bestAnswer = "呵呵";
		String wvAnswer = "呵呵";
		try {
			Query query = null;
			PriorityQueue<ScoreDoc> pq = new PriorityQueue<ScoreDoc>(MAX_RESULT) {

				@Override
				protected boolean lessThan(ScoreDoc a, ScoreDoc b) {
					return a.score < b.score;
				}
			};
			MyCollector collector = new MyCollector(pq);

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

			// extract question real word
			String questionMath = "";
			TokenStream ts = analyzer.tokenStream("myfield", new StringReader(q));
			// 获取词元文本属性
			CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
			// 重置TokenStream（重置StringReader）
			ts.reset();
			// 迭代获取分词结果
			while (ts.incrementToken()) {
				questionMath += term.toString();
			}
			// 关闭TokenStream（关闭StringReader）
			ts.end(); // Perform end-of-stream operations, e.g. set the
						// final offset.

			JSONArray result = new JSONArray();
			String firstAnswer = "";
			double topScore = -1;
			int maxlcs = -1;
			List<String> answerList = new ArrayList<String>(); 
			for (ScoreDoc d : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(d.doc);
				String question = doc.get("question");
				String answer = doc.get("answer");
				log.info("match question:" + question);
				log.info("answer:" + answer);
				AnswerFilter anFilter = new AnswerFilter(answer);
				chainFilter.process(answer, anFilter);
				if(anFilter.isMatchFilter()){
					log.info("there is a spam word. pass the answer");
					continue;
				}
				answerList.add(answer);
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
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Answer(bestAnswer , new ExtraInformation(IndexProcess.class.getSimpleName() , 200));
	}
	public String filterStopWords(String question) {
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
