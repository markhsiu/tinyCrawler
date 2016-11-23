package com.lizi.crawler.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.common.util.FileItemIO;
import com.lizi.crawler.domain.HttpContent;

public class TinyHttpClient {

	private static PoolingHttpClientConnectionManager poolManager = null;
	private static final RequestConfig requestConfig ;
	static {
		requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();//设置请求和传输超时时间
		init();
	}
	
	private static void init() {
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", new PlainConnectionSocketFactory())
				.register("https", sslsf)
				.build();
		poolManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		// 将最大连接数增加到100
		poolManager.setMaxTotal(100);
		// 将每个路由基础的连接增加到3
		poolManager.setDefaultMaxPerRoute(3);

		HttpHost host = new HttpHost("lizi.com");// 针对的主机
		poolManager.setMaxPerRoute(new HttpRoute(host), 5);// 每个路由器对每个服务器允许最大5个并发访问

		//new IdleConnectionMonitorThread(poolManager).start();// 启动线程，5秒钟清空一次失效连接
	}
	
	
	private List<HttpContent> list = Collections.synchronizedList(new ArrayList<HttpContent>());
	private int pages;

	public void Crawler(String[] urls) throws InterruptedException, ClientProtocolException, IOException {
		CloseableHttpClient httpClient = this.getHttpClient();
		// create an array of URIs to perform GETs on
		String[] urisToGet = urls;

		// create a thread for each URI
		CountDownLatch latch = new CountDownLatch(urisToGet.length);
		// 为每个url创建一个线程，GetThread是自定义的类
		GetThread[] threads = new GetThread[urisToGet.length];
		for (int i = 0; i < threads.length; i++) {
			HttpGet httpget = new HttpGet(urisToGet[i]);
			httpget.setConfig(requestConfig);
			threads[i] = new GetThread(latch, httpClient, httpget);
		}

		// 启动线程
		for (int j = 0; j < threads.length; j++) {
			threads[j].start();
		}

		latch.await();// 等待
	}

	

	private CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolManager).build();
		
		/*
		 * CloseableHttpClient httpClient =
		 * HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接
		 */
		return httpClient;
	}
	
	public List<HttpContent> getTextAll() {
		return list;
	}

	public void clear() {
		list.clear();
	}

	public int getPageTotal() {
		return pages;
	}

	
	/**
	 * A thread that performs a GET.
	 */
	class GetThread extends Thread {

		private CountDownLatch latch;
		private final CloseableHttpClient httpClient;
		private final HttpGet httpget;
		private String url;

		public GetThread(CountDownLatch latch, CloseableHttpClient httpClient, HttpGet httpget) {
			this.latch = latch;
			this.httpClient = httpClient;
			this.httpget = httpget;
			
			if(httpget.getURI() == null || httpget.getURI().equals("")){
				throw new RuntimeException(" url cannot null");
			}
			this.url = httpget.getURI().toString();
		}

		@Override
		public void run() {
			String content = "";
			CloseableHttpResponse response = null;
			try {
				HttpContext context = HttpClientContext.create();
				response = httpClient.execute(httpget, context);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					byte[] bytearray = EntityUtils.toByteArray(entity);
					content = new String(bytearray, "gbk");
					
					HttpContent httpContent = new HttpContent();
					httpContent.setContent(content);
					httpContent.setUrl(url);
					list.add(httpContent);
				} 
				
			} catch (IOException e1) {
				System.out.println("error:  " + url);
				FileItemIO io = new FileItemIO("error");
				io.setAppend(true);
				try {
					io.open();
					io.write(url);
					io.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				e1.printStackTrace();
			} finally {
				latch.countDown();
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}

	}
	
	public static void main(String[] args) throws Exception {
		List<String> urls = new ArrayList<>();
		for(int i = 0; i < 257;i++){
			urls.add("http://www.lizi.com");
		}
		 
		long start = System.currentTimeMillis();
		TinyHttpClient tinyHttpClient = new TinyHttpClient();
		tinyHttpClient.Crawler(urls.toArray(new String[urls.size()]));
		long end = System.currentTimeMillis();
		System.out.println("花费时间 === "+(end-start)+" ms");
	}

}
