package cn.edu.njust.chiyuan.receem.pipeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.njust.chiyuan.receem.pojo.CanConjPair;
import cn.edu.njust.chiyuan.receem.pojo.DecomposingPath;
import cn.edu.njust.chiyuan.receem.pojo.Token;
import cn.edu.njust.chiyuan.receem.utils.Word2vec;

public class RunReceem {

	public static void main(String[] args) {
		String coordinationellipsis="Type 1 or 2 diabetes";
		ReceemPipeline receem = new ReceemPipeline();
		List<Token> tokens=receem.prepareTokens4ellipsis(coordinationellipsis);
		List<DecomposingPath> dplist=receem.generateCanConjPairs(tokens);
		Word2vec vec = new Word2vec();
		try {
			vec.loadModel("/Users/cy2465/Downloads/pubmed_phrase_word_combo_vectors_win_5.bin");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HashMap<String, Float> dicmap=new HashMap<String, Float>();
		for(DecomposingPath dp:dplist){
			for(CanConjPair p:dp.dpath){
				String conj_1=p.canconj_1.subsubchunkstr.replace(' ', '-');
				String conj_2=p.canconj_2.subsubchunkstr.replace(' ', '-');
				Float sim=vec.calculateDis(conj_1.toLowerCase(),conj_2.toLowerCase());
				String keystr=conj_1.toLowerCase()+ "&&" +conj_2.toLowerCase();
				dicmap.put(keystr, sim);
			}
		}
		DecomposingPath selectedpath = receem.depathSelection(dplist, dicmap,"pubmedphrase2vec");
		List<String> relist=new ArrayList<String>();
		relist=receem.phraseReconstruction(tokens, selectedpath);
		StringBuffer receemresults=new StringBuffer();
		for(int a=0;a<relist.size();a++){
			if(a<relist.size()-1){
				receemresults.append(relist.get(a)+"|");
			}else{
				receemresults.append(relist.get(a));
			}
		}
		System.out.println(receemresults.toString());
	}

}
