package cn.edu.njust.chiyuan.conjunction.phrase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.occurrence.Occurrence;
import com.hankcs.hanlp.corpus.occurrence.PairFrequency;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.dictionary.stopword.Filter;
import com.hankcs.hanlp.phrase.IPhraseExtractor;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

/**
 * 利用互信息和左右熵的短语提取器
 * 
 * @author hankcs
 */
public class MutualInformationEntropyPhraseExtractor implements IPhraseExtractor {
	@Override
	public List<String> extractPhrase(String text, int size) {
		List<String> phraseList = new LinkedList<String>();
		Occurrence occurrence = new Occurrence();
//		Filter[] filterChain = new Filter[] { CoreStopWordDictionary.FILTER, new Filter() {
//			@Override
//			public boolean shouldInclude(Term term) {
//				switch (term.nature) {
//				case t:
//				case nx:
//					return false;
//				}
//				return true;
//			}
//		} };
//		for (List<Term> sentence : NotionalTokenizer.seg2sentence(text, filterChain)) {
//			System.out.println("=======Seq2sentence=======");
//			if (HanLP.Config.DEBUG) {
//				System.out.println(sentence);
//			}
//			occurrence.addAll(sentence);
//		}
		String c=FileUtil.readfile("/Users/cy2465/Documents/p_str.txt");
		String[] ss=c.split(" ");
		//System.out.println("c="+c);
		List<Term> terms=new ArrayList<Term>();
		for(String s:ss){
			s=s.toLowerCase().trim();
			if(s.length()==0){
				continue;
			}
			//System.out.println(s);
			
			if(Character.isDigit(s.charAt(0))==true||s.equals("or")||s.equals("subjects")||s.equals("must")||s.equals("patients")||s.equals("no")||s.equals("to")||s.equals("is")||s.equals("by")||s.equals("a")||s.equals("as")||s.equals("were")||s.equals("on")||s.equals("we")||s.equals("have")||s.equals("has")||s.equals("which")||s.equals("been")||s.equals("the")||s.equals("a")||s.equals("in")||s.equals("with")||s.equals("at")||s.equals("and")||s.equals("of")||s.equals("be")){
				continue;
			}
			Term t=new Term(s+" ",Nature.mg);
			terms.add(t);
		}
		occurrence.addAll(terms);
		occurrence.compute();
		if (HanLP.Config.DEBUG) {
			System.out.println(occurrence);
			for (PairFrequency phrase : occurrence.getPhraseByMi()) {
				System.out.print(phrase.getKey().replace(Occurrence.RIGHT, '→') + "\tmi=" + phrase.mi + " , ");
			}
			System.out.println();
			for (PairFrequency phrase : occurrence.getPhraseByLe()) {
				System.out.print(phrase.getKey().replace(Occurrence.RIGHT, '→') + "\tle=" + phrase.le + " , ");
			}
			System.out.println();
			for (PairFrequency phrase : occurrence.getPhraseByRe()) {
				System.out.print(phrase.getKey().replace(Occurrence.RIGHT, '→') + "\tre=" + phrase.re + " , ");
			}
			System.out.println();
			for (PairFrequency phrase : occurrence.getPhraseByScore()) {
				System.out.print(phrase.getKey().replace(Occurrence.RIGHT, '→') + "\tscore=" + phrase.score + " , ");
			}
			System.out.println();
		}

		for (PairFrequency phrase : occurrence.getPhraseByScore()) {
			if (phraseList.size() == size)
				break;
			phraseList.add(phrase.first + phrase.second);
		}
		return phraseList;
	}

	/**
	 * 一句话提取
	 * 
	 * @param text
	 * @param size
	 * @return
	 */
	public static List<String> extract(String text, int size) {
		IPhraseExtractor extractor = new MutualInformationEntropyPhraseExtractor();
		return extractor.extractPhrase(text, size);
	}

  public static void main(String[] args)
  {
      MutualInformationEntropyPhraseExtractor extractor = new MutualInformationEntropyPhraseExtractor();
      String text = "";
//      System.out.println(text);
      //HanLP.Config.DEBUG=true;
      List<String> phraseList = extractor.extractPhrase(text, 30);
      StringBuffer sb=new StringBuffer();
      for(String p:phraseList){
    	  sb.append(p+"\n");
      }
      System.out.println(phraseList);
      //FileUtil.write2File("/Users/cy2465/Documents/p_str_text.txt", sb.toString());
  }
}
