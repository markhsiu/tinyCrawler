package com.lizi.crawler.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlManager {
	
 
	/**
	 * 获取评论内容json
	 * @param productId
	 * @param page
	 * @return
	 */
	public  static String getCommentJson(String productId,long page ) {
		
		String url = "http://sclub.jd.com/comment/productPageComments.action?productId=${productId}&score=0&sortType=3&page=${page}&pageSize=10&callback=";
		return  convertTextTemplate(url, "productId", productId,"page", page);
	}
	
	
	public static String convertTextTemplate(String text,Object... params){
		int length = params.length ;
		if(length < 2){
			return text;
		}
		
		if(length % 2 == 1){
			length -- ;
		}
		Map<String,Object> paramMap = new HashMap<>(); 
		for(int index = 0; index < length;index+=2){
			paramMap.put((String)params[index], params[index+1]);
		}
		
		//用参数替换模板中的${}变量  
		Matcher m = Pattern.compile("\\$\\{\\w+\\}").matcher(text);  
		  
		StringBuffer sb = new StringBuffer();  
		while (m.find()) {  
		    String param = m.group(); //${xx}  
		    Object value = paramMap.get(param.substring(2, param.length() - 1).trim());  
		    m.appendReplacement(sb, value==null?"":value.toString());  
		}  
		
		String url = m.appendTail(sb).toString();
		return url;
	}
	
 
	 
}
