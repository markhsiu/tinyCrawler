package com.lizi.crawler.jd;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.constant.TinyConstant;
import com.lizi.crawler.domain.Comment;
import com.lizi.crawler.domain.HttpContent;
import com.lizi.crawler.domain.Item;
import com.lizi.crawler.util.TinyHttpClient;
import com.lizi.crawler.util.UrlManager;

public class JDCommentHander {

	private static  int POOL_SIZE = 20;
	private static TinyHttpClient tinyCrawler = new TinyHttpClient();
	private Item item;
	private List<String> commentUrls = new ArrayList<>();
	
	@SuppressWarnings("unused")
	private JDCommentHander(){};
	public JDCommentHander(Item item){
		this.item = item;
		if(TinyConstant.THREAD_POOL_SIZE_COMMENT  > 0){
			POOL_SIZE = TinyConstant.THREAD_POOL_SIZE_COMMENT;
		}	
	}
	
	
	public List<Comment> getItemCommentAll() throws Exception  {
		this.intPages();
		List<Comment> commentList = new ArrayList<>();
		 
		int multiple =  commentUrls.size() / POOL_SIZE;

		if((commentUrls.size() % POOL_SIZE ) > 0){
			multiple  += 1;
		}
		
		long start = System.currentTimeMillis();
		for(int index = 0; index < multiple ;index ++){
			List<String> urls = null;
	
			if(index == multiple - 1 ){
				urls = commentUrls.subList(POOL_SIZE * index, commentUrls.size());
			} else {
				urls = commentUrls.subList(POOL_SIZE * index, POOL_SIZE * (index+1));
			}
	
			tinyCrawler.clear();
			tinyCrawler.Crawler( urls.toArray(new String[urls.size()]));
			List<HttpContent> data = tinyCrawler.getTextAll();
			commentList.addAll(getCommentJson(data));
		}
		long end = System.currentTimeMillis();
		System.out.println(" 商品所以评论花费时间："+(end-start)+" ms");
		return commentList;
	}
	
	
	private void intPages() throws Exception{
		String url = UrlManager.getCommentJson(item.getItemId(), 1);
		commentUrls.add(url);
		
		tinyCrawler.clear();
		tinyCrawler.Crawler(new String[] { url });
		List<HttpContent> list = tinyCrawler.getTextAll();
		if(list == null || list.size() < 1){
			return;
		}
		HttpContent data = list.get(0);
		
		if(data != null && data.getContent() != null){
			JSONObject json = JSON.parseObject(data.getContent());
			JSONObject productCommentSummary = json.getJSONObject("productCommentSummary");
			int commentCount = productCommentSummary.getIntValue("commentCount");
			item.setCommentCount(commentCount);
			item.setCommentPageMax(json.getIntValue("maxPage"));
		}
		
		long pageMax = item.getCommentPageMax();
		for (int index = 1; index < pageMax; index++) {
			 url = UrlManager.getCommentJson(item.getItemId(), index+1);
			 commentUrls.add(url);
		}
		
		System.out.println("商品：  "+item.getItemId());
		System.out.println("commentUrls : size " +commentUrls.size());
	}

	private List<Comment> getCommentJson(List<HttpContent> data) throws Exception {
		
		List<Comment> commentList = new ArrayList<>();

		for (HttpContent httpContent : data) {
			if(httpContent.getContent().indexOf("comments") < 1 
					|| !httpContent.getContent().startsWith("{")){
				System.err.println(" comment error : ulr == "+ httpContent.getUrl());
				continue;				
			}
			JSONObject json = JSON.parseObject(httpContent.getContent());
			
			//评论列表内容
			JSONArray commentsJson = json.getJSONArray("comments");
			if(commentsJson == null){
				System.out.println("url:::::::"+httpContent.getUrl());
				System.out.println("error:::::"+json);
				System.out.println("=========================");
				continue;
			}
			for (int i = 0; i < commentsJson.size(); i++) {
				JSONObject commentJson = commentsJson.getJSONObject(i);
				Comment comment = new Comment();
				
				String content = commentJson.getString("content");
				comment.setContent(content);
				
				JSONArray imagesJson = commentJson.getJSONArray("images");
				if(imagesJson != null){
					List<String> images = new ArrayList<>();
					for(int j = 0; j < imagesJson.size(); j++){
						JSONObject image = imagesJson.getJSONObject(j);
						String imgUrl = image.getString("imgUrl");
						if(imgUrl != null && imgUrl.trim().length() > 0){
							imgUrl = imgUrl.trim();
							if(!imgUrl.startsWith("http:") && !imgUrl.startsWith("https:")){
								imgUrl = "http:"+ imgUrl;
							}  
							images.add(imgUrl);
						}
					}
					comment.setImages(images);
				}
				comment.setNickname(commentJson.getString("nickname"));
				comment.setUserProvince(commentJson.getString("userProvince"));
				comment.setProductColor(commentJson.getString("productColor"));
				comment.setProductSize(commentJson.getString("productSize"));
				commentList.add(comment);
			}
		}
		return commentList;
	}
	
 
}
