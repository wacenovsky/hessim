package hessim.msgprocessor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.inject.Inject;

import hessim.config.Config;
import hessim.config.MessageQueue;
import hessim.config.MessageType;
import hessim.exception.ConfigException;
import hessim.messagehandler.IMessageHandler;
import hessim.messagehandler.MessageHandlerCollection;
import hessim.xml.XmlContent;

/*
 * 
 */
public class MessageProcessorAsync implements Runnable, MessageListener
{
	BlockingQueue<Message> queue;
	
	public Map<String, MessageProducer> rootElementName2MessageProducer;
	public Map<String, Map<String, String>> rootElementName2Templates;
	Config config;
	private final static Logger LOGGER = Logger.getLogger(MessageProcessorAsync.class.getName());
	private Thread workerThread;
	private Connection inboundConnection;
	private Session inboundSession;
	private Connection outboundConnection;
	private Session outboundSession;
	private MessageHandlerCollection messageHandlerCollection;
	
	@Inject
	public MessageProcessorAsync(Config cnf) throws ConfigException
	{
		config = cnf;
		queue = new LinkedBlockingQueue<Message>();
		rootElementName2MessageProducer = new HashMap<String, MessageProducer>();
		rootElementName2Templates = new HashMap<String, Map<String, String>>() ;
		messageHandlerCollection = new MessageHandlerCollection();
		createConsumers();
		createResponseProducers();
		workerThread = new Thread(this);
		workerThread.start();
	}
	
	public void createConsumers()
	{
		String inboundUrl = String.format("tcp://%s:%s", config.inboundIP, config.inboundPort);
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(inboundUrl);
			connectionFactory.setUserName("michi"); // TODO 
			connectionFactory.setPassword("michi");
			
			inboundConnection = connectionFactory.createConnection();
			inboundConnection.start();
			inboundSession = inboundConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	           
			for (MessageQueue q : config.messageQueues)
			{
				Queue queue = this.inboundSession.createQueue(q.inboundQueue);
				MessageConsumer consumer = this.inboundSession.createConsumer(queue);
				consumer.setMessageListener(this);
			
			}
	    } catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createResponseProducers() throws ConfigException
	{
		String outboundUrl = String.format("tcp://%s:%s", config.outboundIP, config.outboundPort);
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(outboundUrl);
			
			outboundConnection = connectionFactory.createConnection();
			outboundConnection.start();
			outboundSession = outboundConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	           
			for (MessageQueue q : config.messageQueues)
			{
				Queue queue = this.outboundSession.createQueue(q.outboundQueue);
				MessageProducer producer = this.inboundSession.createProducer(queue);
				
				for (Map.Entry<String,MessageType> entry : q.messageTypes.entrySet())
				{
					String messageName = entry.getKey();
					MessageType messageType = entry.getValue();
					if (rootElementName2MessageProducer.containsKey(messageName) || rootElementName2Templates.containsKey(messageName))
					{
						throw new ConfigException(String.format("Message %s already in other queue", messageName));
					}
					rootElementName2MessageProducer.put(messageName, producer);
					rootElementName2Templates.put(messageName, messageType.templates);
				}

			}
	    } catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onMessage(Message message) {  
	    if (message instanceof TextMessage) {
	    	try {
	    		Destination dest = message.getJMSDestination();
	    		String name = dest.toString();
	    		LOGGER.info("   Enqueue message from queue:" + name);
				queue.put(message);
			} catch (InterruptedException | JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
    }
	
	public void run()
	{
		LOGGER.info("Worker thread started");
		while (true)
		{
			try {
				Message msg = queue.take();
				LOGGER.info("   CorrelationId:" + msg.getJMSCorrelationID());
				if (msg != null &&  msg instanceof TextMessage) 
				{
	                TextMessage textMessage = (TextMessage) msg;
	                //LOGGER.info("   Received Message Text:" + textMessage.getText());
	                handleMessageString(textMessage.getText());
	            }
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void handleMessageString(String msg)
	{
		LOGGER.info(String.format("Got Request:\n%s\n", msg));
		XmlContent xml = new XmlContent();
		if (xml.readFromString(msg) == 0)
		{
			String rootElementName = xml.getRootElementName();
			LOGGER.info("   Root Element:" + rootElementName);
			
			IMessageHandler msgHandler = messageHandlerCollection.getByMessageType(rootElementName);
			msgHandler.handleMessage(
					xml, 
					rootElementName2Templates.get(rootElementName), 
					rootElementName2MessageProducer.get(rootElementName), 
					outboundSession);
			
		}
		
	}
}
