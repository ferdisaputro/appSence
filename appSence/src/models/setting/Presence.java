/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.setting;

import controllers.setting.PresenceController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class Presence {
    private StringProperty id;
    private StringProperty start;
    private StringProperty end;
    private StringProperty type;
    private StringProperty point;
    private StringProperty schedule;
    private Button delete;

    public Presence(String id, String start, String end, String type, String point, String schedule, PresenceController presence) {
        this.id = new SimpleStringProperty(id);
        this.start = new SimpleStringProperty(start);
        this.end = new SimpleStringProperty(end);
        this.type = new SimpleStringProperty(type);
        this.point = new SimpleStringProperty(point);
        this.schedule = new SimpleStringProperty(schedule);
        this.delete = new Button("Delete");
        this.delete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        this.delete.setOnAction(event -> {
            if (JOptionPane.showConfirmDialog(null, "Delete \""+ this.start.get()+"-"+ this.end.get() + "\" from database?") == JOptionPane.YES_OPTION) {
                presence.delete(this.id.get());
            }
        });
    }
    
    public String getId() {
        return id.get();
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
    
    public String getStartEnd() {
        if (start.get() == end.get()) {
            return start.get();
        } else {
            return start.get() + "-" + end.get();        
        }
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }
    
    public String getPoint() {
        return point.get();
    }

    public void setPoint(String point) {
        this.point.set(point);
    }
    
    public String getSchedule() {
        return schedule.get();
    }

    public void setSchedule(String schedule) {
        this.schedule.set(schedule);
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }
}
