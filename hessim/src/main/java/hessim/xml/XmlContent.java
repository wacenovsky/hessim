package hessim.xml;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XmlContent {
	
	String stringContent;
	Document domContent;
	
	public XmlContent()
	{
		stringContent = null;
		domContent = null;
	}
	
	public int readFromFile(String filename)
	{
		int retval = -1;
		try (FileInputStream fileIS = new FileInputStream(filename))
	    {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			domContent = builder.parse(fileIS);
			retval = 0;

	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();	
	    } catch (ParserConfigurationException e) {
	    	e.printStackTrace();	
	    } catch (SAXException e) {
	    	e.printStackTrace();	
	    }
		return retval;
	}	
	
	public int readFromString(String content)
	{
		int retval = -1;
		try {
			InputSource source = new InputSource(new StringReader(content));
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = builderFactory.newDocumentBuilder();
			domContent = builder.parse(source);
			retval = 0;
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();	
	    } catch (ParserConfigurationException e) {
	    	e.printStackTrace();	
	    } catch (SAXException e) {
	    	e.printStackTrace();	
	    }
		return retval;
	}	
	
	
	public String getXpathContent(String expression)
	{
		String result = null;
		if (domContent == null) return null;
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			Node node = (Node) xPath.compile(expression).evaluate(domContent, XPathConstants.NODE);
			if (node == null) return null;
		    short nodeType = node.getNodeType();
		    
		    if (nodeType == Node.ELEMENT_NODE) 
		    {
		    	Node child = node.getFirstChild();
		    	if (child.getNodeType() == Node.TEXT_NODE)
		    	{
		    		result = node.getFirstChild().getNodeValue();
		    		if (result != null) result = result.trim();
		    	}
		    }
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Node getXpathNode(String expression)
	{
		Node node  = null;
		if (domContent == null) return null;
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			node = (Node) xPath.compile(expression).evaluate(domContent, XPathConstants.NODE);
			if (node == null) return null;
		    @SuppressWarnings("unused")
			short nodeType = node.getNodeType();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	public String getRootElementName()
	{
		if (domContent == null) return null;
		Element elem = domContent.getDocumentElement();
		if (elem == null) return null;
		return elem.getNodeName();
	}
	
	// https://www.baeldung.com/java-pretty-print-xml
	public String convertToString() {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(domContent), new StreamResult(writer));
            String output = writer.getBuffer().toString().replaceAll("(?m)^[ \t]*\r?\n", "");
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        
        return null;
    }

	//method to convert Document to String
	public String getString()
	{
		StringWriter writer = new StringWriter();
		String result;

		write(domContent.getDocumentElement(), writer);
		result = writer.getBuffer().toString();
		return result;
	}
	
	
	//https://stackoverflow.com/questions/23543699/javax-xml-transform-transformer-very-slow
	private void write(Node e, StringWriter w) {
	    if (e.getNodeType() == Node.ELEMENT_NODE) {
	        w.write("<"+e.getNodeName());
	        if (e.hasAttributes()) {
	            NamedNodeMap attrs = e.getAttributes();
	            for (int i = 0; i < attrs.getLength(); i++) {
	                w.write(" "+attrs.item(i).getNodeName()+"=\""+
	                       attrs.item(i).getNodeValue()+"\"");              
	            }
	        }
	        w.write(">");

	        if (e.hasChildNodes()) {
	            NodeList children = e.getChildNodes();
	            for (int i = 0; i < children.getLength(); i++) {
	                write(children.item(i), w);
	            }

	        }
	        w.write("</"+e.getNodeName()+">");
	    }
	    if (e.getNodeType() == Node.TEXT_NODE) {
	        w.write(e.getTextContent());
	    }
	}
	
	
}
