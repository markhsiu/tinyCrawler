package com.lizi.crawler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.common.constant.TinyConstant;
import com.common.util.ClasspathFileReader;
import com.lizi.crawler.domain.WebPage;

/**
 *   WEB客户端
 * 
 */
public class WebClientUtils {

	public volatile  static WebDriverPool webDriverPool = null;
	
	public synchronized static WebClientUtils instance() {
		return new WebClientUtils();
	}

	public synchronized static void builder(){
		webDriverPool = new WebDriverPool(TinyConstant.WEBCLINET_POOL_SIZE);
	}
	
	public  static void end(){
		webDriverPool.closeAll();
	}
	
	public WebDriver webDriver;


	/**
	 *  wd.manage().window().maximize();
	 *  //当前页面
	 *	wd.get("http://www.163.com/");
	 *  //跳转新页面
	 *	wd.navigate().to("http://www.sina.com.cn");
	 *   //如果要回到之前的页面可以用
	 *	wd.navigate().to("http://www.163.com/");
	 *	或者 
	 *	wd.navigate().back();
	 * @param page
	 * @return
	 * @throws InterruptedException
	 */
	public String getScollHtml(WebPage page) throws InterruptedException {
		
		String sourceHtml = null;
		PhantomJSDriver webDriver = null;
		try {
			webDriver = webDriverPool.get();
			webDriver.manage().window().maximize();  
			webDriver.get(page.getUrl());
	
			sourceHtml  = webDriver.getPageSource();
			Thread.sleep(5000);
			
			
			int oldSize = sourceHtml.length();
			int newSize = 0;
			do {
				
				if(newSize > 0 ){
					oldSize = newSize;
				}
				
				webDriver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				Thread.sleep(400);
				sourceHtml = webDriver.getPageSource();
				newSize = sourceHtml.length();
			} while (oldSize != newSize);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(webDriver != null){
				webDriverPool.returnToPool(webDriver);
			}
		}
		page.setHtmlSource(sourceHtml);
		return sourceHtml;
	}
	
	
	public String getHtml(WebPage page) {
		WebDriver webDriver = setPhantomJSDriver(page);
		webDriver.get(page.getUrl());
		return webDriver.getPageSource();
	}

	/**
	 * 获取PhantomJsDriver(可以爬取js动态生成的html)
	 *
	 * @param page
	 * @return
	 */
	private WebDriver setPhantomJSDriver(WebPage page) {
		if (webDriver == null) {
			System.setProperty("phantomjs.binary.path", getPhantomjsPath());
			webDriver = new PhantomJSDriver();

		}

		webDriver.get(page.getUrl());
		return webDriver;
	}

	private String getPhantomjsPath() {
		ClasspathFileReader classpathFileReader = ClasspathFileReader.getInstance();
		String classpath = classpathFileReader.getClasspath();
		String fileSeparator = classpathFileReader.fileSeparator;
		String phantomjsPath = classpath + "phantomjs" + fileSeparator;
		return phantomjsPath + "phantomjs.exe";
	}

	/**
	 * 直接调用原生phantomJS(即不通过selenium)
	 *
	 * @param page
	 * @return
	 */
	public String getOrigPhantomJSDriver(WebPage page) {
		Runtime rt = Runtime.getRuntime();
		Process process = null;
		try {
			String cmd = "D:\\phantomjs\\bin\\phantomjs.exe "
					+ "D:\\phantomjs\\examples\\loadspeed.js " 
					+ page.getUrl().trim();
			System.out.println("CMD :"+cmd);
			process = rt.exec(cmd);
			InputStream in = process.getInputStream();
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(reader);
			StringBuffer sbf = new StringBuffer();
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
				sbf.append(tmp);
			}
			return sbf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
