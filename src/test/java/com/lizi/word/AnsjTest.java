package com.lizi.word;

import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * http://nlpchina.github.io/ansj_seg/
 * @author Administrator
 *
 */
public class AnsjTest {

	public static void main(String[] args) {
		String str = "让战士们过一个欢乐祥和的新春佳节。" ;
		//精准分词
		System.out.println(ToAnalysis.parse(str));

		
		///基本分词
	    System.out.println(BaseAnalysis.parse(str));
	    //索引
	    System.out.println(IndexAnalysis.parse(str));
	    
	    //nlp分词
	    System.out.println(NlpAnalysis.parse(str));
	}
}
