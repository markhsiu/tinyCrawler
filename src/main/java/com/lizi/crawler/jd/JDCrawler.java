package com.lizi.crawler.jd;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.lizi.crawler.ECCrawler;
import com.lizi.crawler.domain.WebPage;
import com.lizi.crawler.util.UrlManager;
import com.lizi.crawler.util.WebClientUtils;

/**
 * JD 爬虫
 *
 */
public class JDCrawler extends ECCrawler {

	private List<String> keywords;

	public JDCrawler(List<String> keywords) {
		this.keywords = keywords;
	}

	public JDCrawler(String keyword) {
		if (keywords == null) {
			keywords = new ArrayList<String>();
		}
		this.keywords.add(keyword);
	}

	@Override
	protected void visit(WebPage page) {
		tasks.add(new JDItemsCallable(page));
	}

	@Override
	protected void setPageUrls() throws Exception {
		if (keywords == null || keywords.size() == 0) {
			throw new RuntimeException(" 搜索关键字不能为 null !");
		}

		for (String keyword : keywords) {
			if (keyword.trim().length() > 0) {
				addSendUrl(keyword.trim());
			}
		}

	}

	private void addSendUrl(String keyword) throws Exception {
		String urlFisrt = getSeedUrl(1, keyword);
		int totalPage = getTotalPage(getPage(urlFisrt));
		System.out.println("totalPage: " + totalPage);
		System.out.println("urlFisrt: " + urlFisrt);
		pageUrls = new ArrayList<WebPage>();
		WebPage page;
		for (int index = 1; index <= totalPage; index++) {
			page = new WebPage();
			page.setUrl(getSeedUrl(index, keyword));
			pageUrls.add(page);
		}
	}

	/**
	 * 根据url获取Page实例
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private WebPage getPage(String url) throws Exception {
		WebPage page = new WebPage();
		page.setUrl(url);
		page.setHtmlSource(WebClientUtils.instance().getScollHtml(page));
		return page;
	}

	/**
	 * 获取查询商品总页数
	 * 
	 * @param page
	 * @return
	 */
	private int getTotalPage(WebPage page) {
		Document doc = Jsoup.parse(page.getHtmlSource());
		Element ele = null;
		try {
			ele = doc.select("div#J_bottomPage").select("span.p-skip >em").first().select("b").first();
		} catch (Exception e) {
			System.out.println("============================");
			System.out.println("没有分页");
			System.out.println("============================");
		}

		return ele == null ? 1 : Integer.parseInt(ele.text());
	}

	private String getSeedUrl(int page, String keyword) {
		return search(keyword, page);
	}

	// https://search.jd.com/Search?keyword=${keyword}&enc=utf-8&page=${page}
	private String search(String keyword, int page) {
		page = page * 2 - 1;

		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = "http://search.jd.com/Search?keyword=${keyword}&enc=utf-8&page=${page}&click=0";
		return UrlManager.convertTextTemplate(url, "keyword", keyword, "page", page);
	}

}
