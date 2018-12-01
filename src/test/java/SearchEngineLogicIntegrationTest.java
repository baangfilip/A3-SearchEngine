import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import se.kb222vt.logic.SearchEngineLogic;


public class SearchEngineLogicIntegrationTest {
	private static SearchEngineLogic logic;
	private static HashMap<String, Integer> wordMap = new HashMap<>();//<String representation of a word, Id for word>
	private static String[] words = new String[]{"hello", "goodbye", "salut", "hej", "tja", "something", "hejdå", "tjena", "tjenare"};
	
	@Before
	public void setup() {
		wordMap.clear();
		for(int i = 0; i < words.length; i++) {
			int wordID = wordMap.size();
			wordMap.put(words[i], wordID);
		}
		logic = new SearchEngineLogic();
	}
	
	public String getRandomWord() {
		Random rand = new Random();
		return words[rand.nextInt(words.length)];
	}
	
	@Test
	public void testSearch() {
		logic.search("tjena hur mår du hejdå");
		assertEquals("Search test", true, false);
	}
}
