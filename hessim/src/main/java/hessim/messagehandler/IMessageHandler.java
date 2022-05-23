package hessim.messagehandler;

import java.util.Map;

import javax.jms.MessageProducer;
import javax.jms.Session;

import hessim.xml.XmlContent;

public interface IMessageHandler {
	public void handleMessage(XmlContent xml, Map<String, String> templates, MessageProducer producer, Session session);

}
