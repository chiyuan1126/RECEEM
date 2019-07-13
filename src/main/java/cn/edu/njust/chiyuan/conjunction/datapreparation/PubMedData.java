package cn.edu.njust.chiyuan.conjunction.datapreparation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cn.edu.njust.chiyuan.conjunction.utils.DOMParser;
import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class PubMedData {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub	
		//dataGeneration();
//		String s=FileUtil.readfile("/Users/cy2465/Documents/conjunctionAnn/PubMedData.txt");
//		String[] lines=s.split("\n");
//		int i=0;
//		StringBuffer sb=new StringBuffer();
//		for(String r:lines){
//			i++;
//			sb.append(r+"\n");
//			if(i%10==0){
//				FileUtil.write2File("/Users/cy2465/Documents/conjunctionAnn/pubMed/pubMed_"+(i/10)+".txt", sb.toString());
//				FileUtil.write2File("/Users/cy2465/Documents/conjunctionAnn/pubMed/pubMed_"+(i/10)+".ann", "");
//				sb.setLength(0);
//			}
//		}
		/*
		String c=FileUtil.readfile("/Users/cy2465/Downloads/SimConcept/corpus/Disease.txt");
		//System.out.println("c="+c);
		//SCS
		String[] rs=c.split("\n");
		int count=0;
		for(String r:rs){
			if(r.indexOf("SC")>-1 || r.indexOf("AJA")>-1 ){ //AJA
				System.out.println("EE="+r);
				count++;
			}
		}
		System.out.println("count="+count);
		*/
		/*
		File dir=new File("/Users/cy2465/baseline");
		File[] files=dir.listFiles();
		for(File f:files){
			
		}
		*/
		fetchAbstractText("/Users/cy2465/baseline/pubmed18n0695.xml");
		//System.out.println("check?="+Character.isLetterOrDigit(','));
		
		/*
		String content=FileUtil.readfile("/Users/cy2465/Downloads/SimConcept/corpus/Disease.txt");
		String[] rows=content.split("\n");
		int count=0;
		HashSet<String> set=new HashSet<String>();
		StringBuffer sb=new StringBuffer();
		HashMap<String, String> map=new HashMap<String, String>();
		for(String r:rows){
			if(r.contains("\t")){
				
				String[] es=r.split("\t");
				//System.out.println("pattern="+es[6]);
				if(es[6].contains("SC")){
					//System.out.println("==>"+r);
					//count++;
//					if(es[3].contains(",")||es[3].contains("/")||es[3].contains(" and")||es[3].contains(" or")){
//						if(set.contains(es[3].toLowerCase())==false){
//							set.add(es[3].toLowerCase());
//							sb.append(es[3]+"\t"+es[5]+"\n");
//							count++;
//						}else{
//							continue;
//						}
//					}
					map.put(es[3].toLowerCase(), es[5]);
				}
				
			}
		}
		
		String c=FileUtil.readfile("/Users/cy2465/Downloads/RECON_eval/gs_ncbi.txt");
		String[] rs=c.split("\n");
		StringBuffer wsb=new StringBuffer();
		for(String y:rs){
			String[] x=y.split("\t");
			
			if(map.containsKey(x[0].toString())){
				wsb.append(x[0]+"\t"+map.get(x[0].toLowerCase())+"\n");
			}else{
				wsb.append(x[0]+"\t"+"unknown"+"\n");
			}
		}
		System.out.println("count="+count);
		//FileUtil.write2File("/Users/cy2465/Downloads/ncbi_gs.txt", sb.toString());
		FileUtil.write2File("/Users/cy2465/Downloads/ncbi_gs_new.txt", wsb.toString());
		*/
	}
	
	public static void fetchAbstractText(String path){
		StringBuffer textsb=new StringBuffer();
		DOMParser parser = new DOMParser();
		//String path="/Users/cy2465/baseline/pubmed18n0695.xml";
		Document document = parser.parse(path);
		Element rootElement = document.getDocumentElement();
		NodeList subnodeList = rootElement.getElementsByTagName("AbstractText");
		for(int k=0;k<subnodeList.getLength();k++){
			System.out.println(subnodeList.item(k).getTextContent());
			textsb.append(subnodeList.item(k).getTextContent()+"\n");
		}
	}

	public static void dataGeneration() {
		StringBuffer writersb=new StringBuffer();
		StringBuffer writersb2=new StringBuffer();
		String trainset=FileUtil.readfile("/Users/cy2465/Documents/corpora/NCBI/NCBI_corpus/NCBI_corpus_development.txt");
		String testset=FileUtil.readfile("/Users/cy2465/Documents/corpora/NCBI/NCBI_corpus/NCBI_corpus_testing.txt");
		String developset=FileUtil.readfile("/Users/cy2465/Documents/corpora/NCBI/NCBI_corpus/NCBI_corpus_training.txt");
		StringBuffer allset=new StringBuffer();
		allset.append(trainset);
		allset.append(developset);
		allset.append(testset);
		PubMedData pmdata=new PubMedData();
		String str="";
		try {
			String[] sents=pmdata.detectSentence(allset.toString());
			int count=0;
			for(String s:sents){
				if(s.contains("CompositeMention")){
					System.out.println(s);
					if(s.indexOf("\t")!=-1){
						str=pmdata.removeTags(s.substring(s.indexOf("\t")+1));
						System.out.println(str);
						writersb2.append(s.substring(s.indexOf("\t")+1)+"\n");
					}else{
						str=pmdata.removeTags(s);	
						System.out.println(pmdata.removeTags(s));
						writersb2.append(s+"\n");
					}
					if(str.endsWith(" . .")){
						str=str.substring(0, str.length()-2);
					}
					writersb.append(str+"\n");
					count++;
				}
			}
			FileUtil.write2File("/Users/cy2465/Documents/conjunctionAnn/PubMedData.txt", writersb.toString());
			FileUtil.write2File("/Users/cy2465/Documents/conjunctionAnn/PubMedData_w_Hints.txt", writersb2.toString());
			System.out.println("count="+count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String removeTags(String sent){
		String cleansent=sent;
		cleansent=cleansent.replace("<category=\"DiseaseClass\">", "");
		cleansent=cleansent.replace("<category=\"SpecificDisease\">", "");
		cleansent=cleansent.replace("<category=\"Modifier\">", "");
		cleansent=cleansent.replace("<category=\"CompositeMention\">", "");
		cleansent=cleansent.replace("</category>", "");
		return cleansent;
	}
	
	public String[] detectSentence(String paragraph) throws IOException {

        InputStream modelIn = getClass().getResourceAsStream("/en-sent.bin");
        final SentenceModel sentenceModel = new SentenceModel(modelIn);
        modelIn.close();
        SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
        String sentences[] = sentenceDetector.sentDetect(paragraph);
//        for (String sent : sentences) {
//            System.out.println(sent);
//        }
        return sentences;
    }

	public static void preprocessingDataSets() {
		String trainset=FileUtil.readfile("/Users/cy2465/Documents/corpora/NCBI/NCBItrainset_corpus.txt");
		String testset=FileUtil.readfile("/Users/cy2465/Documents/corpora/NCBI/NCBItestset_corpus.txt");
		String developset=FileUtil.readfile("/Users/cy2465/Documents/corpora/NCBI/NCBIdevelopset_corpus.txt");
		StringBuffer allset=new StringBuffer();
		allset.append(trainset);
		allset.append(developset);
		allset.append(testset);
		String str=allset.toString();
		
		String[] rows=str.split("\n");
		String currentsent="";
		StringBuffer currentsb=new StringBuffer();
		for(int i=0;i<rows.length;i++){
			int pos=rows[i].indexOf('|');
			if((rows[i].length()>10)&&(rows[i].substring(pos+1, pos+2).equals("a"))){
				//System.out.println("key str="+rows[i].substring(10));
				currentsb.setLength(0);
				currentsb.append(rows[i-1].substring(10));
				currentsb.append("\n");
				currentsb.append(rows[i].substring(10));
			}
			
			if(rows[i].contains("\t")){
				String[] elements=rows[i].split("\t");
				if(elements[4].equals("CompositeMention")){
					System.out.println(rows[i]);
					int start_index=Integer.valueOf(elements[1]);
					int end_index=Integer.valueOf(elements[2]);
					System.out.println("phrase = "+currentsb.toString().substring(start_index,end_index));
				}
			}
		}
	}

}
