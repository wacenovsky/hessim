package hessim.config;

import java.util.HashMap;

public class MessageQueue {
	public String inboundQueue;
	public String outboundQueue;
	public HashMap<String, MessageType> messageTypes;
	
	public MessageQueue()
	{
		messageTypes = new HashMap<String, MessageType>();
	}
}
