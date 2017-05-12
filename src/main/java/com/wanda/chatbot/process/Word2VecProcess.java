package com.wanda.chatbot.process;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ml.word2vec.Segment;
import com.ml.word2vec.Word2VEC;

public class Word2VecProcess {
	Logger log = Logger.getLogger(Word2VecProcess.class);
	private Word2VEC vec = null;
	public Word2VecProcess() {
		vec = new Word2VEC();
		try {
			vec.loadModel("./vectors.bin");
		} catch (IOException e) {
			log.error(" ", e);
		}
	}
	
	public String getSimilarityAnswer(String question , List<String> answers){
		
		double maxScore = -1;
		String similarity = null;
		float [] qvec = getSentenceVec(question);
		for(int i = 0 ; i < answers.size() ; i++){
			float [] avec = getSentenceVec(answers.get(i));
			double score = cosine(qvec , avec);
			if(maxScore < score){
				maxScore = score;
				similarity = answers.get(i);
			}
		}
		return similarity;
	}
	/**
	 * @description 获取一个名字的向量表示
	 * @param input 输入的字符串
	 * @return 向量数组
	 */
	private float [] getSentenceVec(String input){
		try{
			List<String> terms = Segment.seg(input);
			float [][] matrix = new float[terms.size()][];
			for(int i = 0 ; i < terms.size() ; i++){
				if (vec.contains(terms.get(i))){
					matrix[i] = vec.getWordVector(terms.get(i));
				}else{
					matrix[i] = new float [vec.getSize()];
				}
			}
			return vecSum(matrix);
		}catch(Exception e){
			log.error(" " , e);
		}
		return null;
	}
	/**
	 * @description 合并向量，求和
	 * @param matrix
	 * @return
	 */
	private static float [] vecSum(float [][] matrix){
		float [] vector = new float[matrix[0].length];
		for(int i = 0 ; i < matrix[0].length ; i++){
			for(int j = 0 ; j < matrix.length ; j++){
				vector[i] += matrix[j][i];
			}
		}
		return vector;
	}
	
	
	
	
	private  double cosine(float [] vec1 , float [] vec2){
		try{
			float vec12 = 0;
			float m1 = 0 , m2 = 0;
			for(int i = 0 ; i < vec1.length ; i++){
				vec12 += vec1[i] * vec2[i];
				m1 += vec1[i] * vec1[i];
				m2 += vec2[i] * vec2[i];
			}
			
			return vec12 / (Math.sqrt(m1) * Math.sqrt(m2));
		}catch(Exception e){
			log.error(" " , e);
		}
		return Float.NaN;
	}
	
}
