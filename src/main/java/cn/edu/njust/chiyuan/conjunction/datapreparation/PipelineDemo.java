package cn.edu.njust.chiyuan.conjunction.datapreparation;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class PipelineDemo {

	public static void main(String[] args) throws FileNotFoundException {
		// set up pipeline properties

		/*
		 * // option #1: By sentence. DocumentPreprocessor dp = new
		 * DocumentPreprocessor(""); for (List<HasWord> sentence : dp) {
		 * System.out.println(sentence); } // option #2: By token
		 * PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(new FileReader(""),
		 * new CoreLabelTokenFactory(), ""); while (ptbt.hasNext()) { CoreLabel
		 * label = ptbt.next(); System.out.println(label); }
		 */

		String trainset = FileUtil.readfile("/Users/cy2465/Downloads/SimConcept/corpus/Disease.txt");

		PubMedData pmdata = new PubMedData();
		String str = "";
		String[] sents = trainset.split("\n");
		
		
		Map<String,String> map=new HashMap<String,String>();
		int doccount=0;
		for (String s : sents) {
			if (s.contains("CompositeMention")) {
				//System.out.println(s);
				String[] es=s.split("\t");
				System.out.println(es[3]+"\t"+es[5]);
				map.put(es[3].trim().toLowerCase(), es[5].toLowerCase());
				
			}
			if(s.contains("|t|")){
				System.out.println("=====>"+s);
				doccount++;
			}
		}
		//duchenne_muscular_dystrophy
		StringBuffer sb=new StringBuffer();
		for (Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey()+"\t"+entry.getValue());
			sb.append(entry.getKey()+"\t"+entry.getValue()+"\n");
		}
		//FileUtil.write2File("/Users/cy2465/Downloads/disease_cm.txt", sb.toString());
		System.out.println("count=" + map.size());
		System.out.println("doc count=" + doccount);

	}
}
