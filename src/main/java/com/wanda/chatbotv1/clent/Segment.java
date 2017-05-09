package com.wanda.chatbotv1.clent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Segment {

	public static void main(String[] args) {
		String root = "/Users/nocml/Documents/DATA/corpus/";
		segment(root + "ass.out" , root + "ass.out.seg");
	}

	public static void segment(String input, String output) {
		try {
			BufferedReader sr = new BufferedReader(
					new InputStreamReader(new FileInputStream(input), Charset.forName("utf-8")));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(output), Charset.forName("utf-8")));
			String preline2 = sr.readLine();
			String preline1 = sr.readLine();
			String cline = null;
			Analyzer analyzer = new IKAnalyzer(true);

			// 获取Lucene的TokenStream对象
			TokenStream ts = null;
			int c = 0;
			while ((cline = sr.readLine()) != null) {
				if(c % 100000 == 0)
					System.out.println("processed " + c + ".");
				c++;
				String alls = preline2 + "。" + preline1 + "。" + cline;
				preline2 = preline1;
				preline1 = cline;
				ts = analyzer.tokenStream("myfield", new StringReader(alls));
				CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
				StringBuilder sb = new StringBuilder();
				ts.reset(); 
				while (ts.incrementToken()) {
					sb.append(term.toString()).append(" ");
				}
			
				bw.write(sb.toString().trim() + "\n");
				ts.end(); 
				ts.close();
			}
			
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
