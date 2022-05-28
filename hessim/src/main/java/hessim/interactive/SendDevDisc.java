// java -Dconfig.path="C:/projects/stmk/EN_ATM4/sagemcomsim/testdata/config.json" --add-opens java.base/java.lang=ALL-UNNAMED -cp hessim-0.0.1-SNAPSHOT-jar-with-dependencies.jar hessim.interactive.SendDevDisc

package hessim.interactive;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import hessim.config.Config;
import hessim.config.ConfigReader;
import hessim.xml.XmlContent;

public class SendDevDisc {
	public static void main(String[] args)
	{
		try {
			ConfigReader reader = new ConfigReader();
			Config cfg = reader.readFromFile("");
			String outboundUrl = String.format("tcp://%s:%s", cfg.outboundIP, cfg.outboundPort);
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(outboundUrl);
			
			Connection conn = connectionFactory.createConnection();
			conn.start();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("M2M_DEC_DEVICE");
			MessageProducer producer = session.createProducer(queue);
			
			String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					         + "<devdisc xmlns=\"http://www.sagemcom.com/amm/hes2015/types/devicedisc/v1/\">\n"
					         + "   <devId>$</devId>\n"
					         + "   <time>$</time>\n"
					         + "   <dcId>$</dcId>\n"
					         + "</devdisc>";
			XmlContent xml = new XmlContent();
			System.out.println("Simulate device detection:\n"+ 
			                   "Enter deviceId and dcId and the\n"+
					           "request is sent to queue M2M_DEC_DEVICE\n");
			try (Scanner scanner = new Scanner(System.in)) {
				if (xml.readFromString(xmlString) == 0)		
				while (true)
				{
					System.out.println("\ndevId?");
				    String devId = scanner.nextLine();  
					System.out.println("dcId?");
				    String dcId = scanner.nextLine();  
				    xml.setXpathContent("/*/devId", devId);
				    xml.setXpathContent("/*/dcId", dcId);
				    String timeStr = getIsoUtcDate();
				    xml.setXpathContent("/*/time", timeStr);
				    String finalXmlString = xml.convertToString();
				    TextMessage message = session.createTextMessage();
					message.setText(finalXmlString);
					message.setJMSCorrelationID("");
				    producer.send(message);
				    System.out.println("\n -> Sent to queue M2M_DEC_DEVICE:");
				    System.out.println(finalXmlString);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("ende");
	}
	
    public static String getIsoUtcDate() {
    	return DateTimeFormatter.ISO_INSTANT.format(Instant.now().truncatedTo(ChronoUnit.MILLIS));
    }
    
    public static String getIsoUtcDate2() {
        OffsetDateTime nowUtc = OffsetDateTime.now()
            .withOffsetSameInstant(ZoneOffset.UTC);
        return DateTimeFormatter.ISO_DATE_TIME.format(nowUtc);
    }
}
