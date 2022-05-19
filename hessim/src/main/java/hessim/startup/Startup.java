// Add this to VM Arguments
// --add-opens java.base/java.lang=ALL-UNNAMED
// start as: java -Dconfig.path="C:/projects/stmk/EN_ATM4/sagemcomsim/testdata/config.json" --add-opens java.base/java.lang=ALL-UNNAMED -cp hessim-0.0.1-SNAPSHOT-jar-with-dependencies.jar hessim.startup.Startup
package hessim.startup;

import java.io.IOException;

import hessim.config.Config;
import hessim.config.MessageQueue;
import hessim.config.MessageType;

import com.google.inject.Guice;
import com.google.inject.Injector;

import hessim.msgprocessor.IMessageProcessor;
import hessim.msgprocessor.MessageProcessor;
import hessim.msgprocessor.MessageProcessorAsync;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;  

public class Startup {
	public static void main(String[] args)
	{
		try {
			MyLogger.setup();
			Injector injector = Guice.createInjector(new BindingsModule());
			MessageProcessorAsync msgproc = injector.getInstance(MessageProcessorAsync.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		System.out.println("ende");
	}
}
