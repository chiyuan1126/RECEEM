package cn.edu.njust.chiyuan.conjunction.datapreparation;

import java.io.File;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class PennBioIEData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File dir=new File("/Users/cy2465/Documents/corpora/PennBioIE");
		File[] files=dir.listFiles();
		for(File f:files){
			String jsoncontent=FileUtil.readfile(f.getAbsolutePath());
			if(jsoncontent.contains("Chain")){
				System.out.println(f.getName());
			}
		}
	}

}
