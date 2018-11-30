import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import se.kb222vt.entity.Page;
import se.kb222vt.logic.SearchEngineLogic;


public class NormalizeScoreTest {

	private static SearchEngineLogic logic = new SearchEngineLogic();
	
	@Test
	public void testNormalizeBiggerBetter() {
		ArrayList<Page> pages = new ArrayList<>();
		Page p1 = new Page("Firstplace");
		p1.setScore(10);
		pages.add(p1);
		
		Page p3 = new Page("Thirdplace");
		p3.setScore(2);
		pages.add(p3);

		Page p2 = new Page("Secondplace");
		p2.setScore(8);
		pages.add(p2);
		logic.normalizeScore(pages, true);
		
		assertEquals("p1 should have score 1", 1, p1.getScore(), 0.00001);
		assertEquals("p2 should have score 0.8", 0.8, p2.getScore(), 0.00001);
		assertEquals("p3 should have score 0.2", 0.2, p3.getScore(), 0.00001);
	}
	
	@Test
	public void testNormalizeSmallerBetter() {
		ArrayList<Page> pages = new ArrayList<>();
		
		Page p1 = new Page("Thirdplace");
		p1.setScore(10); //higher is bad
		pages.add(p1);
		
		Page p3 = new Page("Firstplace");
		p3.setScore(2); //low is god
		pages.add(p3);

		Page p2 = new Page("Secondplace");
		p2.setScore(8); //high is bad
		pages.add(p2);
		logic.normalizeScore(pages, false);
		
		assertEquals("p1 should have worst score", 0.2, p1.getScore(), 0.00001);
		assertEquals("p2 should have middle score", 0.25, p2.getScore(), 0.00001);
		assertEquals("p3 should have best score", 1, p3.getScore(), 0.00001);
	}
	
	
}
