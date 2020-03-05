package cn.edu.njust.chiyuan.receem.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cn.edu.njust.chiyuan.receem.pojo.CanConjPair;
import cn.edu.njust.chiyuan.receem.pojo.ConjCandidate;
import cn.edu.njust.chiyuan.receem.pojo.ConjCandidatePair;
import cn.edu.njust.chiyuan.receem.pojo.ConjChunk;
import cn.edu.njust.chiyuan.receem.pojo.DecomposingPath;
import cn.edu.njust.chiyuan.receem.pojo.SubChunk;
import cn.edu.njust.chiyuan.receem.pojo.SubSubChunk;
import cn.edu.njust.chiyuan.receem.pojo.Token;
import cn.edu.njust.chiyuan.receem.utils.FileUtil;
import cn.edu.njust.chiyuan.receem.utils.StringUtil;
import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;

public class ReceemPipeline {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String text="tumor necrosis factor receptor-associated factor (TRAF) 1, 2, or 3";
		text=text.replace(", ", " , ");
		int sindex=text.indexOf(" ");
		List<Integer> arr=new ArrayList<Integer>();
		arr.add(-1);
		while(sindex!=-1){
			sindex = text.indexOf(" ", sindex + 1);
			if(sindex>-1){
				arr.add(sindex);
			}
		}
		for(int i=0;i<arr.size()-1;i++){
			int startindex=arr.get(i)+1;
			int endindex=arr.get(i+1);
			System.out.println(text.substring(startindex,endindex));
		}
	}
	

	public List<Token> preproCE(String text) {
		List<Token> tokenlist = new ArrayList<Token>();
		int wordindex = 0;
		text=text.replace(", ", " , ");
		text=text.replace(", ", " , ");
		List<Integer> arr=new ArrayList<Integer>();
		arr.add(-1);
		int sindex=text.indexOf(" ");
		arr.add(sindex);
		
		while(sindex!=-1){
			sindex = text.indexOf(" ", sindex + 1);
			if(sindex>-1){
				arr.add(sindex);
			}
		}
		for(int i=0;i<arr.size()-1;i++){
			int startindex=arr.get(i)+1;
			int endindex=arr.get(i+1);
			//System.out.println("start:"+startindex);
			//System.out.println("end:"+endindex);
			//System.out.println(text.substring(startindex,endindex));
			String word = text.substring(startindex,endindex);
			String pos = "";
			wordindex++;
			boolean f = false;
			//System.out.println("word:" + word);
			//System.out.println("pos:" + pos);
			if ((word.equals("and")  || word.equals("and/or") || word.equals(",") || word.equals("/") || word.equals("or") || word.equals("&"))) {
				f = true;
			}
			//some
			if (word.equals("the")|| word.equals("some")|| word.equals("its") || word.equals("any")||word.equals("an") ||word.equals("a")|| word.equals("his")|| word.equals("her") || word.equals("my")|| word.equals("their")) {
				continue;
			}
			if (f == true) {
				Token tk = new Token();
				tk.pos = pos;
				tk.word = "<CONJ>";
				tk.origin=word;
				tk.begin=startindex;
				tk.end=endindex;
				tokenlist.add(tk);
			} else {
				Token tk = new Token();
				tk.pos = pos;
				tk.word = word;
				tk.origin=word;
				tk.begin=startindex;
				tk.end=endindex;
				tokenlist.add(tk);
			}

			
		}
		int startindex= arr.get(arr.size()-1)+1;
		int endindex=text.length();
		String word =  text.substring(startindex,endindex);
		String pos = "";
		wordindex++;
		boolean f = false;
		//System.out.println("word:" + word);
		//System.out.println("pos:" + pos);
		if ((word.equals("and") || word.equals(",") || word.equals("/") || word.equals("or") || word.equals("&"))) {
			f = true;
		}
		if (word.equals("the") || word.equals("its")|| word.equals("his")|| word.equals("any")||word.equals("a")|| word.equals("her") || word.equals("my")|| word.equals("their")) {
			
		}
		else{
			if (f == true) {
				Token tk = new Token();
				tk.pos = pos;
				tk.word = "<CONJ>";
				tk.origin=word;
				tk.begin=startindex;
				tk.end=endindex;
				tokenlist.add(tk);
			} else {
				Token tk = new Token();
				tk.pos = pos;
				tk.word = word;
				tk.origin=word;
				tk.begin=startindex;
				tk.end=endindex;
				tokenlist.add(tk);
			}
		}
		
		List<Token> cleaned_tokenlist = new ArrayList<Token>();
		/*
		 * remove continuous conjunction word e.g. breast or/and ovarian cancer
		 * 
		 */
		for (int a = 0; a < tokenlist.size(); a++) {
			if (a > 0) {
				if (cleaned_tokenlist.get(cleaned_tokenlist.size() - 1).word.equals("<CONJ>")
						&& tokenlist.get(a).word.equals("<CONJ>")) {
					continue;
				}
			}
			cleaned_tokenlist.add(tokenlist.get(a));
		}
		return cleaned_tokenlist;
	}
	
	public List<Token> hideBracketTokens(List<Token> tokens) {
		List<Token> hideBracketTokens=new ArrayList<Token>();
		for(int a=0;a<tokens.size();a++){
			StringBuffer leftstrsb=new StringBuffer();
			int hiddentextstart=0;
			int hiddentextend=0;
			Token currenttoken=new Token();
			currenttoken=tokens.get(a);
			currenttoken.representchunk=currenttoken.word;
			if(a+1 < tokens.size()){
				hiddentextstart = tokens.get(a + 1).begin;
				for (int b = a + 1; b < tokens.size(); b++) {
					leftstrsb.append(tokens.get(b).origin + " ");
				}
				// System.out.println("left string:"+leftstr.toString());
				// wordsb.append("word:"+t.word+" ");
				String leftstr = leftstrsb.toString();
				int endindex = 0;

				if (leftstr.startsWith("(")) {
					char[] arr = leftstr.toCharArray();
					for (int c = 0; c < arr.length; c++) {
						if (c + 2 < arr.length) {
							if (arr[c] == ')' && arr[c + 2] != '(') {
								endindex = c;
								break;
							}
						} else {
							if (arr[c] == ')') {
								endindex = c;
								break;
							}
						}
					}
					if (endindex > 0) {
						String hiddentext=leftstr.toString().substring(0, endindex + 1);
						hiddentextend = hiddentextstart + hiddentext.length();
						for (int kk = a + 1; kk < tokens.size(); kk++) {
							if (tokens.get(kk).begin >= hiddentextstart && tokens.get(kk).end <= hiddentextend) {
								currenttoken.representchunk=currenttoken.word+" "+hiddentext.replace("  ,", ",").trim();
								a=kk;
							}
						}
					} else {
						//System.out.println("hidden text: None");
					}
				}
			}
			hideBracketTokens.add(currenttoken);
		}
		return hideBracketTokens;
	}
	
	public List<Token> cleanAbnormalConjunctions(List<Token> tokens) {
		List<Token> tokenlist=new ArrayList<Token>();
		for(int a=0;a<tokens.size();a++){
			Token t=tokens.get(a);
			if(t.word.equals("-and")||t.word.equals("-or")){
				t.word="<CONJ>";
				if(a>0 && tokens.get(a).word.endsWith("-")==false){
					 tokens.get(a-1).word= tokens.get(a-1).word+"-";
				}
			}
			tokenlist.add(t);
		}
		List<Token> cleaned_tokenlist = new ArrayList<Token>();
		/*
		 * remove continuous conjunction word e.g. breast or/and ovarian cancer
		 * 
		 */
		for (int a = 0; a < tokenlist.size(); a++) {
			if (a > 0) {
				if(tokenlist.get(a).word.equals("")){
					continue;
				}else if (cleaned_tokenlist.get(cleaned_tokenlist.size() - 1).word.equals("<CONJ>")
						&& tokenlist.get(a).word.equals("<CONJ>")) {
					continue;
				}
			}
			cleaned_tokenlist.add(tokenlist.get(a));
		}
		return cleaned_tokenlist;
	}
	public List<Token> removeEmptyTokens(List<Token> tokens){
		List<Token> tokenlist=new ArrayList<Token>();
		for(Token t:tokens){
			if(t.word.equals("")){
				continue;
			}else{
				Token nt=new Token();
				nt=t;
				tokenlist.add(nt);
			}
		}
		return tokenlist;
	}
	
	public List<Token> removeHyphenInTokens(List<Token> tokens){
		List<Token> tokenlist=new ArrayList<Token>();
		for(Token t:tokens){
			Token nt=new Token();
			nt=t;
			if(nt.word.startsWith("-")==true ){
				nt.word=nt.word.substring(1);
			}
			if(nt.word.endsWith("-")==true){
				nt.word=nt.word.substring(0,nt.word.length()-1);
			}
			tokenlist.add(nt);
		}
		return tokenlist;
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

		List<ConjChunk> conjchunks = new ArrayList<ConjChunk>();
		for (Integer[] cc : chunkpos) {
			// System.out.println(cc[0] + "\t" + cc[1]);
			List<Token> substr = new ArrayList<Token>();
			ConjChunk conjchunk = new ConjChunk();
			for (int ss = cc[0]; ss <= cc[1]; ss++) {
				substr.add(tokens.get(ss));
			}
			conjchunk.chunkstr = StringUtil.chunkStrRender(substr);
			conjchunk.startindex = cc[0];
			conjchunk.endindex = cc[1];List<ConjCandidatePair> conjccdpairs=new ArrayList<ConjCandidatePair>();
			for (int z = 0; z < substr.size(); z++) {
				if (substr.get(z).word.equals("<CONJ>")) {
					for (int m = z - 1; m >= 0; m--) {
						for (int n = z + 1; n < substr.size(); n++) {
							String type = "";							
							ConjCandidatePair ccdp=new ConjCandidatePair();
							ConjCandidate ccd1=new ConjCandidate();
							ccd1.start_index=cc[0] + m;
							ccd1.text=StringUtil.strRender(m, z - 1, substr);
							ccd1.end_index=cc[0] + z - 1;		
							ConjCandidate ccd2=new ConjCandidate();
							ccd2.start_index=cc[0] + z + 1;
							ccd2.text=StringUtil.strRender(z + 1, n, substr);
							ccd2.end_index=cc[0] + n;
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
	public int findMostProperDecompostion(String model, HashMap<String,Float> dicmap,List<Token> tokenlist, List<ConjChunk> conjchunks) {
		//single Chunk
		for(ConjChunk cck: conjchunks){
			List<ConjCandidatePair> ccps=cck.pairs;
			for (ConjCandidatePair ccp : ccps) {
				String keystr = ccp.conjcan_1.text + "&&" + ccp.conjcan_2.text;
				if(model.equals("bert")||model.equals("clinicalbert")){
					ccp.probability = Math.abs(dicmap.get(keystr));
				}else{
					ccp.probability = Math.abs(dicmap.get(keystr.toLowerCase()));
				}
			}
		}
		//Single Chunks
		if(conjchunks.size()==1){
			ConjChunk cjk=conjchunks.get(0);
			List<ConjCandidatePair> ccps=cjk.pairs;
			ConjCandidatePair cpfinal=ccps.get(0);
			for (int a=1;a<ccps.size();a++) {
				if(ccps.get(a).probability > cpfinal.probability){
					cpfinal=ccps.get(a);
				}
			}
		}else{
			String[][] global=new String[conjchunks.size()-1][50];
			for(int cc=0;cc<conjchunks.size();cc++){
				List<ConjCandidatePair> ccp1=conjchunks.get(cc).pairs;
				List<ConjCandidatePair> ccp2=conjchunks.get(cc+1).pairs;
				int kcount=0;
				for(int p=0;p<ccp1.size();p++){
					for(int q=0;q<ccp2.size();q++){
						if(ccp1.get(p).conjcan_2.text.endsWith(ccp2.get(q).conjcan_1.text)){
							global[cc][kcount]=String.valueOf(p)+"-"+String.valueOf(q);
						}
					}
				}
			}
			for(int i=0;i<conjchunks.size();i++){
				for(int j=0;j<50;j++){
					System.out.print(global[i][j]+",");
				}
				System.out.println();
			}
		}
		return 1;
	}
	
	
	
	public List<DecomposingPath> generateCanConjPairs(List<Token> removedHyphenTokens) {
		int tis=0;
		int tie=0;
		List<List<Token>> subchunklist=new ArrayList<List<Token>>();
		for(int ti=0;ti<removedHyphenTokens.size();ti++){
			if(removedHyphenTokens.get(ti).word.equals("<CONJ>")){
				tie=ti-1;
				if(tie>=0){
					List<Token> subtlist=new ArrayList<Token>();
					for(int cti=tis;cti<=tie;cti++){
						subtlist.add(removedHyphenTokens.get(cti));
					}
					subchunklist.add(subtlist);
				}
				tis=ti+1;
				if(tis>=removedHyphenTokens.size()){
					break;
				}
			}
			if(ti==removedHyphenTokens.size()-1){
				List<Token> subtlist=new ArrayList<Token>();
				for(int cti=tis;cti<=ti;cti++){
					subtlist.add(removedHyphenTokens.get(cti));
				}
				subchunklist.add(subtlist);
			}
		}
		
		List<SubChunk> sclist=new ArrayList<SubChunk>();
		//first chunk
		List<Token> firstsubtokens=subchunklist.get(0);
		SubChunk firstsc=new SubChunk();
		firstsc.tokens=firstsubtokens;
		firstsc.subchunkstr=strRenderFromTokens(firstsc.tokens);
		HashMap<SubSubChunk,SubSubChunk> firstsubsubchunkmap=new HashMap<SubSubChunk,SubSubChunk>();
		for(int fti=firstsc.tokens.size()-1;fti>=0;fti--){
			SubSubChunk scck=new SubSubChunk();
			List<Token> subsublistnew=findTokenList(firstsubtokens, fti, firstsubtokens.size()-1);
			scck.tokens=subsublistnew;
			scck.subsubchunkstr=strRenderFromTokens(subsublistnew);
			firstsubsubchunkmap.put(scck, scck);
		}
		firstsc.subsubchunkmap=firstsubsubchunkmap;
		sclist.add(firstsc);
		//middle chunk
		for(int sbi=1;sbi<subchunklist.size()-1;sbi++){
			List<Token> substlist=subchunklist.get(sbi);
			SubChunk sck=new SubChunk();
			sck.subchunkstr=strRenderFromTokens(substlist);
			HashMap<SubSubChunk,SubSubChunk> ssckmap=new HashMap<SubSubChunk,SubSubChunk>();
			for(int sbk=0;sbk<substlist.size();sbk++){
				SubSubChunk ssck1=new SubSubChunk();
				SubSubChunk ssck2=new SubSubChunk();
				if(sbk+1<substlist.size()){
					List<Token> subsublist1=findTokenList(substlist, 0,sbk);
					List<Token> subsublist2=findTokenList(substlist, sbk+1 ,substlist.size()-1);
					ssck1.tokens=subsublist1;
					ssck1.subsubchunkstr=strRenderFromTokens(ssck1.tokens);
					ssck2.tokens=subsublist2;
					ssck2.subsubchunkstr=strRenderFromTokens(ssck2.tokens);
				}else{
					List<Token> subsublist1=findTokenList(substlist, 0,sbk);
					List<Token> subsublist2=subsublist1;
					ssck1.tokens=subsublist1;
					ssck1.subsubchunkstr=strRenderFromTokens(ssck1.tokens);
					ssck2.tokens=subsublist2;
					ssck2.subsubchunkstr=strRenderFromTokens(ssck2.tokens);
				}
				ssckmap.put(ssck1, ssck2);
			}
			sck.subsubchunkmap=ssckmap;
			sclist.add(sck);
		}
		//last chunk
		List<Token> latsubtokens=subchunklist.get(subchunklist.size()-1);
		SubChunk lastsc=new SubChunk();
		lastsc.subchunkstr=strRenderFromTokens(latsubtokens);
		lastsc.tokens=latsubtokens;
		HashMap<SubSubChunk,SubSubChunk> lastsubsubchunkmap=new HashMap<SubSubChunk,SubSubChunk>();
		for(int fti=0;fti<latsubtokens.size();fti++){
			//System.out.println("LAST_SUB_CHUNK");
			SubSubChunk scck=new SubSubChunk();
			List<Token> subsublistnew=findTokenList(latsubtokens, 0, fti);
			scck.tokens=subsublistnew;
			scck.subsubchunkstr=strRenderFromTokens(scck.tokens);
			lastsubsubchunkmap.put(scck, scck);
		}
		lastsc.subsubchunkmap=lastsubsubchunkmap;
		sclist.add(lastsc);

		//list all the decompose paths
		List<List<SubSubChunk[]>> ssclist=new ArrayList<List<SubSubChunk[]>>();
		for(int sci=0;sci<sclist.size()-1;sci++){
			SubChunk sc=sclist.get(sci);
			SubChunk sc2=sclist.get(sci+1);
			List<SubSubChunk[]> onessc=new ArrayList<SubSubChunk[]>();
			for (Entry<SubSubChunk, SubSubChunk> entry : sc.subsubchunkmap.entrySet()) {
				for (Entry<SubSubChunk, SubSubChunk> entry2 : sc2.subsubchunkmap.entrySet()) {
					SubSubChunk[] sscpair=new SubSubChunk[2];
					sscpair[0]=entry.getValue();
					sscpair[1]=entry2.getKey();
					onessc.add(sscpair);
				}
			}
			ssclist.add(onessc);
		}
		if(ssclist.size()<=1){
			List<SubSubChunk[]> onessc=ssclist.get(0);
			List<DecomposingPath> dplist=new ArrayList<DecomposingPath>();
			List<List<CanConjPair>> llccjp=new ArrayList<List<CanConjPair>>();
			for(SubSubChunk[] tssc:onessc){
				DecomposingPath dp=new DecomposingPath();
				List<CanConjPair> ccjplist=new ArrayList<CanConjPair>();
				CanConjPair ccjp=new CanConjPair();
				ccjp.canconj_1=tssc[0];
				ccjp.canconj_2=tssc[1];
				ccjplist.add(ccjp);
				llccjp.add(ccjplist);
				dp.dpath=ccjplist;
				dplist.add(dp);
			}
			return  dplist;
		}else{
			List<List<SubSubChunk[]>> allchain=new ArrayList<List<SubSubChunk[]>>();
			for(SubSubChunk[] firstlayer:ssclist.get(0)){
				List<SubSubChunk[]> newsubsubchunklist=new ArrayList<SubSubChunk[]>();
				newsubsubchunklist.add(firstlayer);
				allchain.add(newsubsubchunklist);
			}
			for(int yc=1;yc<ssclist.size();yc++){
				int ccindex=allchain.size();
				for(int yci=0;yci<ccindex;yci++){
					List<SubSubChunk[]> inlist=allchain.get(yci);
					for(SubSubChunk[] currentlayer:ssclist.get(yc)){
						List<SubSubChunk[]> newsubsubchunklist=new ArrayList<SubSubChunk[]>();
						newsubsubchunklist.addAll(inlist);
						newsubsubchunklist.add(currentlayer);
						allchain.add(newsubsubchunklist);
					}
				}
				
			}
			List<List<SubSubChunk[]>> allvalidchains=new ArrayList<List<SubSubChunk[]>>();
			for(List<SubSubChunk[]> testls:allchain){
				if(testls.size()==ssclist.size()){
					boolean validchain=true;
					for(int ssct=0;ssct<testls.size()-1;ssct++){
						SubSubChunk[] innerssb=testls.get(ssct);
						SubSubChunk[] nextinnerssb=testls.get(ssct+1);
						SubChunk sck=sclist.get(ssct+1);
						SubSubChunk checksscresult=sck.subsubchunkmap.get(innerssb[1]);
						if(checksscresult.equals(nextinnerssb[0])==false){
							validchain=false;
							break;
						}
					}
					if(validchain==false){
						continue;
					}
					allvalidchains.add(testls);
				}
			}
			System.out.println("_____ALL_________CHAINS_______\n");
			List<DecomposingPath> dplist=new ArrayList<DecomposingPath>();
			for(List<SubSubChunk[]> testls:allvalidchains){
				DecomposingPath dpath=new DecomposingPath();
				List<CanConjPair> ccjplist=new ArrayList<CanConjPair>();
				for(SubSubChunk[] tssc:testls){
					CanConjPair ccjp=new CanConjPair();
					ccjp.canconj_1=tssc[0];
					ccjp.canconj_2=tssc[1];
					System.out.println(tssc[0].subsubchunkstr+" vs "+tssc[1].subsubchunkstr+"=>");
					ccjplist.add(ccjp);
				}
				dpath.dpath=ccjplist;
				dplist.add(dpath);
				System.out.println();
			}
			
			System.out.println("_____ALL_______CHAINS___END____\n");
			return dplist;
		}
	}

	
	public List<Token> findTokenList(List<Token> tokens, int startindex, int endindex) {
		List<Token> tlist=new ArrayList<Token>();
		for(int i=startindex;i<=endindex;i++){
			tlist.add(tokens.get(i));
		}
		return tlist;
	}
	
	public String strRenderFromTokens(List<Token> tokens) {
		StringBuffer sb = new StringBuffer();
		for (Token t : tokens) {
			sb.append(t.word + " ");
		}
		return sb.toString().trim();
	}
	
	public String strRenderforTokenIds(List<Token> tokens) {
		StringBuffer sb = new StringBuffer();
		for (Token t : tokens) {
			sb.append(t.index + " ");
		}
		return sb.toString().trim();
	}

	public String strRenderFromTokenRepresentChunks(List<Token> tokens) {
		StringBuffer sb = new StringBuffer();
		for (Token t : tokens) {
			sb.append(t.representchunk + " ");
		}
		return sb.toString().trim();
	}
	public String p2S(String wordstr){
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		NPPhraseSpec subject = nlgFactory.createNounPhrase(wordstr); 
		subject.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		System.out.println(subject.getAllFeatureNames());
		WordElement word =(WordElement) subject.getFeature("head");
		//System.out.println(word);
		return word.getBaseForm();
	}
	public DecomposingPath depathSelection(List<DecomposingPath> dplist, HashMap<String, Float> dicmap, String model) {
		for(DecomposingPath dp:dplist){
			float allp=1f;
			for(CanConjPair p:dp.dpath){
				String keystr=p.canconj_1.subsubchunkstr.trim().replace(' ', '-').toLowerCase()+ "&&" +p.canconj_2.subsubchunkstr.trim().replace(' ', '-').toLowerCase();
				if(model.endsWith("bert")){
					keystr=p.canconj_1.subsubchunkstr.trim().replace(' ', '-')+ "&&" +p.canconj_2.subsubchunkstr.trim().replace(' ', '-');
				}
				float ccpp=dicmap.get(keystr);
				p.p=Math.abs(ccpp);
				allp=allp*p.p;
			}
			dp.global_p=allp;
		}
		int chid=0;
		DecomposingPath selectedpath=new DecomposingPath();
		selectedpath=dplist.get(0);
		for(DecomposingPath dp:dplist){
			chid++;
			if(dp.global_p>selectedpath.global_p){
				selectedpath=dp;
			}
		}
		if(selectedpath.global_p==0 && dplist.get(0).dpath.size()==1){
			for(DecomposingPath dp:dplist){
				boolean exitflat=false;
				for(CanConjPair p:dp.dpath){
					int conj1_first_index=p.canconj_1.tokens.get(0).index;
					int conj1_last_index=p.canconj_1.tokens.get(p.canconj_1.tokens.size()-1).index;
					int conj2_first_index=p.canconj_2.tokens.get(0).index;
					int conj2_last_index=p.canconj_2.tokens.get(p.canconj_2.tokens.size()-1).index;
					if(conj1_first_index==conj1_last_index && conj2_first_index==conj2_last_index){
						selectedpath=dp;
						exitflat=true;
						break;
					}
				}
				if(exitflat==true){
					break;
				}
			}
		}
		return selectedpath;
	}
	
	
	public List<String> phraseReconstruction(List<Token> removedHyphenTokens, DecomposingPath selectedpath) {
		List<String> relist=new ArrayList<String>();
		try{
		Graph<Integer, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
		g.addVertex(-1);
		//System.out.println("addVertex: -1 \n");
		g.addVertex(removedHyphenTokens.get(0).index);
		//System.out.println("addVertex: "+removedHyphenTokens.get(0).index+" \n");
		g.addEdge(-1, removedHyphenTokens.get(0).index);
		//System.out.println("addEdge: -1   --> "+removedHyphenTokens.get(0).index+" \n");
		for(int ri=1;ri<removedHyphenTokens.size();ri++){
			g.addVertex(removedHyphenTokens.get(ri).index);
			//System.out.println("addVertex: "+removedHyphenTokens.get(ri).index+" \n");
			//System.out.println("addEdge: "+removedHyphenTokens.get(ri-1).index+"  --> "+removedHyphenTokens.get(ri).index+" \n");
			g.addEdge(removedHyphenTokens.get(ri-1).index, removedHyphenTokens.get(ri).index);
		}
		g.addVertex(removedHyphenTokens.size());
		//System.out.println("addVertex: "+removedHyphenTokens.size()+" \n");
		g.addEdge(removedHyphenTokens.get(removedHyphenTokens.size()-1).index, removedHyphenTokens.size());
		//System.out.println("addEdge:"+removedHyphenTokens.get(removedHyphenTokens.size()-1).index+"  -->  100 \n");
		
		for(CanConjPair p:selectedpath.dpath){
			List<Token> conj_1_tokens=p.canconj_1.tokens;
			List<Token> conj_2_tokens=p.canconj_2.tokens;
			Token first_token_in_conj_1=conj_1_tokens.get(0);
			Token first_token_in_conj_2=conj_2_tokens.get(0);
			Token last_token_in_conj_1=conj_1_tokens.get(conj_1_tokens.size()-1);
			Token last_token_in_conj_2=conj_2_tokens.get(conj_2_tokens.size()-1);
			Integer first_token_index_in_conj_1=first_token_in_conj_1.index;
			Integer first_token_index_in_conj_2=first_token_in_conj_2.index;
			Integer last_token_index_in_conj_1=last_token_in_conj_1.index;
			Integer last_token_index_in_conj_2=last_token_in_conj_2.index;
			g.removeEdge(first_token_index_in_conj_2-1, first_token_index_in_conj_2);
			Set<DefaultEdge> incomingEdgeOf_firsttoken_in_conj_1=g.incomingEdgesOf(first_token_index_in_conj_1);
			for(DefaultEdge de:incomingEdgeOf_firsttoken_in_conj_1){
				Integer source=g.getEdgeSource(de);
				g.addEdge(source, first_token_index_in_conj_2);
			}
			Set<DefaultEdge> incomingEdgesOf_after_token_to_last_token_in_conj_1=g.incomingEdgesOf(last_token_index_in_conj_1+1);
			Set<Integer> sources_to_aftertoken_to_last_token_in_conj_1=new HashSet<Integer>();
			for(DefaultEdge de:incomingEdgesOf_after_token_to_last_token_in_conj_1){
				Integer source=g.getEdgeSource(de);
				sources_to_aftertoken_to_last_token_in_conj_1.add(source);
			}
			for(Integer s:sources_to_aftertoken_to_last_token_in_conj_1){
				g.removeEdge(s, last_token_index_in_conj_1+1);
				g.addEdge(s, last_token_index_in_conj_2+1);
			}
		}
		AllDirectedPaths adr = new AllDirectedPaths(g);	
		List<GraphPath<Integer, DefaultEdge>> as =adr.getAllPaths(-1, removedHyphenTokens.size(), true, null);	
		for (int gi = 0; gi < as.size(); gi++) {
			List<Integer> allvs = as.get(gi).getVertexList();	
			//System.out.println("Path: "+allvs.toString()+"\n");
			StringBuffer reconresult=new StringBuffer();
			for (int nid=1; nid< allvs.size()-1;nid++) {
				Integer node=allvs.get(nid);
				if(node>=0&&node<100){
					if(nid>1){
						//System.out.println("nid="+nid);
						Integer prenode=allvs.get(nid-1);
						if(removedHyphenTokens.get(prenode).compound==false){
							reconresult.append("_");
							reconresult.append(" ");
						}else{
							if(removedHyphenTokens.get(node).compound==false){
								reconresult.append("_");
								
							}
						}
					}
					Token showtoken=removedHyphenTokens.get(node);
					//System.out.println(showtoken.representchunk);
					reconresult.append(showtoken.representchunk);
				}
			}
			//String reconen=reconresult.toString().replace(' ', '_');
			String reconen=reconresult.toString().replace('_', ' ');
			reconen=reconen.replace("  ", " ");
			relist.add(reconen);
			//System.out.println();
		}
		}catch(Exception ex){
			System.out.println("Error:"+ex.getMessage());
		}
		return relist;
	}
	
	public HashMap<String, Float> loadDic(String dicpath) {
		String dic=FileUtil.readfile(dicpath);
		HashMap<String, Float> dicmap = new HashMap<String, Float>();
		String[] dic_rows = dic.split("\n");
		for (String dic_r : dic_rows) {
			String[] dicc = dic_r.split("\t");
			dicmap.put(dicc[0] + "&&" + dicc[1], Float.valueOf(dicc[2]));
		}
		return dicmap;
	}
	
	public List<Token> prepareTokens4ellipsis(String ceestr){
		List<Token> tokens = preproCE(ceestr);
		List<Token> afterhiddenbtoken = hideBracketTokens(tokens);
		List<Token> cleanedtokens = cleanAbnormalConjunctions(afterhiddenbtoken);
		String tempecstr = strRenderFromTokens(cleanedtokens);
		String[] tempconjarr = tempecstr.split("<CONJ>");
		int i = 0;
		for (String tr : tempconjarr) {
			if (tr.trim().endsWith("-")) {
				i = 1;
				break;
			}
			if (tr.trim().startsWith("-")) {
				i = 2;
				break;
			}
		}
		List<Token> newtlist = new ArrayList<Token>();
		for (Token ct : cleanedtokens) {
			String[] sct = ct.word.split("-");
			int semi_index=0;
			if (i == 1 || i == 2) {
				for (String ts : sct) {
					semi_index++;
					boolean swflag = false;
					if (ct.word.length() == ts.length()) {
						swflag = false;
					} else {
						swflag = true;
					}
					if (i == 1) {
						if (ct.word.endsWith(ts) == true) {
							Token newt = new Token();
							newt.compound = swflag;
							newt.word = ts;
							//added represented
							String followed=ct.representchunk.replace(ct.word, "");
							newt.representchunk=newt.word + followed;
							newtlist.add(newt);
						} else {
							Token newt = new Token();
							newt.compound = swflag;
							newt.word = ts + "-";
							newt.representchunk = newt.word;
							newtlist.add(newt);
						}
					} else if (i == 2) {
						if (ct.word.startsWith(ts) == true) {
							Token newt = new Token();
							newt.compound = swflag;
							newt.word = ts;
							newt.representchunk = newt.word;
							newtlist.add(newt);
						}else {
							Token newt = new Token();
							newt.compound = swflag;
							newt.word = "-" + ts;
							if(semi_index==sct.length){
								String followed=ct.representchunk.replace(ct.word, "");
								newt.representchunk=newt.word + followed;
							}
							newtlist.add(newt);
						}
					} 
				}
			}else {
				Token newt = new Token();
				newt.word = ct.word;
				newt.compound = false;
				newt.representchunk=ct.representchunk;
				newtlist.add(newt);
			}
		}
		List<Token> cdtokens=removeEmptyTokens(newtlist);
		List<Token> removedHyphenTokens=removeHyphenInTokens(cdtokens);
		
		for(int ti=0;ti<removedHyphenTokens.size();ti++){
			removedHyphenTokens.get(ti).index=ti;
		}
		return removedHyphenTokens;
	}
	
	
	
		

}
