import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.kb222vt.logic.WordUtils;


public class WordUtilsTest {
	
	@Test
	public void testGetArrayOfWords() {
		String query = "software testing";
		String[] wordArr = WordUtils.getArrayOfWords(query);
		assertEquals("First word", "software", wordArr[0]);
		assertEquals("Second word", "testing", wordArr[1]);
	}
	
}
