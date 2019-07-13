package cn.edu.njust.chiyuan.conjunction.phrase;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.ahocorasick.trie.Trie.TrieBuilder;
import org.apache.commons.lang3.StringUtils;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class TestACT {


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String base_dir = "/Users/cy2465/Documents/pubmed_clean_phrase/";
		File dir = new File(base_dir);
		File[] fs = dir.listFiles();
		int x=0;
		for (File f : fs) {
			if(f.getName().endsWith(".txt")){
				String content = FileUtil.readfile(f.getAbsolutePath());
				System.out.println(f.getName());
				if(content.length()>0 && content.charAt(0)==' '){
					System.out.println("remove===>");
					content=content.substring(1);
					FileUtil.write2File(f.getAbsolutePath(), content);
				}
			}
			
		}

	}

}
