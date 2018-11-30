package se.kb222vt.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Page {
	private static final String prefixUrl = "https://en.wikipedia.org";
	private static final String prefixLink = "/wiki/";
	private String title;
	private double score;
	private double wordFrequencyScore;
	private double wordLocationScore;
	private double pageRankScore;
	
	private String url; //full url to wikipedia article
	private String link; //link for page rank other pages
	private ArrayList<Integer> words = new ArrayList<>();//<WordId's for words>
	
	/**
	 * Create a copy of the Page with only title and url
	 */
	public Page(Page p) {
		this.title = p.getTitle();
		this.url = prefixUrl + p.getLink();
		this.link = p.getLink();
	}
	
	public Page(String title) {
		this.title = title;
		this.link = prefixLink + title;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public double getWordFrequencyScore() {
		return wordFrequencyScore;
	}

	public void setWordFrequencyScore(double wordFrequencyScore) {
		this.wordFrequencyScore = wordFrequencyScore;
	}

	public double getWordLocationScore() {
		return wordLocationScore;
	}

	public void setWordLocationScore(double wordLocationScore) {
		this.wordLocationScore = wordLocationScore;
	}

	public double getPageRankScore() {
		return pageRankScore;
	}

	public void setPageRankScore(double pageRankScore) {
		this.pageRankScore = pageRankScore;
	}

	
	/**
	 * Add a word to this blog.
	 * Sensitive to ordering. Add the first words in a article first!
	 * @param word Word object
	 */
	public void addWord(int wordID) {
		words.add(wordID);
	}

	public String getUrl() {
		return url;
	}
	
	public int getInstancesOfWordIDs(ArrayList<Integer> wordIDs) {
		int hits = 0;
		for(Integer wordID : wordIDs) {
			hits += Collections.frequency(words, wordID);
		}
		return hits;
	}
	
	
	public static Comparator<Page> getArticleByScore(){   
		 Comparator<Page> comparator = new Comparator<Page>(){
			@Override
			public int compare(Page page1, Page page2) {
				return new Double(page2.getScore()).compareTo(new Double(page1.getScore()));
			}        
		 };
		 return comparator;
	}
		
	public int getFirstIndexForWord(int wordID) {
		return words.indexOf(wordID);
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
}
