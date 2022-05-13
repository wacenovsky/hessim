package hessim.config;

import java.util.HashMap;
import java.util.Map;

public class MessageTypeEntry  {
	
	public Map<Integer, String> id2TemplateFile;
	public String inboundQueue;
	public String outboundQueue;
	
	public MessageTypeEntry()
	{
		id2TemplateFile = new HashMap<Integer, String>();
	}
	
}