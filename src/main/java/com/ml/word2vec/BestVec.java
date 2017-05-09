package com.ml.word2vec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author nocml
 *
 */
/**
 * @author nocml
 *
 */
public class BestVec {
	private static final Logger log = Logger.getLogger(BestVec.class);
	private static Word2VEC vec = null;
	static {
		vec = new Word2VEC();
		try {
//			vec.loadModel("./word2vec/vector.bin");
			vec.loadModel("./word2vec/vector.chunk1.bin");
		} catch (IOException e) {
			log.error(" " , e);
		}
	}
	public static void main(String[] args) {
		String str1 = "哈哈哈歙呈俣伽";
		String str2 = "这是第五个中文分词的测试！";
		String str3 = "这是第三个中文分词的测试！";
		String str4 = "这是第四个中文分词的测试！";
		String str5 = "这是第二个中文分词的测试！";
		ArrayList<String> list = new ArrayList<String>();
		list.add(str1);
		list.add(str2);
		list.add(str3);
		list.add(str4);
		list.add(str5);
		int index = getBestVectorIndex(list);
		System.out.println("best index is : " + index );
	}
	
	public static boolean test(float [][] matrix){
		matrix[0][0] = -1;
		return true;
	}
	
	
	/**
	 * @description 获取一个名字的向量表示
	 * @param input 输入的字符串
	 * @return 向量数组
	 */
	private static float [] getSentenceVec(String input){
		try{
			List<String> terms = Segment.seg(input);
			float [][] matrix = new float[terms.size()][];
			for(int i = 0 ; i < terms.size() ; i++){
				matrix[i] = vec.getWordVector(terms.get(i));
			}
			matrix = removeNaNAndNull(matrix);
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
	
	/**
	 * @description 求平均向量
	 * @param matrix
	 * @return
	 */
	private static float [] vecMean(float [][] matrix){
		float [] sum = vecSum(matrix);
		float [] result = new float[sum.length];
		for(int i = 0 ; i < sum.length ; i++){
			result[i] = sum[i] / sum.length;
		}
		return result;
	}
	
	
	
	private static double cosine(float [] vec1 , float [] vec2){
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
	
	private static boolean removeoutlier(float[][] matrix) {
		boolean outlier = false;
		try {
			float[] mean = vecMean(matrix);
			ArrayList<Double> distancelist = new ArrayList<Double>();
			double meandis = 0;
			for (int i = 0; i < matrix.length; i++) {
				double dis = cosine(mean, matrix[i]);
				meandis += dis;
				distancelist.add(dis);
			}
			meandis /= distancelist.size();
			ArrayList<Double> distanceSort = new ArrayList<Double>(distancelist);
			Collections.sort(distanceSort);
			int q1_index = distancelist.size() / 4;
			int q3_index = distancelist.size() - q1_index;
			double q1 = distanceSort.get(q1_index);
			double q3 = distanceSort.get(q3_index);
			double iqr = q3 - q1;
			double min = q1 - 1.5 * iqr;
			double max = q3 + 1.5 * iqr;

			
			for (int i = 0; i < distanceSort.size(); i++) {
				double dis = distancelist.get(i);
				if (dis < min || dis > max) {
					distancelist.set(i, Double.NaN);
					Arrays.fill(matrix[i], Float.NaN);
					outlier = true;
				}
			}
			
		} catch (Exception e) {
			log.error(" ", e);
		}
		return outlier;
	}
	private static float [][] removeNaNAndNull(float [][] matrix){
		int size = 0;
		for(int i = 0 ; i < matrix.length ; i++){
			if(matrix[i] != null && !Float.isNaN(matrix[i][0] )){
				size++;
			}
		}
		float [][] reMatrix = new float[size][];
		size = 0;
		for(int i = 0 ; i < matrix.length ; i++){
			if(matrix[i] != null && !Float.isNaN(matrix[i][0]) )
				reMatrix[size++] = matrix[i];
		}
		return reMatrix;
	}
	public static int getBestVectorIndex(List<String> data){
		try{
			float [][] matrix = new float[data.size()][];
			float [][] oriMatrix = new float[data.size()][];
			for(int i = 0 ; i < matrix.length ; i++){
				matrix[i] = getSentenceVec(data.get(i));
				oriMatrix[i] = Arrays.copyOf(matrix[i], matrix[i].length);
			}
			
			while(removeoutlier(matrix)){
				matrix = removeNaNAndNull(matrix);
				System.out.println("pause !");
			}
			float [] mean = vecMean(matrix);
			double max = Double.MIN_VALUE;
			int minDistanceIndex = -1;
			for(int i = 0 ; i < oriMatrix.length ; i++){
				double dis = cosine(mean , oriMatrix[i]);
				if(dis > max){
					max = dis;
					minDistanceIndex = i;
				}
			}
			
			return minDistanceIndex;
			
		}catch(Exception e){
			log.error(" " , e);
		}
		return -1;
	}
	
}
