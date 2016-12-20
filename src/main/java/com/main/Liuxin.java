package com.main;

import com.lizi.crawler.domain.WebPage;
import com.lizi.crawler.util.WebClientUtils;

public class Liuxin {

	private static final String url = 
			"http://gsxt.gdgs.gov.cn/aiccips/GSpublicity/GSpublicityList.html?service=entInfo_i1hWfEEgMLQ7xAUni96soum8L3ko11c82v1+R9AVE8MYNDfrXVCs/2yxfT4dfkNy-7vusEl1hPU+qjV70QwcUXQ==";
		
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
