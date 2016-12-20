package com.main;

import com.lizi.crawler.domain.WebPage;
import com.lizi.crawler.util.WebClientUtils;

public class Liuxin {

	private static final String url = 
			"http://gsxt.gdgs.gov.cn//GSpublicity/GSpublicityList.html?service=entInfo&entNo=3c8f9781-014e-1000-e000-56d00a76b50b&regOrg=441920";
	
	public static void main(String[] args) throws InterruptedException {
		
		WebClientUtils.builder();
		try {
			WebPage page = new WebPage();
			page.setUrl(url);
			page.setHtmlSource(WebClientUtils.instance().getScollHtml(page));
			System.out.println(page.getHtmlSource());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			WebClientUtils.end();
		}
		
	}
}
