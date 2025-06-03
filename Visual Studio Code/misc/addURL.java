package main.misc;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import main.Scraper;
import main.NewsCategorizer;
import main.DatabaseManager;
import main.misc.DatabaseConnection;

public class addURL extends Application{
    public void showModal() {
        Stage stage = new Stage();
        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        buildUI(stage); // new method
        stage.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) {
        buildUI(primaryStage);
        primaryStage.show();
    }

    public void buildUI(Stage primaryStage)
    {
        primaryStage.setTitle("Add news");
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #E8DCCB;");

        //top container
        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.TOP_LEFT);

        Region spacer = new Region();
        spacer.setMinHeight(5);

        Label tLabel = new Label("Enter news website URL :");
        tLabel.setStyle("-fx-font-size: 16px;");
        VBox.setMargin(tLabel, new Insets(0, 0, 0, 10));
        contentBox.getChildren().add(tLabel);
        contentBox.getChildren().add(spacer);

        HBox wrapper = new HBox(5);
        wrapper.setPadding(new Insets(0, 0, 0, 10));
        TextField field = new TextField();
        field.setMinWidth(220);
        field.setMaxWidth(220);
        Button add = new Button("Add");
        add.setPrefWidth(60);
        wrapper.getChildren().add(field);
        wrapper.getChildren().add(add);

        contentBox.getChildren().add(wrapper);

        Label urlLabel = new Label("Example : https://edition.cnn.com/news");
        VBox.setMargin(urlLabel, new Insets(0, 0, 0, 10));
        urlLabel.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
        contentBox.getChildren().add(urlLabel);

        Label catLabel = new Label();
        catLabel.setWrapText(true);
        VBox.setMargin(catLabel, new Insets(0, 0, 0, 10));
        contentBox.getChildren().add(catLabel);

        Label dataLabel = new Label();
        dataLabel.setWrapText(true);
        VBox.setMargin(dataLabel, new Insets(0, 0, 0, 10));
        contentBox.getChildren().add(dataLabel);

        URLHandler handler = new URLHandler(field, catLabel, dataLabel);
        add.setOnAction(handler);
        field.setOnAction(handler);
        root.setCenter(contentBox);

        //bottom container
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            primaryStage.close();
        });
        HBox bottomBox = new HBox(closeButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10, 0, 10, 0));
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 310, 230);
        primaryStage.setScene(scene);
    }
}

class URLHandler implements EventHandler<ActionEvent>{
    private final TextField field;
    private final Label catLabel, dataLabel;

    public URLHandler(TextField field, Label catLabel, Label dataLabel)
    {
        this.field = field;
        this.catLabel = catLabel;
        this.dataLabel = dataLabel;
    }

    @Override
    public void handle(ActionEvent e) {
        String url = field.getText();

        dataLabel.setText("");
        if(!url.contains("edition.cnn.com"))
        {
            catLabel.setText("This program only accepts news from cnn.com");
            catLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            return;
        }
        else if(url.equalsIgnoreCase("https://edition.cnn.com") || url.equalsIgnoreCase("http://edition.cnn.com"))
        {
            catLabel.setText("This page is the main page of cnn.com, there is no news here");
            catLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            return;
        }
        
        try {
            Scraper scraper = new Scraper(url);
            String scrapedText = scraper.Scrape();
            String[] parts = scrapedText.split("\n\n", 2);
            String title = parts[0];

            if (scrapedText.equalsIgnoreCase("url not found [ERROR]")) 
            {
                catLabel.setText("This page does not exist or has no article content.");
                catLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            } 
            else if(scrapedText.equalsIgnoreCase("not an url [ERROR]"))
            {
                catLabel.setText("This URL format is not recognized, please use the correct format.");
                catLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            }
            else 
            {
                //categorizer
                NewsCategorizer cat = new NewsCategorizer();
                ArrayList<String> categories = cat.categorize(url);
                String text = "";
                text += "Scraping successful.\nTitle: \"" + title + "\"\nFound Category:";
                for(int i = 0; i < categories.size(); i++)
                {
                    if(i != 0) text += ",";
                    text += " " + categories.get(i);
                }
                catLabel.setText(text);
                catLabel.setStyle("-fx-font-weight: normal; -fx-text-fill: black;");

                //preprocessing
                String[] addedCategories = new String[categories.size()];
                for(int i = 0; i < categories.size(); i++)
                {
                    addedCategories[i] = categories.get(i);
                }

                //database
                DatabaseManager dao = new DatabaseManager();
                dataLabel.setText("Unexpected Error Occured. Please try again");
                dataLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                if(dao.isURLExist(url))
                {
                    dataLabel.setText("This news has been registered, please input another URL");
                    dataLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                }
                else
                {
                    boolean success = dao.addNews(url, title, addedCategories);
                    if (success) {
                        dataLabel.setText("News successfully recorded to database");
                        dataLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
                    } else {
                        dataLabel.setText("Unexpected Error Occured. Please try again");
                        dataLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                    }
                }
            }
        } catch (IOException ex) {
            catLabel.setText("Failed to connect or retrieve page.");
            catLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            ex.printStackTrace();
        }
    }
}