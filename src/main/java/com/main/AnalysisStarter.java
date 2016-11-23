package com.main;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.FilterRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.common.util.FileItemIO;
import com.common.util.IOUtils;

/**
 * 分词
 * 
 * @author Mark Hsiu
 *
 */
public class AnalysisStarter {

	
	public static void main(String[] args) throws IOException {
		FilterRecognition fitler = new FilterRecognition();
		fitler.insertStopNatures("uj");
		fitler.insertStopNatures("ul");
		fitler.insertStopNatures("w");
		fitler.insertStopNatures("y");
		fitler.insertStopNatures("null");
		ToAnalysis analysis = new ToAnalysis();
		
		long start  = System.currentTimeMillis();
		List<String> list = IOUtils.getAllFileForDir("D:\\test\\jd\\comment");
		System.out.println("== "+(System.currentTimeMillis()-start) + " ms");
		FileItemIO io = new FileItemIO("markhsiu");
		io.open();
		System.out.println("== "+(System.currentTimeMillis()-start) + " ms");
		for (String file : list) {
			List<String> data = IOUtils.readFileByLines(file);
			if(data.size() < 1){
				continue;
			}
			for (String d : data) {
				Result modifResult = analysis.parseStr(d).recognition(fitler);
				StringBuffer text = new StringBuffer();
				modifResult.forEach(new Consumer<Term>() {
					@Override
					public void accept(Term term) {
						text.append(term.getRealName()+ " ");
					}
				});
 
				if (text.length() > 0) {
					io.write(text.toString());
				} else {
					io.write("");
				}
				
			}
		}
		System.out.println("== "+(System.currentTimeMillis()-start) + " ms");
		io.close();
		System.out.println("end "+(System.currentTimeMillis()-start) + " ms");
	}
}
