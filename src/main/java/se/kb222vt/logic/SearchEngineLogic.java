package se.kb222vt.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import se.kb222vt.app.Application;
import se.kb222vt.entity.Page;

public class SearchEngineLogic {
	public static double DIVISION_BY_ZERO_SAFETY = 0.00001;
	public static int INDEX_NOT_FOUND_SCORE = 100000;

	
	/**
	 * Search for articles with query.
	 * Right now uses location of query words in articles and instances of words in articles to determine score
	 * @param query
	 * @return List of maximum 5 pages with scores
	 */
	public List<Page> search(String query){
		//Get word ranking scores
		ArrayList<Integer> wordIDs = getWordIDsForQuery(query, Application.getWordMap());
		ArrayList<Page> result = wordRanking(wordIDs, Application.getArticles());
		normalizeWordRankingScores(result);
		
		//weigh the different scores together to a finalScore
		summarizeScores(result);
		Collections.sort(result, Page.getArticleByScore());
		int maxResult = result.size() > 4 ? 5 : result.size();
		return result.subList(0, maxResult);
	}
	
	/**
	 * Summarize Page-objects different ranking scores to one score
	 * @param pages
	 */
	public void summarizeScores(ArrayList<Page> pages) {
		//weigh the different scores together to a finalScore
		for(Page p : pages) {
			/*p.setScore(p.getWordFrequencyScore() + 0.8 * p.getWordLocationScore()); //whitout pageRank
			
			//score = 1.0 * WordFrequency + 1.0 * PageRank + 0.5 * DocumentLocation
			p.setScore((1.0 * p.getWordFrequencyScore()) + (1.0 * p.getPageRankScore()) + (0.5 * p.getWordLocationScore())); //with pageRank FROM slides 
			*/
			//score = word_frequency + 0.8 * document_location + 0.5 * pagerank
			p.setScore(p.getWordFrequencyScore() + 0.8 * p.getWordLocationScore() +  0.5 * p.getPageRankScore()); 
		}
	}
	
	
	/**
	 * Look through articles and find the ones with with wordIDs in them, if a wordIDs are found in article we will find get the location score for the article aswell
	 * @param query wordIDs for corresponding to the words to find in articles
	 * @param articles list of articles to search for query in
	 * @return list of blogs with the word from param query in them
	 */
	public ArrayList<Page> wordRanking(ArrayList<Integer> wordIDs, HashMap<String, Page> articles) {
		//THOUGT: use treemap, to avoid sorting the list later? naah, really need to combine result later and then sort it anyways
		ArrayList<Page> result = new ArrayList<Page>();
		//convert query to hashCode
		//lets see which article has this word
		for(Page p : articles.values()) {
			int instanceOfWord = p.getInstancesOfWordIDs(wordIDs);
			if(instanceOfWord > 0) {
				Page hitPage = new Page(p);
				hitPage.setWordFrequencyScore(instanceOfWord);
				result.add(hitPage);
				//if we had instance of words, check for indexes aswell
				int wordLocationScore = 0;
				for(Integer wordID : wordIDs) {
					int index = p.sumIndexesOfWord(wordID);
					wordLocationScore += (index > -1 ? index : INDEX_NOT_FOUND_SCORE);
				}
				hitPage.setWordLocationScore(wordLocationScore);
			}
		}
		return result;
	}
	
	/**
	 * Get the id for a word. 
	 * @param word
	 * @return id for word or -1 if the word doesn't exist 
	 */
	public int getIdForWord(String word, HashMap<String, Integer> wordMap) {
		if(wordMap.get(word) != null)
			return wordMap.get(word);
		else
			return -1;
	}
	
	/**
	 * Normalize the word ranking scores (wordLocationScore and wordFrequencyScore)
	 * @param pages list of the articles to normalize the score on
	 */
	public void normalizeWordRankingScores(ArrayList<Page> pages) {
		//Reasoning for code here: this prevents one extra iterations of the pages and no need to repeat normalize function for different score attributes.
		double minLocScore = Double.MAX_VALUE, minInstScore = Double.MAX_VALUE, maxLocScore = 0, maxInstScore = 0;
		//get the min and max scores for word rankings for the pages
		for(Page p : pages) {
			if(p.getWordLocationScore() > maxLocScore)
				maxLocScore = p.getWordLocationScore();

			if(p.getWordFrequencyScore() > maxInstScore)
				maxInstScore = p.getWordFrequencyScore();

			if(p.getWordLocationScore() < minLocScore)
				minLocScore = p.getWordLocationScore();

			if(p.getWordFrequencyScore() < minInstScore)
				minInstScore = p.getWordFrequencyScore();
		}
		for(Page p : pages) {
			double normalizedLocationScore = normalizeScore(p.getWordLocationScore(), false, minLocScore, maxLocScore);
			p.setWordLocationScore(normalizedLocationScore);
			double normalizedFrequencyScore = normalizeScore(p.getWordFrequencyScore(), true, minInstScore, maxInstScore);
			p.setWordFrequencyScore(normalizedFrequencyScore);
		}
	}
	
	/**
	 * Normalize score to always have higher is better and be between 0 and 1 
	 * Algorithm inspiration from: http://coursepress.lnu.se/kurs/web-intelligence/files/2018/10/4.-Search-Engines.pdf page 19
	 * @param score score to normalize
	 * @param biggerBetter is the score before normalized better if its bigger
	 * @param min used if smallerIsBetter 
	 * @param max used if biggerIsBetter
	 * @return 
	 */
	public double normalizeScore(double score, boolean biggerBetter, double min, double max) {
		if(biggerBetter) {			
			//divide score by maxscore to get scores between 0 and 1
			return (score / max);
		}else {
			//smaller score is better, normalize this to be the range of: 1 for best score and 0 for worst instead 
			return (min / (score == 0 ? DIVISION_BY_ZERO_SAFETY : score));
		}
	}
	
	/**
	 * Get the wordID's for the words in the query
	 * If the words arent in the wordMap the words wont be represented in the resulting list.
	 * Will ignore duplicate words
	 * @param query
	 * @param wordMap
	 * @return a list with wordIDs
	 */
	public ArrayList<Integer> getWordIDsForQuery(String query, HashMap<String, Integer> wordMap) {
		ArrayList<Integer> wordIDs = new ArrayList<>();
		String[] words = WordUtils.getArrayOfWords(query.toLowerCase());
		for(int i = 0; i < words.length; i++) {
			int wordID = getIdForWord(words[i], wordMap);
			if(wordID > -1 && !wordIDs.contains(wordID))
				wordIDs.add(wordID);
		}
		return wordIDs;
	}
}