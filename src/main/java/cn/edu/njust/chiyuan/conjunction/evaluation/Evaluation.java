package cn.edu.njust.chiyuan.conjunction.evaluation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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

import cn.edu.njust.chiyuan.conjunction.datapreparation.Word2vec;
import cn.edu.njust.chiyuan.conjunction.pojo.ConjunctCandidate;
import cn.edu.njust.chiyuan.conjunction.pojo.ConjunctChunk;
import cn.edu.njust.chiyuan.conjunction.pojo.Token;
import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;
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

public class Evaluation {
	public static void main(String[] args) throws IOException {
		
		//pubmed_phrase_word_combo_vectors_win_5
		//String embeddingpath="/Users/cy2465/Downloads/vectors-phrase.bin";
		
		String embeddingpath="/Users/cy2465/Documents/pubmed_phrase_word_combo_vectors_win_5.bin";
		//test i2b2
		
		/*
		String cbow_win_5_model=embeddingpath;
		String gspath="/Users/cy2465/Downloads/conj_data/i2b2_ec_ziran_gold_standard.txt";
		String prepath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/i2b2_eval_cbow_win_5_df.txt";
		String errorpath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/i2b2_eval_cbow_win_5_error_df.txt";
		String debuggerpath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/i2b2_eval_cbow_win_5_df_debugger.txt";
		String pairfilepath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/i2b2_eval_pairs.txt";
		
		predictResultsDefault(gspath,prepath,cbow_win_5_model,pairfilepath,debuggerpath);
		calculateAccuracy(gspath,prepath,errorpath);
		
		*/
		//
		
		
		String cbow_win_5_model=embeddingpath;//pubmed_phrase_word_combo_vectors_win_5
		String gspath="/Users/cy2465/Downloads/conj_data/ncbi_gold_standard_answers.txt";
		String prepath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/ncbi_eval_cbow_win_5_df.txt";
		String debuggerpath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/ncbi_eval_cbow_win_5_df_debugger.txt";
		String errorpath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/ncbi_eval_cbow_win_5_error_df.txt";
		String pairfilepath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/ncbi_eval_pairs.txt";
		predictResultsDefault(gspath,prepath,cbow_win_5_model,pairfilepath,debuggerpath);
		//calculateAccuracy(gspath,prepath,errorpath);
		
		
		
		//pubmed_word_vectors_win_5.bin
		/*
		String cbow_win_5_model=embeddingpath;//pubmed_phrase_word_combo_vectors_win_5
		String gspath="/Users/cy2465/Downloads/conj_data/ct_gov_gold_standard_w_o_dup.txt";
		String prepath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/ct_gov_eval_cbow_win_5_df.txt";
		String debuggerpath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/ct_gov_eval_cbow_win_5_df_debugger.txt";
		String errorpath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/ct_gov_eval_cbow_win_5_error_df.txt";
		String pairfilepath="/Users/cy2465/Downloads/conj_data/patch_single_word_comp/ct_gov_eval_pairs.txt";
		predictResultsDefault(gspath,prepath,cbow_win_5_model,pairfilepath,debuggerpath);
		calculateAccuracy(gspath,prepath,errorpath);
*/
	}

	public static void outputResults(List<String> cleantext,List<ConjunctCandidate> cclist){
		//branching point
		Graph<Integer, DefaultEdge> goutput = new DefaultDirectedGraph<>(DefaultEdge.class);
		//cleantext
		goutput.addVertex(0);//start_point
		for(Integer windex=1;windex<cleantext.size();windex++){
			goutput.addVertex(windex);
		}
		goutput.addVertex(cleantext.size());//end_point
		
		//1) 多种交叉的情况breast or ovarian cancer and tumor disease;
		
		//2）不交叉的情况 breast or ovarian cancer
		
		List<LinkedHashSet<ConjunctCandidate>> lls=new ArrayList<LinkedHashSet<ConjunctCandidate>>();
		
		LinkedHashSet<ConjunctCandidate> ccset=new LinkedHashSet<ConjunctCandidate>();
		ccset.add(cclist.get(0));
		ccset.add(cclist.get(0));
		for (int y=1;y< cclist.size();y++) {
			int headconjunct_1=cclist.get(y).conjunct_1[0];
			int tailconjunct_1=cclist.get(y).conjunct_1[1];
			int headconjunct_2=cclist.get(y).conjunct_2[0];
			int tailconjunct_2=cclist.get(y).conjunct_2[1];
			
			if(cclist.get(y).conjunct_1[0]==cclist.get(y-1).conjunct_2[0]
					&&cclist.get(y).conjunct_1[1]==cclist.get(y-1).conjunct_2[1]){
				
			}
		}
		
		for (ConjunctCandidate al : cclist) {
			int headconjunct_1=al.conjunct_1[0];
			int tailconjunct_1=al.conjunct_1[1];
			int headconjunct_2=al.conjunct_2[0];
			int tailconjunct_2=al.conjunct_2[1];
			//顺次连接Conjunct_1前面的所有token
			for(int c=0;c<headconjunct_1;c++){
				goutput.addEdge(c, c+1);
			}
		}
	}

	public static void calculateAccuracy(String gspath,String prepath,String errorpath) {
		String gold_standard=FileUtil.readfile(gspath);
		String discern_results=FileUtil.readfile(prepath);	
		String[] grows=gold_standard.split("\n");
		String[] drows=discern_results.split("\n");
		StringBuffer sb=new StringBuffer();
		Integer accuracycount=0;
		int errorcount=0;
		for(int j=0;j<grows.length;j++){
			System.out.println("=>"+grows[j]);
			String[] es=grows[j].split("\t");
			List<String> glist=new ArrayList<String>();
			for(int i=1;i<es.length;i++){
				if(es[i].length()<=1){
					continue;
				}
				System.out.println("---gold standard---->"+es[i].trim().replace(" ", "-"));
				glist.add(es[i].trim().replace(" ", "-"));
			}
			String[] eds=drows[j].split("\t");
			List<String> elist=new ArrayList<String>();
			for(int i=1;i<eds.length;i++){
				if(eds[i].length()<=1){
					continue;
				}
				System.out.println("---eval results---->"+eds[i].trim().replace(" ", "-"));
				elist.add(eds[i].trim().replace(" ", "-"));
			}
			boolean ifa=true;
			System.out.println(glist.size()+" vs "+elist.size());
			if(glist.size()!=elist.size()){
				ifa=false;
			}else{
				for(int a=0;a<glist.size();a++){
					System.out.println(glist.get(a)+" vs "+elist.get(a));
					if(glist.get(a).equals(elist.get(a))){
						ifa=true;
					}else{
						ifa=false;
						break;
					}
				}
			}
			
			if(ifa==true){
				System.out.println(j+"======> true");
				accuracycount++;
			}else{
				errorcount++;
				System.out.println(j+"===================================================> wrong");
				sb.append("g=>"+grows[j]+"\n" +"t=>"+drows[j]+"\n");
			}
		}
		
		System.out.println(grows.length+"\t"+drows.length);
		System.out.println("accuracycount="+accuracycount);
		System.out.println("errorcount="+errorcount);
		Double ac=((double) accuracycount/(double) drows.length);
		System.out.println("ac="+ac);
		FileUtil.write2File(errorpath, sb.toString());
	}
	
	
	public static void predictResultsDefault(String gspath, String predictpath, String modelpath, String pairfile,
		String debuggerfile) throws IOException {
		String content = FileUtil.readfile(gspath);
		String[] rows = content.split("\n");
		int count = 0;
		int k = 0;
		Word2vec phrasevec = new Word2vec();
		phrasevec.loadModel(modelpath);// bio_embedding_intrinsic.octet-stream///Users/cy2465/Documents/vectors-phrase.bin
		// pubmed_phrase_word_combo_vectors_win_5.bin
		StringBuffer anssb = new StringBuffer();
		StringBuffer pairlist = new StringBuffer();
		// wordvec.loadModel("/Users/cy2465/Downloads/pubmed_word_vectors_win_5.bin");
		List<ConjunctCandidate> canlist = new ArrayList<ConjunctCandidate>();
		StringBuffer sbeval = new StringBuffer();
		StringBuffer debugger = new StringBuffer();		
		Annotation annotation;
		Properties properties = PropertiesUtils.asProperties("annotators", "tokenize,ssplit,pos,lemma,depparse");
		AnnotationPipeline pipeline = new StanfordCoreNLP(properties);
		
		for (String r : rows) {
			String text = r.split("\t")[0];
			sbeval.append(text + "\t");
			text = text.replace("/", " / ");
			annotation = new Annotation(text);
			pipeline.annotate(annotation);
			List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
			List<Token> tokenlist=new ArrayList<Token>();
			for (CoreMap sentence : sentences) {
				for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
					String word = token.get(TextAnnotation.class);
					String pos = token.get(PartOfSpeechAnnotation.class);
					
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
			List<String> cleantext = new ArrayList<String>();

			for (int a = 0; a < tokenlist.size(); a++) {
				if (a > 0) {
					if (cleantext.get(cleantext.size() - 1).equals("<CONJ>") && tokenlist.get(a).word.equals("<CONJ>")) {
						continue;
					}
				}
				cleantext.add(tokenlist.get(a).word);
			}
			// chunking by CONJ units
			List<Integer[]> chunkpos = new ArrayList<Integer[]>();
			for (int j = 0; j < cleantext.size(); j++) {
				if (cleantext.get(j).equals("<CONJ>") == true) {
					int start = 0;
					int end = cleantext.size() - 1;
					if (j >= 1) {
						for (int q = j - 1; q > 0; q--) {
							if (cleantext.get(q).equals("<CONJ>") == true) {
								start = q + 1;
								break;
							}
						}
					}
					if (j <= cleantext.size() - 2) {
						for (int p = j + 1; p < cleantext.size(); p++) {
							if (cleantext.get(p).equals("<CONJ>") == true) {
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
			
			List<ConjunctChunk> clist = new ArrayList<ConjunctChunk>();
			
			//clist=calSemanticSyntactic(cleantext, chunkpos, phrasevec,tokenlist);
			clist=calSemanticOnly(cleantext, chunkpos, phrasevec,tokenlist);
			
			//
			for(ConjunctChunk cc:clist){
				for(ConjunctCandidate cce:cc.set){
					pairlist.append(cce.conjunct_1_str_space+"\t"+cce.conjunct_2_str_space+"\n");
				}
			}
			//
			Graph<ConjunctCandidate, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

			for (int cjtci = 0; cjtci < clist.size(); cjtci++) {
				for (ConjunctCandidate cce : clist.get(cjtci).set) {
					g.addVertex(cce);
				}
				if (cjtci > 0) {
					for (ConjunctCandidate cce : clist.get(cjtci).set) {
						for (ConjunctCandidate cceupper : clist.get(cjtci - 1).set) {
							g.addEdge(cceupper, cce);
						}
					}
				}
			}
			if (clist.size() == 0) {
				sbeval.append("\n");
				continue;
			}
			HashMap<List<ConjunctCandidate>, Float> map = new HashMap<List<ConjunctCandidate>, Float>();
			AllDirectedPaths adr = new AllDirectedPaths(g);
			for (ConjunctCandidate ccetop : clist.get(0).set) {
				for (ConjunctCandidate ccebottom : clist.get(clist.size() - 1).set) {
					List<GraphPath<ConjunctCandidate, DefaultEdge>> as = adr.getAllPaths(ccetop, ccebottom, true, null);
					for (int i = 0; i < as.size(); i++) {
						List<ConjunctCandidate> allvs = as.get(i).getVertexList();
						float f = 1;
						float totalp = 0;
						for (ConjunctCandidate al : allvs) {
							f = f * al.probability;
							totalp = totalp + al.probability;
						}
						map.put(allvs, f);
					}
				}
			}
			List<Map.Entry<List<ConjunctCandidate>, Float>> list = new ArrayList<Map.Entry<List<ConjunctCandidate>, Float>>(
					map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<List<ConjunctCandidate>, Float>>() {
				public int compare(Entry<List<ConjunctCandidate>, Float> o1, Entry<List<ConjunctCandidate>, Float> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
			// high possibility
			Map.Entry<List<ConjunctCandidate>, Float> mapping = list.get(0);
			
			for(int x=0;x<cleantext.size();x++){
				System.out.print(x+",");
			}
			System.out.println();
			
			for(int x=0;x<cleantext.size();x++){
				System.out.print(cleantext.get(x)+",");
			}
			
			System.out.println();
			for (ConjunctCandidate al : mapping.getKey()) {
				sbeval.append(al.conjunct_1_str.toLowerCase() + "&&" + al.conjunct_2_str.toLowerCase() + "\t");
				System.out.println(al.conjunct_1[0]+"-"+al.conjunct_1[1]+"\t"+al.conjunct_2[0]+"-"+al.conjunct_2[1]);
			}
			
			sbeval.append("\n");
		}
		
		FileUtil.write2File(predictpath, sbeval.toString());
		FileUtil.write2File(pairfile, pairlist.toString());
		FileUtil.write2File(debuggerfile, debugger.toString());
	}
	
	public static List<ConjunctChunk> calSemanticOnly(List<String> cleantext, List<Integer[]> chunkpos, Word2vec phrasevec, List<Token> tokenlist){
		List<ConjunctChunk> clist=new ArrayList<ConjunctChunk>();
		for (Integer[] cc : chunkpos) {
			System.out.println(cc[0] + "\t" + cc[1]);
			List<String> substr = new ArrayList<String>();
			ConjunctChunk conjchunk = new ConjunctChunk();
			for (int ss = cc[0]; ss <= cc[1]; ss++) {
				substr.add(cleantext.get(ss));
			}
			conjchunk.chunkstr = transStr(substr);
			conjchunk.startindex = cc[0];
			conjchunk.endindex = cc[1];
			Set<ConjunctCandidate> ccset = new HashSet<ConjunctCandidate>();
			for (int z = 0; z < substr.size(); z++) {
				if (substr.get(z).equals("<CONJ>")) {
					for (int m = z - 1; m >= 0; m--) {
						for (int n = z + 1; n < substr.size(); n++) {
							float x = 0;
							String type = "";
							float x_entire = 0;
							try {

								type = "phrase2vec";
								x_entire = phrasevec.calculateDis(strRender(m, z - 1, substr).toLowerCase(),
										strRender(z + 1, n, substr).toLowerCase());
							} catch (Exception ex) {
								x_entire = 0;
							}
							
							float syntactic=0;
							x_entire=Math.abs(x_entire);
							syntactic=Math.abs(syntactic);
							
							//POS patch
							if(syntactic>x_entire){
								x=syntactic;
							}else{
								x=x_entire;
							}								
							ConjunctCandidate ccd = new ConjunctCandidate();
							ccd.conjunct_1[0] = cc[0] + m;
							ccd.conjunct_1_str = strRender(m, z - 1, substr);
							ccd.conjunct_1_str_space = strRenderwithSpace(m, z - 1, substr);
							ccd.conjunct_1[1] = cc[0] + z - 1;
							ccd.conjunct_2[0] = cc[0] + z + 1;
							ccd.conjunct_2_str = strRender(z + 1, n, substr);
							ccd.conjunct_2_str_space=strRenderwithSpace(z + 1, n, substr);
							ccd.conjunct_2[1] = cc[0] + n;
							ccd.probability = x;//Math.abs
							ccd.type = type;
							ccset.add(ccd);
						}
					}
				}
			}
			conjchunk.set = ccset;
			clist.add(conjchunk);
		}
		return clist;
	}
	
	
	
	public static List<ConjunctChunk> calSemanticSyntactic(List<String> cleantext, List<Integer[]> chunkpos, Word2vec phrasevec, List<Token> tokenlist){
		List<ConjunctChunk> clist=new ArrayList<ConjunctChunk>();
		for (Integer[] cc : chunkpos) {
			System.out.println(cc[0] + "\t" + cc[1]);
			List<String> substr = new ArrayList<String>();
			ConjunctChunk conjchunk = new ConjunctChunk();
			for (int ss = cc[0]; ss <= cc[1]; ss++) {
				substr.add(cleantext.get(ss));
			}
			conjchunk.chunkstr = transStr(substr);
			conjchunk.startindex = cc[0];
			conjchunk.endindex = cc[1];
			Set<ConjunctCandidate> ccset = new HashSet<ConjunctCandidate>();
			for (int z = 0; z < substr.size(); z++) {
				if (substr.get(z).equals("<CONJ>")) {
					for (int m = z - 1; m >= 0; m--) {
						for (int n = z + 1; n < substr.size(); n++) {
							float x = 0;
							String type = "";
							float x_entire = 0;
							try {

								type = "phrase2vec";
								x_entire = phrasevec.calculateDis(strRender(m, z - 1, substr).toLowerCase(),
										strRender(z + 1, n, substr).toLowerCase());
							} catch (Exception ex) {
								x_entire = 0;
							}
							
							float syntactic=0;

							if ((z - 1 - m) > 0 || (n - z - 1) > 0) {
								// 如果尾部的POS不同
								// 1 vs n 的情况
								// 那么1跟n的最后一个比 
								//hematopoietic and solid tumors
								//fibroepithelial or epithelial hyperplasias
								if ((z - 1 - m) == 0 && (n - z - 1) > 0) {
									
									int dis= (n- z -1 +1) - 1 +1;
									if(posComparator(tokenlist.get(conjchunk.startindex + z -1 ).pos,tokenlist.get(conjchunk.startindex + n ).pos)){//如果POS相同的话
										try{
										syntactic=phrasevec.calculateDis(strRender(z-1, z - 1, substr).toLowerCase(),
												strRender(n, n, substr).toLowerCase());
										}catch(Exception ex){
											syntactic=0;
										}
										syntactic=syntactic/dis;
									}
								}
								// n vs 1 的情况
								// n的最后一个 跟 1 比
								if ((z - 1 - m) > 0 && (n - z - 1) == 0) {
									int dis= (z- 1 -m +1) - 1 +1;
									if(posComparator(tokenlist.get(conjchunk.startindex + z -1 ).pos,tokenlist.get(conjchunk.startindex + n ).pos)){
										try{
										syntactic=phrasevec.calculateDis(strRender(z-1 , z - 1, substr).toLowerCase(),
											strRender(n, n, substr).toLowerCase());
										}catch(Exception ex){
											syntactic=0;
										}
										syntactic=syntactic/dis;
									}
								}
								// n vs n的情况
								// 1 vs 1 && n 最后一个 vs n 最后一个	除以2 
								if ((z - 1 - m) > 0 && (n - z - 1) > 0) {
									int dis= (z- 1 -m +1) - ( n- z -1 +1);
									dis=Math.abs(dis)+1;
									
									if(posComparator(tokenlist.get(conjchunk.startindex + m ).pos,tokenlist.get(conjchunk.startindex + z+1 ).pos)
											&& posComparator(tokenlist.get(conjchunk.startindex + z-1 ).pos,tokenlist.get(conjchunk.startindex + n).pos)){
									try{
										syntactic=syntactic+phrasevec.calculateDis(strRender(m , m, substr).toLowerCase(),
											strRender(z+1, z+1, substr).toLowerCase());
									}catch(Exception ex){
										syntactic=syntactic+0;
									}
									try{
										syntactic=syntactic+phrasevec.calculateDis(strRender(z-1 , z - 1, substr).toLowerCase(),
											strRender(n, n, substr).toLowerCase());
									}catch(Exception ex){
										syntactic=syntactic+0;
									}
									syntactic=syntactic/dis;
									syntactic=syntactic/2;
									}
								}
								
							}	
							x_entire=Math.abs(x_entire);
							syntactic=Math.abs(syntactic);
							
							//POS patch
							if(syntactic>x_entire){
								x=syntactic;
							}else{
								x=x_entire;
							}								
							ConjunctCandidate ccd = new ConjunctCandidate();
							ccd.conjunct_1[0] = cc[0] + m;
							ccd.conjunct_1_str = strRender(m, z - 1, substr);
							ccd.conjunct_1_str_space = strRenderwithSpace(m, z - 1, substr);
							ccd.conjunct_1[1] = cc[0] + z - 1;
							ccd.conjunct_2[0] = cc[0] + z + 1;
							ccd.conjunct_2_str = strRender(z + 1, n, substr);
							ccd.conjunct_2_str_space=strRenderwithSpace(z + 1, n, substr);
							ccd.conjunct_2[1] = cc[0] + n;
							ccd.probability = x;//Math.abs
							ccd.type = type;
							ccset.add(ccd);
						}
					}
				}
			}
			conjchunk.set = ccset;
			clist.add(conjchunk);
		}
		return clist;
	}
	
	public static boolean posComparator(String pos_1, String pos_2){
		if(pos_1.substring(0, 1).equals(pos_2.substring(0, 1))){
			return true;
		}else{
			if( (pos_1.equals("JJ")&&pos_2.equals("RB")) ||(pos_1.equals("RB")&&pos_2.equals("JJ"))){
				return true;
			}else{
				return false;
			}
		}
	}

	public static String transStr(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(s + " ");
		}
		return sb.toString().trim();
	}
	// 按照start index & end index 组合词组
	public static String strRender(Integer start, Integer end, List<String> arr) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (int a = start; a <= end; a++) {
			if (i == 0) {
				sb.append(arr.get(a));
			} else {
				sb.append("-" + arr.get(a));// pubmed
			}
			i++;

		}
		return sb.toString();
	}
	
	public static String strRenderwithSpace(Integer start, Integer end, List<String> arr) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (int a = start; a <= end; a++) {
			if (i == 0) {
				sb.append(arr.get(a));
			} else {
				sb.append(" " + arr.get(a));// pubmed
			}
			i++;

		}
		return sb.toString();
	}

	public static int getCount(String string, String a) {
		int i = string.length() - string.replace(a, "").length();
		return i / a.length();
	}
}
