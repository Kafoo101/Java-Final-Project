package main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.HttpStatusException;
import java.io.IOException;


public class Scraper{
	private String url;
	public Scraper(String url)
	{
		this.url = url;
        
	}

	public String Scrape() throws IOException
	{
		try {
            Document doc = Jsoup.connect(url).get();
            String title = doc.title();
            if (title.endsWith("| CNN")) {
                title = title.substring(0, title.length() - "| CNN".length());
            }

            Elements paragraphs = doc.select("div.article__content p");
            StringBuilder text = new StringBuilder();
            text.append(title).append("\n\n");
            for (Element paragraph : paragraphs) {
                text.append(paragraph.text()).append("\n\n");
            }
            return text.toString();
        } catch (IllegalArgumentException e) {
            System.err.println("URL Format Unrecognized");
            return "not an url [ERROR]"; 
        } catch (HttpStatusException e) {
            System.err.println("HTTP error fetching URL: " + e.getStatusCode() + ", URL: " + e.getUrl());
            return "url not found [ERROR]"; 
        }
	}
}