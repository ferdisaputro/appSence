/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.components.detailAbsence;

import controllers.connection.Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.importRecords.AbsenceRecord;
import models.importRecords.details.DetailRecord;
import models.importRecords.details.PermitDetail;

/**
 *
 * @author USER
 */
public class ShowDetailAbsence {
    String nik;
    AbsenceRecord record;
    int absenceId;
    String sql;
    Statement stat;
    Connect conn = new Connect();
    HashMap<String, String> absence;
    ObservableList<PermitDetail> permitRecords;
    
    public ShowDetailAbsence(AbsenceRecord record, int absenceId) {
        this.record = record;
        this.absenceId = absenceId;
//        getSpecificDetail();
        getAbsence();
    }
    
    public void showEmployeeData(
            TextField nikEmployee, 
            TextField codeEmployee, 
            TextField nameEmployee, 
            TextField workHourEmployee) {
        nikEmployee.setText(record.getNik());
        codeEmployee.setText(record.getEmployeeCode());
        nameEmployee.setText(record.getName());
        workHourEmployee.setText(record.getSchedule());
    }
    
    public void setPoints(Label permitPointCard, Label timingPointCard, Label totalPointCard) {
        permitPointCard.setText(record.getSubTotalPermit());
        timingPointCard.setText(record.getSubTotalTime());
        totalPointCard.setText(record.getTotalPoint());
    }
    
    public void showDetailPermit(TextField cutiIjinTotalDetail, TextField alfaTotalDetail, TextField totalPointPermitDetail,
            TableColumn permitTypeDetail, 
            TableColumn permitDateDetail, 
            TableColumn permitDescDetail,
            TableView permitTableDetail) {
//        HashMap<String, String> permits = getTotal();
        cutiIjinTotalDetail.setText(record.getTotalCutiIjin() + "x");
        alfaTotalDetail.setText(record.getTotalAlfa() + "x");
        totalPointPermitDetail.setText(record.getSubTotalPermit());
        
        getPermitRecord();
        permitTypeDetail.setCellValueFactory(new PropertyValueFactory<PermitDetail, String>("date"));
        permitDateDetail.setCellValueFactory(new PropertyValueFactory<PermitDetail, String>("type"));
        permitDescDetail.setCellValueFactory(new PropertyValueFactory<PermitDetail, String>("description"));
        permitTableDetail.setItems(permitRecords);
    }
    
    private void getAbsence() {
        absence = new HashMap<>();
        sql = "SELECT MONTH(date) AS month, YEAR(date) AS year from absences WHERE id = " + absenceId;
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {                
                absence.put("month", res.getString("month"));
                absence.put("year", res.getString("year"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void getPermitRecord() {
        sql = "SELECT " +
                "    permits.date AS `date`, " +
                "    permits.type AS `type`, " +
                "    permits.description AS `description` " +
                "FROM  " +
                "	employee  " +
                "INNER JOIN  " +
                "	permits  " +
                "ON  " +
                "	employee.nik = permits.nik_employee  " +
                "WHERE  " +
                "	employee.nik = " + record.getNik() +
                "    AND MONTH(permits.date) = " + absence.get("month") + " " +
                "    AND YEAR(permits.date) = " + absence.get("year") + " " +
                "ORDER BY " +
                "	permits.date ASC;";
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            
            permitRecords = FXCollections.observableArrayList();
            while (res.next()) {
                permitRecords.add(new PermitDetail(
                        res.getString("date"), 
                        res.getString("type"), 
                        res.getString("description")
                    ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void showDetailTime(
            Label totalPointTimeDetail, 
            Label avgClockIn,
            Label avgClockOut,
            Label timeAccuracy) {
        totalPointTimeDetail.setText(record.getSubTotalTime());
//        System.out.println(record.getIdSchedule());
        
        HashMap<String, Double> avgTime = new HashMap<>();
        HashMap<String, String> avgClockInOut = new HashMap<>();
        
        getAvgTime(avgTime);
        getAvgClockInOut(avgClockInOut);
        
        Double timeAcc = avgTime.get("late") == 0? 100 : 100 - ((avgTime.get("late")/avgTime.get("active_day")) * 100);
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(2);
        String roundedValue = df.format(timeAcc);
        
        avgClockIn.setText(avgClockInOut.get("avg_clock_in"));
        avgClockOut.setText(avgClockInOut.get("avg_clock_out"));
        timeAccuracy.setText(roundedValue + "%");
        
        
        System.out.println((avgTime.get("late")/avgTime.get("active_day")) * 100);
        System.out.println(avgTime);
//        System.out.println(avgClockInOut);
        
    }
    
    private void getAvgClockInOut(HashMap<String, String> clockInOut) {
        sql = "SELECT "
                + "LEFT(SEC_TO_TIME(AVG(TIME_TO_SEC(clock_in))), 8) AS avg_clock_in, "
                + "LEFT(SEC_TO_TIME(AVG(TIME_TO_SEC(clock_out))), 8) AS avg_clock_out "
                + "FROM detail_absence WHERE nik_employee = " + record.getNik();
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                clockInOut.put("avg_clock_in", res.getString("avg_clock_in")); 
                clockInOut.put("avg_clock_out", res.getString("avg_clock_out")); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private HashMap getAvgTime(HashMap<String, Double> avgTime) {
//        HashMap<String, Integer> avgTime = new HashMap<>();
        sql = "SELECT  " +
                "    MAX(active_day) AS active_day, " +
                "    MAX(late) AS late " +
                "FROM ( " +
                "    SELECT  " +
                "        COUNT(*) AS active_day, " +
                "        NULL AS late " +
                "    FROM " +
                "        employee  " +
                "    INNER JOIN " +
                "        detail_absence " +
                "    ON " +
                "        employee.nik = detail_absence.nik_employee " +
                "    WHERE  " +
                "        employee.nik = " + record.getNik() +
                "        AND MONTH(detail_absence.absence_date) = " + absence.get("month") +
                "        AND YEAR(detail_absence.absence_date) = " + absence.get("year") +
                "     " +
                "    UNION ALL " +
                "     " +
                "    SELECT  " +
                "        NULL as active_day, " +
                "        COUNT(*) AS late " +
                "    FROM  " +
                "        employee  " +
                "    INNER JOIN " +
                "        detail_absence " +
                "    ON " +
                "        employee.nik = detail_absence.nik_employee " +
                "    INNER JOIN " +
                "        schedules " +
                "    ON schedules.id = employee.id_schedule " +
                "    WHERE  " +
                "        detail_absence.clock_in > schedules.start " +
                "        AND employee.nik = " + record.getNik() +
                "        AND MONTH(detail_absence.absence_date) = " + absence.get("month") +
                "        AND YEAR(detail_absence.absence_date) = " + absence.get("year") +
                ") AS unionedQuery;";
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {                
                avgTime.put("active_day", Double.valueOf(res.getString("active_day")));
                avgTime.put("late", Double.valueOf(res.getString("late")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avgTime;
    }
    
    public void showRecords(TableColumn dateRecords, 
            TableColumn clockInPointRecords, TableColumn clockInTimeRecords, 
            TableColumn clockOutTimeRecords, TableColumn clockOutPointRecords, 
            TableColumn totalPointRecords, TableColumn entryLocRecords, TableColumn exitLocRecords,
            TableView recordsTable) {
        ObservableList<DetailRecord> records = FXCollections.observableArrayList();
        sql = "SELECT * FROM detail_absence WHERE nik_employee = ? AND id_absences = ? ";
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, record.getNik());
            ps.setInt(2, absenceId);
            ResultSet res = ps.executeQuery();
            
            while (res.next()) {                
                records.add(new DetailRecord(res.getString("absence_date"), 
                        res.getString("clock_in"), res.getString("clock_in_point"), 
                        res.getString("clock_out"), res.getString("clock_out_point"), 
                        res.getString("entry_location"), res.getString("exit_location")
                ));
            }
            
            dateRecords.setCellValueFactory(new PropertyValueFactory<DetailRecord, String>("date"));
            clockInTimeRecords.setCellValueFactory(new PropertyValueFactory<DetailRecord, String>("clockInTime"));
            clockInPointRecords.setCellValueFactory(new PropertyValueFactory<DetailRecord, String>("clockInPoint"));
            clockOutTimeRecords.setCellValueFactory(new PropertyValueFactory<DetailRecord, String>("clockOutTime"));
            clockOutPointRecords.setCellValueFactory(new PropertyValueFactory<DetailRecord, String>("clockOutPoint"));
            totalPointRecords.setCellValueFactory(new PropertyValueFactory<DetailRecord, String>("totalPoint"));
            entryLocRecords.setCellValueFactory(new PropertyValueFactory<DetailRecord, String>("entryLoc"));
            exitLocRecords.setCellValueFactory(new PropertyValueFactory<DetailRecord, String>("exitLoc"));
            
            recordsTable.setItems(records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
