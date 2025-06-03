package myJava;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class NewsCategorizer {
    public static ArrayList<String> categorize(String url) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            // Load the trained model
            FileInputStream modelIn = new FileInputStream("model/newsCategorizerModelV3.0.bin");
            DoccatModel model = new DoccatModel(modelIn);
            modelIn.close();

            DocumentCategorizerME categorizer = new DocumentCategorizerME(model);
            SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

            // Scraping in progress
            Scraper scraper = new Scraper(url);
            String scrapedContent = scraper.Scrape();
            scrapedContent = TextProcessor.cleanText(scrapedContent, true);

            // Data processing
            scrapedContent = scrapedContent.replaceAll("\\r?\\n", " ");
            String[] tokens = tokenizer.tokenize(scrapedContent);
            double[] outcome = categorizer.categorize(tokens);

            // Normalize and collect category scores
            Map<String, Double> categoryScores = new HashMap<>();
            double total = Arrays.stream(outcome).sum();
            for (int i = 0; i < outcome.length; i++) {
                String category = categorizer.getCategory(i);
                double normalizedScore = outcome[i] / total;
                categoryScores.put(category, normalizedScore);
            }

            // Sort the categories by score in descending order
            List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(categoryScores.entrySet());
            sortedEntries.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            //output
            double firstScore = sortedEntries.get(0).getValue();
            double threshold = Math.max(0.1, firstScore * 0.6);
            int count = 0;
            for (Map.Entry<String, Double> entry : sortedEntries) {
                if (entry.getValue() >= threshold) {
                    result.add(entry.getKey());
                    count++;
                    if (count >= 3) break; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       return result; 
    }
}
