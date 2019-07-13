package cn.edu.njust.chiyuan.conjunction.evaluation;

import java.io.File;
import java.io.IOException;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class DataPreparation {
//String target="/Users/cy2465/Documents/pubmed_clean_word032802/";
	
	public static void main(String[] args) throws IOException {
		String ncbi=FileUtil.readfile("/Users/cy2465/Downloads/conj_data/ncbi_cm.txt");
		String ctgov=FileUtil.readfile("/Users/cy2465/Downloads/conj_data/ctdata_ec_cleaned.txt");
		String i2b2=FileUtil.readfile("/Users/cy2465/Downloads/conj_data/i2b2_challenge_eee.txt");
		
		String[] ncbi_rows = ncbi.split("\n");
		StringBuffer sb=new StringBuffer();
		for(String r:ncbi_rows){
			System.out.println("==>"+r);
			String[] es=r.split("\t");
			sb.append(es[0]+"\t"+es[0]+"\t"+es[1]+"\n");
		}
		FileUtil.write2File("/Users/cy2465/Downloads/conj_data/ncbi_exp.txt", sb.toString());
		
		String[] ctrows = ctgov.split("\n");
		StringBuffer ctsb=new StringBuffer();
		for(String r:ctrows){
			System.out.println("==>"+r);
			String[] es=r.split("\t");
			ctsb.append(es[0]+"\t"+es[0]+"\n");
		}
		FileUtil.write2File("/Users/cy2465/Downloads/conj_data/ctdata_exp.txt", ctsb.toString());
		
		String[] i2b2rows = i2b2.split("\n");
		StringBuffer i2b2rowssb=new StringBuffer();
		for(String r:i2b2rows){
			System.out.println("==>"+r);
			String[] es=r.split("\t");
			i2b2rowssb.append(es[0]+"\t"+es[0]+"\n");
		}
		FileUtil.write2File("/Users/cy2465/Downloads/conj_data/i2b2data_exp.txt", i2b2rowssb.toString());
		
	}
}
