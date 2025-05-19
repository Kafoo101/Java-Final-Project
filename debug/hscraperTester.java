package main.debug;
import main.trainer.hscraper2;

public class hscraperTester{
    public static void main(String[] args)
    {
        hscraper2 scraper = new hscraper2("https://www.huffpost.com/entry/police-fecal-sandwich-homeless_n_581cb9c0e4b0e80b02c97652");

            String scrapedContent = scraper.scrape();
            if (scrapedContent != null) {
                System.out.println(scrapedContent);
            } else {
                System.out.println("Error: Unable to scrape content.");
            }

    }
}