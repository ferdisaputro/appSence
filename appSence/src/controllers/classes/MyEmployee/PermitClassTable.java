/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.classes.MyEmployee;

import controllers.EmployeeController;
import java.sql.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.apache.poi.ss.usermodel.DataValidation;

/**
 *
 * @author ASUS
 */
public class PermitClassTable {
    private SimpleIntegerProperty id;
    private ObjectProperty<Date> date;
    private SimpleStringProperty type;
    public Button delete;
    
    public PermitClassTable(Integer id, Date date, String type, EmployeeController ec){
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleObjectProperty<>(date);
        this.type = new SimpleStringProperty(type);
        this.delete = new Button("Delete");
        this.delete.setOnAction(event ->{
            ec.deletePermit(this.id.get());
        });
    }

    public int getId(){
        return this.id.get();
    }
    
    public ObjectProperty<Date> dateProperty() {
        return date;
    }
    public Date getDate(){
        return date.get();
    }

    public void setDate(Date date) {
        this.date.set(date);    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }
    
    public Button getDelete() {
        return delete;
    }

}
