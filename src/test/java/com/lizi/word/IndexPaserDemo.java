package com.lizi.word;

import org.ansj.splitWord.analysis.IndexAnalysis;

public class IndexPaserDemo {
	public static void main(String[] args) {
		System.out.println(IndexAnalysis.parse("上海虹桥机场南路"));
		System.out.println(IndexAnalysis.parse("主副食品"));
		System.out.println(IndexAnalysis.parse("花生油"));
		System.out.println(IndexAnalysis.parse("让战士们过一个欢乐祥和的新春佳节。"));
	}
}
