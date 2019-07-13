package cn.edu.njust.chiyuan.conjunction.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMParser {
	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

	// Load and parse XML file into DOM
	public Document parse(String filePath) {
		Document document = null;
		try {
			// DOM parser instance
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			// parse an XML file into a DOM tree
			document = builder.parse(new File(filePath));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	public static void main(String[] args) {
		DOMParser parser = new DOMParser();
		Document document = parser.parse("/Users/cy2465/Documents/pubmed18n0001.xml");
		Element rootElement = document.getDocumentElement();
		NodeList subnodeList = rootElement.getElementsByTagName("AbstractText");
		for(int k=0;k<subnodeList.getLength();k++){
			System.out.println("=======Start=========");
			System.out.println(subnodeList.item(k).getTextContent());
			System.out.println("=======End=========");
		}
	}
}