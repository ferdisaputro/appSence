/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.setting;

import controllers.setting.ScheduleController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class Schedule {
    private StringProperty id;
    private StringProperty start;
    private StringProperty end;
    private StringProperty description;
    private Button delete;
    
    public Schedule(String id, String start, String end, String description, ScheduleController sc) {
        this.id = new SimpleStringProperty(id);
        this.start = new SimpleStringProperty(start);
        this.end = new SimpleStringProperty(end);
        this.description = new SimpleStringProperty(description);
        this.delete = new Button("Delete");
        this.delete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        this.delete.setOnAction(event -> {
            if (JOptionPane.showConfirmDialog(null, "Delete \""+ this.description.get() + "\" from database?") == JOptionPane.YES_OPTION) {
                sc.delete(this.id.get());
            }
        });
    }

    public String getStart() {
        return start.get();
    }

    public void setStart(String start) {
        this.start.set(start);
    }

    public String getEnd() {
        return end.get();
    }

    public void setEnd(String end) {
        this.end.set(end);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }
}
