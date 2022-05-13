package hessim.msgprocessor;

import javax.jms.Message;

import hessim.config.MessageQueue;

public class MessageContainer {
	public Message message;
	public MessageQueue queue;
	
	public MessageContainer(Message imsg, MessageQueue iqueue)
	{
		message = imsg;
		queue = iqueue;
	}
}
