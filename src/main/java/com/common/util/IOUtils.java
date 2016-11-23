package com.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * IO操作工具
 * @author Mark Hsiu
 *
 */
public class IOUtils {

	public static List<String> readFileByLines(String path) {
		List<String> data = new ArrayList<>();
		File file = new File(path);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				data.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return data;
	}
	
	

	public static List<String> getAllFileForDir(String directory) {
		File file = new File(directory);
		List<String> list = new ArrayList<>();
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					list.addAll(getAllFileForDir(f.getPath()));
				} else {
					list.add(f.getPath());
				}

			}
		} else {
			list.add(directory);
		}
		return list;
	}

	
}
