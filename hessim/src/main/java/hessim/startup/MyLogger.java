// https://www.vogella.com/tutorials/Logging/article.html
package hessim.startup;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger {

    static public void setup() throws IOException {

        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.INFO);
    }
}