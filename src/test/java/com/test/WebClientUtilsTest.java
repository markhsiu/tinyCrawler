package com.test;

import com.lizi.crawler.domain.WebPage;
import com.lizi.crawler.util.WebClientUtils;

public class WebClientUtilsTest {

	public static void main(String[] args) throws InterruptedException {
		 String url = "http://www.lizi.com";

		 System.out.println("url:"+url);
		 WebPage page = new WebPage();
	     page.setUrl(url);
	     page.setHtmlSource(WebClientUtils.instance().getScollHtml(page));
	     System.out.println(page.getHtmlSource());
	     System.out.println("++++++++++++++++++++++++");
	}
	
}
