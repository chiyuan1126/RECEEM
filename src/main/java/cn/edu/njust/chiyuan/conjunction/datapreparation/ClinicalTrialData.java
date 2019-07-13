package cn.edu.njust.chiyuan.conjunction.datapreparation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import cn.edu.njust.chiyuan.conjunction.pojo.ConjunctCandidate;
import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class ClinicalTrialData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//extractFromAnn();
		
		/*
		String content=FileUtil.readfile("/Users/cy2465/Documents/ctdata_ec_clean_index.txt");
		String[] rows=content.split("\n");
		String dir_path="/Users/cy2465/Documents/ctdata_ann/s4/";
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<rows.length;i++){
			try{
				String[] columns=rows[i].split("\t");
				System.out.println(rows[i]);
				//System.out.println(rows[i]);
				//System.out.println(columns[3]);
				String fc=FileUtil.readfile(dir_path+columns[0]+".txt");
				//System.out.println("=>"+fc.substring(Integer.valueOf(columns[2]),Integer.valueOf(columns[3])));
				sb.append(rows[i]+"\n");
				for(int j=i+1;j<rows.length;j++){
					String[] next_columns=rows[j].split("\t");
					if(next_columns[0].equals(columns[0])
							&&next_columns[1].equals(columns[1])
									&&next_columns[3].equals(columns[3])){
						System.out.println(rows[j]);
						sb.append(rows[j]+"\n");
						
					}else{
						sb.append("==================================="+"\n");
						System.out.println("=================================================");
						i=j-1;
						break;
					}
				}
			}catch(Exception ex){
				
				System.out.println(ex.toString());
			}
		}
		*/
		//conjCandidate();
		/*
		String content=FileUtil.readfile("/Users/cy2465/Documents/ctdata_ec.txt");

		String[] rows=content.split("\n"); 
		Set<String> set=new HashSet<String>();
		List<String> list=new ArrayList<String>();
		for(int x=0; x<rows.length;x++){
			
			String[] columns=rows[x].split("\t");
			String ts=columns[0]+columns[1]+columns[3];
			for(int y=x-1;y>=0;y--){
				String[] lastcolumns=rows[y].split("\t");
				if(lastcolumns[0].equals(columns[0])==false){
					break;
				}
				else{ 
					if(lastcolumns[0].equals(columns[0])
						&&lastcolumns[1].equals(columns[1])
						&&lastcolumns[3].equals(columns[3])){
					list.add(rows[y]);
					list.add(rows[x]);
					}
				}
			}
		}
		StringBuffer sb=new StringBuffer();
		LinkedHashSet<String> lhm=new LinkedHashSet<String>();
		for(String a:list){
			//System.out.println(a);
			lhm.add(a);
			
		}
		for(String b:lhm){
			sb.append(b+"\n");
		}
		System.out.println("size="+list.size());
		System.out.println("lhm size="+lhm.size());
		FileUtil.write2File("/Users/cy2465/Documents/ctdata_ec_clean_index.txt", sb.toString());
		
		
		*/
		
		/*
		 * String
		 * content=FileUtil.readfile("/Users/cy2465/Documents/ctdata_ec.txt");
		 * String[] rows=content.split("\n"); Set<String> set=new
		 * HashSet<String>(); for(String r:rows){ //System.out.println("r="+r);
		 * if(r.contains("or")||r.contains("and")||r.contains("/")){ set.add(r);
		 * } } StringBuffer sb=new StringBuffer(); for(String s:set){
		 * sb.append(s+"\n"); } System.out.println(rows.length+"=>"+set.size());
		 * FileUtil.write2File("/Users/cy2465/Documents/ctdata_ec_cleaned.txt",
		 * sb.toString());
		 */
		
		
		String content=FileUtil.readfile("/Users/cy2465/Downloads/conj_data/ct_gov_gold_standard.txt");
		String[] rows=content.split("\n");
		Set<String> set=new HashSet<String>();
		StringBuffer sb=new StringBuffer();
		for(String r:rows){
			System.out.println(r);
			String[] es=r.split("\t");
			if(set.contains(es[0].toLowerCase())){
				continue;
			}else{
				set.add(es[0].toLowerCase());
				sb.append(r+"\n");
			}
		}
		FileUtil.write2File("/Users/cy2465/Downloads/conj_data/ct_gov_gold_standard_w_o_dup.txt", sb.toString());
		
	}

	public static void conjCandidate() {
		String content=FileUtil.readfile("/Users/cy2465/Documents/ct_data_ec_index.txt");
		String[] rows=content.split("\n");
		int x=0;
		List<List<String>> list=new ArrayList<List<String>>();
		for (int i = 1; i < rows.length; i++) {
			List<String> sublist=new ArrayList<String>();
			System.out.println("... new set");
			for (int j = i-1; j < rows.length; j++) {
				//System.out.println(x + "=>" + rows[j]);
				if (rows[j].startsWith("====")==true) {
					i=j+1;
					break;
				}else{
					System.out.println("to add =>" + rows[j]);
					sublist.add(rows[j]);
				}
			}
			System.out.println("finish add new set");
			list.add(sublist);
		}
		
		String dir_path = "/Users/cy2465/Documents/ctdata_ann/s4/";
		StringBuffer sb=new StringBuffer();
		for (int a = 0; a < list.size(); a++) {
			List<String> sublist = list.get(a);
			Map<String,Integer> map=new HashMap<String,Integer>();
			if(sublist.size()<2){
				continue;
			}
			for (int b = 0; b < sublist.size(); b++) {
				try {
					System.out.print(a + "=>" + sublist.get(b));
					String[] columns = sublist.get(b).split("\t");
					String fc = FileUtil.readfile(dir_path + columns[0] + ".txt");
					System.out.println("\t" + fc.substring(Integer.valueOf(columns[2]), Integer.valueOf(columns[3])));
				    //frag
					for(int c=2;c<=5;c++){
						if(columns[c].equals("-1")==false){
							if(map.containsKey(columns[c])){
								Integer count=map.get(columns[c]);
								count=count+1;
								map.put(columns[c], count);
							}else{
								map.put(columns[c], 1);
							}
						}
					}
					
					
				} catch (Exception ex) {

				}
			}
			List<Map.Entry<String, Integer>> slist = new ArrayList<Map.Entry<String, Integer>>(
					map.entrySet());
			Collections.sort(slist, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
			List<Integer> index_list=new ArrayList<Integer>();
			Map<Integer,Integer> sortedmap=new HashMap<Integer,Integer>();
			for (Map.Entry<String, Integer> mapping : slist) {
				System.out.println(mapping.getKey() + ":" + mapping.getValue());
				sortedmap.put(Integer.valueOf(mapping.getKey()), Integer.valueOf(mapping.getValue()));
				index_list.add(Integer.valueOf(mapping.getKey()));
			}
			
			Collections.sort(index_list);
			
			Integer min_index=index_list.get(0);
			System.out.println("min = "+min_index);
			Integer max_index=index_list.get(index_list.size()-1);
			System.out.println("max = "+max_index);
			
			
			Integer[] prefix=new Integer[2];
			prefix[0]=min_index;
			prefix[1]=min_index;
			if(sortedmap.get(min_index)>1){
				//前缀位置
				prefix[0]=index_list.get(0);
				prefix[1]=index_list.get(1);
				System.out.println("pre suffix"+ index_list.get(0) + " , "+index_list.get(1));
			}
			Integer[] postfix=new Integer[2];
			postfix[0]=max_index;
			postfix[1]=max_index;
			if(sortedmap.get(max_index)>1){
				//后缀位置
				postfix[0]=index_list.get(index_list.size()-2);
				postfix[1]=index_list.get(index_list.size()-1);
				System.out.println("post suffix "+ index_list.get(index_list.size()-2)+" , "+ index_list.get(index_list.size()-1) );
			}
			String[] tcolumns = sublist.get(0).split("\t");
			String tfc = FileUtil.readfile(dir_path + tcolumns[0] + ".txt");
			if (tcolumns[1].equals("Measurement")
					||tcolumns[1].equals("Condition")
					||tcolumns[1].equals("Procedure")
					||tcolumns[1].equals("Drug")) {

				sb.append(tcolumns[0].substring(0, 11)+"\t"+tfc.substring(min_index, max_index) + "\t" + tcolumns[1]);
				StringBuffer ssb = new StringBuffer();
				for (int b = 0; b < sublist.size(); b++) {

					// System.out.println("_____"+sublist.get(b));
					String[] columns = sublist.get(b).split("\t");
					String fc = FileUtil.readfile(dir_path + columns[0] + ".txt");
					// System.out.println("\t" +
					// fc.substring(Integer.valueOf(columns[2]),
					// Integer.valueOf(columns[3])));
					// frag
					List<Integer> compare_index_list = new ArrayList<Integer>();
					for (int c = 2; c <= 5; c++) {
						compare_index_list.add(Integer.valueOf(columns[c]));
					}
					Collections.sort(compare_index_list);

					// 前缀有无出现？
					if (compare_index_list.contains(prefix[0]) && compare_index_list.contains(prefix[1])) {
						// 剩下部分减去后缀
						for (int k = 1; k < compare_index_list.size(); k++) {

							if (compare_index_list.get(k) >= postfix[0] && compare_index_list.get(k - 1) < postfix[0]) {
								// System.out.println("pre==index==>"+prefix[1]+"
								// , "+ compare_index_list.get(k));
								System.out.println(
										"conjunct====>" + fc.substring(compare_index_list.get(k - 1), postfix[0]));
								String tobeadded=fc.substring(compare_index_list.get(k - 1), postfix[0]).trim();
								if(tobeadded.startsWith("and ")){
									tobeadded=tobeadded.substring(4);
								}else if(tobeadded.startsWith("or ")){
									tobeadded=tobeadded.substring(3);
								}
								ssb.append( tobeadded.trim()+ "&&");
								break;
							}

						}
					}
					// 后缀有无出现？
					if (compare_index_list.contains(postfix[0]) && compare_index_list.contains(postfix[1])) {
						// 剩下部分减去前缀
						for (int k = 1; k < compare_index_list.size(); k++) {
							if (compare_index_list.get(k) > prefix[1] && compare_index_list.get(k - 1) <= prefix[1]) {
								// System.out.println("pre==index==>"+prefix[1]+"
								// , "+ compare_index_list.get(k));
								System.out
										.println("conjunct====>" + fc.substring(prefix[1], compare_index_list.get(k)));
								String tobeadded=fc.substring(prefix[1], compare_index_list.get(k)).trim();
								if(tobeadded.startsWith("and ")){
									tobeadded=tobeadded.substring(4);
								}else if(tobeadded.startsWith("or ")){
									tobeadded=tobeadded.substring(3);
								}
								ssb.append( tobeadded.trim()+ "&&");
								break;
							}

						}
					}

				}

				String re = ssb.toString();
				sb.append("\t" + re.substring(0, re.length() - 2) + "\n");
			}
		}
		FileUtil.write2File("/Users/cy2465/Documents/ctdata_ec_conjcandidates.txt", sb.toString());
	}

	public static void extractFromAnn() {
		File dir = new File("/Users/cy2465/Documents/ctdata_ann/s4");
		File[] files = dir.listFiles();
		Set<String> filelist = new HashSet<String>();
		String base_path = "/Users/cy2465/Documents/ctdata_ann/s4/";
		for (File f : files) {
			if (f.getName().endsWith(".ann")) {
				System.out.println(f.getName().substring(0, 11));
				filelist.add(f.getName().substring(0, 11));
				System.out.println(f.getAbsolutePath());
			}

		}
		StringBuffer sb = new StringBuffer();

		for (String nctid : filelist) {

			String content = FileUtil.readfile(base_path + nctid + "_inc.ann");
			String origintext = FileUtil.readfile(base_path + nctid + "_inc.txt");
			String[] rows = content.split("\n");
			for (String r : rows) {
				// System.out.println("r="+r);
				if (r.startsWith("T")) {
					String[] es = r.split("\t");
					if (es.length > 2) {
						// System.out.println("es===>"+es[1]);
						if (es[1].contains(";")) {
							System.out.println(nctid + "============>" + r);
							System.out.println("es[1]============>" + es[1]);
							int first_space = es[1].indexOf(" ");
							System.out.println("f =>" + es[1].substring(0, first_space));
							System.out.println("r =>" + es[1].substring(first_space + 1));
							String index_arr = es[1].substring(first_space + 1);
							String[] isa = index_arr.trim().split(";");
							String[] firstfrag=isa[0].split(" ");
							String[] secondfrag=isa[1].split(" ");
							String domain=es[1].substring(0, first_space).trim();
							sb.append(nctid+"_inc"+"\t"+domain+"\t"+firstfrag[0]+"\t"+secondfrag[1]+"\t"+firstfrag[1]+"\t"+secondfrag[0]+"\t"+es[2]+"\n");

						}else{
							System.out.println(nctid + "============>" + r);
							System.out.println("no ; es[1]============>" + es[1]);
							int first_space = es[1].indexOf(" ");
							System.out.println("f =>" + es[1].substring(0, first_space));
							System.out.println("r =>" + es[1].substring(first_space + 1));
							String index_arr = es[1].substring(first_space + 1);
							String[] pos_arr = index_arr.trim().split(" ");
							String domain=es[1].substring(0, first_space).trim();
							sb.append(nctid+"_inc"+"\t"+domain+"\t"+pos_arr[0]+"\t"+pos_arr[1]+"\t"+"-1"+"\t"+"-1"+"\t"+es[2]+"\n");
						}
					}
				}
			}
		}

		for (String nctid : filelist) {

			String content = FileUtil.readfile(base_path + nctid + "_exc.ann");
			String origintext = FileUtil.readfile(base_path + nctid + "_exc.txt");
			String[] rows = content.split("\n");
			for (String r : rows) {
				if (r.startsWith("T")) {
					String[] es = r.split("\t");
					if (es.length > 2) {
						// System.out.println("es===>"+es[1]);
						if (es[1].contains(";")) {
							System.out.println(nctid + "============>" + r);
							System.out.println("es[1]============>" + es[1]);
							int first_space = es[1].indexOf(" ");
							System.out.println("f =>" + es[1].substring(0, first_space));
							System.out.println("r =>" + es[1].substring(first_space + 1));
							String index_arr = es[1].substring(first_space + 1);
							String[] isa = index_arr.trim().split(";");
							String[] firstfrag=isa[0].split(" ");
							String[] secondfrag=isa[1].split(" ");
							String domain=es[1].substring(0, first_space).trim();
							sb.append(nctid+"_exc"+"\t"+domain+"\t"+firstfrag[0]+"\t"+secondfrag[1]+"\t"+firstfrag[1]+"\t"+secondfrag[0]+"\n");

						}else{
							System.out.println(nctid + "============>" + r);
							System.out.println("no ; es[1]============>" + es[1]);
							int first_space = es[1].indexOf(" ");
							System.out.println("f =>" + es[1].substring(0, first_space));
							String domain=es[1].substring(0, first_space).trim();
							System.out.println("r =>" + es[1].substring(first_space + 1));
							String index_arr = es[1].substring(first_space + 1);
							String[] pos_arr = index_arr.trim().split(" ");
							sb.append(nctid+"_exc"+"\t"+domain+"\t"+pos_arr[0]+"\t"+pos_arr[1]+"\t"+"-1"+"\t"+"-1"+"\n");
						}
					}
				}
			}
		}
		FileUtil.write2File("/Users/cy2465/Documents/ctdata_ec.txt",sb.toString());
		//System.out.println("size=" + filelist.size());
	}

}
