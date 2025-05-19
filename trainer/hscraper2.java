package main.trainer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class hscraper2 {
    private String url;

    public hscraper2(String url) {
        this.url = url;
    }

    public String scrape() {
        StringBuilder text = new StringBuilder();
        try {
            Document doc = Jsoup.connect(url).get();

            Elements paragraphs = doc.select("article p");

            for (Element paragraph : paragraphs) {
                String paraText = paragraph.text().trim();

                // === Junk filters ===
                // Too short
                if (paraText.length() < 40) continue;

                // Contains marketing or legal disclaimers
                if (paraText.toLowerCase().contains("by entering your email")) continue;
                if (paraText.toLowerCase().contains("terms of service")) continue;
                if (paraText.toLowerCase().contains("support our mission")) continue;
                if (paraText.toLowerCase().contains("already contributed")) continue;
                if (paraText.toLowerCase().contains("subscribe") || paraText.toLowerCase().contains("sign up")) continue;
                if (paraText.toLowerCase().contains("privacy policy")) continue;

                // Otherwise include the paragraph
                text.append(paraText).append("\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text.toString();
    }
}
