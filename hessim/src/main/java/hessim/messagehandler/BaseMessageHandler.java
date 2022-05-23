package hessim.messagehandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import javax.jms.MessageProducer;
import javax.jms.Session;

import hessim.xml.XmlContent;

public abstract class BaseMessageHandler implements IMessageHandler{

	public abstract void handleMessage(XmlContent xml, Map<String, String> templates, MessageProducer producer, Session session);
	
	protected XmlContent getTemplateFromFilename(String filename)
	{
		Path filePath = Path.of(filename);
		String content = null;
		try {
			content = Files.readString(filePath, StandardCharsets.US_ASCII);
			XmlContent xml = new XmlContent();
			xml.readFromString(content);
			return xml;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
