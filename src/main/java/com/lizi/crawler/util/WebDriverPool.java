package com.lizi.crawler.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.common.util.ClasspathFileReader;

/**
 * web驱动池
 */
public class WebDriverPool {

	private Logger logger = Logger.getLogger(getClass());

	private final static int DEFAULT_CAPACITY = 5;

	private final int capacity;

	private final static int STAT_RUNNING = 1;

	private final static int STAT_CLODED = 2;

	private AtomicInteger stat = new AtomicInteger(STAT_RUNNING);

	/*
	 * new fields for configuring phantomJS
	 */
	private PhantomJSDriver  mDriver = null;
	private static final String phantomjs_driver_loglevel = "INFO";
	private static String phantomjs_exec_path = null;// bin/phantomjs

	private static DesiredCapabilities sCaps;

	/**
	 * Configure the GhostDriver, and initialize a WebDriver instance. This part
	 * of code comes from GhostDriver.
	 * https://github.com/detro/ghostdriver/tree/
	 * master/test/java/src/test/java/ghostdriver
	 * 
	 */
	public void configure() throws IOException {
		ClasspathFileReader classpathFileReader = ClasspathFileReader
				.getInstance();
		String classpath = classpathFileReader.getClasspath();
		String fileSeparator = classpathFileReader.fileSeparator;
		classpath = classpath + "phantomjs" + fileSeparator;
		phantomjs_exec_path = classpath + "phantomjs.exe";
		
		// Prepare capabilities
		sCaps = new DesiredCapabilities();
		sCaps.setJavascriptEnabled(true);
		sCaps.setCapability("takesScreenshot", false);

		// "phantomjs_exec_path"
		if (phantomjs_exec_path != null) {
			sCaps.setCapability(
					PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					phantomjs_exec_path);
		} else {
			throw new IOException(String.format("Property '%s' not set!",
					PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY));
		}

		// Disable "web-security", enable all possible "ssl-protocols" and
		// "ignore-ssl-errors" for PhantomJSDriver
		// sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new
		// String[] {
		// "--web-security=false",
		// "--ssl-protocol=any",
		// "--ignore-ssl-errors=true"
		// });

		ArrayList<String> cliArgsCap = new ArrayList<String>();
		cliArgsCap.add("--web-security=false");
		cliArgsCap.add("--ssl-protocol=any");
		cliArgsCap.add("--ignore-ssl-errors=true");
		sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,cliArgsCap);

		// Control LogLevel for GhostDriver, via CLI arguments
		sCaps.setCapability(
				PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
				new String[] { "--logLevel=" + phantomjs_driver_loglevel });

		mDriver = new PhantomJSDriver(sCaps);

	}

	/**
	 * store webDrivers created
	 */
	private List<WebDriver> webDriverList = Collections
			.synchronizedList(new ArrayList<WebDriver>());

	/**
	 * LinkedBlockingDeque类实现了BlockingDeque接口。
	 * LinkedBlockingDeque是一个双端队列，如果一个线程试图从一个空的队列中取出元素，
	 * 它将被阻塞住，不管线程是从哪一端去获取元素。，向其中加入元素或从中取出元素都是线程安全的
	 * store webDrivers available
	 */
	private BlockingDeque<PhantomJSDriver> innerQueue = new LinkedBlockingDeque<PhantomJSDriver>();

	public WebDriverPool(int capacity) {
		this.capacity = capacity;
	}

	public WebDriverPool() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public PhantomJSDriver get() throws InterruptedException {
		checkRunning();
		PhantomJSDriver poll = innerQueue.poll();
		if (poll != null) {
			return poll;
		}
		if (webDriverList.size() < capacity) {
			synchronized (webDriverList) {
				if (webDriverList.size() < capacity) {

					// add new WebDriver instance into pool
					try {
						configure();
						innerQueue.add(mDriver);
						webDriverList.add(mDriver);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return innerQueue.take();
	}

	public void returnToPool(PhantomJSDriver webDriver) {
		checkRunning();
		innerQueue.add(webDriver);
	}

	private void checkRunning() {
		if (!stat.compareAndSet(STAT_RUNNING, STAT_RUNNING)) {
			throw new IllegalStateException("Already closed!");
		}
	}

	public void closeAll() {
		boolean b = stat.compareAndSet(STAT_RUNNING, STAT_CLODED);
		if (!b) {
			throw new IllegalStateException("Already closed!");
		}
		for (WebDriver webDriver : webDriverList) {
			logger.info("Quit webDriver" + webDriver);
			webDriver.quit();
			webDriver = null;
		}
	}

}
