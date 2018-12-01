package se.kb222vt.endpoint;


import com.google.gson.Gson;

import se.kb222vt.app.Application;
import se.kb222vt.logic.SearchEngineLogic;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchController {
	private static Gson gson = new Gson();
	private static SearchEngineLogic logic = new SearchEngineLogic();
	
	public static Route articleSearch = (Request request, Response response) -> {
		String query = request.queryParams("query");
		if(query == null)
    		throw new IllegalArgumentException("Missing query param");
    	return gson.toJson(logic.search(query));
	};
    
    public static Route articles = (Request request, Response response) -> {
    	String pageTitle = request.queryParams("title");
    	if(pageTitle == null || pageTitle.isEmpty()) {
        	return gson.toJson(Application.articles);
    	}else {
    		if(Application.articles.containsKey(pageTitle)) {
    			return gson.toJson(Application.articles.get(pageTitle));
    		}else {
    			throw new IllegalArgumentException("Could not find entry for articles: " + pageTitle);
    		}
    	}

    };      
}