package main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Scraper{
	private String url;
	public Scraper(String url)
	{
		this.url = url;
        
	}

	public String Scrape()
	{
		String text = "";
		try {
            Document doc = Jsoup.connect(url).get();
            Elements paragraphs = doc.select("div.article__content p");

            for (Element paragraph : paragraphs){
				text += paragraph.text() + "\n\n";
            }
        } 
		catch (Exception e){
            e.printStackTrace();
        }
		return text;
	}
}