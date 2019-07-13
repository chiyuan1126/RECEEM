package cn.edu.njust.chiyuan.pipeline;

import java.util.HashSet;
import java.util.Set;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class Eval {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] type_arr = { "ct_gov", "i2b2", "ncbi" };
		String[] model_arr = { "pubmedphrase2vec", "phrase2vec" };
		StringBuffer sb = new StringBuffer();
		for (String m : model_arr) {
			for (String ty : type_arr) {

				String type = ty;
				String model = m;
				String gs = FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/gs_" + type + ".txt");
				String predic = FileUtil
						.readfile("/Users/cy2465/Downloads/RECON_eval/" + type + "_" + model + "_answer.txt");
				String[] gsr = gs.split("\n");
				String[] prer = predic.split("\n");
				int totalp = 0;
				int totalt = 0;
				int totalc = 0;

				for (int i = 0; i < gsr.length; i++) {
					Set<String> gsset = new HashSet<String>();
					String[] gsarr = gsr[i].split("\t")[1].split("\\|");
					for (String g : gsarr) {
						// System.out.println("g=>"+g);
						gsset.add(g.toLowerCase().trim());
					}
					Set<String> predicset = new HashSet<String>();
					String[] predicarr = prer[i].split("\t")[1].split("&&");
					for (String p : predicarr) {
						// System.out.println("p=>"+p);
						predicset.add(p.toLowerCase().trim());
					}
					int c = 0;
					for (String p : predicset) {
						if (gsset.contains(p)) {
							c++;
						}
					}
					if(c!=gsset.size()){
						sb.append(m+"\t"+type+"\t"+"gs"+"\t"+gsr[i]+"\n");
						sb.append(m+"\t"+type+"\t"+"predict"+"\t"+prer[i]+"\n");
					}
					totalp = totalp + predicset.size();
					totalt = totalt + gsset.size();
					totalc = totalc + c;
					// System.out.println("t="+gsset.size());
					// System.out.println("p="+predicset.size());
					// System.out.println("c="+c);
					
					
				}
				System.out.println("model=" + m + "\t" + "type=" + type);
				System.out.print("total recognized=" + totalp);
				System.out.print("\ttotal number=" + totalt);
				System.out.println("\ttotalc=" + totalc);
				System.out.println("precision=" + (float) totalc / totalp + "(" + totalc + "/" + totalp + ")");
				System.out.println("recall=" + (float) totalc / totalt + "(" + totalc + "/" + totalt + ")");
				System.out.println("=========");
			}
		}
		FileUtil.write2File("/Users/cy2465/Downloads/RECON_eval/error_report.txt", sb.toString());
	}

}
