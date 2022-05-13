package hessim.startup;

import java.io.IOException;

import hessim.config.Config;
import hessim.config.MessageQueue;
import hessim.config.MessageType;

import com.google.inject.Guice;
import com.google.inject.Injector;

import hessim.msgprocessor.IMessageProcessor;
import hessim.msgprocessor.MessageProcessor;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;  

public class Startup {
	public static void main(String[] args)
	{
		try {
			MyLogger.setup();
			Injector injector = Guice.createInjector(new BindingsModule());
			IMessageProcessor msgproc = injector.getInstance(MessageProcessor.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		System.out.println("ende");
	}
}
