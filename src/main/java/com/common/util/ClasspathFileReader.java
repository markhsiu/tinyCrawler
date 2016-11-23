package com.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;


/**
 * classpath 路径下的文件读取
 * @author Mark Hsiu
 *
 */
public class ClasspathFileReader {
	 
	private final String encoding="UTF-8";
	public  final String fileSeparator = System.getProperty("file.separator");
	
	private static ClasspathFileReader classpathFileReader = new ClasspathFileReader();
	
	public static ClasspathFileReader getInstance(){
		return classpathFileReader;
	}
	
	public  String getFileContent(File file) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return IOUtils.toString(is,encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return null;
	}

	public File getFile(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		List<String> classpath = getClasspathDirectories();
		for (Iterator<String> i = classpath.iterator(); i.hasNext();) {
			String directory =   i.next();
			if (!directory.endsWith(fileSeparator)
					&& !fileName.startsWith(fileSeparator)) {
				directory = directory + fileSeparator;
			}
			File file = new File(directory + fileName);
			if (file.exists() && file.isFile() && file.canRead()) { 
				return file;
			}
		}
		 
		throw new FileNotFoundException("Could not locate file in classpath : "+fileName);
	}
	
	public String getClasspath() {
		String threadPath = Thread.currentThread().getContextClassLoader().getResource(fileSeparator).getPath();
		String classpath = "";
		try {
			classpath = URLDecoder.decode(threadPath,encoding);
			if (!classpath.endsWith(fileSeparator)) {
				classpath = classpath + fileSeparator;
			}
			return  classpath;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return threadPath; 
	}

	private List<String> getClasspathDirectories() throws UnsupportedEncodingException {
		List<String> directories = new LinkedList<String>();
		String threadPath = Thread.currentThread().getContextClassLoader().getResource(fileSeparator).getPath();
		directories.add(URLDecoder.decode(threadPath,encoding));
		
		String classpath = System.getProperty("java.class.path");
		String separator = System.getProperty("path.separator");
		StringTokenizer st = new StringTokenizer(classpath, separator);
		while (st.hasMoreTokens()) {
			String possibleDir = st.nextToken();
			File file = new File(possibleDir);
			if (file.isDirectory()) {
				directories.add(possibleDir);
			}
		}
		
		return directories;
	}
	
}
