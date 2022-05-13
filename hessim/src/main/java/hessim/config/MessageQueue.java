package hessim.config;

import java.util.HashMap;
import java.util.Map;

public class MessageQueue {
	public String inboundQueue;
	public String outboundQueue;
	public Map<String, MessageType> messageTypes;
	
	public MessageQueue()
	{
		messageTypes = new HashMap<String, MessageType>();
	}
}
