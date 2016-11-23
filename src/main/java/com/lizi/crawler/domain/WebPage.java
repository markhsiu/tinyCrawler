package com.lizi.crawler.domain;

import java.io.Serializable;

public class  WebPage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3842844309717345591L;
	
	private String url;
	private String htmlSource;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHtmlSource() {
		return htmlSource;
	}
	public void setHtmlSource(String htmlSource) {
		this.htmlSource = htmlSource;
	}
	
	
}
