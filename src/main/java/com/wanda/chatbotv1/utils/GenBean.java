package com.wanda.chatbotv1.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class GenBean {

	public abstract String genImport();

	public static String genEnum(String name, List<String> list, String pattern) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("public enum ").append(name).append("{").append("\r\n");

			int index = 0;
			// GBID("GBID",0),
			for (String key : list) {
				String item = pattern + key + StrUtils.space + "(\"" + key
						+ "\"," + index++ + ")";
				if (index < list.size())
					item += (",");
				else
					item += (";");

				sb.append("\t").append(item).append("\r\n");
			}

			String function = "private String name ;enter	private int index;enter	private "
					+ name
					+ "(String name , int index) {enter		this.name = name;enter		this.index = index;enter	}enter	public String getName() {enter		return name;enter	}enter	public void setName(String name) {enter		this.name = name;enter	}enter	public int getIndex() {enter		return index;enter	}enter	public void setIndex(int index) {enter		this.index = index;enter	}";
			sb.append("\t").append(function.replaceAll("enter", "\r\n"))
					.append("\r\n");

			function = "	public static String getName(int index) {enter		for (enum_name c : enum_name.values()) {enter			if (c.getIndex() == index) {enter				return c.getName();enter			}enter		}enter		return null;enter	}enter	public static enum_name getEnum(String name){enter		for (enum_name c : enum_name.values()) {enter			if (c.getName().equals(name)) {enter				return c;enter			}enter		}enter		return null;enter	}enter	public static enum_name getEnum(int index){enter		for (enum_name c : enum_name.values()) {enter			if (c.getIndex() == index) {enter				return c;enter			}enter		}enter		return null;enter	}";
			sb.append(function.replaceAll("enum_name", name).replaceAll("enter", "\r\n"));
			sb.append("}");

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public static String genGetMethod(String name, String type) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("public ").append(type).append(" get")
					.append(formatName(name)).append("() {enter\treturn ")
					.append(name).append(";enter}");
			return sb.toString().replaceAll("enter", StrUtils.endwin);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String genSetMethod(String name) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("public void set").append(formatName(name))
					.append("(String ").append(name).append("){")
					.append(StrUtils.endwin);
			sb.append("\t").append("this.").append(name).append("=")
					.append(name).append(";").append(StrUtils.endwin);
			sb.append("}");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String formatName(String name) {
		try {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < name.length(); i++) {
				if (i == 0)
					sb.append(Character.toUpperCase(name.charAt(i)));
				else
					sb.append(name.charAt(i));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return name;
		}
	}

	// public static String parserValue(String name , String type){
	// try{
	//
	// }catch(Exception e){
	// log.error(" " , e);
	// }
	// }

	public abstract String toJSON();

	public abstract String fromJSON();

	public abstract String fromIndex();

	private String ENUM_NAME;

	// public boolean genBean(String enumName, String className,
	// Map<String, String> map) {
	// StringBuilder sb = new StringBuilder();
	// this.ENUM_NAME = enumName;
	// try {
	// String path = FileUtil.getFileNameFromFilePath(GenBean.class
	// .getName());
	//
	// return true;
	// } catch (Exception e) {
	// return false;
	// }
	//
	// }

	public static void main(String[] args) {
		try {
			String root = "./";
			// Map<String, String> map = FileTool.LoadStrStrFromFile(root
			// + "test.txt", 0, 0, 0, StrUtils.space,
			// Charset.forName("utf-8"));
			List<String> list = FileTool.LoadListFromFile(root + "test.txt", 0,
					Charset.forName("utf-8"));
			String result = genEnum("TagNameEnum", list, "");
			List<String> save = new ArrayList<String>();
			save.add(result);
			FileTool.SaveListToFile(save, root + "Bm_pos_storesalesEnum", false);
			System.out.println(result);
			// System.out.println(genSetMethod("price"));
			// System.out.println(genGetMethod("price" , "double"));
			// System.out.println(formatName("name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
