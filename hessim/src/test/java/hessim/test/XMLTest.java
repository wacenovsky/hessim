package hessim.test;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import hessim.xml.XmlContent;

public class XMLTest {
	

	@Test
	public void testReadFromFile()
	{
		@SuppressWarnings("unused")
		XmlContent xml = new XmlContent();
		xml.readFromFile("C:/junk/books.xml");
		String name = xml.getRootElementName();
		System.out.println("Root Element Name:"+name);
		 

	}
	
	@Test
	public void testXpath()
	{
		XmlContent xml = new XmlContent();
		xml.readFromFile("C:\\projects\\stmk\\EN_ATM4\\sagemcomsim\\testdata\\MeterAssetSendText\\MeterAssetSendTextRequestMessage.xml");
		String name = xml.getRootElementName();
		System.out.println("Root Element Name:"+name);
		//Node node = xml.getXpathNode("/*/header/messageID");
		xml.setXpathContent("/*/header/messageID", "9999");
		String nodeval = xml.getXpathContent("/*/header/messageID");
		
	}
	
	@Test
	public void testReadFromString()
	{
		Path filePath = Path.of("C:/junk/books.xml");
		String content = null;
		try {
			content = Files.readString(filePath, StandardCharsets.US_ASCII);
			System.out.println(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		XmlContent xml = new XmlContent();
		xml.readFromString(content);
		
		String name = xml.getRootElementName();
		System.out.println("Root Element Name:"+name);
		
		 

	}
	
	
}
