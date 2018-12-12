package se.kb222vt.logic;

import java.util.HashMap;

import se.kb222vt.entity.Page;
/*
 * Algorithm inspiration from: http://coursepress.lnu.se/kurs/web-intelligence/files/2018/10/4.-Search-Engines.pdf page 42
 */
public class PageRankLogic {
	
	/**
	 * Calculate the page rank for pages
	 * @param pages the pages to be ranked
	 * @param iterations number of times to run page rank
	 */
	public static void calculatePageRank(HashMap<String, Page> pages, int iterations) {
		System.out.println("Calculate pagerank on " + pages.size() + " pages for " + iterations + " iterations");
		for (int i = 0; i < iterations; i++) {
			System.out.println("ranking pages: " + (i+1) + "/" + iterations);
			for(Page p : pages.values()) {
				pageRank(p, pages);
			}
		}
		normalizePageRank(pages);
	}
	
	/**
	 * Calculate the page rank for a page
	 * @param p the page to rank
	 * @param pages the pages to determine the page rank according to
	 */
	public static void pageRank(Page p, HashMap<String, Page> pages) {
		double pr = 0;
		for(Page po : pages.values()){
			if(po.hasLinkTo(p))
				pr += po.getPageRankScore() / po.getOutgoingLinkSize();
		}
		p.setPageRankScore(0.85 * pr + 0.15);
	}
	
	/**
	 * Normalize the page rank score for pages. Makes the score go between 1 and 0, 1 for the best score and 0 for the worst score.
	 * @param pages
	 */
	public static void normalizePageRank(HashMap<String, Page> pages) {
		double maxPageRank = 0;
		for(Page p : pages.values()) {
			if(p.getPageRankScore() > maxPageRank)
				maxPageRank = p.getPageRankScore();
		}
		for(Page p : pages.values()) {
			p.setPageRankScore(p.getPageRankScore() / maxPageRank);
		}
	}
}