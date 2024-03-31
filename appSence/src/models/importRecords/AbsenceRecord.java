/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.importRecords;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author USER
 */
public class AbsenceRecord {
    private StringProperty nik;
    private StringProperty name;
    private StringProperty schedule;
    private StringProperty employeeCode;
    private IntegerProperty subTotalPermit;
    private IntegerProperty totalCutiIjin;
    private IntegerProperty totalAlfa;
    private IntegerProperty subTotalTime;
    private IntegerProperty totalPoint;
    
    public AbsenceRecord(String nik, String name, String schedule, String employeeCode, String totalCutiIjin, String totalAlfa, String subTotalPermit, String subTotalTime, String totalPoint) {
        this.nik = new SimpleStringProperty(nik);
        this.name = new SimpleStringProperty(name);
        this.schedule = new SimpleStringProperty(schedule);
        this.employeeCode = new SimpleStringProperty(employeeCode);
        this.totalCutiIjin = new SimpleIntegerProperty(Integer.parseInt(totalCutiIjin));
        this.totalAlfa = new SimpleIntegerProperty(Integer.parseInt(totalAlfa));
        this.subTotalPermit = new SimpleIntegerProperty(Integer.parseInt(subTotalPermit));
        this.subTotalTime = new SimpleIntegerProperty(Integer.parseInt(subTotalTime));
        this.totalPoint = new SimpleIntegerProperty(Integer.parseInt(totalPoint));
    }

    public String getNik() {
        return nik.get();
    }

    public void setNik(String nik) {
        this.nik.set(nik);
    }
    
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
    
    public String getSchedule() {
        return schedule.get();
    }

    public void setSchedule(String schedule) {
        this.schedule.set(schedule);
    }
    
    public String getEmployeeCode() {
        return employeeCode.get();
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode.set(employeeCode);
    }
    
    public String getTotalCutiIjin() {
        return String.valueOf(totalCutiIjin.get());
    }

    public void setTotalCutiIjin(int totalCutiIjin) {
        this.totalCutiIjin.set(totalCutiIjin);
    }
    
    public String getTotalAlfa() {
        return String.valueOf(totalAlfa.get());
    }

    public void setTotalAlfa(int totalAlfa) {
        this.totalAlfa.set(totalAlfa);
    }
    
    public String getSubTotalPermit() {
        return String.valueOf(subTotalPermit.get());
    }

    public void setSubTotalPermit(int subTotalPermit) {
        this.subTotalPermit.set(subTotalPermit);
    }

    public String getSubTotalTime() {
        return String.valueOf(subTotalTime.get());
    }

    public void setSubTotalTime(int subTotalTime) {
        this.subTotalTime.set(subTotalTime);
    }

    public String getTotalPoint() {
        return String.valueOf(totalPoint.get());
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint.set(totalPoint);
    }
}
