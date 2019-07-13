package cn.edu.njust.chiyuan.pipeline;

import java.util.HashMap;
import java.util.List;

import cn.edu.njust.chiyuan.conjunction.pojo.ConjCandidatePair;
import cn.edu.njust.chiyuan.conjunction.pojo.ConjChunk;
import cn.edu.njust.chiyuan.conjunction.pojo.Token;
import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;
import cn.edu.njust.chiyuan.conjunction.utils.StringUtil;

public class CallReCON {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String type="ct_gov";
		String model="pubmedphrase2vec";
		String dic = FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/dic_" + type + "_" + model + ".txt");
		System.out.println("=>"+"/Users/cy2465/Downloads/RECON_eval/gs_" + type + ".txt");
		System.out.println("/Users/cy2465/Downloads/RECON_eval/dic_" + type + "_" + model + ".txt");
		HashMap<String, Float> dicmap = new HashMap<String, Float>();
		String[] dic_rows = dic.split("\n");
		for (String dic_r : dic_rows) {
			String[] dicc = dic_r.split("\t");
			dicmap.put(dicc[0] + "&&" + dicc[1], Float.valueOf(dicc[2]));
		}
		ReCON recon = new ReCON();
		String str="drug or alcohol use or dependence";
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
				ccp.probability = Math.abs(dicmap.get(ccp.conjcan_1.text.toLowerCase() + "&&" + ccp.conjcan_2.text.toLowerCase()));
				
			}
		}
		// select resolution path

		List<ConjCandidatePair> ccdps = recon.resolutionPathSelection(chunks);
		// phrase reconstruction
		List<String> phrases = recon.phraseReconstruction(tlist, ccdps);
		String ansstr = StringUtil.answerRender(phrases);
		System.out.println("=>"+ansstr);
	}

}
