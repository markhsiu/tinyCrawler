package com.main;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.common.util.FileItemIO;
import com.common.util.IOUtils;

/**
 * 统计
 * @author Mark Hsiu
 *
 */
public class CountWordStarter {

	public static Map<String, Integer> map = new HashMap<>();

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		List<String> data = IOUtils.readFileByLines("D:\\test\\jd\\markhsiu.txt");
		System.out.println("== " + (System.currentTimeMillis() - start) + " ms");
		for (String text : data) {
			String[] words = text.split(" ");
			if (words.length > 0) {
				for (String word : words) {
					word = word.trim();
					if (word.length() > 0) {
						int count = 1;
						if (map.containsKey(word)) {
							count = map.get(word) + 1;
						}
						map.put(word, count);
					}
				}
			}
		}
		
		System.out.println("word count === " + map.size());

		ValueComparator bvc = new ValueComparator(map);
		TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
		
		sorted_map.putAll(map);
		System.out.println("== " + (System.currentTimeMillis() - start) + " ms");
		FileItemIO io = new FileItemIO("wordCount");
		io.open();
		for ( Entry<String, Integer> entry: sorted_map.entrySet()) {
			io.write(entry.getKey() + " " + entry.getValue());
		}
		io.close();

		System.out.println("end " + (System.currentTimeMillis() - start) + " ms");
	}

}

class ValueComparator implements Comparator<String> {

	Map<String, Integer> base;

	public ValueComparator(Map<String, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}
