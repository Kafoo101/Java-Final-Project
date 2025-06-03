package main.misc;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import javafx.util.Callback;

import main.DatabaseManager;

public class addNewsToCat {
    private String selectedUrl;
    private String cat;

    public String getSelectedUrl() {
        return selectedUrl;
    }

    public void setCategory(String cat) {
        this.cat = cat;
    }

    private Region createVSeparator() {
        Region sep = new Region();
        sep.setPrefWidth(1);
        sep.setStyle("-fx-background-color: #999;");
        return sep;
    }

    public void showModalSelector() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Select News");

        VBox root = new VBox();
        root.setStyle("-fx-background-color: #E8DCCB;");
        Label label = new Label("Double click to select news to be added to " + cat);
        label.setPadding(new Insets(0, 0, 0, 5));
        label.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 14px;");
        Region spacer = new Region();
        spacer.setMinHeight(2);
        root.getChildren().add(label);
        root.getChildren().add(spacer);

        DatabaseManager dao = new DatabaseManager();
        ListView<String> listView = new ListView<>();
        listView.setPadding(new Insets(0, 0, 0, 5));
        listView.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 12px;");
        listView.setMaxWidth(420);

        Map<String, String> titleToURL = new LinkedHashMap<>();
        String[][] queryResult = dao.getAllNews();
        for(int i = 0; i < queryResult.length; i++)
        {
            String title = queryResult[i][0];
            String URL = queryResult[i][1];

            if(title.length() > 50) title = title.substring(0, 47) + "...";
            titleToURL.put(title, URL);
        }

        ObservableList<String> titles = FXCollections.observableArrayList();
        titles.add("NEWS"); 
        titles.addAll(titleToURL.keySet());
        listView.setItems(titles);

        listView.setCellFactory(list -> new ListCell<>() {
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

                    HBox headerBox = new HBox(noHeader, createVSeparator(), titleHeader);
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
                titleLabel.setOnMouseClicked(e -> {
                    selectedUrl = titleToURL.get(title);
                    stage.close();
                });

                HBox row;
                row = new HBox(noLabel, createVSeparator(), titleLabel);

                row.setSpacing(0);
                row.setStyle("-fx-border-color: transparent transparent black transparent;");
                setGraphic(row);
            }
        });

        root.getChildren().add(listView);
        stage.setScene(new Scene(root, 450, 300));
        stage.showAndWait();
    }
}