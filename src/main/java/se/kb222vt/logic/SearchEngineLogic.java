package se.kb222vt.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import se.kb222vt.app.Application;
import se.kb222vt.entity.Page;

public class SearchEngineLogic {

	
	public boolean pageRank() {
		//TODO: implement page rank
		/*
		 Run the algorithm for 20 iterations
		 Results shall be ranked using:
		 score = word_frequency + 0.8 * document_location + 0.5 * pagerank
		 */
		return false;
	}
	
	public List<Page> search(String query){
		//which methods to use....
		ArrayList<Page> result = wordRanking(getWordIDsForQuery(query, Application.wordMap), Application.articles);
		
		//TODO: Normalize correct score
		normalizeScore(result, true);
		
		//weigh the different scores together to a finalScore
		for(Page p : result) {
			
		}
		Collections.sort(result, Page.getArticleByScore());
		int maxResult = result.size() > 4 ? 5 : result.size();
		return result.subList(0, maxResult);
	}
	
	
	/**
	 * Look through pages with the wordIDs appear in them and sets the score of the article to the number of times it appears
	 * @param query
	 * @param articles 
	 * @return list of blogs with the word from param query in them
	 */
	//------------------------- TODO ---------------------------------
	//TODO: also score by document location metric, I think that must be included in this algorithm, to not loop to many times...
	//TODO: word_frequency + 0.8 * document_location
	//------------------------- ---- ---------------------------------
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
			}
			int wordLocationScore = 0;
			for(Integer wordID : wordIDs) {
				int index = p.getFirstIndexForWord(wordID);
				int notFoundScore = 100000;
				wordLocationScore += (index > -1 ? index : notFoundScore);
			}
			p.setWordLocationScore(wordLocationScore);
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
	 * Normalize score for pages
	 * Algorithm inspiration from: http://coursepress.lnu.se/kurs/web-intelligence/files/2018/10/4.-Search-Engines.pdf page 19
	 * @param pages
	 * @param biggerBetter
	 */
	public void normalizeScore(ArrayList<Page> pages, boolean biggerBetter) {
		if(biggerBetter) {			
			double maxScore = 0;
			//get max score of the result
			for(Page p : pages) {
				if(p.getScore() > maxScore)
					maxScore = p.getScore();
			}
			//divide every score by maxscore to get scores between 0 and 1
			for(Page p : pages) {
				p.setScore(p.getScore() / maxScore);
			}
		}else {
			//smaller score is better, normalize this to be the range of: 1 for best score and 0 for worst instead 
			double minScore = Double.MAX_VALUE;
			for(Page p : pages) {
				if(p.getScore() < minScore)
					minScore = p.getScore();
			}
			for(Page p : pages) {		//avoid division by zero
				p.setScore(minScore / (p.getScore() == 0 ? 0.00001 : p.getScore()));
			}
		}
	}
	
	public ArrayList<Integer> getWordIDsForQuery(String query, HashMap<String, Integer> wordMap) {
		ArrayList<Integer> wordIDs = new ArrayList<>();
		String[] words = WordUtils.getArrayOfWords(query);
		for(int i = 0; i < words.length; i++) {
			if(getIdForWord(words[i], wordMap) > -1)
				wordIDs.add(getIdForWord(words[i], wordMap));
		}
		return wordIDs;
	}
}