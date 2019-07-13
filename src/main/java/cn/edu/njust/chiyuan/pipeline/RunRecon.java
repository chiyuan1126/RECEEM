package cn.edu.njust.chiyuan.pipeline;

import java.io.IOException;
import java.util.List;

import cn.edu.njust.chiyuan.conjunction.datapreparation.Word2vec;
import cn.edu.njust.chiyuan.conjunction.pojo.ConjCandidatePair;
import cn.edu.njust.chiyuan.conjunction.pojo.ConjChunk;
import cn.edu.njust.chiyuan.conjunction.pojo.Token;

public class RunRecon {
	public Word2vec vec = new Word2vec();
	

	public static void main(String[] args) {
		System.out.println("preparing_____");
		RunRecon runrecon=new RunRecon();
		// TODO Auto-generated method stub
		ReCON recon = new ReCON();
		
		System.out.println("start_____");
		String str="active liver or biliary disease";
		List<Token> tlist = recon.preproCE(str);
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
				// Calculate semantic similarity
				ccp.probability = runrecon.vec.calculateSim(ccp.conjcan_1.text,ccp.conjcan_2.text);
				
			}
		}
		// select resolution path

		List<ConjCandidatePair> ccdps = recon.resolutionPathSelection(chunks);
		// phrase reconstruction
		List<String> phrases = recon.phraseReconstruction(tlist, ccdps);
		
		for(String p:phrases){
			System.out.println("=>"+p);
		}
	}

}
