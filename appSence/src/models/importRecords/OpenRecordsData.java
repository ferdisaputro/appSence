/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.importRecords;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class OpenRecordsData {
    private StringProperty id, title, date, createdAt;
    private Button openButton;
    private EmployeeRecordVariable erv;
    
    public OpenRecordsData(String id, String title, String date, String createdAt, EmployeeRecordVariable erv) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.date = new SimpleStringProperty(date);
        this.createdAt = new SimpleStringProperty(createdAt);
        this.openButton = new Button("Open");
        this.erv = erv;
        
        openButton.setOnAction(event -> {
//            JOptionPane.showMessageDialog(null, this.id+" "+this.title+" "+this.date+" "+this.createdAt);
            erv.setId(Integer.parseInt(this.id.get()));
        });
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt.set(createdAt);
    }

    public Button getOpenButton() {
        return openButton;
    }

    public void setOpenButton(Button openButton) {
        this.openButton = openButton;
    }

    
    
    
}
