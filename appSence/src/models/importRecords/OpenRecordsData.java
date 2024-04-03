/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.importRecords;

import controllers.components.importRecords.ImportOpenFiles.ShowOpenRecords;
import controllers.connection.Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Color;

/**
 *
 * @author USER
 */
public class OpenRecordsData {
    private StringProperty id, title, date, createdAt;
    private Button openButton, deleteButton;
    private EmployeeRecordVariable erv;
    private HBox actionButtons;
    
    public OpenRecordsData(String id, String title, String date, String createdAt, EmployeeRecordVariable erv, ShowOpenRecords showOpenRecords) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.date = new SimpleStringProperty(date);
        this.createdAt = new SimpleStringProperty(createdAt);
        this.openButton = new Button("Open");
        this.openButton.setStyle("-fx-background-color: #8DD06C;");
        this.deleteButton = new Button("Delete");
        this.deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        this.actionButtons = new HBox(this.openButton, this.deleteButton);
        this.erv = erv;
        
        openButton.setOnAction(event -> {
            erv.setId(Integer.parseInt(this.id.get()));
        });
        deleteButton.setOnAction(event -> {
            if (JOptionPane.showConfirmDialog(null, "Delete \""+ this.title.get() + "\" from database?") == JOptionPane.YES_OPTION) {
                deleteRecordData();
                showOpenRecords.show();
            }
        });
    }
    
    private void deleteRecordData() {
        Connect conn = new Connect();
        String sql = "DELETE FROM absences WHERE id = ?;";
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, Integer.valueOf(this.id.get()));
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Data "+ this.title.get() + " removed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    
    public HBox getActionButtons() {
        return actionButtons;
    }

    public void setActionButtons(HBox actionButtons) {
        this.actionButtons = actionButtons;
    }

    
    
    
}
