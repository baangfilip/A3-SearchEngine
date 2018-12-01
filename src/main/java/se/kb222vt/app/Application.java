package se.kb222vt.app;

import static spark.Spark.exception;
import static spark.Spark.get;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.gson.Gson;

import se.kb222vt.endpoint.SearchController;
import se.kb222vt.entity.Page;
import se.kb222vt.logic.WordUtils;
import spark.servlet.SparkApplication;

//Start Application by web.xml
public class Application implements SparkApplication {
	//putting some logic here since it will be so much overhead to put it somewhere else
	
	private Gson gson = new Gson();
	private HashMap<String, String> dataFolders = new HashMap<>();
	public static HashMap<String, Page> articles = new HashMap<String, Page>();//<Title, Page>
	
	//we need this to translate a hashCode -> word
	public static HashMap<String, Integer> wordMap = new HashMap<>();//<String representation of a word, Id for word>
	
	@Override
	public void init() {
		System.out.println("Start endpoints");
		exception(IllegalArgumentException.class, (e, req, res) -> {
		  res.status(404);
		  res.body(gson.toJson(e));
		});
        get("/API/article/search/", SearchController.articleSearch);
        get("/API/article/", SearchController.articles);
        
        //add data folders if we didn't start from Initalize.java
        if(dataFolders.size() < 1) {
        	dataFolders.put("Games", "webapps/searchengine/WEB-INF/classes/data/wikipedia/Words/Games");
        	dataFolders.put("Programming", "webapps/searchengine/WEB-INF/classes/data/wikipedia/Words/Programming");
        }
        
        try {
			getArticlesFromDataFolders();
		} catch (IOException e1) {
			// TODO FIX THIS
			e1.printStackTrace();
		}
		System.out.println("Found: " + articles.size() + " pages");
		System.out.println("Found: " + wordMap.size() + " different words");
	}
	
	private void getArticlesFromDataFolders() throws IOException {
		for(String folderPath : dataFolders.values()) {
			//read all files in folder and send to readData
			File folder = new File(folderPath);
			File[] listOfFiles = folder.listFiles();
			for(int i = 0; i < listOfFiles.length; i++) {
				String articleName = listOfFiles[i].getName();
				articles.put(articleName, getArticle(listOfFiles[i].getPath(), articleName));
			}
		}
	}
	
	private Page getArticle(String path, String articleName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path).toAbsolutePath()));
        //for the content, create a page and fix all the words
        //so split the content and do whats needs to be done for the words
        Page article = new Page(articleName);
		String[] wordsArr = WordUtils.getArrayOfWords(content);
		for(int i = 0; i < wordsArr.length; i++){
			String word = wordsArr[i].toLowerCase();
			int wordID = wordMap.size();
			
			if(wordMap.containsKey(word)) {
				//wordMap contains word since before, get the id for that word
				wordID = wordMap.get(word);
			}else {
				//wordMap doesn't contain word since before, insert it and set ID for the word to wordMap.size();
				wordMap.put(word, wordID);
			}
			
			//add word to article
			article.addWord(wordID);
		}
		return article; 
	}
	
	public void addDataFolder(String title, String path) {
        dataFolders.put(title, path);
	}
	
	public static HashMap<String, Page> getArticles(){
		return articles;
	}
	
	public static HashMap<String, Integer> getWordMap(){
		return wordMap;
	}
}