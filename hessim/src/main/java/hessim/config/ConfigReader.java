// https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/#4-read-json-from-a-file
// https://www.tutorialspoint.com/gson/gson_first_application.htm
package hessim.config;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;  
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class ConfigReader {

	public Config readFromFile(String filename)
	{
		Path filePath = Path.of("C:\\projects\\stmk\\EN_ATM4\\sagemcomsim\\testdata\\config.json");
		String content;
		try {
			content = Files.readString(filePath, StandardCharsets.US_ASCII);
			GsonBuilder builder = new GsonBuilder(); 
		    builder.setPrettyPrinting(); 
		    Gson gson = builder.create();
			Config config  = gson.fromJson(content, Config.class); 
			return config;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}