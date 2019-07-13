package cn.edu.njust.chiyuan.conjunction.datapreparation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.edu.njust.chiyuan.conjunction.utils.FileUtil;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class SentenceDetectExample {
	 
    public static void main(String[] args) {
        try {
        	String p="Dr. Zhang is not good at math. Pt. Li has type 2 diabetes. He is very good at coding.";
            new SentenceDetectExample().sentenceDetect(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * This method is used to detect sentences in a paragraph/string
     * @throws InvalidFormatException
     * @throws IOException
     */
    public String[] sentenceDetect(String paragraph) throws InvalidFormatException, IOException {
        // refer to model file "en-sent,bin", available at link http://opennlp.sourceforge.net/models-1.5/
        InputStream is = new FileInputStream("/Users/cy2465/Downloads/public_mm_lite/data/models/en-sent.bin");  
        SentenceModel model = new SentenceModel(is);  
        // feed the model to SentenceDetectorME class
        SentenceDetectorME sdetector = new SentenceDetectorME(model);     
        // detect sentences in the paragraph
        String sentences[] = sdetector.sentDetect(paragraph);
        for(String s:sentences){
        	System.out.println("=>"+s);
        }
        // print the sentences detected, to console    
        is.close();
        return sentences;
    }
}