package se.kb222vt.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Page {
	private static final String prefixUrl = "https://en.wikipedia.org";
	private static final String prefixLink = "/wiki/";
	private String title;
	private double score;
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
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
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
	
	public int getInstancesOfWord(int wordID) {
		return Collections.frequency(words, wordID);
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
