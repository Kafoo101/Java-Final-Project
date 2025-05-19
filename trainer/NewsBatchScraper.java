package main.trainer;
import java.io.*;
import java.util.*;
import org.json.JSONObject;
import main.TextProcessor;

public class NewsBatchScraper {

    public static void main(String[] args) {
        String jsonFilePath = "train_data/Limited_Entries_News_Category_Dataset_v3_1.json";
        String outputFile = "train_data/TrainingDataV3.1.txt";
        String label = "worldnews";

        int startIndex = 900;  
        int endIndex = 999;   
        int currentIndex = 0;
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (currentIndex > endIndex) break;
                if (currentIndex < startIndex) {
                    currentIndex++;
                    continue;
                }

                try {
                    JSONObject obj = new JSONObject(line);
                    String url = obj.getString("link");

                    hscraper2 scraper = new hscraper2(url);
                    String rawText = scraper.scrape();
                    Thread.sleep(800);

                    if (rawText != null && !rawText.isBlank()) {
                        String cleaned = TextProcessor.cleanText(rawText, true);
                        String singleLine = cleaned.replaceAll("\\s+", " ").trim();
    
                        if (!singleLine.isEmpty()) {
                            writer.write(label + " " + singleLine + "\n");
                            count++;
                            System.out.println("Scraped [" + currentIndex + "]: " + url);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Skipping index " + currentIndex + ": " + e.getMessage());
                }

                currentIndex++;
            }

            System.out.println("Scraping complete. Articles scraped: " + count);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
