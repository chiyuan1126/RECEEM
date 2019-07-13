package cn.edu.njust.chiyuan.conjunction.datapreparation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie.Hit;
import com.hankcs.hanlp.collection.trie.DoubleArrayTrie;
import com.hankcs.hanlp.dictionary.CoreDictionary;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class PubMedAbGenerator {
	
	public static void main(String[] args) {
		PubMedAbGenerator pmdg=new PubMedAbGenerator();
		try {
			AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<String>();
			String all_dic=FileUtil.readfile("/Users/cy2465/Downloads/PubMed_Phrases/all_dictionary.txt");
			String[] phrases=all_dic.split("\n");
			TreeMap<String, String> map = new TreeMap<String, String>();		
			
			
			for(String p:phrases){
				map.put(" "+p+" ", " "+p+" ");
			}
			/*
			map.put("breast neoplasm", "breast neoplasm");
			map.put("breast neoplasms", "breast neoplasms");
			map.put("squamous cell neoplasm", "squamous cell neoplasm");
			map.put("squamous cell neoplasms", "squamous cell neoplasms");
			*/
			
			acdat.build(map);
			
			String source_path="/Users/cy2465/Documents/pub_test/";
			File dir=new File(source_path);
			Integer x=1;
			String target="/Users/cy2465/Documents/PubmedContextPhrase/";
			List<String> filepathes=new ArrayList<String>();
			for(File f:dir.listFiles()){
				if(f.getName().endsWith(".xml.txt")){
					filepathes.add(f.getName());
				}
			}
			
			for(String fn:filepathes){
				System.out.println("Current "+ (x++) +" of "+filepathes.size()+" "+ fn);
				String text=FileUtil.readfile(source_path+fn);
				String newtxt=pmdg.replacePhrases(acdat,text);
				FileUtil.write2File(target+"p_"+fn, newtxt);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String replacePhrases(AhoCorasickDoubleArrayTrie<String> acdat,String text) throws FileNotFoundException, IOException{
		//ObjectOutputStream oos = new ObjectOutputStream( new java.util.zip.GZIPOutputStream(new FileOutputStream("/Users/cy2465/Documents/pubmed_phrase_rule_model.ser.gz")));
		//oos.writeObject(acdat);
		//oos.close();
		List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = acdat.parseText(" "+text+" ");
		Integer last_start = 0;
		Integer last_end = 0;
		/*
		for(Hit<String> s :wordList){
			System.out.println("=>"+s.value+" ("+s.begin+","+s.end+")");
		}
		*/
		for (int i=wordList.size()-1;i>=0;i--) {
			for(int j=i-1;j>=0;j--){
				if(wordList.get(j).end <= wordList.get(i).begin ){
					break;
				}else{
					if(wordList.get(j).begin <= wordList.get(i).begin  && wordList.get(j).end >= wordList.get(i).end ){
						wordList.remove(i);
					}else if(wordList.get(j).begin >= wordList.get(i).begin  && wordList.get(j).end <= wordList.get(i).end ){
						wordList.remove(j);
					}
				}
			}
		}
		//System.out.println("======================================");
		StringBuffer text_new=new StringBuffer(" "+text+" ");
		for(Hit<String> s :wordList){
			//System.out.println("=>"+s.value+" ("+s.begin+","+s.end+")");
			String rstr=s.value.trim().replace(" ", "&nbsp");
			text_new.replace((s.begin+1), (s.end-1), rstr);
		}

		return text_new.toString();
	}
	
	private static String MaxSubString(String shortstr, String longstr) {
        // TODO 自动生成的方法存根
        String temp = new String("");
        for(int i = 0;i<shortstr.length();i++)//先从短字符串的长度开始，逐步递减长度，直到出现符合的字符串
        {
            for(int j = 0,k = shortstr.length()-i;k<shortstr.length();j++,k++)
            {
                temp = shortstr.substring(j, k);
                if(longstr.contains(temp))
                {
                    return temp;
                }
            }
        }
        return null;
    }

	
	private void sortListById(List<Hit<String>> list) {
        Collections.sort(list, new Comparator<Hit<String>>() {
            @Override
            public int compare(Hit<String> o1, Hit<String> o2) {
                if (o1.begin > o2.begin) {
                    return 1;
                } else if (o1.begin < o2.begin) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }
	
	private void sortListByEnd(List<Hit<String>> list) {
        Collections.sort(list, new Comparator<Hit<String>>() {
            @Override
            public int compare(Hit<String> o1, Hit<String> o2) {
                if (o1.end > o2.end) {
                    return 1;
                } else if (o1.end < o2.end) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }


}
