package com.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.common.util.FileItemIO;
import com.common.util.IOUtils;

/**
 * Created by Mark Hsiu on 2016/11/23.
 */
public class StylesCityCountStarter {

	private static final String[] MMSize = new String[]{"A","B","C","D","E","F","G","H"} ;
	private static Map<String,Integer> countMap = new HashMap<>();
    public static void main(String[] args) throws IOException {
    	 
    	long start  = System.currentTimeMillis();
		List<String> list = IOUtils.getAllFileForDir("D:\\test\\jd\\styles");
		System.out.println("== "+(System.currentTimeMillis()-start) + " ms");
		
		System.out.println("== "+(System.currentTimeMillis()-start) + " ms");
		for (String file : list) {
			List<String> data = IOUtils.readFileByLines(file);
			if(data.size() < 1){
				continue;
			}
			for (String d : data) {
				String[] texts = d.split("\\s+");
				if (texts.length > 2) {
					for (String mm : MMSize) {
						if(texts[2].toUpperCase().indexOf(mm) > -1){
							String key = texts[0]+"-"+mm;
							if(countMap.containsKey(key)){
								countMap.put(key, countMap.get(key)+1);
							} else {
								countMap.put(key, 1);
							}
							break;
						} 
					}
				} 
			}
		}
		
	 

		ValueComparator bvc = new ValueComparator(countMap);
		TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
		
		sorted_map.putAll(countMap);
		System.out.println("== " + (System.currentTimeMillis() - start) + " ms");
		FileItemIO io = new FileItemIO("styleCityCount");
		io.open();
		for ( Entry<String, Integer> entry: sorted_map.entrySet()) {
			io.write(entry.getKey() + " " + entry.getValue());
		}
		io.close();

		System.out.println("end " + (System.currentTimeMillis() - start) + " ms");
    }
}

 