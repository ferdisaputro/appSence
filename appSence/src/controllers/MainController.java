package controllers;

import appsence.AppSence;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class MainController implements Initializable{
    @FXML
    private HBox absence;

    @FXML
    private HBox employee;

    @FXML
    private BorderPane mainContainer;

    @FXML
    private HBox settings;
    
    @FXML
    private Label routeTitle;
    
    @FXML
    void showAbsence( ) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/DataRecord.fxml"));
        mainContainer.setCenter(root);
        routeTitle.setText("Absence");
    }
    
    @FXML
    void showEmployee() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/Employee.fxml"));
        mainContainer.setCenter(root);
        routeTitle.setText("Employee");
    }

    @FXML
    void showSettings() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/Setting.fxml"));
        mainContainer.setCenter(root);
        routeTitle.setText("Settings");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/DataRecord.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainContainer.setCenter(root);
        routeTitle.setText("Absence");
    }

}
