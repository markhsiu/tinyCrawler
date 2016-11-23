package com.lizi.word;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 在使用TF-IDF计算文章关键词的时候，需要知道某个词的词频是多少，
 * 使用 idf = Math.log(10000 + 10000.0 / (docFreq + 1)) 
 * 这个公式求解出idf的值。一般来说，词频统计越准确，每个词的idf的值就越准确，
 * 而词频的准确性与语料的多少有关，语料自然越多越好，但是通常情况下10G的数据量应该足够了。
 *   假设现在我们有了10G的新闻语料和词列表，现在就可以来统计词频了，首先使用ANSJ对语料进行分词，
 *   分词时就把我们自定义的词列表作为用户字典，在得到分好词的数据后，再统计词频即可
 * @author Administrator
 *
 */
public class CalFreq {

	static Map<String, Integer> map = new HashMap<String, Integer>();

	public static void main(String[] args) throws IOException {
		InputWord();
		FileReader fr = new FileReader("resultbig.txt");
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		String outline[] = new String[2000];
		int count;
		int progress = 0;

		while (line != null) {
			line = br.readLine();
			if (line == null) {
				break;
			}
			//System.out.println(line);
			progress++;
			System.out.println("now is processing the " + progress + "'s news");
			outline = line.split(" ", -1);
			for (int i = 0; i < outline.length; i++) {
				if (map.containsKey(outline[i]) != false) {
					count = map.get(outline[i]) + 1;
					map.put(outline[i], count);
				}
			}

		}
		fr.close();
		br.close();
		WriteWord();

	}

	// put the word into the map
	public static void InputWord() throws IOException {
		System.out.println("begin get the word");
		FileReader fr = new FileReader("baike.txt");
		BufferedReader br = new BufferedReader(fr);
		String line = new String();
		while (line != null) {
			line = br.readLine();
			if (line == null) {
				break;
			}
			System.out.println(line);
			map.put(line, 0);
		}
		br.close();
		fr.close();
		System.out.println("get the word is ok");
	}

	// write word and frequency into the file
	public static void WriteWord() throws IOException {
		System.out.println("begin write the word");
		File file = new File("wordfreq.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter("wordfreq.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			String key = entry.getKey().toString();
			int value = entry.getValue();
			bw.write(key + " " + value);
			bw.newLine();
			bw.flush();
		}
		fw.close();
		bw.close();
		System.out.println("write the word is ok");
	}

}
