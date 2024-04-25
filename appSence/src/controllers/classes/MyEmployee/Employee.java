/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.classes.MyEmployee;

import controllers.EmployeeController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import controllers.EmployeeController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javax.swing.JOptionPane;

/**
 *
 * @author ASUS
 */
public class Employee {
    private StringProperty nik;
    private StringProperty employee_code;
    private StringProperty name;
    private IntegerProperty schedule;
    public Button button1;
    public Button button2;
    public HBox hbox;
    
    public Employee(String nik, String employee_code, String name,int schedule, EmployeeController employee ) {
        this.nik = new SimpleStringProperty(nik) ;
        this.employee_code = new SimpleStringProperty(employee_code);
        this.name = new SimpleStringProperty(name);
        this.schedule = new SimpleIntegerProperty(schedule);
        this.button2  = new Button("Delete");
        this.button2.setOnAction(event ->{
            if (JOptionPane.showConfirmDialog(null, "Delete \""+ this.nik.get() + "\" from database?") == JOptionPane.YES_OPTION){
            employee.Delete_Employee(this.nik.get());
            }
        });
        this.hbox = new HBox(this.button2);
    }
    
    public String getNik() {
        return nik.get();
    }
    public void setNik(String nik) {
        this.nik.set(nik);
    }

    public String getEmployee_code() {
        return employee_code.get();
    }
    public void setEmployee_code(String employee_code) {
        this.employee_code.set(employee_code);
    }

    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    
    public Integer getSchedule() {
        return schedule.get();
    }
    public void setSchedule(int schedule) {
        this.schedule.set(schedule);
    }
    
    public void setButton1(Button button){
        this.button1 = button;
    }
    public Button getButton1 (){
        return button1;
    }
    public void setButton2(Button button){
        this.button2 = button;
    }
    public Button getButton2 (){
        return button2;
    }
    public HBox getActionButtons() {
        return hbox;
    }

    public void setActionButtons(HBox actionButtons) {
        this.hbox = actionButtons;
    }
}
    