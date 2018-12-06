import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import se.kb222vt.entity.Page;
import se.kb222vt.logic.SearchEngineLogic;


public class SearchEngineLogicTest {
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
	public void testWordFrequency() {
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
	public void testWordLocation() {
		HashMap<String, Page> pages = new HashMap<>();

		String word1 = "findthisword";
		String word2 = "findanotherword";
		wordMap.put(word1, wordMap.size());
		wordMap.put(word2, wordMap.size());
		ArrayList<Integer> query = new ArrayList<>();
		query.add(wordMap.get(word1));
		query.add(wordMap.get(word2));
		
		int p1ExpectedScore = 12; 
		/*
		 * the query has two words, both will be found for page p1
		 * p1ExpectedScore should then be index-location for both words
		 * index-location for word2 is = 4
		 * index-location for word1 is = 8 
		 * p1ExpectedScore = 12
		 */
		Page p1 = new Page("Article1");
		p1.addWord(wordMap.get(getRandomWord())); //1 (index for this logic starts at 1)
		p1.addWord(wordMap.get(getRandomWord()));
		p1.addWord(wordMap.get(getRandomWord()));
		p1.addWord(wordMap.get(word2));
		p1.addWord(wordMap.get(word2));
		p1.addWord(wordMap.get(getRandomWord()));
		p1.addWord(wordMap.get(getRandomWord()));
		p1.addWord(wordMap.get(word1));
		p1.addWord(wordMap.get(word2));
		pages.put(p1.getTitle(), p1);

		int p2ExpectedScore = logic.INDEX_NOT_FOUND_SCORE + 3; 
		/*
		 * the query has two words, only one will be found for page p2
		 * p2ExpectedScore should then be index-location for word1 + logic.indexNotFoundScore
		 * index-location for word2 is = 4
		 * p2ExpectedScore = 10
		 */
		Page p2 = new Page("Article2");
		p2.addWord(wordMap.get(getRandomWord())); //1 (index for this logic starts at 1)
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
				assertEquals("p1 should location", p1ExpectedScore, p.getWordLocationScore(), 0.00001);
				pagesTested++;
			}else if(p.getTitle().equals(p2.getTitle())) {
				assertEquals("p2 should location", p2ExpectedScore, p.getWordLocationScore(), 0.00001);
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

		query = "GOODbye salute då TJENA goodbye";
		wordIDs = logic.getWordIDsForQuery(query, wordMap);

		assertEquals("wordIDs should contain 2 ids", 2, wordIDs.size());
		assertEquals("wordIDs should contain id 1 for goodbye", true, wordIDs.contains(1));
		assertEquals("wordIDs should contain id 7 for tjena", true, wordIDs.contains(7));
	}
	
	@Test
	public void testNormalizeScoreBiggerBetter() {
		double score = 10;
		double min = 394569589; //doesnt matter
		double max = 20;
		double normalizedScore = logic.normalizeScore(score, true, min, max);
		assertEquals("The normalized score bigger is better", 0.5, normalizedScore, 0.000001);
	}
	
	@Test
	public void testNormalizeScoreSmallerBetter() {
		double score = 10;
		double min = 1; //doesnt matter
		double max = 394569589;
		double normalizedScore = logic.normalizeScore(score, false, min, max);
		assertEquals("The normalized score bigger is better", 0.1, normalizedScore, 0.000001);
	}
	
	@Test
	public void testSummarizeScores() {
		ArrayList<Page> pages = new ArrayList<>();
		
		Page p1 = new Page("Thirdplace");
		p1.setWordFrequencyScore(2); //lower is bad
		p1.setWordLocationScore(10); //higher is bad
		pages.add(p1);

		Page p2 = new Page("Secondplace");
		p2.setWordFrequencyScore(3); //lower is bad
		p2.setWordLocationScore(5); //higher is bad
		pages.add(p2);
		
		Page p3 = new Page("Firstplace");
		p3.setWordFrequencyScore(5); //lower is bad
		p3.setWordLocationScore(2); //higher is bad
		pages.add(p3);
		
		double p1ExpectedScore = p1.getWordFrequencyScore() + (0.8 * p1.getWordLocationScore()) + (0.5 * p1.getPageRankScore());
		double p2ExpectedScore = p2.getWordFrequencyScore() + (0.8 * p2.getWordLocationScore()) + (0.5 * p1.getPageRankScore());
		double p3ExpectedScore = p3.getWordFrequencyScore() + (0.8 * p3.getWordLocationScore()) + (0.5 * p1.getPageRankScore());
		
		logic.summarizeScores(pages);
		int pagesTested = 0;
		for(Page p : pages) {
			if(p.equals(p1)) {
				assertEquals("p1 score", p1ExpectedScore, p.getScore(), 0.00001);
				pagesTested++;
			}else if(p.equals(p2)) {
				assertEquals("p2 score", p2ExpectedScore, p.getScore(), 0.00001);
				pagesTested++;
			}else if(p.equals(p3)) {
				assertEquals("p3 score", p3ExpectedScore, p.getScore(), 0.00001);
				pagesTested++;
			}
		}
		assertEquals("We should get back all 3 pages with results", 3, pagesTested);
	}
	
}
