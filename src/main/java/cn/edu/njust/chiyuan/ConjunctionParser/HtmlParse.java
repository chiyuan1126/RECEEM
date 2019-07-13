package cn.edu.njust.chiyuan.ConjunctionParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;

public class HtmlParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String html=HttpUtil.doGet("https://clinicaltrials.gov/ct2/show/NCT03294629");
		//Document doc = Jsoup.parse(html);
		
		/*
		String csvfile=FileUtil.readfile("/Users/cy2465/Documents/trials_no_inc_or_exc.csv");
		String[] arr=csvfile.split("\n");
		List<String> nctidlist=new ArrayList<String>();
		for(int a=1;a<arr.length;a++){
			System.out.println("r="+arr[a].split(",")[1].substring(1, 12));
			nctidlist.add(arr[a].split(",")[1].substring(1, 12));
		}
		*/
		
		String csvneitherincexc=FileUtil.readfile("/home/njust4060/All_trial_files.txt");;
		String[] arr=csvneitherincexc.split("\n");
		List<String> nctidlist=new ArrayList<String>();
		for(String r:arr){
			//System.out.println("r="+r);
			if(r.endsWith(".xml")){
			int a=r.lastIndexOf("/");
			String ss=r.substring(a);
			//System.out.println(ss);
			//System.out.println(ss.substring(1,12));
			nctidlist.add(ss.substring(1,12));
			}
		}
		//System.out.println("count="+nctidlist.size());
		fetchEC(nctidlist,"/home/njust4060/allctfiles/");
	}

	public static void fetchEC(List<String> nctidlist,String targetpath) {
		int cc=0;
		for(String nctid:nctidlist){
			System.out.println((++cc)+" of "+nctidlist.size());
			StringBuffer sb=new StringBuffer();
			try {
				String url="https://clinicaltrials.gov/ct2/show/"+nctid+"/";
				//System.out.println("url+"+url);
				Document doc = Jsoup.connect(url).get();
				//System.out.println("doc="+doc);
				Elements content = doc.getElementsByClass("ct-header3");
				for (Element link : content) {
					if(link.text().equals("Criteria")){
						//System.out.println("=====");
						Element se=link.nextElementSibling();
						Elements earray=se.getAllElements();
						for(Element ea:earray){
							if(ea.getAllElements().size()==1){
								//System.out.println("size="+ea.getAllElements().size());
								//System.out.println(">>>"+ea.text());
								sb.append(ea.text()+"\n");
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//ziran_trial
			//System.out.println("ec="+sb.toString());
			FileUtil.write2File(targetpath+nctid+".txt", sb.toString());
		}
	}

}
