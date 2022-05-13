package consumer;

import hessim.config.MessageQueue;
import hessim.msgprocessor.MessageContainer;
import hessim.msgprocessor.MessageProcessor;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class Consumer implements Runnable, IConsumer {
    private static int ackMode;
    private Connection connection;
    private Session session;
    private Destination queue;
    private MessageConsumer consumer;
    private String messageBrokerUrl;
    private MessageProcessor messageProcessor;
    private MessageQueue messageQueue;
    
    private boolean transacted = false;
    public boolean hasConnection = false;

    //private MessageProtocol messageProtocol;

    static {
        ackMode = Session.AUTO_ACKNOWLEDGE;
    }

    public Consumer(String url, MessageQueue cfgQueue, MessageProcessor msgproc) {
    	messageBrokerUrl = url;
    	messageQueue = cfgQueue;
    	messageProcessor = msgproc;
        this.setupMessageQueueConsumer();
    }

    private void setupMessageQueueConsumer() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(this.transacted, ackMode);
            queue = this.session.createQueue(messageQueue.inboundQueue);
            consumer = this.session.createConsumer(queue);
            hasConnection = true;
        } catch (JMSException e) {
            //Handle the exception appropriately
        	hasConnection = false;
        }
    }

    @Override
    public void run() {
        try {
        	while(true)
        	{
	            Message message = consumer.receive();
	            if (message instanceof TextMessage) 
	            {
	            	this.messageProcessor.putMessage(new MessageContainer(message, messageQueue));
	            }
        	}
        }
        catch (Exception ex) 
        {
        	ex.printStackTrace();
        }
        
        try {
        	session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
