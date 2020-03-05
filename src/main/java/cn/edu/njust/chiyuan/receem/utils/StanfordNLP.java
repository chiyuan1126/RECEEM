package cn.edu.njust.chiyuan.receem.utils;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class StanfordNLP {
	public static final String grammars = "cn/edu/njust/chiyuan/conjunction/utils/wsjRNN.ser.gz";
	public static LexicalizedParser lp;

	public static void main(String[] args) {
		
		String content= FileUtil.readfile("/Users/cy2465/Documents/ecfiltertxt.txt");
		String[] rows=content.split("\n");
		StringBuffer sb=new StringBuffer();
		StringBuffer textsb=new StringBuffer();
		for(String r:rows){
			String nctid=r.split("\t")[0];
			String sent=r.split("\t")[1];
			int begin=sent.indexOf("(");
			String substr= sent.substring(begin);
			int end=substr.indexOf(")");
			if(end!=-1){
				String ss=substr.substring(0, end+1);
				//System.out.println("==>"+ss);
				if(ss.contains(",")||ss.contains(" and ")||ss.contains(" or ")){
					sb.append(nctid+"\t"+sent+"\t"+ss+"\n");
					textsb.append(sent.substring(0, begin)+"\n");
				}
			}
		}
		String sentence="Disease extent must be determined by scans (CT or PET CT) within 6 weeks of enrollment.";
		StanfordNLP snlp=new StanfordNLP();
		Tree tree = snlp.parseSentence(sentence);
		//snlp.extractTree(tree);
		ArrayList<TaggedWord> twlist = tree.taggedYield();
		for (int x = 0; x < twlist.size(); x++) {
			TaggedWord tw = twlist.get(x);
			System.out.println("[" + (x) + "]:" + tw.tag() + "--" + tw.word() + " (" + tw.value() + ")" + "--" + tw.beginPosition() + "--" + tw.endPosition()+"-");
			//System.out.print(tw.word() + "/" + tw.tag() + " ");	
		}
		//snlp.tagWords(tree);
		//output tree structure
		//System.out.println("-----------");
		//snlp.outputTreeStruture(tree);
		//System.out.println("---dependency--------");
		//snlp.outputDependency(tree);
		//System.out.println(snlp.extractTree(tree));
	}
	public StanfordNLP() {
		this.lp = LexicalizedParser.loadModel(grammars);
	}
	
	public static String getChunksbySentenceStanford(String sentence){
		StanfordNLP snlp=new StanfordNLP();
		Tree tree = snlp.parseSentence(sentence);
		String result=snlp.outputTreeStruture(tree);
		return result;
	}
	
	
	public List<String[]> getAnswerChunksbySentenceStanford(String sentence){
		Tree tree = parseSentence(sentence);
		String result=outputTreeStruture(tree);
		String[] resset=result.split("\n");
		List<String[]> rlist=new ArrayList<String[]>();
		for(String s:resset){
			String[] eachrow=s.split("\t");
			rlist.add(eachrow);
		}
		return rlist;
	}
	//with improve better 
	public List<String[]> getAnswerChunksbySentenceStanford2(String sentence){
		Tree tree = parseSentence(sentence);
		String result=extractTree(tree);
		String[] resset=result.split("\n");
		List<String[]> rlist=new ArrayList<String[]>();
		for(String s:resset){
			String[] eachrow=s.split("\t");
			rlist.add(eachrow);
		}
		return rlist;
	}

	public List<String> preprocess(String str) {
		Reader reader = new StringReader(str);
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		List<String> sentenceList = new ArrayList<String>();

		for (List<HasWord> sentence : dp) {
			// SentenceUtils not Sentence
			String sentenceString = SentenceUtils.listToString(sentence);
			sentenceList.add(sentenceString);
		}
		return sentenceList;

	}
	
	

	public ArrayList<TaggedWord> tagWords(Tree t) {
		ArrayList<TaggedWord> twlist = t.taggedYield();
		for (int x = 0; x < twlist.size(); x++) {
			TaggedWord tw = twlist.get(x);
			//System.out.println("[" + (x) + "]:" + tw.tag() + "--" + tw.word() + " (" + tw.value() + ")" + "--" + tw.beginPosition() + "--" + tw.endPosition()+"-");
			//System.out.print(tw.word() + "/" + tw.tag() + " ");	
		}
		return twlist;
	}

	/**
	 * parseSentence Author:chi Date:2017-3-22
	 * 
	 */
	public Tree parseSentence(String input) {
		Tree tree = lp.parse(input);
		return tree;
	}

	public List<Tree> GetNounPhrases(Tree parse) {

		List<Tree> phraseList = new ArrayList<Tree>();
		for (Tree subtree : parse) {
			if (subtree.label().value().equals("NP")) {
				phraseList.add(subtree);
				System.out.println(subtree);
			}
		}
		return phraseList;

	}

	//without improve
	public String outputTreeStruture(Tree tree){
		StringBuffer sb=new StringBuffer();
		List<Tree> tree_list = tree.getLeaves();
		for (Tree treeunit : tree_list) {
			if(treeunit.numChildren()==0){
				System.out.println(treeunit+"\t"+treeunit.parent(tree).label().toString()+"\t"+treeunit.parent(tree).parent(tree).label().toString());
				//sb.append(treeunit+"\t"+treeunit.parent(tree).label().toString()+"\t"+treeunit.parent(tree).parent(tree).label().toString()+"\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * with improve for ADJP and QP
	 * 
	 * */
	public String extractTree(Tree tree){
		StringBuffer sb=new StringBuffer();
		List<Tree> tree_list = tree.getLeaves();
		for (Tree treeunit : tree_list) {
			if(treeunit.numChildren()==0){
				//System.out.println(treeunit+"\t"+treeunit.parent(tree).label().toString()+"\t"+treeunit.parent(tree).parent(tree).label().toString());
				//System.out.println(treeunit.parent(tree).parent(tree).label().toString());
				if(treeunit.parent(tree).parent(tree).parent(tree).label().toString().equals("NP")&&(treeunit.parent(tree).parent(tree).label().toString().equals("ADJP")||treeunit.parent(tree).parent(tree).label().toString().equals("PRN"))){
					sb.append(treeunit+"\t"+treeunit.parent(tree).label().toString()+"\t"+"NP"+"\n");
				}else{
					sb.append(treeunit+"\t"+treeunit.parent(tree).label().toString()+"\t"+treeunit.parent(tree).parent(tree).label().toString()+"\n");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * Word Dependency Author:chi Date:2017-3-22
	 * 
	 */
	public Collection<TypedDependency> outputDependency(Tree t) {
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		// tlp.setGenerateOriginalDependencies(true); Standford Dependency
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(t);
		
		Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		
		int countforitem = 0;
		int source = 0;
		int target = 0;
		
		for (TypedDependency item : tdl) {
			System.out.println(item);
		}
		
		return tdl;

	}
	
	
}
