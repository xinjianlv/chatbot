package com.wanda.chatbot.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class SQLhelper {
	public static void main(String[] args) {
		try{
			String root = "D:/data/wanda/";
			create( "product_category" ,root + "sql.txt");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String create(String tablename , String filename){
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + "`" + tablename + "`(");
		try{
			ArrayList<String> list = FileTool.LoadListFromFile(filename, 0, Charset.forName("utf-8"));
			for(String line : list){
				String [] arrays = line.split(StrUtils.tab);
				sb.append("`").append(arrays[0]).append("`").append(StrUtils.space);
				sb.append(arrays[1].replace(StrUtils.space, "")).append(StrUtils.space);
				if (arrays.length > 2)
					sb.append("COMMENT").append(StrUtils.space).append("'")
							.append(arrays[2]).append("'");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")").append("ENGINE=MYISAM  DEFAULT CHARSET=utf8;");
			System.out.println(sb.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
}
