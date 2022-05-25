package hessim.messagehandler;

import java.util.Map;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import hessim.msgprocessor.MessageProcessorAsync;
import hessim.xml.XmlContent;

public class GroupReqMessageHandler extends BaseMessageHandler{
	
	private final static Logger LOGGER = Logger.getLogger(MessageProcessorAsync.class.getName());

	@Override
	public void handleMessage(XmlContent request, Map<String, String> templates, MessageProducer producer, Session session) {
		
		if (request == null || templates == null || producer == null || session == null) 
		{
			return;
		}
		
		XmlContent reply = null;
		if (templates.containsKey("default"))
		{
			String filename = templates.get("default");
			reply = this.getTemplateFromFilename(filename);
		}
		
		if (reply == null )
		{
			String rootElementName = request.getRootElementName();
			LOGGER.severe(String.format("No reply template found for %s", rootElementName));
			return;
		}
		
		// do substitutions
		
		// get request Id
		String reqID = request.getXpathContent("/*/reqId");
		reply.setXpathContent("/*/reqId", reqID);
		
		// get device
		String deviceId = request.getXpathContent("/*/device");
		reply.setXpathContent("/*/devId", deviceId);
		
		String xmlString = reply.convertToString();
		TextMessage message = null;
		
		try {
			message = session.createTextMessage();
			message.setText(xmlString);
			message.setJMSCorrelationID("");
			producer.send(message);
			LOGGER.info(String.format("Sent reply:\n%s\n", xmlString));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
