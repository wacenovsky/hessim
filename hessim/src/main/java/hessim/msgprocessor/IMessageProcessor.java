package hessim.msgprocessor;

import java.util.concurrent.BlockingQueue;

import javax.jms.Destination;

import org.apache.activemq.Message;

public interface IMessageProcessor {
	public int putMessage(MessageContainer msg);
	public int sendMessage(MessageContainer msg, Destination dest);
	public BlockingQueue<MessageContainer> getMsgQueue();
}
