package main.debug;

import main.DatabaseManager;

public class DatabaseTester {
    public static void main(String[] args) {
        // 1. Create your DatabaseManager object
        DatabaseManager dao = new DatabaseManager();

        //addNews Test
        String url = "http://example.com/article";
        String title = "New News Title";
        String[] categories = {"Technology", "Science", "World News"};
        boolean success = dao.addNews(url, title, categories);
        if (success) {
            System.out.println("News inserted for all categories!");
        } else {
            System.out.println("Failed to insert news.");
        }

        //deleteNewsFromCategory Test
        String newsID = "N-001";
        String[] categoriesDeletion = {"World News"};
        dao.deleteNewsFromCategory(newsID, categoriesDeletion);

        //takeNewsURL Test
        String newsURL = dao.takeNewsURL(newsID);
        if(newsURL != null) {
            System.out.println("News URL: " + newsURL);
        } else {
            System.out.println("No such NewsID.");
        }

        //takeNewsTitle Test
        String newsTitle = dao.takeNewsTitle(newsID);
        if(newsTitle != null) {
            System.out.println("News Title: " + newsTitle);
        } else {
            System.out.println("No such NewsID.");
        }

        //getNewsFromCategory Test
        String cate = "Technology";
        String[] newsArray = dao.getNewsFromCategory(cate);
        System.out.println("News in category: " + cate);
        for (String ids : newsArray) {
            System.out.println("- " + ids);
        }

        //getAllCategory Test
        String[] categoryArray = dao.getAllCategory();
        System.out.println("Categories: ");
        for (String cats : categoryArray) {
            System.out.println("- " + cats);
        }

        //isURLExist Test
        boolean existing = dao.isURLExist(url);
        if(existing) {
            System.out.println("There exist URL: " + url);
        } else {
            System.out.println("Doesn't exist URL: " + url);
        }
    }
}
