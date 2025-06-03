package myJava;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Screen;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import myJava.DatabaseManager;
import myJava.misc.DatabaseConnection;
import myJava.misc.addURL;
import myJava.misc.addNewsToCat;

public class NewsPortal extends Application{
    private ComboBox<String> catComboBox = new ComboBox<>();
    private ListView<String> newsList = new ListView<>();
    private Button addNews = new Button("placeholder");
    private WebView web = new WebView();
    private WebEngine engine = web.getEngine();
    private TextField UrlField = new TextField();
    private String defaultURL = "https://edition.cnn.com";
    private Stage primaryStage;
    private VBox leftContainer;
    private VBox rightContainer;
    private double leftWidth = 488;
    private double fullWidth = 1290;
    private boolean firstTimeExpand = true;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void animateStageWidth(Stage stage, double targetWidth) {
        if (firstTimeExpand) {
            stage.sizeToScene(); // force layout the first time
            firstTimeExpand = false;
        }

        double startWidth = stage.getWidth();
        double animationDurationMs = 300;
        int frames = 30; // smoothness: 30 frames over 300ms means 10ms per frame
        double frameDuration = animationDurationMs / frames;

        Timeline timeline = new Timeline();

        for (int i = 0; i <= frames; i++) {
            double fraction = (double) i / frames;
            double frameWidth = startWidth + fraction * (targetWidth - startWidth);

            KeyFrame kf = new KeyFrame(Duration.millis(i * frameDuration), e -> {
                stage.setWidth(frameWidth);
            });

            timeline.getKeyFrames().add(kf);
        }

        timeline.play();
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("News Portal");
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #E8DCCB;");
        Scene scene = new Scene(root, 470, 600);

        //left area
        leftContainer = new VBox();
        leftContainer.setMinWidth(470);
        leftContainer.setAlignment(Pos.TOP_LEFT);

        HBox categorySelect = new HBox(5);
        categorySelect.setPadding(new Insets(0, 0, 0, 5));
        Label catLabel = new Label("Select Category :");
        catLabel.setPadding(new Insets(3, 0, 0, 0));
        catLabel.setStyle("-fx-font-size: 14px;");
        catComboBox.getItems().addAll("Show All", "Politics", "Economy", "Environment", "Entertainment", "Social", "Technology", "Sports", "Health", "Science", "World News");
        catComboBox.setValue("Show All");
        catComboBox.setOnAction(e -> {
            String selected = catComboBox.getValue().toLowerCase().replaceAll("\\s", "");
            refresh(selected);
        });
        refresh("showall");
        categorySelect.getChildren().add(catLabel);
        categorySelect.getChildren().add(catComboBox);
        leftContainer.getChildren().add(categorySelect);

        Region spacer = new Region();
        spacer.setMinHeight(2);
        leftContainer.getChildren().add(spacer);

        VBox listContainer = new VBox();
        listContainer.setPadding(new Insets(0, 0, 0, 5));
        HBox toolbox = new HBox(10); // spacing 10px between buttons
        toolbox.setPadding(new Insets(5));
        toolbox.setStyle("-fx-background-color: #f4f1e9;"); 
        addNews.setPrefHeight(25);
        addNews.setStyle("-fx-font-size: 11px;");
        toolbox.getChildren().add(addNews);
        newsList.setMinWidth(470);
        newsList.setMinHeight(536);
        newsList.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 12px;");
        listContainer.getChildren().add(toolbox);
        listContainer.getChildren().add(newsList);
        leftContainer.getChildren().add(listContainer);
        root.setLeft(leftContainer);

        //right area
        rightContainer = new VBox();
        rightContainer.setPadding(new Insets(0, 5, 0, 0));
        rightContainer.setMinWidth(800);
        rightContainer.setAlignment(Pos.TOP_RIGHT);
        
        loadPage(defaultURL);
        HBox urlBox = new HBox();
        urlBox.setMaxHeight(26);
        UrlField.setPrefWidth(772);
        UrlField.setOnAction(e -> {
            loadPage(UrlField.getText());
        });
        urlBox.getChildren().add(UrlField);
        ImageView hide = new ImageView(new Image(getClass().getResource("/hide.png").toExternalForm()));
        hide.setFitWidth(15);
        hide.setFitHeight(15);
        Button hideButton = new Button();
        hideButton.setGraphic(hide);
        hideButton.setOnAction(e -> {
            animateStageWidth(primaryStage, leftWidth);
            primaryStage.setX(525.6);
            primaryStage.setY(89.2);
        });
        urlBox.getChildren().add(hideButton);

        rightContainer.getChildren().add(urlBox);
        web.setMaxHeight(574);
        engine.locationProperty().addListener((obs, oldLocation, newLocation) -> {
            UrlField.setText(newLocation);
        });
        rightContainer.getChildren().add(web);
        root.setRight(rightContainer);
        primaryStage.setScene(scene);
        primaryStage.setWidth(leftWidth);
        primaryStage.setX(525.6);
        primaryStage.setY(89.2);
        primaryStage.show();
    }

    private void loadPage(String url)
    {
        try
        {
            primaryStage.sizeToScene();
            primaryStage.setWidth(fullWidth);
            primaryStage.setX(122.8);
            primaryStage.setY(89.2);
            Document doc = Jsoup.connect(url).get();
            removeDataUriMedia(doc, "video");
            removeDataUriMedia(doc, "audio");
            UrlField.setText(url);
            engine.loadContent(doc.outerHtml());
        }
        catch(IOException e)
        {
            System.out.println("Failed to load page");
        }
    }

    private void removeDataUriMedia(Document doc, String tag) {
        Elements elements = doc.select(tag + "[src^=data:]");
        for (Element element : elements) {
            element.remove();
        }
    }

    private Region createVSeparator() {
        Region sep = new Region();
        sep.setPrefWidth(1);
        sep.setStyle("-fx-background-color: #999;");
        return sep;
    }

    public void refresh(String category)
    {
        DatabaseManager dao = new DatabaseManager();

        Map<String, String> titleToURL = new LinkedHashMap<>();
        if(category.equals("showall"))
        {
            String[][] queryResult = dao.getAllNews();
            for(int i = 0; i < queryResult.length; i++)
            {
                String title = queryResult[i][0];
                String URL = queryResult[i][1];

                if(title.length() > 50) title = title.substring(0, 47) + "...";
                titleToURL.put(title, URL);
            }

            addNews.setText("Add News");
            addNews.setOnAction(e -> {
                addURL addUrlWindow = new addURL();
                addUrlWindow.showModal();
                refresh("showall");
            });
        }
        else
        {
            String[] queryResult = dao.getNewsFromCategory(category);
            for(int i = 0; i < queryResult.length; i++)
            {
                String[] q2 = dao.takeNewsInfo(queryResult[i]);
                String title = q2[0];
                String URL = q2[1];

                if(title.length() > 50) title = title.substring(0, 47) + "...";
                titleToURL.put(title, URL);
            }

            String text = category.substring(0, 1).toUpperCase() + category.substring(1);
            if(category.equals("worldnews")) text = "World News";
            addNews.setText("Add News to " + text);
            addNews.setOnAction(e -> {
                addNewsToCat addNewsToCatWindow = new addNewsToCat();
                addNewsToCatWindow.setCategory(category);
                addNewsToCatWindow.showModalSelector();
                String resultURL = addNewsToCatWindow.getSelectedUrl();
                if(resultURL != null)
                {
                    String addID = dao.takeNewsID(resultURL);
                    dao.addNewsToCategoryTable(addID, category);
                    refresh(category);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Addition Successful");
                    alert.setHeaderText(null); // No header
                    alert.setContentText("The news has been added successfully.");
                    alert.showAndWait();
                }
            });
        }

        ObservableList<String> titles = FXCollections.observableArrayList();
        titles.add("NEWS"); 
        titles.addAll(titleToURL.keySet());
        newsList.setItems(titles);
        newsList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(String title, boolean empty) {
                super.updateItem(title, empty);

                if (empty || title == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                // Handle header row
                if (title.equals("NEWS")) {
                    Label noHeader = new Label("No.");
                    noHeader.setPrefWidth(40);
                    noHeader.setStyle("-fx-padding: 3;");

                    Label titleHeader = new Label("Title");
                    titleHeader.setPrefWidth(350);
                    titleHeader.setStyle("-fx-padding: 3;");

                    Region deleteHeader = new Region();
                    deleteHeader.setPrefWidth(60);

                    HBox headerBox = new HBox(noHeader, createVSeparator(), titleHeader, createVSeparator(), deleteHeader);
                    headerBox.setStyle("-fx-border-color: transparent transparent black transparent;");
                    setGraphic(headerBox);
                    return;
                }

                int index = getIndex(); // index includes the header
                int number = index;     // because header is at index 0

                Label noLabel = new Label(number + ".");
                noLabel.setPrefWidth(40);
                noLabel.setStyle("-fx-padding: 3;");
                noLabel.setAlignment(Pos.CENTER_RIGHT);

                Label titleLabel = new Label(title);
                titleLabel.setPrefWidth(350);
                titleLabel.setStyle("-fx-padding: 3;");
                titleLabel.setTooltip(new Tooltip(title));
                titleLabel.setOnMouseClicked(e -> {
                    String url = titleToURL.get(title);
                    loadPage(url);
                });

                HBox row;
                if (!category.equalsIgnoreCase("showall")) {
                    Label deleteLabel = new Label("Delete");
                    deleteLabel.setStyle("-fx-text-fill: red; -fx-underline: true; -fx-padding: 3;");
                    deleteLabel.setPrefWidth(60);
                    deleteLabel.setAlignment(Pos.CENTER_LEFT);
                    deleteLabel.setOnMouseClicked(e -> {
                        String newsID = dao.takeNewsID(titleToURL.get(title));
                        dao.deleteNewsFromCategory(newsID, category);
                        refresh(category);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Deletion Successful");
                        alert.setHeaderText(null); // No header
                        alert.setContentText("The news has been deleted successfully.");
                        alert.showAndWait();
                    });

                    row = new HBox(noLabel, createVSeparator(), titleLabel, createVSeparator(), deleteLabel);
                } else {
                    row = new HBox(noLabel, createVSeparator(), titleLabel, createVSeparator());
                }

                row.setSpacing(0);
                row.setStyle("-fx-border-color: transparent transparent black transparent;");
                setGraphic(row);
            }
        });
    }
}