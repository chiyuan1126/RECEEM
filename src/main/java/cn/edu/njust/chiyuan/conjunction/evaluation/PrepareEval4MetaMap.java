package cn.edu.njust.chiyuan.conjunction.evaluation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class PrepareEval4MetaMap {
	public static void main(String[] args) {
		//ctgovEval();
		String evalpath="/Users/cy2465/Downloads/eval_metamap_ncbi_clean_cy.txt";
		getResults(evalpath);
		
		//i2b2Eval();
		
		//ncbiEval();
		
	}

	public static void ncbiEval() {
		String content=FileUtil.readfile("/Users/cy2465/Downloads/eval_metamap_ncbi_clean_cy.txt");
		String[] rows=content.split("\n");
		String gs=FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/gs_ncbi.txt");
		
		String[] gsr=gs.split("\n");
		HashMap<String,Set<String>> map=new HashMap<String,Set<String>>();
		for(int i=0;i<gsr.length;i++){
			Set<String> gsset=new HashSet<String>();
			String[] gsarr=gsr[i].split("\t")[1].split("\\|");
			for(String g:gsarr){
				//System.out.println("g=>"+g);
				gsset.add(g.toLowerCase().trim());
			}
			map.put(gsr[i].split("\t")[0], gsset);
		}
		int count=0;
		StringBuffer sb=new StringBuffer();
		for(String r:rows){
			if(r.startsWith(">>>>>>>")){
				String y=r.split("\t")[0];
				System.out.println("===>"+y.substring(15));
				if(map.containsKey(y.substring(15))){
					sb.append(">>>>>>>"+y.substring(15)+"\t"+strRender(map.get(y.substring(15)))+"\n");
					sb.append("cal_t="+map.get(y.substring(15)).size()+"\n");
				}else{
					sb.append("xxxxxxx>>"+y+"\n");
					sb.append("cal_t=unknown"+"\n");
				}
				count++;
				
			}else{
				sb.append(r+"\n");
			}
		}
		System.out.println("count="+count);
		//FileUtil.write2File("/Users/cy2465/Downloads/eval_metamap_ncbi_clean_cy.txt", sb.toString());
	}

	public static void i2b2Eval() {
		String content=FileUtil.readfile("/Users/cy2465/Downloads/eval_metamap_i2b2_clean_xq.txt");
		String[] rows=content.split("\n");
		String gs=FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/gs_i2b2.txt");
		
		String[] gsr=gs.split("\n");
		HashMap<String,Set<String>> map=new HashMap<String,Set<String>>();
		for(int i=0;i<gsr.length;i++){
			Set<String> gsset=new HashSet<String>();
			String[] gsarr=gsr[i].split("\t")[1].split("\\|");
			for(String g:gsarr){
				//System.out.println("g=>"+g);
				gsset.add(g.toLowerCase().trim());
			}
			map.put(gsr[i].split("\t")[0], gsset);
		}
		int count=0;
		StringBuffer sb=new StringBuffer();
		for(String r:rows){
			if(r.startsWith(">>>>>>>")){
				String y=r.split("\t")[0];
				System.out.println("===>"+y.substring(15));
				count++;
				sb.append(">>>>>>>"+y.substring(15)+"\t"+strRender(map.get(y.substring(15)))+"\n");
				sb.append("cal_t="+map.get(y.substring(15)).size()+"\n");
			}else{
				sb.append(r+"\n");
			}
		}
		System.out.println("count="+count);
		FileUtil.write2File("/Users/cy2465/Downloads/eval_metamap_i2b2_clean_cy.txt", sb.toString());
	}

	public static void getResults(String evalpath) {
		String content=FileUtil.readfile(evalpath);
		String[] rows=content.split("\n");
		int total=0;
		int recognized=0;
		int accurate=0;
		for(String r:rows){
			System.out.println(r);
			if(r.startsWith("cal_t=")){
				System.out.println(r);
				String count=r.substring(6);
				System.out.println("count="+count);
				Integer st=Integer.valueOf(count);
				total=total+st;
			}else if(r.startsWith("r=")){
				System.out.println(r);
				System.out.println(r.substring(2));
				recognized=recognized+Integer.valueOf(r.substring(2));
			}else if(r.startsWith("c=")){
				System.out.println(r);
				System.out.println(r.substring(2));
				accurate=accurate+Integer.valueOf(r.substring(2));
			}
		}
		System.out.println("total="+total);
		System.out.println("recognized="+recognized);
		System.out.println("accurate="+accurate);
	}

	public static void ctgovEval() {
		String content=FileUtil.readfile("/Users/cy2465/Downloads/eval_metamap_ct_gov_clean.txt");
		String[] rows=content.split("\n");
		String gs=FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/gs_ct_gov.txt");
		
		String[] gsr=gs.split("\n");
		HashMap<String,Set<String>> map=new HashMap<String,Set<String>>();
		for(int i=0;i<gsr.length;i++){
			Set<String> gsset=new HashSet<String>();
			String[] gsarr=gsr[i].split("\t")[1].split("\\|");
			for(String g:gsarr){
				//System.out.println("g=>"+g);
				gsset.add(g.toLowerCase().trim());
			}
			map.put(gsr[i].split("\t")[0], gsset);
		}
		int count=0;
		StringBuffer sb=new StringBuffer();
		for(String r:rows){
			if(r.startsWith(">>>>>>>")){
				String y=r.split("\t")[0];
				System.out.println("===>"+y.substring(15));
				count++;
				sb.append(">>>>>>>"+y.substring(15)+"\t"+strRender(map.get(y.substring(15)))+"\n");
				sb.append("cal_t="+map.get(y.substring(15)).size()+"\n");
			}else{
				sb.append(r+"\n");
			}
		}
		System.out.println("count="+count);
		FileUtil.write2File("/Users/cy2465/Downloads/eval_metamap_ct_gov_clean_cy.txt", sb.toString());
	}
	
	public static String strRender(Set<String> set){
		StringBuffer sb=new StringBuffer();
		for(String s:set){
			sb.append(s+"|");
		}
		return sb.toString();
	}
	
}
