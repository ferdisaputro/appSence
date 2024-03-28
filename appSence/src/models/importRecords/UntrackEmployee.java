/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.importRecords;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
//import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 *
 * @author USER
 */
public class UntrackEmployee {
    private SimpleStringProperty nik;
    private TextField idEmployee;
    private TextField name;
    private ComboBox shift;
    
    public UntrackEmployee(String nik, String name, ObservableList shifts){
        this.nik = new SimpleStringProperty(nik);
        this.idEmployee = new TextField();
        this.name = new TextField(name);
        this.shift = new ComboBox(shifts);
    }

    
    public String getNik() {
        return nik.get();
    }

//    public void setNik(String nik) {
//        this.nik.set(nik);
//    }
    
    public TextField getIdEmployee() {
        return idEmployee;
    }

//    public void setIdEmployee(String idEmployee) {
//        this.idEmployee.set(idEmployee);
//    }

    public TextField getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name.set(name);
//    }
    
    public ComboBox getShift() {
        return shift;
    }

//    public void setShift(String shift) {
//        this.shift.set(shift);
//    }

}
