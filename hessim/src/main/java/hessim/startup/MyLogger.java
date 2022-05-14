// https://www.vogella.com/tutorials/Logging/article.html
package hessim.startup;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

    static public void setup() throws IOException {
    	
    	System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s [%1$tc]%n");
    	
    	final ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINEST);
        consoleHandler.setFormatter(new SimpleFormatter());

        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);
        
    }
}