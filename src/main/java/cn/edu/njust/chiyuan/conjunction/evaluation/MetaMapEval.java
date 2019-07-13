package cn.edu.njust.chiyuan.conjunction.evaluation;

import java.util.HashSet;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class MetaMapEval {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//bashGeneration();
		
		/*
		String cttype="ct_gov";
		evalResultCleaning(cttype);
		
		String i2b2type="i2b2";
		evalResultCleaning(i2b2type);
		
		String ncbitype="ncbi";
		evalResultCleaning(ncbitype);
		*/
		
		String content=FileUtil.readfile("/Users/cy2465/Downloads/eval_metamap_i2b2_clean_xq.txt");
		String[] rows=content.split("\n");
		int t=0;
		int re=0;
		int c=0;
		for(String r:rows){	
			//System.out.println(r);
			if(r.startsWith("t=")){
				System.out.println("t=");
				System.out.println(r.substring(2));
				t=t+Integer.valueOf(r.substring(2,3));
			}
			if(r.startsWith("r=")){
				System.out.println("r=");
				System.out.println(r.substring(2));
				re=re+Integer.valueOf(r.substring(2,3));
			}
			if(r.startsWith("c=")){
				System.out.println("c=");
				System.out.println(r.substring(2));
				
				c=c+Integer.valueOf(r.substring(2,3));
				
			}
		}
		System.out.println("total="+t);
		System.out.println("recognized="+re);
		System.out.println("correct="+c);
		
	}

	public static void evalResultCleaning(String type) {
		String gspath="/Users/cy2465/Downloads/conj_metamap_eval/eval_metamap_"+type+".txt";
		String content=FileUtil.readfile(gspath);
		String[] rows=content.split("\n");
		StringBuffer sb=new StringBuffer();
		boolean flag=false;
		HashSet<String> set=new HashSet<String>();
		for(String r:rows){	
			if(r.startsWith(">>>>>")){
				set.clear();
			}
			
			if(r.startsWith("Processing USER")
				||r.startsWith("/home/njust4060")){
				continue;
			}
			if(r.startsWith("Could not find")){
				sb.append("t=\n");
				sb.append("r=\n");
				sb.append("c=\n");
				continue;
			}
			
			if(r.startsWith(" ")){
				int start_index=r.indexOf(":");
				int end_index=0;
				if(r.indexOf("(")>-1){
					end_index=r.indexOf("(");
				}else{
					end_index=r.indexOf("[");
				}
				String concept=r.substring(start_index+1,end_index).toLowerCase();
				if(set.contains(concept)){
					
				}else{
					set.add(concept);
					System.out.println("Concept=>"+concept);
					sb.append("\t"+concept+"\n");
					continue;
				}
			}else{
				sb.append(r+"\n");
			}
			System.out.println("====>"+r);
			
		}
		FileUtil.write2File("/Users/cy2465/Downloads/conj_metamap_eval/eval_metamap_"+type+"_clean.txt", sb.toString());
	}

	public static void bashGeneration() {
		String gspath="/Users/cy2465/Downloads/conj_data/i2b2_ec_ziran_gold_standard.txt";
		String content=FileUtil.readfile(gspath);
		String[] rs=content.split("\n");
		StringBuffer sb=new StringBuffer();
		for(String r:rs){
			System.out.println("r="+r);
			String[] es=r.split("\t");
			sb.append("printf \"\\n>>>>>>>>>>>>>>>"+r.substring(0, r.length()-1)+"\\n\"\n");
			sb.append("echo \""+es[0]+"\" | /home/njust4060/public_mm/bin/metamap -I --conj\n");
			
		}
		FileUtil.write2File("/Users/cy2465/Downloads/i2b2_metamap_bash.sh", sb.toString());
	}

}
