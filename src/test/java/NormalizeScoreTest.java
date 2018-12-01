import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import se.kb222vt.entity.Page;
import se.kb222vt.logic.SearchEngineLogic;


public class NormalizeScoreTest {

	private static SearchEngineLogic logic = new SearchEngineLogic();
	
	@Test
	public void testNormalizeWordRankingsZeroValues() {
		ArrayList<Page> pages = new ArrayList<>();
		
		Page p1 = new Page("Thirdplace");
		p1.setWordFrequencyScore(10); //lower is bad
		p1.setWordLocationScore(2); //higher is bad
		pages.add(p1);
		

		Page p2 = new Page("Secondplace");
		p2.setWordFrequencyScore(8); //lower is bad
		p2.setWordLocationScore(1); //higher is bad
		pages.add(p2);
		
		//this shouldn't happen, since pages with 0 score shouldn't be in the result list
		Page p3 = new Page("Firstplace");
		p3.setWordFrequencyScore(0); //lower is bad
		p3.setWordLocationScore(0); //higher is bad
		pages.add(p3);

		
		double minLocationScore = 0;
		double maxFrequencyScore = 10;
		
		logic.normalizeWordRankingScores(pages);
		for(Page p : pages) {
			if(p.equals(p1)) {
				assertEquals("p1 shouldnt have word location score", minLocationScore/2, p.getWordLocationScore(), 0.00001);
				assertEquals("p1 shouldnt have word frequency score", 10/maxFrequencyScore, p.getWordFrequencyScore(), 0.00001);
			}else if(p.equals(p2)) {
				assertEquals("p2 shouldnt have word location score", minLocationScore/1, p.getWordLocationScore(), 0.00001);
				assertEquals("p2 shouldnt have word frequency score", 8/maxFrequencyScore, p.getWordFrequencyScore(), 0.00001);
			}else if(p.equals(p3)) {
				assertEquals("p3 shouldnt have word location score", minLocationScore/logic.DIVISION_BY_ZERO_SAFETY, p.getWordLocationScore(), 0.00001);
				assertEquals("p3 shouldnt have word frequency score", 0/maxFrequencyScore, p.getWordFrequencyScore(), 0.00001);
			}
		}
	}
	
	@Test
	public void testNormalizeWordRankings() {
		ArrayList<Page> pages = new ArrayList<>();
		
		Page p1 = new Page("Thirdplace");
		p1.setWordFrequencyScore(10); //lower is bad
		p1.setWordLocationScore(2); //higher is bad
		pages.add(p1);

		Page p2 = new Page("Secondplace");
		p2.setWordFrequencyScore(8); //lower is bad
		p2.setWordLocationScore(1); //higher is bad
		pages.add(p2);
		
		Page p3 = new Page("Firstplace");
		p3.setWordFrequencyScore(20); //lower is bad
		p3.setWordLocationScore(5); //higher is bad
		pages.add(p3);

		
		double minLocationScore = 1;
		double maxFrequencyScore = 20;
		
		logic.normalizeWordRankingScores(pages);
		for(Page p : pages) {
			if(p.equals(p1)) {
				assertEquals("p1 shouldnt have word location score", minLocationScore/2, p.getWordLocationScore(), 0.00001); //smallerIsBetter 	minLocationScore / score 
				assertEquals("p1 shouldnt have word frequency score", 10/maxFrequencyScore, p.getWordFrequencyScore(), 0.00001); //higherIsBetter 	score / maxFrequencyScore 
			}else if(p.equals(p2)) {
				assertEquals("p2 shouldnt have word location score", minLocationScore/1, p.getWordLocationScore(), 0.00001);
				assertEquals("p2 shouldnt have word frequency score", 8/maxFrequencyScore, p.getWordFrequencyScore(), 0.00001);
			}else if(p.equals(p3)) {
				assertEquals("p3 shouldnt have word location score", minLocationScore/5, p.getWordLocationScore(), 0.00001);
				assertEquals("p3 shouldnt have word frequency score", 20/maxFrequencyScore, p.getWordFrequencyScore(), 0.00001);
			}
		}
	}
}
