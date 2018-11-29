package se.kb222vt.app;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.port;

public class Initalize {
	//Initalize from Eclipse
    public static void main(String[] args) {
		externalStaticFileLocation("src/main/webapp");
		port(8080);
    	Application app = new Application();
    	app.addDataFolder("Games", "src/main/resources/data/wikipedia/Words/Games");
    	app.addDataFolder("Programming", "src/main/resources/data/wikipedia/Words/Programming");
    	app.init();
    }
}