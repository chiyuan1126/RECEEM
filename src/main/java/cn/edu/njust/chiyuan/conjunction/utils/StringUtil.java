package cn.edu.njust.chiyuan.conjunction.utils;

import java.util.List;

import cn.edu.njust.chiyuan.conjunction.pojo.Token;

public class StringUtil {
	//construct phrase according to start index & end index 
	public static String strRender(Integer start, Integer end, List<Token> tokens) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (int a = start; a <= end; a++) {
			if (i == 0) {
				sb.append(tokens.get(a).word);
			} else {
				if(tokens.get(a).word.equals("'s")==false){
					sb.append("-" + tokens.get(a).word);
				}else{
					sb.append(tokens.get(a).word);
				}
			}
			i++;
		}
		return sb.toString();
	}
	public static String answerRender(List<String> phrases) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (int a = 0; a < phrases.size(); a++) {
			if (i == 0) {
				sb.append(phrases.get(a));
			} else {
				sb.append("&&"+phrases.get(a));
				
			}
			i++;
		}
		return sb.toString();
	}
	
	public static String chunkStrRender(List<Token> list) {
		StringBuffer sb = new StringBuffer();
		for (Token s : list) {
			sb.append(s.word + " ");
		}
		return sb.toString().trim();
	}
}
