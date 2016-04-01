package com.idega.block.creditcard.data;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class BorgunDocument {
	private Document xmlDocument = null;
	
	public BorgunDocument(String xmlString) throws SAXException, IOException, ParserConfigurationException{
		setData(xmlString);
	}
	
	public BorgunDocument(Map<String, String> dataMap) throws Exception  {
		setData(dataMap);
	}
	
	public void setData(String xmlString) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlString));
		DOMBuilder jdomBuilder = new DOMBuilder();
		this.xmlDocument =jdomBuilder.build(db.parse(is)) ;
	}
	
	public void setData(Map<String, String> dataMap) throws Exception {
		if (dataMap == null) throw new Exception("Data map empty!");
		if (!dataMap.containsKey("BurgunActionName")) throw new Exception("No action defined!");
		Element rootElement = new Element(dataMap.get("BurgunActionName"));
		this.xmlDocument = new Document(rootElement);
		Map<String, String> tmpMap = new HashMap<String, String>(dataMap);
		tmpMap.remove("BurgunActionName");
		Iterator<Entry<String, String>> it = tmpMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        rootElement.addContent(new Element(pair.getKey()).setText(pair.getValue()));
	        it.remove();
	    }
	}
	
	public Map<String, String> getData(){
		if (this.xmlDocument==null) return null;
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("BurgunActionName", this.xmlDocument.getRootElement().getName());
		List<Element> children = this.xmlDocument.getRootElement().getChildren();
		for (Element childElement: children){
			result.put(childElement.getName(), childElement.getText());
		}
		return result;
	}
	
	@Override 
	public String toString(){
		if (this.xmlDocument == null) return null;
		XMLOutputter outputter = new XMLOutputter();
		return outputter.outputString(this.xmlDocument);
	}
	
}
