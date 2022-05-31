package hessim.messagehandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandlerCollection {
	public Map<String, IMessageHandler> msgType2Handler;
	
	public MessageHandlerCollection ()
	{
		msgType2Handler = new ConcurrentHashMap<String, IMessageHandler>();
	}
	
	public IMessageHandler getByMessageType(String msgType)
	{
		synchronized(msgType2Handler)
		{
			if (!msgType2Handler.containsKey(msgType))
			{
				switch (msgType)
				{
					case "groupReq":
					{
						IMessageHandler newHandler = new GroupReqMessageHandler();
						msgType2Handler.put(msgType, newHandler);
						break;
						
					}
					case "ami:MeterAssetSendTextRequestMessage":
					case "ami:MeterAssetUpdateRequestMessage":
					{
						IMessageHandler newHandler = new DefaultMessageHandler();
						msgType2Handler.put(msgType, newHandler);
						break;
					}
					default:
					{
						IMessageHandler newHandler = new DefaultMessageHandler();
						msgType2Handler.put(msgType, newHandler);
					}
				}
			}
		}
		
		return  msgType2Handler.get(msgType);
	}

}
