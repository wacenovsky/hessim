package hessim.config;

public class Config {
	public String inboundIP;
	public int inboundPort;
	public String outboundIP;
	public int outboundPort;
	
	public java.util.ArrayList<MessageQueue> messageQueues;
	
	public Config()
	{
		messageQueues = new java.util.ArrayList<MessageQueue>();
	}

}
