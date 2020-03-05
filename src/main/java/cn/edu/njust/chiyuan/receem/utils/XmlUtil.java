package cn.edu.njust.chiyuan.receem.utils;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

public class XmlUtil {
    public static Document parseXmlString(String xmlStr){

        try{
            InputSource is = new InputSource(new StringReader(xmlStr));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            return doc;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getXmlBodyContext(String bodyXml) {
    		StringBuffer sb=new StringBuffer();
        Document doc = parseXmlString(bodyXml);
        String[] ellipsis=new String[2];
        if(null != doc){
            NodeList rootNode = doc.getElementsByTagName("cons");
            if(rootNode != null){

                Node root = rootNode.item(0);
                NamedNodeMap nnm=root.getAttributes();
                System.out.println(nnm.getNamedItem("lex").getTextContent());
                ellipsis[0]=nnm.getNamedItem("lex").getTextContent();
                NodeList nodes = root.getChildNodes();
                for(int i = 0;i < nodes.getLength(); i++){
                    Node node = nodes.item(i);
                    //dataMap.put(node.getNodeName(), node.getTextContent());
                    System.out.println("->"+node.getTextContent());
                    sb.append(node.getTextContent());
                }
            }
        }
        ellipsis[1]=sb.toString();
        return ellipsis;
    }
    
    public static void main(String[] args) throws SAXParseException {
    		
    	/*
    		String xmlStr = "<cons lex=\"(OR X1-box X2-box)\" sem=\"(OR G#DNA_domain_or_region G#DNA_domain_or_region)\"><cons lex=\"X1-*\"><w c=\"NN\">X1-</w></cons> <w c=\"CC\">or</w> <cons lex=\"X2-*\"><w c=\"*\">X2-</w></cons><cons lex=\"*box\"><w c=\"NNS\">boxes</w></cons></cons>";
        String[] map = XmlUtil.getXmlBodyContext(xmlStr);
        System.out.println(map[0]);
        System.out.println(map[1]);
        
        String xmlStr2="<cons lex=\"(OR Elf-1_binding_site HMG-I(Y)_binding_site)\" sem=\"(OR G#DNA_domain_or_region G#DNA_domain_or_region)\"><cons lex=\"Elf-1\" sem=\"G#protein_molecule\"><cons lex=\"Elf-1*\"><w c=\"NN\">Elf-1</w></cons></cons>";
        
        String[] map2 = XmlUtil.getXmlBodyContext(xmlStr2);
        System.out.println(map2[0]);
        System.out.println(map2[1]);
      */  
    		String content=FileUtil.readfile("/Users/cy2465/Documents/corpora/GENIAcorpus3.02p/GENIAcorpus3.02.merged.xml");
    		XmlUtil.getXmlBodyContext(content);
        
    }


}

