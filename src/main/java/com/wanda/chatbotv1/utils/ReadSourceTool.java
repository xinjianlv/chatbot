package com.wanda.chatbotv1.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class ReadSourceTool {
	/**
	 * @function 读取包内的文件
	 * @param filePackage 文件路径
	 * @return 文件内容
	 */
	public static List<String> readResourceFile(String filePackage){
		List<String> sb = new ArrayList<String>();
		BufferedReader fin = null;
		try{
			InputStream is = ReadSourceTool.class.getClassLoader().getResourceAsStream(filePackage);
			fin = new BufferedReader(new InputStreamReader(is, "utf8"));
			String line = null;
			while ((line = fin.readLine()) != null) {
				sb.add(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != fin)
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return sb;
	}
	public static Object getObject(String filePackage) {
		Object obj = new Object();
		try {
			InputStream is = ReadSourceTool.class.getClassLoader().getResourceAsStream(filePackage);
			ObjectInputStream ois = new ObjectInputStream(is);
			obj = ois.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return obj;
	}
	public static HashMap<String, String> LoadStrStrFromFile(String filePackage,
			int lineOffset, int keyIndex, int valIndex, String split,
			Charset charset) throws IOException {
		String line = null;

		HashMap<String, String> dict = new HashMap<String, String>();

		InputStream is = ReadSourceTool.class.getClassLoader().getResourceAsStream(filePackage);
		BufferedReader sr = new BufferedReader(new InputStreamReader(is, "utf8"));
		
		for (int i = 0; i < lineOffset; ++i) {
			line = sr.readLine();
			if (line == null)
				return null;
		}

		while ((line = sr.readLine()) != null) {
			String[] tmp = line.trim().split(split);
			if (tmp.length <= Math.max(keyIndex, valIndex) || keyIndex < 0
					|| valIndex < 0) {
				System.out.println("Error key/val index");
				// continue;
				return null;
			}
			String strKey = tmp[keyIndex].trim();
			String strVal = tmp[valIndex].trim();
			dict.put(strKey, strVal);
		}
		sr.close();

		return dict;
	}
	public static HashMap<String, Double> LoadStrDoubleFromFile(String filePackage,
			int lineOffset, int keyIndex, int valIndex, String split,
			Charset charset) throws IOException {
		String line = null;
		
		HashMap<String, Double> dict = new HashMap<String, Double>();
		
		InputStream is = ReadSourceTool.class.getClassLoader().getResourceAsStream(filePackage);
		BufferedReader sr = new BufferedReader(new InputStreamReader(is, "utf8"));
		
		for (int i = 0; i < lineOffset; ++i) {
			line = sr.readLine();
			if (line == null)
				return null;
		}
		
		while ((line = sr.readLine()) != null) {
			String[] tmp = line.trim().split(split);
			if (tmp.length <= Math.max(keyIndex, valIndex) || keyIndex < 0
					|| valIndex < 0) {
				System.out.println("Error key/val index");
				// continue;
				return null;
			}
			String strKey = tmp[keyIndex].trim();
			double strVal = Double.parseDouble(tmp[valIndex].trim());
			dict.put(strKey, strVal);
		}
		sr.close();
		
		return dict;
	}
	
	/**
	 * @function 读取包内文件中指定列的数据
	 * @param filePackage 文件路径
	 * @param splitPunc 列分隔符
	 * @param contentIndex 指定列的下标
	 * @return
	 */
	public static Set<String> readString(String filePackage , String splitPunc , int contentIndex){
		 try{
			 Set<String> set = new HashSet<String>();
			 ArrayList<String> lst = (ArrayList<String>) readResourceFile(filePackage);
			 for(String line : lst){
				 String [] arrs = line.split(splitPunc);
				 String content = arrs[contentIndex];
				 set.add(content);
			 }
			 return set;
		 }catch(Exception e){
			 e.printStackTrace();
			 return null;
		 }
	}
	
	public static String readString(String filePath , String splitPunc){
		try{
			List<String> list = readResourceFile(filePath);
			StringBuilder sb = new StringBuilder();
			for(int i = 0 ; i< list.size() ; ++i){
				if(i == 0)
					sb.append(list.get(i));	
				else
					sb.append(splitPunc + list.get(i));
			}							
			return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		try{
			Set<String> set = readString("resource/weather", "\t", 0);
			System.out.println(set.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
