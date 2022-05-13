// https://www.adesso.de/de/news/blog/dependency-injection-mit-google-guice.jsp
// https://www.tutorialspoint.com/guice/guice_provides_annotation.htm
// https://www.baeldung.com/guice
package hessim.startup;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import hessim.config.ConfigReader;
import hessim.config.Config;


//Binding Module
class BindingsModule extends AbstractModule {

	@Override
    protected void configure() {
      //bind(IConfig.class).to(Config.class);
    } 
	
	@Provides 
	@Singleton
	public Config provideConfig() {
		ConfigReader reader = new ConfigReader();
		Config cfg = reader.readFromFile("");
	   return cfg;
	}
}
