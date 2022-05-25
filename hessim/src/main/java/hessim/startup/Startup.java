// Add this to VM Arguments
// --add-opens java.base/java.lang=ALL-UNNAMED
// start as: java -Dconfig.path="C:/projects/stmk/EN_ATM4/sagemcomsim/testdata/config.json" --add-opens java.base/java.lang=ALL-UNNAMED -cp hessim-0.0.1-SNAPSHOT-jar-with-dependencies.jar hessim.startup.Startup
package hessim.startup;

import java.io.IOException;
import com.google.inject.Guice;
import com.google.inject.Injector;

import hessim.exception.ConfigException;
import hessim.msgprocessor.MessageProcessorAsync;  

public class Startup {
	public static void main(String[] args)
	{
		try {
			MyLogger.setup();
			Injector injector = Guice.createInjector(new BindingsModule());
			MessageProcessorAsync msgproc = injector.getInstance(MessageProcessorAsync.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("ende");
	}
}
