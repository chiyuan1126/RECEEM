package cn.edu.njust.chiyuan.conjunction.evaluation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class SimConceptEval {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//prepareEntities();
		String type="ct_gov";
		//generateReviewFile(type);
		
		String filepath="/Users/cy2465/Downloads/SimConcept_eval/"+type+"review_cy.txt";
		getResults(filepath);
	}
	public static void getResults(String evalpath) {
		String content=FileUtil.readfile(evalpath);
		String[] rows=content.split("\n");
		int total=0;
		int recognized=0;
		int accurate=0;
		for(String r:rows){
			System.out.println(r);
			if(r.startsWith("t=")){
				System.out.println(r);
				String count=r.substring(2);
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
		System.out.println("precision="+(float)accurate/recognized);
		System.out.println("recall="+(float)accurate/total);
	}

	public static void generateReviewFile(String type) {
		String content = FileUtil.readfile("/Users/cy2465/Downloads/SimConcept_eval/output/"+type+".txt.SimConcept");
		String[] rows = content.split("\n");
		String gs = FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/gs_"+type+".txt");
		String[] gsr = gs.split("\n");
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
		for (int i = 0; i < gsr.length; i++) {
			Set<String> gsset = new HashSet<String>();
			String[] gsarr = gsr[i].split("\t")[1].split("\\|");
			for (String g : gsarr) {
				gsset.add(g.toLowerCase().trim());
			}
			map.put(gsr[i].split("\t")[0], gsset);
		}
		int count = 0;
		StringBuffer sb = new StringBuffer();
		for (String r : rows) {
			String[] ess=r.split("\t");
			String y = ess[0];
			String ans="";
			Set<String> ansset = new HashSet<String>();
			if(ess.length>1){
				ans=ess[1].substring(17);
				String[] ansarr = ans.split("\\|");
				for (String g : ansarr) {
					ansset.add(g.toLowerCase().trim());
				}
			}
			System.out.println("===>" + y);
			System.out.println("ans===>" + ans);
			if (map.containsKey(y)) {
				sb.append(">>>gs>>>>" + y + "\t" + strRender(map.get(y)) + "\n");
				sb.append(">>>ans>>>>" + y + "\t" + strRender(ansset) + "\n");
				sb.append("t=" + map.get(y).size() + "\n");
				sb.append("r=" + ansset.size() + "\n");
				sb.append("c=" +  "\n");
			} else {
				sb.append("xxxxxxx>>" + y + "\n");
				sb.append("t=unknown" + "\n");
				sb.append("r=unknown" + "\n");
				sb.append("c=" +  "\n");
			}
			count++;

		}
		System.out.println("count=" + count);
		FileUtil.write2File("/Users/cy2465/Downloads/SimConcept_eval/"+type+"review_cy.txt", sb.toString());
	}

	public static void prepareEntities() {
		String type="ct_gov";
		String ncbistr=FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/gs_"+type+".txt");
		//String i2b2str=FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/gs_i2b2.txt");
		//String ct_govstr=FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/gs_ct_gov.txt");
		StringBuffer sb=new StringBuffer();
		String[] rows=ncbistr.split("\n");
		for(String s:rows){
			//System.out.println(s);
			sb.append(s.split("\t")[0]+"\n");
		}
		FileUtil.write2File("/Users/cy2465/Downloads/SimConcept_eval/"+type+".txt", sb.toString());
	}
	
	public static String strRender(Set<String> set){
		StringBuffer sb=new StringBuffer();
		for(String s:set){
			sb.append(s+"|");
		}
		return sb.toString();
	}

}
