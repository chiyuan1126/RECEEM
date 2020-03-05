package cn.edu.njust.chiyuan.receem.pojo;

import java.util.List;
import java.util.Map;

public class SubChunk {
	public String subchunkstr;
	//public List<List<Token>> tokenlist_p1;
	//public List<List<Token>> tokenlist_p2;
	public Map<SubSubChunk,SubSubChunk> subsubchunkmap;
	public List<Token> tokens;
}
