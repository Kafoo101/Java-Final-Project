package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

import main.misc.DatabaseConnection;

public class DatabaseManager {

    public static String getNextNewsID() {
        String sql = "SELECT NewsID FROM news ORDER BY NewsID DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("NewsID"); // e.g., "N-007"
                int number = Integer.parseInt(lastID.substring(2)); // get "007" → 7
                number++;
                return String.format("N-%03d", number); // → "N-008"
            } else {
                return "N-001"; // If no rows found, start with "N-001"
            }
        } catch (SQLException e) {
            System.err.println("Error fetching next NewsID");
            return null;
        }
    }

    public static boolean addNews(String url, String title, String[] categories) {
        String sql = "INSERT INTO news (NewsID, Title, URL) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            String newID = getNextNewsID();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newID);
                stmt.setString(2, title);
                stmt.setString(3, url);

                int affected = stmt.executeUpdate();
                
                if (affected > 0) {
                    addNewsToCategoryTables(newID, categories);
                    System.out.println("Inserted news with ID: " + newID);
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addNewsToCategoryTables(String newsID, String[] categories) {
        for(String category : categories) {
            //sanitize table name to avoid SQL injection
            String tableName = category.toLowerCase().replaceAll("\\s+", "").replaceAll("[^a-z0-9_]", "");

            String sql = "INSERT INTO " + tableName + " (NewsID) VALUES (?)";


            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, newsID);
                int affected = stmt.executeUpdate();
                System.out.println("Added to category table: " + tableName + " (rows affected: " + affected + ")");

            } catch (SQLException e) {
                System.err.println("Error adding to category table: " + tableName);
            }
        }
    }

    public static void addNewsToCategoryTable(String newsID, String category) {
        String tableName = category.toLowerCase().replaceAll("\\s+", "").replaceAll("[^a-z0-9_]", "");
        String sql = "INSERT INTO " + tableName + " (NewsID) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newsID);
            int affected = stmt.executeUpdate();
            System.out.println("Added to category table: " + tableName);

        } catch (SQLException e) {
            System.err.println("Error adding to category table: " + tableName);
            e.printStackTrace();
        }
    }

    public static void deleteNewsFromCategory(String newsID, String category) {
        String tableName = category.toLowerCase().replaceAll("\\s+", "").replaceAll("[^a-z0-9_]", "");
        String sql = "DELETE FROM " + tableName + " WHERE NewsID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newsID);
            int affected = stmt.executeUpdate();
            System.out.println("Delete from category table: " + tableName + " (rows affected: " + affected + ")");

        } catch (SQLException e) {
            System.err.println("Error deleting from category table: " + tableName);
            e.printStackTrace();
        }
    }


    public static String takeNewsURL(String newsID) {
        String sql = "SELECT URL FROM news WHERE NewsID = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newsID);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getString("URL");
                } else {
                    System.out.println("No news found with ID: " + newsID);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving URL for NewsID: " + newsID);
            return null;
        }
    }

    public static String takeNewsTitle(String newsID) {
        String sql = "SELECT Title FROM news WHERE NewsID = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newsID);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getString("Title");
                } else {
                    System.out.println("No news found with ID: " + newsID);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving Title for NewsID: " + newsID);
            return null;
        }
    }

    public static String takeNewsID(String url) {
        String sql = "SELECT NewsID FROM news WHERE URL = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, url);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getString("NewsID");
                } else {
                    System.out.println("No news found with url: " + url);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ID for url: " + url);
            return null;
        }
    }

    public static String[] getNewsFromCategory(String category) {
        String tableName = category.toLowerCase().replaceAll("\\s+", "").replaceAll("[^a-z0-9_]", "");
        String sql = "SELECT NewsID FROM " + tableName;

        List<String> newsList = new ArrayList<>();

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            
            while(rs.next()) {
                newsList.add(rs.getString("NewsID"));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving news from category table: " + tableName);
        }

        return newsList.toArray(new String[0]);
    }

    public static String[] takeNewsInfo(String newsID)
    {
        String sql = "SELECT Title, URL FROM news WHERE NewsID = ?";
        String[] info = null;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newsID);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    String col1 = rs.getString(1);
                    String col2 = rs.getString(2);

                    info = new String[]{col1, col2};
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving info for NewsID: " + newsID);
            return null;
        }

        return info;
    }

    public static String[][] getAllNews(){
        String sql = "SELECT Title, URL FROM news";
        List<String[]> newsList = new ArrayList<>();

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                String col1 = rs.getString(1);
                String col2 = rs.getString(2);

                newsList.add(new String[]{col1, col2});
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving news.");
        }

        String[][] result = new String[newsList.size()][2];
        for (int i = 0; i < newsList.size(); i++) {
            result[i] = newsList.get(i);
        }

        return result;
    }

    public static String[] getAllCategory() {
        String sql = "SELECT Name FROM category";
        List<String> categoryList = new ArrayList<>();

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                categoryList.add(rs.getString("Name"));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving categories.");
        }

        return categoryList.toArray(new String[0]);
    }

    public static boolean isURLExist(String url) {

        String sql = "SELECT 1 FROM news WHERE URL = ? LIMIT 1";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, url);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            return false;
        }
    }

}