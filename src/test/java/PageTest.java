import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import se.kb222vt.entity.Page;


public class PageTest {
	
	@Test
	public void testGetFirstIndexForWord() {
		Page page = new Page("test page");
		page.addWord(1);
		page.addWord(2);
		page.addWord(1);
		assertEquals("index of wordID 1 should be 0", 0, page.getFirstIndexForWord(1));
		assertEquals("index of wordID 1 should be 0", 1, page.getFirstIndexForWord(2));
	}
	
	@Test
	public void testGetInstancesOfWordIDs() {
		Page page = new Page("test page");
		page.addWord(1);
		page.addWord(2);
		page.addWord(1);
		ArrayList<Integer> words = new ArrayList<>();
		words.add(1);
		assertEquals("instances of wordID should be 2", 2, page.getInstancesOfWordIDs(words));
		words.add(2);
		assertEquals("instances of mulitple wordID should be 3", 3, page.getInstancesOfWordIDs(words));
		words.clear();
		assertEquals("instances of no wordIDs should be 0", 0, page.getInstancesOfWordIDs(words));
		words.add(5);
		assertEquals("instances of wordIDs thats not in page should be 0", 0, page.getInstancesOfWordIDs(words));
	}
	
	@Test
	public void testSumIndexesOfWord() {
		Page page = new Page("test page");
		page.addWord(1); //0
		page.addWord(2);
		page.addWord(1); //2
		page.addWord(2);
		page.addWord(2);
		page.addWord(2);
		assertEquals("sum of indexes", 2, page.sumIndexesOfWord(1));
		assertEquals("sum of indexes", 13, page.sumIndexesOfWord(2));
		assertEquals("sum of indexes", -1, page.sumIndexesOfWord(3));
	}
	
}
