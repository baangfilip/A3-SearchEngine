package se.kb222vt.logic;

public class WordUtils {
	
	/**
	 * Returns an array of words, splits content by white space
	 * @param content
	 * @return
	 */
	public static String[] getArrayOfWords(String content) {
        content = content.replaceAll("\\s+", ",");
		return content.split(",");
	}
	
}