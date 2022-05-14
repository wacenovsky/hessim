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
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.inject.Inject;

import hessim.config.Config;
import hessim.config.MessageQueue;
import hessim.config.MessageType;
import hessim.xml.XmlContent;

public class MessageProcessorAsync implements Runnable, MessageListener
{
	BlockingQueue<Message> queue;
	
	public Map<String, MessageQueue> rootEelementName2messageQueue;
	Config config;
	private final static Logger LOGGER = Logger.getLogger(MessageProcessorAsync.class.getName());
	private Thread workerThread;
	private Connection inboundConnection;
	private Session inboundSession;
	
	@Inject
	public MessageProcessorAsync(Config cnf)
	{
		config = cnf;
		queue = new LinkedBlockingQueue<Message>();
		rootEelementName2messageQueue = new HashMap<String, MessageQueue>();
		createConsumers();
		workerThread = new Thread(this);
		workerThread.start();
	}
	
	public void createConsumers()
	{
		String inboundUrl = String.format("tcp://%s:%s", config.inboundIP, config.inboundPort);
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(inboundUrl);
			
			inboundConnection = connectionFactory.createConnection();
			inboundConnection.start();
			inboundSession = inboundConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	           
			for (MessageQueue q : config.messageQueues)
			{
				Queue queue = this.inboundSession.createQueue(q.inboundQueue);
				MessageConsumer consumer = this.inboundSession.createConsumer(queue);
				consumer.setMessageListener(this);
				
				// create map root element name -> messageQueue
				for (Map.Entry<String,MessageType> entry : q.messageTypes.entrySet())
				{
					String messageName = entry.getKey();
					rootEelementName2messageQueue.put(messageName, q);
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
	
	
	public int sendMessage(MessageContainer msg, Destination dest)
	{
		int retval = 0;
		return retval;		
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
	
		XmlContent xml = new XmlContent();
		if (xml.readFromString(msg) == 0)
		{
			String rootElementName = xml.getRootElementName();
			LOGGER.info("   Root Element:" + rootElementName);
		}
		
	}
}
