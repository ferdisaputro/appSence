/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.importRecords.details;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author USER
 */
public class DetailRecord {
    private StringProperty date;
    private StringProperty clockInTime;
    private IntegerProperty clockInPoint;
    private StringProperty clockOutTime;
    private IntegerProperty clockOutPoint;
    private IntegerProperty totalPoint;
    private StringProperty entryLoc;
    private StringProperty exitLoc;
    
    public DetailRecord (String date, 
            String clockInTime, String clockInPoint, 
            String clockOutTime, String clockOutPoint, 
            String entryLoc, String exitLoc) {
        this.date = new SimpleStringProperty(date);
        this.clockInTime = new SimpleStringProperty(clockInTime);
        this.clockInPoint = new SimpleIntegerProperty(Integer.parseInt(clockInPoint));
        this.clockOutTime = new SimpleStringProperty(clockOutTime);
        this.clockOutPoint = new SimpleIntegerProperty(Integer.parseInt(clockOutPoint));
        this.totalPoint = new SimpleIntegerProperty(this.clockInPoint.get() + this.clockOutPoint.get());
        this.entryLoc = new SimpleStringProperty(entryLoc);
        this.exitLoc = new SimpleStringProperty(exitLoc);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getClockInTime() {
        return clockInTime.get();
    }

    public void setClockInTime(String clockInTime) {
        this.clockInTime.set(clockInTime);
    }

    public String getClockInPoint() {
        return String.valueOf(clockInPoint.get());
    }

    public void setClockInPoint(int clockInPoint) {
        this.clockInPoint.set(clockInPoint);
    }

    public String getClockOutTime() {
        return clockOutTime.get();
    }

    public void setClockOutTime(String clockOutTime) {
        this.clockOutTime.set(clockOutTime);
    }

    public String getClockOutPoint() {
        return String.valueOf(clockOutPoint.get());
    }

    public void setClockOutPoint(int clockOutPoint) {
        this.clockOutPoint.set(clockOutPoint);
    }

    public String getTotalPoint() {
        return String.valueOf(totalPoint.get());
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint.set(totalPoint);
    }

    public String getEntryLoc() {
        return entryLoc.get();
    }

    public void setEntryLoc(String entryLoc) {
        this.entryLoc.set(entryLoc);
    }

    public String getExitLoc() {
        return exitLoc.get();
    }

    public void setExitLoc(String exitLoc) {
        this.exitLoc.set(exitLoc);
    }
}
