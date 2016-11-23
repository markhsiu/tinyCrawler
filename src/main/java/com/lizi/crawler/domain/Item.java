package com.lizi.crawler.domain;

import com.alibaba.fastjson.JSON;

/**
 * 商品信息
 *
 */
public class Item {

	private String platform;//平台
	private String name;// 商品名
	private String itemId;// 商品ID
	private String url;// 商品链接
	private Double price;// 价格
	
	private long commentCount = 0;// 评价数
	private long commentPageMax = 0;
	
	public Item() {	
	}
	
	public Item(String itemId) {
		this.itemId = itemId;
	}


	public long getCommentPageMax() {
		return commentPageMax;
	}

	public void setCommentPageMax(long commentPageMax) {
		this.commentPageMax = commentPageMax;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

    	 

	public long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(long commentCount) {
		this.commentCount = commentCount;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
