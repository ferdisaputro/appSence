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


/**
 *
 * @author ASUS
 */
public class Employee {
    private StringProperty nik;
    private StringProperty employee_code;
    private StringProperty name;
    private IntegerProperty schedule;
    public Button button;
 

    public Employee(String nik, String employee_code, String name,int schedule, EmployeeController employee ) {
        this.nik = new SimpleStringProperty(nik) ;
        this.employee_code = new SimpleStringProperty(employee_code);
        this.name = new SimpleStringProperty(name);
        this.schedule = new SimpleIntegerProperty(schedule);
        this.button = new Button("Add Permit");
        this.button.setOnAction(event ->{
            employee.ButtonKlikPermit(this);
        });
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
    
    public void setButton(Button button){
        this.button = button;
    }
    public Button getButton (){
        return button;
    }
    
    
}
    