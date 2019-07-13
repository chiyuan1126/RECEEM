package cn.edu.njust.chiyuan.conjunction.datapreparation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class ClinicalNarrativeData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//extractTermsFromi2b2();
		
		String content=FileUtil.readfile("/Users/cy2465/Documents/i2b2_challenge_eee_ZRannotated.txt");
		String[] rows=content.split("\n");
		List<String> list=new ArrayList<String>();
		for(String r:rows){
			System.out.println("r="+r);
			String[] es=r.split("\t");
			list.add(r);
			
		}
		Collections.sort(list);
		
		StringBuffer sb=new StringBuffer();
		for(String r:list){
			sb.append(r+"\n");
		}
		FileUtil.write2File("/Users/cy2465/Documents/i2b2_ec_ziran.txt", sb.toString());
	}

	public static void extractTermsFromi2b2() {
		File dir=new File("/Users/cy2465/Documents/corpora/i2b2_dataset/2010_Relations_Challenge/concept_assertion_relation_training_data/beth/concept");
		File[] files=dir.listFiles();
		Integer eccount=0;
		Integer entitycount=0;
		Integer ecpcount=0;
		
		Integer pcount=0;
		
		StringBuffer sb=new StringBuffer();
		int doccount=0;
		for(File f:files){
			System.out.println(f.getName());
			doccount++;
			String content=FileUtil.readfile(f.getAbsolutePath());
			String[] rows=content.split("\n");
			for(String r:rows){
				entitycount++;
				if(r.endsWith("t=\"problem\"")){
					pcount++;
				}
				if(r.contains(" and ")||r.contains(" or ")){
					eccount++;
					if(r.endsWith("t=\"problem\"")){
						ecpcount++;
					}
					System.out.println("r="+r);
					String[] es=r.split("\"");
					System.out.println("str=="+es[1]);
					sb.append(es[1]+"\n");
				}
			}
		}
		Double d=(double) (eccount/entitycount);
		System.out.println("pct:"+d+ "\t"+eccount+ " of "+entitycount);
		System.out.println(ecpcount+" of "+pcount);
		System.out.println("doc count "+doccount);
	}

}
