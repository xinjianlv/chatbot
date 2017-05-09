package com.wanda.chatbotv1.utils;

public class Matrix {
	public static float[] getSum(float [][] matrix , int size){
		float [] vec = new float[size];
		for(int i = 0 ; i < size ; i++){
			float sum = 0f;
			for(int j = 0 ; j < matrix.length ; j++){
				sum += matrix[j][i];
			}
			vec[i] = sum;
		}
		return vec;
	}
	public static double getConsine(float [] vec1 , float []vec2){
		double Exy = 0.0 , xx =0 , yy = 0;
		for(int i = 0 ; i < vec1.length ; i++){
			Exy += (vec1[i] * vec2[i]);
			xx += (vec1[i] * vec1[i]);
			yy += (vec2[i] * vec2[i]);
		}
		return  Exy /(Math.sqrt(xx) * Math.sqrt(yy));
	}
}
