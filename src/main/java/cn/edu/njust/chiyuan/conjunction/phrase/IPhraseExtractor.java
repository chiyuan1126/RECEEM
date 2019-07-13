package cn.edu.njust.chiyuan.conjunction.phrase;

import java.util.List;

public interface IPhraseExtractor {
	 /**
     * extract phrases
     * @param text 
     * @param size the number of extracted phrases
     * @return phrase list
     */
    List<String> extractPhrase(String text, int size);
}
