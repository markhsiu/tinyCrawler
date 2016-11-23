package com.lizi.crawler.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.common.util.FileItemIO;
import com.common.util.IOUtils;
import com.common.util.Tools;
import com.lizi.crawler.ECCrawler;
import com.lizi.crawler.ItemList;
import com.lizi.crawler.domain.Comment;
import com.lizi.crawler.domain.Item;
import com.lizi.crawler.jd.JDCommentHander;
import com.lizi.crawler.jd.JDCrawler;

@Component(value = "jdComment")
public class JdCommentController {
	
	public void start(List<String> search) {
		long start = System.currentTimeMillis();
		ECCrawler crawler = new JDCrawler( search);
		crawler.start();
		ItemList itemList = crawler.getData();
		
		
		System.out.println(" item size :" +itemList.size());
		
		System.out.println("========== item get 花费时间："+(System.currentTimeMillis() - start)+" ms");
		try {
			FileItemIO io = new FileItemIO("item");
			io.open();
			for(Item item : itemList){
				io.write(item.getItemId());
			}
			io.close();	
			System.out.println("========== item write花费时间："+(System.currentTimeMillis() - start)+" ms");
			this.crawlerItem(io.getFolder());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(" ========== 总花费时间："+(System.currentTimeMillis() - start)+" ms");
	}
	
	private void crawlerItem(String folder) throws Exception{
		List<String> data = IOUtils.readFileByLines(folder+"item.txt");
		List<String> items = new ArrayList<>();
		for (String text : data) {
			if(!Tools.isEmpty(text)){
				items.add(text.trim());
			}
		}
		
		for (String itemId : items) {
			JDCommentHander jdCommentHander = new JDCommentHander(new Item(itemId));
			List<Comment> comments = jdCommentHander.getItemCommentAll();
			if (comments != null && comments.size() > 0) {
				this.crawlerComment(comments, itemId);
				this.crawlerImage(comments, itemId);
				this.crawlerStyle(comments, itemId);
			}  
		}
		
	}
	
	/**
	 * 评论内容
	 * @param comments
	 * @param itemId
	 * @throws Exception
	 */
	private void crawlerComment(List<Comment> comments, String itemId) throws Exception{
	
		FileItemIO io = new FileItemIO("comment",itemId);
		io.open();
		for (Comment comment : comments) {
			io.write(comment.getContent());
		}
		io.close();	
	}
	
	/**
	 * 评论图片
	 * @param comments
	 * @throws Exception
	 */
	private void crawlerImage(List<Comment> comments,String itemId) throws Exception{
		
		FileItemIO io = new FileItemIO("images",itemId);
		io.open();
		for (Comment comment : comments) {
			List<String> images = comment.getImages();
			if(images == null || images.size() == 0){
				continue;
			}
			for (String image : images) {
				if (Tools.isEmpty(image)) {
					continue;
				}
				if(comment.getImages() != null){
					io.write(image); 
				}
				
			}
		}
		
		io.close();
	}
 
	/**
	 * sku规格
	 * @param comments
	 * @throws Exception
	 */
	private void crawlerStyle(List<Comment> comments,String itemId) throws Exception{
		
		FileItemIO io = new FileItemIO("styles",itemId);
		io.open();
		for (Comment comment : comments) {
			
			String userProvince = comment.getUserProvince();
			String productColor = comment.getProductColor();
			String productSize = comment.getProductSize();
			if(Tools.isEmpty(userProvince)){
				userProvince = "无";
			}
			if(Tools.isEmpty(productColor)){
				productColor = "无";
			}
			if(Tools.isEmpty(productSize)){
				productSize = "无";
			}
			io.write(String.format("%s %s %s", userProvince,productColor,productSize)); 
		}
		
		io.close();
	}
	 
}
