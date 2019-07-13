package cn.edu.njust.chiyuan.pipeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cn.edu.njust.chiyuan.conjunction.pojo.ConjCandidate;
import cn.edu.njust.chiyuan.conjunction.pojo.ConjCandidatePair;
import cn.edu.njust.chiyuan.conjunction.pojo.ConjChunk;
import cn.edu.njust.chiyuan.conjunction.pojo.Token;
import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;
import cn.edu.njust.chiyuan.conjunction.utils.StringUtil;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

public class ReCON {
	Properties properties = PropertiesUtils.asProperties("annotators", "tokenize,ssplit,pos,lemma,depparse");
	AnnotationPipeline pipeline = new StanfordCoreNLP(properties);
	
	public static void main(String[] args) throws IOException {
		ReCON recon = new ReCON();

		String[] type_arr = { "ct_gov", "i2b2", "ncbi" };
		String[] model_arr = { "pubmedphrase2vec", "phrase2vec" };
		for (String ty : type_arr) {
			for (String m : model_arr) {
				String type = ty;
				String model = m;// phrase2vec
				String content = FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/gs_" + type + ".txt");

				String dic = FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/dic_" + type + "_" + model + ".txt");
				
				System.out.println("=>"+"/Users/cy2465/Downloads/RECON_eval/gs_" + type + ".txt");
				System.out.println("/Users/cy2465/Downloads/RECON_eval/dic_" + type + "_" + model + ".txt");
				HashMap<String, Float> dicmap = new HashMap<String, Float>();
				String[] dic_rows = dic.split("\n");
				for (String dic_r : dic_rows) {
					String[] dicc = dic_r.split("\t");
					dicmap.put(dicc[0] + "&&" + dicc[1], Float.valueOf(dicc[2]));
				}

				String[] rows = content.split("\n");

				StringBuffer pairs = new StringBuffer();
				StringBuffer results = new StringBuffer();
				for (String r : rows) {
					String[] es = r.split("\t");
					List<Token> tlist = recon.preproCE(es[0]);
					for (Token t : tlist) {
						System.out.println(t.word + "\t" + t.pos);
					}
					List<ConjChunk> chunks = recon.conjCandidatePairGeneration(tlist);
					for (ConjChunk cck : chunks) {
						System.out.println("chunk:" + cck.chunkstr);
						List<ConjCandidatePair> ccps = cck.pairs;
						float x = 0.8f;
						int c = 1;
						for (ConjCandidatePair ccp : ccps) {
							System.out.println(ccp.conjcan_1.text + " vs " + ccp.conjcan_2.text);
							// pairs.append(ccp.conjcan_1.text + "\t" +
							// ccp.conjcan_2.text + "\n");
							ccp.probability = Math.abs(dicmap.get(ccp.conjcan_1.text.toLowerCase() + "&&" + ccp.conjcan_2.text.toLowerCase()));
							// Calculate semantic similarity
						}
					}
					// select resolution path

					List<ConjCandidatePair> ccdps = recon.resolutionPathSelection(chunks);
					// phrase reconstruction
					List<String> phrases = recon.phraseReconstruction(tlist, ccdps);
					String ansstr = StringUtil.answerRender(phrases);
					results.append(es[0] + "\t" + ansstr + "\n");

				}
				FileUtil.write2File("/Users/cy2465/Downloads/RECON_eval/" + type + "_" + model + "_answer.txt",
						results.toString());
			}
	}
		//FileUtil.write2File("/Users/cy2465/Downloads/RECON_eval/0517results/"+type+"_pairs.txt", pairs.toString());
	}
	
	public List<String> phraseReconstruction(List<Token> tokens, List<ConjCandidatePair> ccdps) {
		Graph<Integer, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
		//grouping the conjunct candidate pairs
		List<List<ConjCandidate>> ccdjlist=new ArrayList<List<ConjCandidate>>();
		
		for (int c = 0; c < ccdps.size(); c++) {
			ConjCandidatePair ccdp=ccdps.get(c);
			ConjCandidate c1=ccdp.conjcan_1;
			ConjCandidate c2=ccdp.conjcan_2;
			int index=-1;
			for (int p = 0; p < ccdjlist.size(); p++) {
				List<ConjCandidate> conjcanset = ccdjlist.get(p);
				for(ConjCandidate ccd:conjcanset){
					if ((ccd.start_index==c1.start_index && ccd.end_index==c1.end_index)){
						index=p;
						break;
					}
				}
				if(index!=-1){
					break;
				}
				
			}
			if(index==-1){
				List<ConjCandidate> conjcanset = new ArrayList<ConjCandidate>();
				conjcanset.add(c1);
				conjcanset.add(c2);
				ccdjlist.add(conjcanset);
			}else{
				List<ConjCandidate> conjcanset =ccdjlist.get(index);
				conjcanset.add(c2);
				ccdjlist.remove(index);
				ccdjlist.add(index,conjcanset);
			}
			
		}
		for(List<ConjCandidate> conjcanset:ccdjlist){
			System.out.println("=====================");
			for(ConjCandidate ccd:conjcanset){
				System.out.println(ccd.text);
			}
			System.out.println("=====================");
		}
		HashMap<Integer, Integer> backward=new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> forward=new HashMap<Integer, Integer>();
		// Step 1 add starting token and ending token
		Integer start=-1;
		Integer end=tokens.size();
		g.addVertex(start);
		g.addVertex(end);	
		for(int i=0;i<tokens.size();i++){
			g.addVertex(i);
		}	
		g.addEdge(start, 0);
		backward.put(0, start);
		forward.put(start, 0);
		g.addEdge(tokens.size()-1,end);	
		backward.put(end, tokens.size()-1);
		forward.put(tokens.size()-1, end);
		int last_conjchunk_last_index=0;
		//确定前向词
		for (int c = 0; c < ccdjlist.size(); c++) {
			List<ConjCandidate> ccds=ccdjlist.get(c);
			//第一个词连接前面所有词
			if (c == 0) {
				int first_index=ccds.get(0).start_index;
				for(int e=0;e<=first_index-1;e++){
					System.out.println("add edge: "+e+"\t"+(e+1));
					g.addEdge(e, e+1);
					backward.put(e+1, e);
					forward.put(e, e+1);
				}
			}
			//查找首词的前面一个词
			System.out.println("first start->"+ccds.get(0).start_index);
			int source_to_first=0;
			if(backward.containsKey(ccds.get(0).start_index)==true){
				source_to_first=backward.get(ccds.get(0).start_index);
			}else{
				//
				source_to_first=ccds.get(0).start_index-1;
				List<ConjCandidate> lastconjcc=ccdjlist.get(c-1);
				int pre_last_index=lastconjcc.get(lastconjcc.size()-1).end_index;
				for(int h=pre_last_index;h<=ccds.get(0).start_index-1;h++){
					System.out.println("add edge: "+h+"\t"+(h+1));
					g.addEdge(h, h+1);
					backward.put(h+1, h);
					forward.put(h, h+1);
				}
			}
			//连接内部的所有词 && 连接首词前置词
			for(ConjCandidate ccd:ccds){
				for(int d=ccd.start_index;d<=ccd.end_index-1;d++){
					System.out.println("add edge: "+d+"\t"+(d+1));
					g.addEdge(d, d+1);
					backward.put(d+1, d);
					forward.put(d, d+1);
				}
				System.out.println("add edge: "+source_to_first+"\t"+(ccd.start_index));
				g.addEdge(source_to_first, ccd.start_index);
				backward.put(ccd.start_index, source_to_first);
				forward.put(source_to_first, ccd.start_index);
				//System.out.println("add edge: "+source_to_first+"\t"+(ccd.start_index));
			}	
			
		}
		
		//确定结尾词
		for (int f = ccdjlist.size()-1; f >= 0; f--) {
			List<ConjCandidate> ccds=ccdjlist.get(f);
			if (f == ccdjlist.size()-1) {
				int last_index=ccds.get(ccds.size()-1).end_index;
				for(int e=last_index;e<=tokens.size()-2;e++){
					System.out.println("add edge (last to end): "+e+"\t"+(e+1));
					g.addEdge(e, e+1);
					backward.put(e+1, e);
					forward.put(e,e+1);
				}	
			}
			int target_to_last=forward.get(ccds.get(ccds.size()-1).end_index);
			if(forward.containsKey(ccds.get(ccds.size()-1).end_index)==true){
				target_to_last=forward.get(ccds.get(ccds.size()-1).end_index);
			}else{
				//
				target_to_last=ccds.get(ccds.size()-1).end_index+1;
				List<ConjCandidate> lastconjcc=ccdjlist.get(f+1);
				int after_first_index=lastconjcc.get(0).start_index;
				for(int h=ccds.get(ccds.size()-1).end_index;h<=after_first_index-1;h++){
					g.addEdge(h, h+1);
					backward.put(h+1, h);
					forward.put(h, h+1);
				}
			}
			
			//连接内部的所有词 && 连接首词前置词
			for(ConjCandidate ccd:ccds){
				//
				
				//
				g.addEdge(ccd.end_index, target_to_last);
				backward.put(target_to_last, ccd.end_index);
				forward.put(ccd.end_index,target_to_last);
				//System.out.println("add edge: "+ccd.end_index+"\t"+(target_to_last));
			}	
			
		}
		for(DefaultEdge e : g.edgeSet())
		    System.out.println(g.getEdgeSource(e)+"->"+g.getEdgeTarget(e));
		
		AllDirectedPaths adr = new AllDirectedPaths(g);	
		List<GraphPath<Integer, DefaultEdge>> as =adr.getAllPaths(start, end, true, null);	
		List<String> phrases=new ArrayList<String>();
		for (int i = 0; i < as.size(); i++) {
			List<Integer> allvs = as.get(i).getVertexList();	
			/*
			for (Integer node : allvs) {
				System.out.print(node+"->");
			}
			*/
			//System.out.println();
			StringBuffer sb=new StringBuffer();
			for (Integer node : allvs) {
				if(node>start && node<end){
					//System.out.print(tokens.get(node).word+" ");
					sb.append(tokens.get(node).word+" ");
				}
				
			}
			phrases.add(sb.toString().trim());
			//System.out.println();
		}
		
		return phrases;
		
	}
	
	public List<ConjCandidatePair> resolutionPathSelection(List<ConjChunk> conjchunks){
		Graph<ConjCandidatePair, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

		for (int cjtci = 0; cjtci < conjchunks.size(); cjtci++) {
			for(ConjCandidatePair ccp:conjchunks.get(cjtci).pairs){
				g.addVertex(ccp);
				System.out.println("add vertex "+ccp.conjcan_1.text+"&&"+ccp.conjcan_2.text);
			}
		}
		//add in edges
		for (int cjtci = 0; cjtci < (conjchunks.size()-1); cjtci++) {
			for (int a = 0; a < conjchunks.get(cjtci).pairs.size(); a++) {
				for (int b = 0; b < conjchunks.get(cjtci+1).pairs.size(); b++) {
					g.addEdge(conjchunks.get(cjtci).pairs.get(a), conjchunks.get(cjtci+1).pairs.get(b));
					ConjCandidatePair ccp1 = conjchunks.get(cjtci).pairs.get(a);
					ConjCandidatePair ccp2 = conjchunks.get(cjtci+1).pairs.get(b);
					//System.out.println("add edge " + ccp1.conjcan_1.text + "&&" + ccp1.conjcan_2.text + " -->"
					//		+ ccp2.conjcan_1.text + "&&" + ccp2.conjcan_2.text);
				}
			}

		}	
		HashMap<List<ConjCandidatePair>, Float> map = new HashMap<List<ConjCandidatePair>, Float>();
		AllDirectedPaths adr = new AllDirectedPaths(g);	
		if(conjchunks.size()==0){
			return new ArrayList<ConjCandidatePair>();
		}
		for (int a=0;a<conjchunks.get(0).pairs.size();a++) {
			for (int b=0;b<conjchunks.get(conjchunks.size()-1).pairs.size();b++) {
				ConjCandidatePair first_ccdp=conjchunks.get(0).pairs.get(a);
				ConjCandidatePair last_ccdp=conjchunks.get(conjchunks.size()-1).pairs.get(b);
				List<GraphPath<ConjCandidatePair, DefaultEdge>> as =adr.getAllPaths(first_ccdp, last_ccdp, true, 1000);		
				//System.out.println("start with "+first_ccdp.conjcan_1.text+" vs "+first_ccdp.conjcan_2.text);
				//System.out.println("end with "+last_ccdp.conjcan_1.text+" vs "+last_ccdp.conjcan_2.text);
				for (int i = 0; i < as.size(); i++) {
					List<ConjCandidatePair> allvs = as.get(i).getVertexList();
					float f = 1;
					float totalp = 0;
					for (ConjCandidatePair al : allvs) {
						f = f * al.probability;
						totalp = totalp + al.probability;
					}
					map.put(allvs, f);
				}
			}
		}
		
		List<Map.Entry<List<ConjCandidatePair>, Float>> list = new ArrayList<Map.Entry<List<ConjCandidatePair>, Float>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<List<ConjCandidatePair>, Float>>() {
			public int compare(Entry<List<ConjCandidatePair>, Float> o1, Entry<List<ConjCandidatePair>, Float> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		Map.Entry<List<ConjCandidatePair>, Float> mapping = list.get(0);
		return mapping.getKey();
	}
	
	public List<ConjChunk> conjCandidatePairGeneration(List<Token> tokens) {
		
		List<Integer[]> chunkpos = new ArrayList<Integer[]>();
		for (int j = 0; j < tokens.size(); j++) {
			if (tokens.get(j).word.equals("<CONJ>") == true) {
				int start = 0;
				int end = tokens.size() - 1;
				if (j >= 1) {
					for (int q = j - 1; q > 0; q--) {
						if (tokens.get(q).word.equals("<CONJ>") == true) {
							start = q + 1;
							break;
						}
					}
				}
				if (j <= tokens.size() - 2) {
					for (int p = j + 1; p < tokens.size(); p++) {
						if (tokens.get(p).word.equals("<CONJ>") == true) {
							end = p - 1;
							break;
						}
					}
				}
				Integer[] pos = new Integer[2];
				pos[0] = start;
				pos[1] = end;
				chunkpos.add(pos);
			}
		}
		
		List<ConjChunk> conjchunks=new ArrayList<ConjChunk>();
		for (Integer[] cc : chunkpos) {
			//System.out.println(cc[0] + "\t" + cc[1]);
			List<Token> substr = new ArrayList<Token>();
			ConjChunk conjchunk = new ConjChunk();
			for (int ss = cc[0]; ss <= cc[1]; ss++) {
				substr.add(tokens.get(ss));
			}
			conjchunk.chunkstr = StringUtil.chunkStrRender(substr);
			conjchunk.startindex = cc[0];
			conjchunk.endindex = cc[1];
			List<ConjCandidatePair> conjccdpairs=new ArrayList<ConjCandidatePair>();
			for (int z = 0; z < substr.size(); z++) {
				if (substr.get(z).word.equals("<CONJ>")) {
					for (int m = z - 1; m >= 0; m--) {
						for (int n = z + 1; n < substr.size(); n++) {
							String type = "";							
							//ConjunctCandidate ccd = new ConjunctCandidate();
							ConjCandidatePair ccdp=new ConjCandidatePair();
							
							ConjCandidate ccd1=new ConjCandidate();
							ccd1.start_index=cc[0] + m;
							ccd1.text=StringUtil.strRender(m, z - 1, substr);
							ccd1.end_index=cc[0] + z - 1;		
							ConjCandidate ccd2=new ConjCandidate();
							ccd2.start_index=cc[0] + z + 1;
							ccd2.text=StringUtil.strRender(z + 1, n, substr);
							ccd2.end_index=cc[0] + n;
							//ccd.conjunct_1[0] = cc[0] + m;
							//ccd.conjunct_1_str = StringUtil.strRender(m, z - 1, substr);
							//ccd.conjunct_1_str_space = StringUtil.strRenderwithSpace(m, z - 1, substr);
							//ccd.conjunct_1[1] = cc[0] + z - 1;	
							//ccd.conjunct_2[0] = cc[0] + z + 1;
							//ccd.conjunct_2_str = StringUtil.strRender(z + 1, n, substr);
							//ccd.conjunct_2_str_space=strRenderwithSpace(z + 1, n, substr);
							//ccd.conjunct_2[1] = cc[0] + n;
							//ccd.probability = 0;//Math.abs
							//ccd.type = type;
							ccdp.conjcan_1=ccd1;
							ccdp.conjcan_2=ccd2;
							conjccdpairs.add(ccdp);
						}
					}
				}
			}
			conjchunk.pairs = conjccdpairs;
			conjchunks.add(conjchunk);
		}
		return conjchunks;
	}
	
	public List<Token> preproCE(String text){
		text = text.replace("/", " / ");
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		List<Token> tokenlist=new ArrayList<Token>();
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);//token.get(LemmaAnnotation.class);//TextAnnotation.class
				String pos = token.get(PartOfSpeechAnnotation.class);
				//String lemma = token.get(LemmaAnnotation.class);
				boolean f = false;
				if ((word.equals("and") || word.equals(",") || word.equals("/") || word.equals("or"))) {
					f = true;
				}
				if (pos.equals("DT")) {
					continue;
				}
				if (pos.equals("PRP$")) {
					continue;
				}
				if (f == true) {
					Token tk = new Token();
					tk.pos = pos;
					tk.word = "<CONJ>";
					tokenlist.add(tk);
				} else {
					Token tk = new Token();
					tk.pos = pos;
					tk.word = word;
					tokenlist.add(tk);
				}
			}
		}
		List<Token> cleaned_tokenlist = new ArrayList<Token>();
		/* 
		 * remove continuous conjunction word
		 * e.g. breast or/and ovarian cancer
		 * 
		 */
		for (int a = 0; a < tokenlist.size(); a++) {
			if (a > 0) {
				if (cleaned_tokenlist.get(cleaned_tokenlist.size() - 1).word.equals("<CONJ>") && tokenlist.get(a).word.equals("<CONJ>")) {
					continue;
				}
			}
			cleaned_tokenlist.add(tokenlist.get(a));
		}
		return cleaned_tokenlist;
	}
}
