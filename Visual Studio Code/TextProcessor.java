package main;
public class TextProcessor {
    public static String cleanText(String unprocessedText, boolean fullProcessed) {
        if (unprocessedText == null || unprocessedText.isEmpty()) {
            return "";
        }

		unprocessedText = unprocessedText.replace("“", "\""); 
        unprocessedText = unprocessedText.replace("”", "\""); 
        unprocessedText = unprocessedText.replace("‘", "'");  
        unprocessedText = unprocessedText.replace("’", "'");  
        unprocessedText = unprocessedText.replace("—", "--"); 
		unprocessedText = unprocessedText.replace("…", "...");
		unprocessedText = unprocessedText.replace("é", "e");
		unprocessedText = unprocessedText.replace("ó", "o");
		unprocessedText = unprocessedText.replace("–", "-");

		if (fullProcessed)
		{
			unprocessedText = unprocessedText.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "");
			return unprocessedText.toLowerCase().trim();
		}
		
		
        return unprocessedText.trim();
    }
}
