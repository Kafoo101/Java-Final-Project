package main.debug;
import main.NewsCategorizer;
import java.util.ArrayList;

public class NewsCategorizerTest{
    public static void main(String[] args)
    {
        NewsCategorizer cat = new NewsCategorizer();
        ArrayList<String> result = cat.categorize("https://edition.cnn.com/2025/05/13/australia/australia-rohan-dennis-sentenced-melissa-hoskins-hnk-intl");
        for(String category : result)
        {
            System.out.println(category);
        }
    }
}