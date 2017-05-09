package com.wanda.chatbot.utils;


import java.util.Collection;

public class MathTool {

	public static double log(double num , double base){
		try{
			return  Math.log(num)/Math.log(base);
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public static double getSigma(Collection<Double> list){
		try{
			double variance = getVariance(list);
			return Math.sqrt(variance);
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	public static double getVariance(Collection<Double> list) throws Exception{
		try{
			if(list == null ||list.size() == 0)
				return 0;
			double x = getArithmeticMean(list);
			double sum = 0.0;
			for(double d : list){
				sum += (d - x)*(d - x);
			}
			return sum / list.size();
		}catch(Exception e){
			e.printStackTrace();
			throw(e);
		}
	}
	public static double getArithmeticMean(Collection<Double> list) throws Exception{
		try{
			if(list.size() == 0)
				return 0;
			double sum = 0.0;
			for(double d : list){
				sum += d;
			}
			return sum / list.size();
		}catch(Exception e){
			throw(e);
		}
	}
}
