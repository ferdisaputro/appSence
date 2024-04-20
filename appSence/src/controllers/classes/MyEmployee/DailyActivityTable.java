/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.classes.MyEmployee;

import java.sql.Date;
import java.sql.Time;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ASUS
 */
public class DailyActivityTable {
    private ObjectProperty<Date> absence_date;
    private ObjectProperty<Time> time_ci;
    private ObjectProperty<Time> time_co;
    private IntegerProperty point_ci;
    private IntegerProperty point_co;
    private IntegerProperty total_point;
    private StringProperty entry;
    private StringProperty exit;
    
    public DailyActivityTable(Date absence_date,Time time_ci,Time time_co,int point_ci,int point_co,String entry, String exit){
        this.absence_date = new SimpleObjectProperty<>(absence_date);
        this.time_ci = new SimpleObjectProperty<>(time_ci);
        this.time_co = new SimpleObjectProperty<>(time_co);
        this.point_ci = new SimpleIntegerProperty(point_ci);
        this.point_co = new SimpleIntegerProperty(point_co);
        this.total_point = new SimpleIntegerProperty(point_ci + point_co);
        this.entry = new SimpleStringProperty(entry);
        this.exit = new SimpleStringProperty(exit);
    }
        public ObjectProperty<Date> dateProperty() {
            return absence_date;
        }
        public Date getDate(){
            return absence_date.get();
        }
        public void setDate(Date absence_date) {
            this.absence_date.set(absence_date);
        }
//        ----------------------------------------------------------------------
        public ObjectProperty<Time> TimeCiProperty(){
            return time_ci;
        }
        public Time getTimeCi(){
            return time_ci.get();
        }
        public void setTimeCi(Time time_ci){
            this.time_ci.set(time_ci);
        }
//        ----------------------------------------------------------------------
        public ObjectProperty<Time> TimeCoProperty(){
            return time_co;
        }
        public Time getTimeCo(){
            return time_co.get();
        }
        public void setTimeCo(Time time_co){
            this.time_co.set(time_co);
        }
//        ----------------------------------------------------------------------
        public int getPointCi(){
            return point_ci.get();
        }
        public void setPointCi(int point_ci){
            this.point_ci.set(point_ci);
        }
//        ----------------------------------------------------------------------
        public int getPointCo(){
            return point_co.get();
        }
        public void setPointCo(int point_co){
            this.point_co.set(point_co);
        }
//        ----------------------------------------------------------------------
        public int getTotalPoint(){
            return total_point.get();
        }
        public void setTotalPoint(int total_point){
            this.total_point.set(total_point);
        }
//        ----------------------------------------------------------------------
        public String getEntry(){
            return entry.get();
        }
        public void setEntry(String entry){
            this.entry.set(entry);
        }
//        ----------------------------------------------------------------------
        public String getExit(){
            return exit.get();
        }
        public void setExit(String exit){
            this.exit.set(exit);
        }
}
