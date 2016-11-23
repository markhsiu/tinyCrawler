package com.lizi.crawler;

import com.common.constant.TinyConstant;
import com.lizi.crawler.domain.WebPage;
import com.lizi.crawler.util.WebClientUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * 电商平台爬虫
 *
 */
public abstract class ECCrawler  {

    protected static Logger log = Logger.getLogger(ECCrawler.class);
    protected ExecutorService executor = Executors.newFixedThreadPool(TinyConstant.THREAD_POOL_SIZE);
    protected List<Callable<ItemList>> tasks = new ArrayList<Callable<ItemList>>();
    protected ItemList itemList = new ItemList();
    
    protected List<WebPage> pageUrls;//分页信息
    protected abstract void visit(WebPage page);
    protected abstract void setPageUrls() throws Exception;  
	
	public void start()   {
		WebClientUtils.builder();
		try {
			 setPageUrls();
		     setItemUrls();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			WebClientUtils.end();
		}
	}

    
    public ItemList getData(){
        return  itemList;
    }

    private void setItemUrls() throws InterruptedException{
        for(WebPage page : pageUrls){
            this.visit(page);
        }
        
        // 调用该方法的线程会阻塞,直到tasks全部执行完成(正常完成/异常退出)  
        List<Future<ItemList>> results = executor.invokeAll(tasks);  
      
        // 任务列表中所有任务执行完毕,才能执行该语句  
        System.out.println("the result." + results.size());  
      
        executor.shutdown();  
      
        for (Future<ItemList> f : results)  {  
          
            if(!f.isCancelled() && f.isDone()){
            
                try {
                	ItemList data = f.get();
                	itemList.addAll(data);
                	if(data.size() < 60){
                		System.out.println(" maybe error ::: "+data.size());
                	}
    			} catch (ExecutionException e) {
    				e.printStackTrace();
    			}  
            } else {
            	System.out.println("出错："+f.toString());
            }
           
        }    
    }
 
}
