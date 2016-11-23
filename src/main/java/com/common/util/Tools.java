package com.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 */
public class Tools {

	/**
	 * 判断字符窜是否等于null、""," ","null"
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return StringUtils.isBlank(str) || "null".equals(str);
	}

	/**
	 * 判断字符窜是否不等于null、""," ","null"
	 * 
	 * @param str
	 * @return
	 */
	public static boolean notEmpty(String str) {
		return !StringUtils.isBlank(str) && !"null".equals(str);
	}

	/**
	 * 检查所提供的String是否全是数字
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0,1,2,3,4,5,6,7,8,9,.]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static boolean isInteger(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return str.matches("[0-9]+");
	}

	public static String getTextFromHTML(String htmlStr) {
		Document doc = Jsoup.parse(htmlStr);
		String text = doc.text();
		// remove extra white space
		StringBuilder builder = new StringBuilder(text);
		int index = 0;
		while (builder.length() > index) {
			char tmp = builder.charAt(index);
			if (Character.isSpaceChar(tmp) || Character.isWhitespace(tmp)) {
				builder.setCharAt(index, ' ');
			}
			index++;
		}
		text = builder.toString().replaceAll(" +", " ").trim();
		return text;
	}

}
