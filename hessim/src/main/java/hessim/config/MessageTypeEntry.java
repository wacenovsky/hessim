package hessim.config;

import java.util.HashMap;

public class MessageTypeEntry  {
	
	public HashMap<Integer, String> id2TemplateFile;
	public String inboundQueue;
	public String outboundQueue;
	
	public MessageTypeEntry()
	{
		id2TemplateFile = new HashMap<Integer, String>();
	}
	
}