package cn.edu.njust.chiyuan.conjunction.evaluation;

import java.util.LinkedHashSet;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class TestEval {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String content = FileUtil.readfile("/Users/cy2465/Documents/ctdata_ec_conjcandidates.txt");
		String[] rows = content.split("\n");
		LinkedHashSet<String> set = new LinkedHashSet<String>();

		for (String r : rows) {

			String[] es = r.split("\t");

			set.add(es[0]);
			if (set.size() == 100) {
				break;
			}
		}

		StringBuffer sb=new StringBuffer();
		for (String r : rows) {

			String[] es = r.split("\t");

			if(set.contains(es[0])){
				sb.append(r+"\n");
			}
		}
		FileUtil.write2File("/Users/cy2465/Documents/test_sample.txt", sb.toString());

		/*
		 * List<ConjunctCandidate> lcc = new ArrayList<ConjunctCandidate>();
		 * String e = "infl A and B"; //
		 * System.out.println("========================="); StanfordNLP snlp =
		 * new StanfordNLP(); Tree tree = snlp.parseSentence(e);
		 * ArrayList<TaggedWord> twlist = tree.taggedYield(); List<Integer>
		 * conjindex = new ArrayList<Integer>(); List<String> ws = new
		 * ArrayList<String>(); for (int x = 0; x < twlist.size(); x++) { //
		 * System.out.println(twlist.get(x).word()+"\t"+twlist.get(x).tag());
		 * boolean f = false; if ((twlist.get(x).word().trim().equals("and") ||
		 * twlist.get(x).word().trim().equals(",") ||
		 * twlist.get(x).word().trim().equals("/") ||
		 * twlist.get(x).word().trim().equals("or"))) { f = true; }
		 * System.out.println(twlist.get(x).word()+"\t"+twlist.get(x).tag()); if
		 * (twlist.get(x).tag().equals("DT")) { continue; } if (f == true) {
		 * ws.add("<CONJ>"); } else { ws.add(twlist.get(x).word()); } }
		 */
	}

}
