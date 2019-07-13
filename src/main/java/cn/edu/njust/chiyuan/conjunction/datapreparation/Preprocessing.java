package cn.edu.njust.chiyuan.conjunction.datapreparation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


public class Preprocessing {

	public static void main(String[] args) {
	    // set up pipeline properties
		
		 String str = "**AA13CCCAA**";
	        //if(StringUtils.isNotBlank(str)){
	             StringBuilder sb = new StringBuilder("18698587234");
	             sb.replace(3, 7, str);
	             System.err.println("========"+sb.toString());
	       // }
		/*
		Properties props = new Properties();
		  props.put("annotators", "tokenize,ssplit,pos, lemma");
		  StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		  Annotation document = new Annotation("Actually, it is a very good idea.");
		  pipeline.annotate(document);
		  List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		  for(CoreMap sentence: sentences) {
			 for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
			      String word = token.get(TextAnnotation.class);
			      String lema = token.get(LemmaAnnotation.class);
			      System.out.println(word + " =>" + lema);
			      }
			 }
		*/
		//String filename="/Users/cy2465/Downloads/Exp_NCBI/without_Composite_Mention/wo_NCBItestset_corpus.txt";
		//dataFormat(filename,filename+".col");	
		
		/*
		String c=FileUtil.readfile("/Users/cy2465/Downloads/SimConcept/corpus/Disease.txt");
		String[] rows=c.split("\n");
		int count=0;
		int total_entity_count=0;
		HashSet<String> set=new HashSet<String>();
		for(String r:rows){
			//System.out.println("r="+r);
			if(r.contains("\t")){
				total_entity_count++;
			}
			if((r.contains(" or ")||r.contains("/")||r.contains(" and "))&&r.contains("\t")){
				System.out.println(r);
				set.add(r.split("\t")[3]);
				count++;
			}
		}
		System.out.println("count="+count);
		System.out.println("total_entity_count="+total_entity_count);
		System.out.println("distinct count="+set.size());
		//dataFormat2();
		 * 
		 */
	}

	public static void dataFormat2() {
		String surfix="dev";
		//addAnn();
		String a=FileUtil.readfile("/Users/cy2465/Documents/git/BERT-BiLSTM-CRF-NER/NERdata/SimConcept_Disease_sents_w_tag_"+surfix+".txt");
		String[] rows=a.split("\n");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<rows.length;i++){
			if(rows[i].length()==0 && rows[i+1].equals(". O")){
				i=i+2;
				continue;
			}
			sb.append(rows[i]+"\n");
		}
		String[] newrows=sb.toString().split("\n");
		StringBuffer sb_new=new StringBuffer();
		for(int i=0;i<newrows.length;i++){
			if(newrows[i].length()==0 && newrows[i+1].length()==0){
				i=i+1;
				continue;
			}
			sb_new.append(newrows[i]+"\n");
		}
		FileUtil.write2File("/Users/cy2465/Documents/git/BERT-BiLSTM-CRF-NER/NERdata/Disease_"+surfix+"_2.txt", sb_new.toString().replace("TCompsiteMention", "B-CompsiteMention"));
	}

	public static void addAnn() {
		String content=FileUtil.readfile("/Users/cy2465/Documents/conjunctionAnn/SimConcept_Disease_sents_w_tag.txt");
	    String[] rows=content.split("\n");
	    HashSet<String> set=new HashSet<String>();
	    for(String r:rows){
	    	System.out.println("r="+r);
	    	String[] es=r.split("\t");
	    	set.add(es[0]);
	    }  
		System.out.println("abstract count="+set.size());
		int train_count=(int) (set.size()*0.6);
		int test_count=(int) (set.size()*0.8);
		StringBuffer trainsb=new StringBuffer(); 
		StringBuffer testsb=new StringBuffer(); 
		StringBuffer devsb=new StringBuffer();
		int t1=0;
		int t2=0;
		HashSet<String> ss=new HashSet<String>();
		for(int i=0;i<rows.length;i++){
			String r=rows[i];
			String[] e=r.split("\t");
			ss.add(e[0]);
			if(ss.size()<train_count){
				System.out.println("train!!");
				if(i>0 && rows[i].split("\t")[0].equals(rows[i-1].split("\t")[0])==false){
					trainsb.append("\n");
				}
				if(e.length>3){
				trainsb.append(e[1]+" "+e[4]+"\n");
				}else{
					trainsb.append(r+"\n");
				}
			}else if(ss.size()>=train_count && ss.size()<test_count){
				System.out.println("dev!!");
				if(i>0 && rows[i].split("\t")[0].equals(rows[i-1].split("\t")[0])==false){
					devsb.append("\n");
				}
				if(e.length>3){
					devsb.append(e[1]+" "+e[4]+"\n");
				}else{
					devsb.append(r+"\n");
				}
			}else {
				System.out.println("test!!");
				if(i>0 && rows[i].split("\t")[0].equals(rows[i-1].split("\t")[0])==false){
					testsb.append("\n");
				}
				if(e.length>3){
					testsb.append(e[1]+" "+e[4]+"\n");
				}else{
					testsb.append(r+"\n");
				}
			}
		}
		System.out.println("count="+t1+"\t"+t2);
		
		FileUtil.write2File("/Users/cy2465/Documents/conjunctionAnn/SimConcept_Disease_sents_w_tag_train.txt", trainsb.toString().replace("-LRB-", "(").replace("-RRB-", ")"));
		FileUtil.write2File("/Users/cy2465/Documents/conjunctionAnn/SimConcept_Disease_sents_w_tag_dev.txt", devsb.toString().replace("-LRB-", "(").replace("-RRB-", ")"));
		FileUtil.write2File("/Users/cy2465/Documents/conjunctionAnn/SimConcept_Disease_sents_w_tag_test.txt", testsb.toString().replace("-LRB-", "(").replace("-RRB-", ")"));
	}

	public static void dataFormat(String source_file, String targe_file) {
		String str=FileUtil.readfile(source_file);//"/Users/cy2465/Downloads/SimConcept/corpus/Disease.txt"
		String[] rows=str.split("\n");
		String currentID="0000";
		HashMap<String,String> id_content=new HashMap<String,String>();
		HashMap<String,List<String>> id_ann=new HashMap<String,List<String>>();
		HashSet<String> idset=new HashSet<String>();
		
		
		for(int i=0;i<rows.length;i++){
			int pos=rows[i].indexOf('|');
			if((rows[i].length()>10)&&(rows[i].substring(pos+1, pos+2).equals("a"))){
				//System.out.println("t str="+rows[i].substring(pos+3));
				//System.out.println("a str="+rows[i+1].substring(pos+3));
				StringBuffer currentsb=new StringBuffer();
				currentsb.append(rows[i-1].substring(pos+3)+"\n");
				currentsb.append(rows[i].substring(pos+3)+"\n");
				currentID=rows[i].substring(0,pos);
				idset.add(currentID);
				//System.out.println(currentID);
				List<String> ann=new ArrayList<String>();
				while((i+1)<rows.length && rows[i+1].startsWith(currentID)){
					//System.out.println(rows[i+1]);
					ann.add(rows[i+1]);
					i++;
				}
				id_content.put(currentID, currentsb.toString());
				id_ann.put(currentID, ann);
			}
		}
		Properties props = new Properties();
	    props.setProperty("annotators", "tokenize,ssplit");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    StringBuffer outputsb=new StringBuffer();
	    int cmcount=0;
		for(String id:idset){
			//System.out.println("id="+id);
			//System.out.println("content="+id_content.get(id));
			CoreDocument exampleDocument = new CoreDocument(id_content.get(id));
			pipeline.annotate(exampleDocument);
			String current_tag="";
			for(int a=0;a<exampleDocument.sentences().size();a++){
				outputsb.append("\n");
			    List<CoreLabel> firstSentenceTokens = exampleDocument.sentences().get(a).tokens();    
			    for (CoreLabel token : firstSentenceTokens) {
			    	//System.out.println(token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition());
			    	List<String> annlist=id_ann.get(id);
			    	boolean flag=false;
			    	for(String r:annlist){
			    		//System.out.println("r="+r);
			    		String[] elements=r.split("\t");
			    		Integer start_index=Integer.valueOf(elements[1]);
			    		Integer end_index=Integer.valueOf(elements[2]);
			    		
			    		if(token.beginPosition()==start_index && token.endPosition()<=end_index){
			    			current_tag="B-"+elements[4];
			    			flag=true;
			    			
			    		}else if(token.beginPosition()>=start_index && token.endPosition()<=end_index){
			    			current_tag="I-"+elements[4];
		    				flag=true;
		    			}
			    	}
			    	if(flag==true){
			    		outputsb.append(id+"\t"+token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition()+"\t"+current_tag+"\n");
			    	}else{
			    		outputsb.append(id+"\t"+token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition()+"\t"+"O"+"\n");
			    	}
			    }
			}
		    //System.out.println("ann size="+id_ann.get(id).size());
		}
		StringBuffer write_buff=new StringBuffer();
		String colformatstr=outputsb.toString();
		String[] token_tags=colformatstr.split("\n");
		for(int i=1;i<token_tags.length-1;i++){
			if(token_tags[i-1].length()==0&&token_tags[i+1].length()==0&&token_tags[i].split("\t")[1].equals(".")){
				i=i+1;
				//System.out.println("===token[i-1]===>>  "+token_tags[i-1]);
				//System.out.println("===token[i]===>>  "+token_tags[i]);
				//System.out.println("===token[i+1]===>>  "+token_tags[i+1]);
				continue;
			}
			write_buff.append(token_tags[i]+"\n");
		}
		write_buff.append(token_tags[token_tags.length-1]+"\n");
		FileUtil.write2File(targe_file, write_buff.toString());//"/Users/cy2465/Documents/conjunctionAnn/SimConcept_Disease_sents_w_tag.txt"
	    
		System.out.println("id size="+idset.size());
		//cmcount
		System.out.println("cmcount size="+cmcount);
	}

}
