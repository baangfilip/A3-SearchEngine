import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import se.kb222vt.entity.Page;
import se.kb222vt.logic.SearchEngineLogic;


public class SearchEngineLogicTest {
	private static SearchEngineLogic logic = new SearchEngineLogic();
	private static HashMap<String, Integer> wordMap = new HashMap<>();//<String representation of a word, Id for word>
	private static String[] words = new String[]{"hello", "goodbye", "salut", "hej", "tja", "something", "hejdå", "tjena", "tjenare"};
	
	@Before
	public void setup() {
		wordMap.clear();
		for(int i = 0; i < words.length; i++) {
			int wordID = wordMap.size();
			wordMap.put(words[i], wordID);
		}
	}
	
	public String getRandomWord() {
		Random rand = new Random();
		return words[rand.nextInt(words.length)];
	}
	
	@Test
	public void testWordFrequency() {
		SearchEngineLogic logic = new SearchEngineLogic();
		HashMap<String, Page> pages = new HashMap<>();
		
		String word1 = "findthisword";
		String word2 = "findanotherword";
		wordMap.put(word1, wordMap.size());
		wordMap.put(word2, wordMap.size());
		ArrayList<Integer> query = new ArrayList<>();
		query.add(wordMap.get(word1));
		query.add(wordMap.get(word2));
		
		Page p1 = new Page("Article1");
		p1.addWord(wordMap.get(getRandomWord()));
		p1.addWord(wordMap.get(getRandomWord()));
		p1.addWord(wordMap.get(getRandomWord()));
		p1.addWord(wordMap.get(word2));
		p1.addWord(wordMap.get(word2));
		p1.addWord(wordMap.get(getRandomWord()));
		p1.addWord(wordMap.get(getRandomWord()));
		p1.addWord(wordMap.get(word1));
		p1.addWord(wordMap.get(word2));
		pages.put(p1.getTitle(), p1);

		Page p2 = new Page("Article2");
		p2.addWord(wordMap.get(getRandomWord()));
		p2.addWord(wordMap.get(getRandomWord()));
		p2.addWord(wordMap.get(getRandomWord()));
		p2.addWord(wordMap.get(word1));
		p2.addWord(wordMap.get(getRandomWord()));
		p2.addWord(wordMap.get(getRandomWord()));
		pages.put(p2.getTitle(), p2);

		Page p3 = new Page("Article3");
		p3.addWord(wordMap.get(getRandomWord()));
		p3.addWord(wordMap.get(getRandomWord()));
		p3.addWord(wordMap.get(getRandomWord()));
		p3.addWord(wordMap.get(getRandomWord()));
		p3.addWord(wordMap.get(getRandomWord()));
		pages.put(p3.getTitle(), p3);


		ArrayList<Page> pagesWithScore = logic.wordRanking(query, pages);
		int pagesTested = 0;
		for(Page p : pagesWithScore) {
			if(p.getTitle().equals(p1.getTitle())) {
				assertEquals("p1 should have found word nbr of times", 4, p.getWordFrequencyScore(), 0.00001);
				pagesTested++;
			}else if(p.getTitle().equals(p2.getTitle())) {
				assertEquals("p2 should have found word nbr of times", 1, p.getWordFrequencyScore(), 0.00001);
				pagesTested++;
			}
		}
		assertEquals("We should only get back pages with results", 2, pagesTested);
		
	}
	
	@Test
	public void getWordIDsForQuery() {
		String query = "goodbye salute då tjena";
		ArrayList<Integer> wordIDs = logic.getWordIDsForQuery(query, wordMap);
		assertEquals("wordIDs should contain 2 ids", 2, wordIDs.size());
		assertEquals("wordIDs should contain id 1 for goodbye", true, wordIDs.contains(1));
		assertEquals("wordIDs should contain id 7 for tjena", true, wordIDs.contains(7));
	}
	
}
