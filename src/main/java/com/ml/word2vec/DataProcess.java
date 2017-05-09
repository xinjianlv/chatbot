package com.ml.word2vec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class DataProcess {

	public static void merge(String [] inputs , String output , Charset charset){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(output, false));
			int c = 0;
			for(String input : inputs){
				BufferedReader sr = new BufferedReader(new InputStreamReader(
					new FileInputStream(input), charset));
				String line = null;
				while((line = sr.readLine()) != null){
					if (c++ % 100000 == 0)
						System.out.println("processed " + (c - 1) + " lines.");
					bw.write(line);
					bw.write("\n");
				}
				sr.close();
			}
			bw.flush();
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void chunk(String input , String output , int num){
		BufferedReader sr = null;
		BufferedWriter bw = null;
		try {
			sr = new BufferedReader(new InputStreamReader(new FileInputStream(input), Charset.forName("utf-8")));
			bw = new BufferedWriter(new FileWriter(output, false));
			String line = null;
			int c = 0;
			Queue<String> queue = new LinkedList<String>();
			for(int i = 0 ; i < num - 1 ; i++){
				queue.offer(sr.readLine());
			}
			while((line = sr.readLine()) != null){
				queue.offer(line);
				bw.write(getStrFromQueue(queue.iterator()) + "\n");
				if(c++ % 100000 == 0)
					System.out.println("chunk : processed " + (c - 1) + " lines.");
				queue.poll();
			}
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(sr != null){
			      try {
					sr.close();
			      } catch (IOException e) {
					e.printStackTrace();
			      }
				}
				if(	bw != null){
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
	}
	
	public static String getStrFromQueue(Iterator<String> it){
		StringBuilder sb = new StringBuilder();
		while(it.hasNext()){
			sb.append(it.next());
		}
		return sb.toString();
	}
	
	public static void generateVector(String input , String output){
		try{
			//./word2vec -train ~/Documents/DATA/corpus/ssa.out.seg  -size 100 -binary 1  -output ssa.out.model
			String cmdstring = "chmod 777 ./word2vec/word2vec";
			Process proc = Runtime.getRuntime().exec(cmdstring);
			cmdstring = "./word2vec/word2vec -size 100 -binary 1 -train " + input + " -output " + output;
			proc = Runtime.getRuntime().exec(cmdstring);
			proc.waitFor();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void seg(String input , String output){
		//构建IK分词器，使用smart分词模式
		Analyzer analyzer = new IKAnalyzer(true);
		//获取Lucene的TokenStream对象
	    TokenStream ts = null;
	    BufferedReader sr = null;
	    BufferedWriter wr = null;
		try {
			sr = new BufferedReader(new InputStreamReader(
					new FileInputStream(input), Charset.forName("utf-8")));
			wr = new BufferedWriter(new FileWriter(output, false));
			String line = null;
			int c = 0;
			while((line = sr.readLine()) != null){
				if(c++ % 100000 == 0)
					System.out.println("seg : processed " + (c - 1) + " lines.");
				ts = analyzer.tokenStream("myfield", new StringReader(line));
			    CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
			    
			    //重置TokenStream（重置StringReader）
				ts.reset(); 
				StringBuilder sb = new StringBuilder();
				//迭代获取分词结果
				while (ts.incrementToken()) {
					sb.append(term.toString()).append(" ");
				}
				wr.write(sb.toString().trim() + "\n");
				//关闭TokenStream（关闭StringReader）
				ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
				ts.close();
			}
			wr.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(sr != null){
		      try {
				sr.close();
		      } catch (IOException e) {
				e.printStackTrace();
		      }
			}
			if(	wr != null){
				try {
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	    }
	}

	public static void run(){
		String baseDir = "../word2vec/";
		String [] corpus = new String [] {baseDir + "corpus/ass.out" , baseDir + "corpus/srt.out" , baseDir + "corpus/ssa.out"};
		String corpusall = baseDir + "corpus/corpus.out";
//		merge(corpus, corpusall, Charset.forName("utf-8"));
		String corpus4vec = baseDir + "corpus/corpus.chunk.out";
		chunk(corpusall , corpus4vec , 1);
		String corpusSeg4vec = baseDir + "corpus/corpus.chunk.seg.out";
		seg(corpus4vec , corpusSeg4vec);
//		String corpusVectors = baseDir + "corpus/vectors.bin";
//		generateVector(corpusSeg4vec, corpusVectors);
	}
	public static void main(String[] args) {
//		generateVector(""  , "");
//		seg("README.md", "README.md.seg");
		run();
	}
}
