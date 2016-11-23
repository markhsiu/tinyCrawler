package com.lizi.tinyCrawler;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import com.common.util.ClasspathFileReader;

public class ClassPathTest {

	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		ClasspathFileReader classpathFileReader = new ClasspathFileReader();
		classpathFileReader.getFile("log4j.properties");
		String path2 = Thread.currentThread().getContextClassLoader().getResource("").getPath();  
		System.out.println("path2 = " + path2); 
		 
	}
}
