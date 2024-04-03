/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.setting;

import controllers.setting.PermitPointController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class Permit {
    private StringProperty id;
    private StringProperty min;
    private StringProperty max;
    private StringProperty permitType;
    private StringProperty point;
    private Button delete;

    public Permit(String id, String min, String max, String permitType, String point, PermitPointController permit) {
        this.id = new SimpleStringProperty(id);
        this.min = new SimpleStringProperty(min);
        this.max = new SimpleStringProperty(max);
        this.permitType = new SimpleStringProperty(permitType);
        this.point = new SimpleStringProperty(point);
        this.delete = new Button("Delete");
        this.delete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        this.delete.setOnAction(event -> {
            if (JOptionPane.showConfirmDialog(null, "Delete \""+ this.min.get()+"-"+ this.max.get() + "\" from database?") == JOptionPane.YES_OPTION) {
                permit.delete(this.id.get());
            }
        });
    }
    
    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }
    
    public String getMinMax() {
        int min = Integer.valueOf(this.min.get());
        int max = Integer.valueOf(this.max.get());
        if (min == max) {
            return this.min.get();
        } else {
            return min + "-" + max;
        }
    }

    public void setMinMax(String min) {
        this.min.set(min);
    }
    
    public String getMin() {
        return min.get();
    }

    public void setMin(String min) {
        this.min.set(min);
    }
    
    public String getMax() {
        return max.get();
    }

    public void setMax(String max) {
        this.max.set(max);
    }
    
    public String getPermitType() {
        return permitType.get();
    }

    public void setPermitType(String permitType) {
        this.permitType.set(permitType);
    }
    
    public String getPoint() {
        return point.get();
    }

    public void setPoint(String point) {
        this.point.set(point);
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }
}
