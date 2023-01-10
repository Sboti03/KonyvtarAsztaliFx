package com.example.konyvtarasztalifx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class AppController {

    @FXML
    private TableView<Konyv> bookTable;
    @FXML
    private TableColumn<Konyv, String> titleCol;
    @FXML
    private TableColumn<Konyv, String> authorCol;
    @FXML
    private TableColumn<Konyv, Integer> publishYearCol;
    @FXML
    private TableColumn<Konyv, Integer> pageCountCol;


    BookDB bookDB;


    @FXML
    private void initialize() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publishYearCol.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
        pageCountCol.setCellValueFactory(new PropertyValueFactory<>("page_count"));

        Platform.runLater(()-> {
            try {
                bookDB = new BookDB();
                listBooks();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nem sikerült kapcsolódni a szerverre!");
                alert.setHeaderText("");
                alert.showAndWait();
                Platform.exit();
            }
        });
    }


    private void listBooks() {
        bookTable.getItems().clear();
        List<Konyv> books = null;
        try {
            books = bookDB.readBooks();
            for (Konyv k: books) {
                bookTable.getItems().add(k);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Hiba");
            alert.setHeaderText("");

            alert.showAndWait();
        }

    }

    public void deleteClick(ActionEvent actionEvent) {
        int selectedIndex = bookTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Törléshez előbb válasszon könyvet");
            alert.setHeaderText("");
            alert.show();
            return;
        }
        Konyv konyv = bookTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Törlés");
        alert.setHeaderText("");
        alert.setContentText("Biztos szeretnéd törölni a kiválasztott könyvet?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                if (bookDB.deleteBook(konyv.getId())) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setHeaderText("Sikeres törlés");
                    alert1.setTitle("");
                    alert1.setContentText("");
                }
                listBooks();
            } catch (SQLException e) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR, "Nem sikerült a törlés");
                alert.setHeaderText("");
                alert1.setContentText(e.getMessage());
            }
        }
    }
}