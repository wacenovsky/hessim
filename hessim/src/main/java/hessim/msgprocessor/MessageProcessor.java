package hessim.msgprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.activemq.Message;

import com.google.inject.Inject;

import hessim.config.Config;
import hessim.config.MessageQueue;
import hessim.consumer.Consumer;

public class MessageProcessor implements IMessageProcessor, Runnable
{
	BlockingQueue<MessageContainer> queue;
	Config config;
	private final static Logger LOGGER = Logger.getLogger(MessageProcessor.class.getName());
	private Thread workerThread;
	
	private List<Thread> consumerThreads;
	
	@Inject
	public MessageProcessor(Config cnf)
	{
		consumerThreads = new ArrayList<Thread>();
		config = cnf;
		queue = new LinkedBlockingQueue<MessageContainer>();
		createConsumers();
		workerThread = new Thread(this);
		workerThread.start();
	}
	
	public void createConsumers()
	{
		String inboundUrl = String.format("tcp://%s:%s", config.inboundIP, config.inboundPort);
		for (MessageQueue q : config.messageQueues)
		{
			Consumer consumer = new Consumer(inboundUrl, q , this);
			if (consumer.hasConnection)
			{
				Thread consumerThread = new Thread(consumer);
				consumerThreads.add(consumerThread);
				consumerThread.start();
			}
			else
			{
				//todo
			}
		}
	}
	
	public int putMessage(MessageContainer msg)
	{
		int retval = 0;
		try {
			queue.put(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retval = 1;
		}
		
		return retval;
	}
	public int sendMessage(MessageContainer msg, Destination dest)
	{
		int retval = 0;
		
		
		return retval;		
	}
	
	public BlockingQueue<MessageContainer> getMsgQueue()
	{
		return queue;
	}
	
	public void run()
	{
		LOGGER.info("Worker thread started");
		while (true)
		{
			try {
				MessageContainer msg = queue.take();
				
				LOGGER.info("Received Message from:" + msg.queue.inboundQueue);
				LOGGER.info("   CorrelationId:" + msg.message.getJMSCorrelationID());
				if (msg.message != null &&  msg.message instanceof TextMessage) 
				{
	                TextMessage textMessage = (TextMessage) (msg.message);
	                LOGGER.info("   Received Message Text:" + textMessage.getText());
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
	
	public void handlemessage(Message msg)
	{
		
	}
}
