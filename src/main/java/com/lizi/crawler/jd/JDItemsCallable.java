package com.lizi.crawler.jd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.common.util.Tools;
import com.lizi.crawler.ItemList;
import com.lizi.crawler.ItemsCallable;
import com.lizi.crawler.domain.Item;
import com.lizi.crawler.domain.WebPage;
import com.lizi.crawler.util.Platform;
import com.lizi.crawler.util.WebClientUtils;

public  class  JDItemsCallable extends ItemsCallable {

	private WebPage page;
	private ItemList itemList ;  
 
	public JDItemsCallable(WebPage page){
		this.page = page;
	}
	
	@Override
	public  ItemList call() throws Exception {
		itemList = new ItemList();
		this.addItem();
		return itemList;
	}

 
	private void addItem() {
		
		try {
			String html = WebClientUtils.instance().getScollHtml(page);
			Document doc = Jsoup.parse(html);
			
			 Elements  eles = doc.select("li.gl-item");
			if (!eles.isEmpty()) {
				Item g;
				for (Element ele : eles) {
					g = new Item();
					g.setPlatform(Platform.JD);// 电商平台
					// 价格
					String priceStr = ele.select("[class=p-price] i").text();
					if (Tools.isNumeric(priceStr) &&Tools.notEmpty(priceStr)) {
						g.setPrice(Double.parseDouble(priceStr));
					} else {
						g.setPrice(-1D);
					}
					
					// 商品ID
					g.setItemId(ele.attr("data-sku").toString());
					
					// 商品名
					g.setName(ele.select("[class=p-name] em").text());
					// 商品链接
					g.setUrl(ele.select("[class=p-name] a").attr("href").toString());
					if(Tools.isEmpty(g.getItemId())){
						continue;
					}
					itemList.add(g);
				}
			} else {
				System.out.println("else is empty");
				System.out.println(page.getUrl());
				System.out.println(html);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
}
