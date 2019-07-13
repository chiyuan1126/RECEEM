package cn.edu.njust.chiyuan.conjunction.phrase;

import java.io.File;
import java.util.TreeMap;

import com.hankcs.hanlp.collection.trie.DoubleArrayTrie;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;

import cn.edu.njust.chiyuan.conjunction.datapreparation.SentenceDetectExample;
import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;
import junit.framework.TestCase;

public class DoubleArrayTrieTest {
	SentenceDetectExample sde = new SentenceDetectExample();

	public static void main(String[] args) {
		DoubleArrayTrieTest dat = new DoubleArrayTrieTest();
		try {
			//dat.testLongestSearcher();
			dat.cleanPubAbstract();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void cleanPubAbstract() throws Exception {
		TreeMap<String, String> buildFrom = new TreeMap<String, String>();
		//cleaning dic
		//buildFrom.put(",", ",");
		//buildFrom.put(".", ".");
		//buildFrom.put(":", ":");
		//buildFrom.put(";", ";");
		//buildFrom.put("(", "(");
		//buildFrom.put(")", ")");
		//buildFrom.put("/", "/");//±
		//buildFrom.put("\n", "\n");
		
		//cleaning space
		buildFrom.put("     ", "     ");
		buildFrom.put("    ", "    ");
		buildFrom.put("   ", "   ");
		buildFrom.put("  ", "  ");
		
		
		DoubleArrayTrie<String> trie = new DoubleArrayTrie<String>(buildFrom);

		File dir = new File("/Users/cy2465/Documents/PubAbstract_Attached_Replaced_Phrase_clean");
		File[] files = dir.listFiles();
		int count = 0;
		for (File f : files) {
			String text = FileUtil.readfile(f.getAbsolutePath());
		//String text="d hypoplastic lungs with cdh  cdh +    n = 10 at each time-point  respectively   pulmonary gene expression a parameter   that measures the relative strength of a  ligand to bias the receptor into the active conformation. a model with this additional receptor state is sufficient to describe response data when two ligands (agonist/agonist or agonist/antagonist pairs)";
		count++;
			System.out.println("current " + count + " of " + files.length);

			DoubleArrayTrie<String>.LongestSearcher searcher = trie.getLongestSearcher(text.toCharArray(), 0);
			// Set<String> matcher=new LinkedHashSet<String>();
			int prevIndex = 0;
			StringBuffer sb = new StringBuffer();
			while (searcher.next()) {
				int matchIndex = searcher.begin;
				sb.append(text.substring(prevIndex, matchIndex));
				String replacement = searcher.value;
				replacement = " ";
				sb.append(replacement);
				prevIndex = searcher.begin + searcher.length + 1 - 1;
			}
			sb.append(text.substring(prevIndex));
			//System.out.println(sb.toString());
			FileUtil.write2File("/Users/cy2465/Documents/PubAbstract_Attached_Replaced_Phrase_clean2/" + f.getName(),
					sb.toString());
		}
		

	}

	public void testLongestSearcher() throws Exception {
		String all_dic = FileUtil.readfile("/Users/cy2465/Downloads/PubMed_Phrases/all_dictionary.txt");
		String[] phrases = all_dic.split("\n");
		TreeMap<String, String> buildFrom = new TreeMap<String, String>();
		for (String p : phrases) {
			buildFrom.put(" " + p + " ", " " + p + " ");
		}

		DoubleArrayTrie<String> trie = new DoubleArrayTrie<String>(buildFrom);

		File dir = new File("/Users/cy2465/Documents/PubmedAbstractText");
		File[] files = dir.listFiles();
		int count = 0;
		for (File f : files) {
			String text = FileUtil.readfile(f.getAbsolutePath());
			text=" "+text.toLowerCase()+" ";
			String[] arrstr = sde.sentenceDetect(text);
			StringBuffer writer = new StringBuffer();

			count++;
			System.out.println("current " + count + " of " + files.length);
			for (String astr : arrstr) {

				DoubleArrayTrie<String>.LongestSearcher searcher = trie.getLongestSearcher(astr.toCharArray(), 0);
				// Set<String> matcher=new LinkedHashSet<String>();
				int prevIndex = 0;
				StringBuffer sb = new StringBuffer();
				int matchcount = 0;
				while (searcher.next()) {
					matchcount++;
					int matchIndex = searcher.begin;
					sb.append(astr.substring(prevIndex, matchIndex));
					String replacement = searcher.value.replace(" ", "-");
					replacement = " " + replacement.substring(1, replacement.length() - 1) + " ";
					sb.append(replacement);
					prevIndex = searcher.begin + searcher.length + 1 - 1;
				}
				sb.append(astr.substring(prevIndex));
				if (matchcount > 0) {
					writer.append(" " + sb.toString() + " " + astr);
				} else {
					writer.append(" " + sb.toString());
				}
				// System.out.println("sent=>" + sb.toString() + " " + astr);
			}
			FileUtil.write2File(
					"/Users/cy2465/Documents/PubAbstract_Attached_Replaced_Phrase/attached_re_phrase_" + f.getName(),
					writer.toString());
		}
	}

	public void testHandleEmptyString() throws Exception {
		String emptyString = "";
		DoubleArrayTrie<String> dat = new DoubleArrayTrie<String>();
		TreeMap<String, String> dictionary = new TreeMap<String, String>();
		dictionary.put("bug", "问题");
		dat.build(dictionary);
		DoubleArrayTrie<String>.Searcher searcher = dat.getSearcher(emptyString, 0);
		while (searcher.next()) {
		}
	}

	public void testIssue966() throws Exception {
		TreeMap<String, String> map = new TreeMap<String, String>();
		for (String word : "001乡道, 北京, 北京市通信公司, 来广营乡, 通州区".split(", ")) {
			map.put(word, word);
		}
		DoubleArrayTrie<String> trie = new DoubleArrayTrie<String>(map);
		DoubleArrayTrie<String>.LongestSearcher searcher = trie.getLongestSearcher("北京市通州区001乡道发生了一件有意思的事情，来广营乡歌舞队正在跳舞",
				0);
		while (searcher.next()) {
			System.out.printf("%d %s\n", searcher.begin, searcher.value);
		}
	}
}