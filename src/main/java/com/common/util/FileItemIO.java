package com.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import com.common.constant.TinyConstant;

public class FileItemIO {

	public static Set<String> fileFolders = new HashSet<String>();
	private BufferedWriter bw;

	private long line = 0;
	private boolean append = false;// 是否文件追加内容
	private String fileName;
	private String suffix = "txt";// 文件格式
	private String folder = null;

	public FileItemIO(String fileName) {
		folder = TinyConstant.TINY_CRAWLER_FOLDER;
		
		if (!fileFolders.contains("root")) {
			File file = new File(folder);
			if (!file.exists()) {
				file.mkdirs();
			}
			fileFolders.add("root");
		}

		this.fileName = fileName;
	}

	public FileItemIO(String childFolder, String fileName) {
		folder = TinyConstant.TINY_CRAWLER_FOLDER + childFolder + TinyConstant.FILE_SEP;
		
		if (!fileFolders.contains("root-" + childFolder)) {
			File file = new File(folder);
			if (!file.exists()) {
				file.mkdirs();
			}
			fileFolders.add("root-" + childFolder);
		}
		this.fileName = fileName;
	}

	public String getFolder(){
		return folder;
	}
	public void setAppend(boolean flag) {
		line = 1;
		this.append = flag;
	}

	public void setSuffix(String extend) {
		this.suffix = extend;
	}

	public void open() throws IOException {
		File file = new File(folder + this.fileName + "." + suffix);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fs = new FileOutputStream(file, append);
		OutputStreamWriter ow = new OutputStreamWriter(fs);
		bw = new BufferedWriter(ow);
	}

	public void write(String appendContent) throws IOException {
		if (line > 0) {
			bw.newLine();
		}

		bw.write(appendContent);
		line++;
	}

	public void close() throws IOException {
		if (bw != null) {
			bw.flush();
			bw.close();
		}
	}

	public static void main(String[] args) {

		FileItemIO io = new FileItemIO("error");
		try {
			io.setAppend(true);
			io.open();
			io.write("error:");
			io.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		io = new FileItemIO("error");
		try {
			io.setAppend(true);
			io.open();
			io.write("error:");
			io.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		io = new FileItemIO("error","ss");
		try {
			io.setAppend(true);
			io.open();
			io.write("error:");
			io.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		io = new FileItemIO("error","ss11");
		try {
			io.setAppend(true);
			io.open();
			io.write("error:");
			io.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
