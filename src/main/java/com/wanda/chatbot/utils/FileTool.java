package com.wanda.chatbot.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FileTool {

	public static HashMap<Integer, Double> LoadIdValFromFile(String filePath,
			int lineOffset, int keyIndex, int valIndex, String split,
			Charset charset) throws IOException, FileNotFoundException {
		File file = new File(filePath);
		if (!file.exists()) {
			System.err.println(filePath + " not exist...");
			return null;
		}
		String line = null;

		HashMap<Integer, Double> dict = new HashMap<Integer, Double>();

		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charset));

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

			// key
			Integer k = null;
			k = Integer.parseInt(strKey);

			// value
			Double v = null;
			v = Double.parseDouble(strVal);
			dict.put(k, v);
		}
		sr.close();

		return dict;
	}

	public static HashMap<Integer, String> LoadIdStrFromFile(String filePath,
			int lineOffset, int keyIndex, int valIndex, String split,
			Charset charset) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			System.err.println(filePath + " not exist...");
			return null;
		}
		String line = null;

		HashMap<Integer, String> dict = new HashMap<Integer, String>();

		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charset));

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

			// key
			Integer k = null;
			k = Integer.parseInt(strKey);

			// value

			dict.put(k, strVal);
		}
		sr.close();

		return dict;
	}

	
	public static HashMap<String, Double> LoadStrValFromFile(String filePath,
			int lineOffset, int keyIndex, int valIndex, String split,
			Charset charset) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			System.err.println(filePath + " not exist...");
			return null;
		}
		String line = null;

		HashMap<String, Double> dict = new HashMap<String, Double>();

		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charset));

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

			// key
			String k = strKey;

			// value
			Double v = null;
			v = Double.parseDouble(strVal);
			dict.put(k, v);
		}
		sr.close();

		return dict;
	}


	public static HashMap<String, String> LoadStrStrFromFile(String filePath,
			int lineOffset, int keyIndex, int valIndex, String split,
			Charset charset) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			System.err.println(filePath + " not exist...");
			return null;
		}
		String line = null;

		HashMap<String, String> dict = new HashMap<String, String>();

		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charset));

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
	
	public static HashMap<String, Integer> LoadStrIntValFromFile(
			String filePath, int lineOffset, int keyIndex, int valIndex,
			int minValue, String split, Charset charset) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			System.err.println(filePath + " not exist...");
			return null;
		}
		String line = null;

		HashMap<String, Integer> dict = new HashMap<String, Integer>();

		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charset));

		for (int i = 0; i < lineOffset; ++i) {
			line = sr.readLine();
			if (line == null)
				System.out.println("line == null");
				return null;
		}

		while ((line = sr.readLine()) != null) {
			String[] tmp = line.trim().split(split);
			if (tmp.length <= Math.max(keyIndex, valIndex) || keyIndex < 0
					|| valIndex < 0) {
				System.out.println("Error key/val index");
				continue;
				// return null;
			}

			String strKey = tmp[keyIndex].trim();
			String strVal = tmp[valIndex].trim();

			// key
			String k = strKey;

			// value
			int v = 0;
			try{
			v = Integer.parseInt(strVal);
			}catch(Exception e){
				System.out.println("pause !");
			}
			if (v > minValue)
				dict.put(k, v);
		}
		sr.close();

		return dict;
	}
	/**
	 * @function 读取包内的文件
	 * @param filePackage 文件路径
	 * @return 文件内容
	 */
	public static List<String> readResourceFile(String filePackage){
		List<String> sb = new ArrayList<String>();
		BufferedReader fin = null;
		try{
			InputStream is = FileTool.class.getClassLoader().getResourceAsStream(filePackage);
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
	public static HashSet<String> LoadDictKeysFromFile(String filePath,
			int lineOffset, Charset charset) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			System.err.println(filePath + " not exist...");
			return null;
		}
		String line = null;

		HashSet<String> dict = new HashSet<String>();

		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charset));

		for (int i = 0; i < lineOffset; ++i) {
			line = sr.readLine();
			if (line == null)
				return null;
		}

		while ((line = sr.readLine()) != null) {

			// String strKey = line.trim();
			String strKey = line;
			// key
			String k = strKey.trim();

			dict.add(k);
		}
		sr.close();

		return dict;
	}
	public static HashSet<Double> LoadDoubleKeysFromFile(String filePath,
			int lineOffset, Charset charset) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			System.err.println(filePath + " not exist...");
			return null;
		}
		String line = null;
		
		HashSet<Double> dict = new HashSet<Double>();
		
		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charset));
		
		for (int i = 0; i < lineOffset; ++i) {
			line = sr.readLine();
			if (line == null)
				return null;
		}
		
		while ((line = sr.readLine()) != null) {
			
			// String strKey = line.trim();
			String strKey = line;
			// key
			String k = strKey;
			try{
			dict.add(Double.parseDouble(k));
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
		}
		sr.close();
		
		return dict;
	}

	public static ArrayList<String> LoadListFromFile(String filePath,
			int lineOffset, Charset charset) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			System.err.println(filePath + " not exist...");
			return null;
		}
		String line = null;

		ArrayList<String> dict = new ArrayList<String>();

		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charset));

		for (int i = 0; i < lineOffset; ++i) {
			line = sr.readLine();
			if (line == null)
				return null;
		}

		while ((line = sr.readLine()) != null) {

			// String strKey = line.trim();
			String strKey = line;
			// key
			String k = strKey;

			dict.add(k);
		}
		sr.close();

		return dict;
	}

	public static <T> void SaveListToFile(ArrayList<T> list, String filePath,
			boolean saveSize, Charset charset) throws IOException {
		File file = new File(filePath);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), charset));
		if (saveSize) {
			bw.write(list.size() + " entries\n");
		}

		for (T entry : list) {
			bw.write(entry.toString() + "\n");
		}

		bw.close();
	}
	
	public static <T> void SaveListToFile(List<T> list, String filePath,
			boolean isAttach) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, isAttach));

		for (T entry : list) {
			bw.write(entry.toString() + "\n");
		}

		bw.flush();
		bw.close();
	}

	public static <T, K> void SaveDictToFile(HashMap<T, K> dict,
			String filePath, boolean saveSize, Charset charset)
			throws IOException {
		File file = new File(filePath);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), charset));
		if (saveSize) {
			bw.write(dict.size() + " entries\n");
		}

		for (Map.Entry<T, K> entry : dict.entrySet()) {
			bw.write(entry.getKey().toString() + "\t"
					+ entry.getValue().toString() + "\n");
		}
		bw.close();
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			} else {
				System.out.println("不存在 ");
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错 ");
			e.printStackTrace();

		}
	}

	public static void DelDir(String filepath) throws IOException {
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory())
						DelDir(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					else
						delFile[j].delete();
				}
				f.delete();
			}
		}
	}
	
	public static String readString(String filePath , String splitPunc , Charset charset){
		try{
			ArrayList<String> list = LoadListFromFile(filePath, 0 , charset);
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
	static public boolean saveList2File(ArrayList<String> data ,String path ,  boolean saveByMaxSize , int maxsize){
		try{			
			if(!saveByMaxSize || data.size() > maxsize){
				FileTool.SaveListToFile(data, path, true);
				data.clear();
				return true;
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	
}
