/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.components.importRecords.showAbsenceRecord;

import controllers.connection.Connect;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import models.importRecords.AbsenceRecord;

/**
 *
 * @author USER
 */
public class ShowAbsenceRecords {
    String sql, absenceMonth, absenceYear;
    int absenceId;
    Connect conn = new Connect();
    Statement stat;
    ObservableList<AbsenceRecord> absenceRecords;
    ArrayList<HashMap<String, String>> absences;
    HashMap<String, ArrayList<HashMap<String, Integer>>> permitsPoint;
    
    public ShowAbsenceRecords(int absenceId, ObservableList<AbsenceRecord> absenceRecords) {
        this.absenceRecords = absenceRecords;
        this.absenceId = absenceId;
        getAbsenceData();
        
        //not implemented feature
    }
    
    private void getAbsenceData () {
        sql = "SELECT MONTH(absences.date) AS month, YEAR(absences.date) AS year FROM absences WHERE absences.id = " + absenceId;
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                absenceMonth = res.getString("month");
                absenceYear = res.getString("year");
            }
            getRecords();
            
            
            getPermitsPoint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void getRecords() {
        absences = new ArrayList();
        sql = "SELECT " +
                "    nik, " +
                "    id_schedule, " +
                "    name, " +
                "    employee_code, " +
                "    start, " +
                "    end, " +
                "    description, " +
                "    MAX(total_cuti_ijin) AS total_cuti_ijin, " +
                "    MAX(total_alfa) AS total_alfa, " +
//                "    MAX(total_cuti_ijin) + MAX(total_alfa) AS sub_total_permit, " +
                "    MAX(clock_in_point) + MAX(clock_out_point) AS sub_total_time " +
                "FROM ( " +
                "    SELECT  " +
                "        e.nik, " +
                "        e.id_schedule, " +
                "        e.name, " +
                "        e.employee_code, " +
                "        s.*, " +
                "        COALESCE(COUNT(CASE WHEN p.type = 'cuti/ijin' THEN p.id END), 0) AS total_cuti_ijin, " +
                "        COALESCE(COUNT(CASE WHEN p.type = 'alfa' THEN p.id END), 0) AS total_alfa, " +
                "        NULL AS clock_in_point, " +
                "        NULL AS clock_out_point " +
                "    FROM  " +
                "        employee e " +
                "    LEFT JOIN  " +
                "        permits p ON e.nik = p.nik_employee  " +
                "                AND MONTH(p.date) = " + absenceMonth +
                "                AND YEAR(p.date) = " + absenceYear +
                "    INNER JOIN  " +
                "        schedules s ON e.id_schedule = s.id  " +
                "    GROUP BY  " +
                "        e.nik " +
                "    UNION ALL " +
                "    SELECT  " +
                "        e.nik, " +
                "        e.id_schedule, " +
                "        e.name, " +
                "        e.employee_code, " +
                "        s.*, " +
                "        NULL AS total_cuti_ijin, " +
                "        NULL AS total_alfa, " +
                "        SUM(da.clock_in_point) AS clock_in_point, " +
                "        SUM(da.clock_out_point) AS clock_out_point " +
                "    FROM  " +
                "        employee e " +
                "    LEFT JOIN  " +
                "        detail_absence da ON e.nik = da.nik_employee " +
                "                AND da.id_absences =  " + absenceId +
                "    INNER JOIN  " +
                "        schedules s ON e.id_schedule = s.id  " +
                "    GROUP BY  " +
                "        e.nik " +
                ") AS combined_data " +
                "GROUP BY nik " +
                "ORDER BY sub_total_time DESC, total_cuti_ijin ASC, total_alfa ASC";        
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            int absencesCount = 0;
            while (res.next()) {                
                absences.add(new HashMap());
                absences.get(absencesCount).put("nik", res.getString("nik"));
                absences.get(absencesCount).put("name", res.getString("name"));
                absences.get(absencesCount).put("employee_code", res.getString("employee_code"));
                absences.get(absencesCount).put("schedule", res.getString("start") + " - " + res.getString("end") + " (" +  res.getString("description") + ")");
                absences.get(absencesCount).put("total_cuti_ijin", res.getString("total_cuti_ijin"));
                absences.get(absencesCount).put("total_alfa", res.getString("total_alfa"));
                absences.get(absencesCount).put("sub_total_time", res.getString("sub_total_time"));
                absencesCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //not implemented feature
    private void getPermitsPoint() {
        permitsPoint = new HashMap<>();
        permitsPoint.put("cuti/ijin", new ArrayList<>());
        permitsPoint.put("alfa", new ArrayList<>());
        sql = "SELECT * FROM permit_point_requirements ORDER BY max DESC";
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            int permitsCount = 0;
            while (res.next()) {
                HashMap<String, Integer> hashPoint = new HashMap<>();
                hashPoint.put("min", Integer.parseInt(res.getString("min")));
                hashPoint.put("max", Integer.parseInt(res.getString("max")));
                hashPoint.put("point", Integer.parseInt(res.getString("point")));
                permitsPoint.get(res.getString("permit_type")).add(hashPoint);
            }
            validateAbsences();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void validateAbsences () {
        for (HashMap<String, String> absence : absences) {
            int totalAlfa = Integer.parseInt(absence.get("total_alfa"));
            int totalCutiIjin = Integer.parseInt(absence.get("total_cuti_ijin"));
            int totalPermitsPoint = 0;
            if (totalAlfa >= permitsPoint.get("alfa").get(0).get("max")) {
                totalPermitsPoint = 0;
            } else {
                for (HashMap<String, Integer> alfa : permitsPoint.get("alfa")) {
                    if (totalAlfa >= alfa.get("min") && totalAlfa <= alfa.get("max")) {
                        totalPermitsPoint += alfa.get("point");
                    }
                }
                
                if (totalCutiIjin > permitsPoint.get("cuti/ijin").get(0).get("max")) {
                    totalPermitsPoint = 0;
                } else {
                    for (HashMap<String, Integer> cutiIjin : permitsPoint.get("cuti/ijin")) {
                        if (totalCutiIjin >= cutiIjin.get("min") && totalCutiIjin <= cutiIjin.get("max")) {
                            totalPermitsPoint += cutiIjin.get("point");
                        }
                    }
                }
            }
            
            absence.put("total_permits_point", String.valueOf(totalPermitsPoint));
            absence.put("total_point", 
                        String.valueOf(totalPermitsPoint + Integer.parseInt(absence.get("sub_total_time")))
                    );
            
            absenceRecords.add(new AbsenceRecord(
                absence.get("nik"),
                absence.get("name"),
                absence.get("schedule"),
                absence.get("employee_code"),
                absence.get("total_cuti_ijin"),
                absence.get("total_alfa"),
                absence.get("total_permits_point"),
                absence.get("sub_total_time"),
                absence.get("total_point")
            ));
        }
    }
    
    public void show(
            TableColumn<AbsenceRecord, String> nameEmployees, 
            TableColumn<AbsenceRecord, String> nikEmployees, 
            TableColumn<AbsenceRecord, String> timePointEmployees, 
            TableColumn<AbsenceRecord, String> permitPointEmployees,
            TableColumn<AbsenceRecord, String> totalPointEmployees, 
            TableView<AbsenceRecord> employeesTable, 
            Label dateField) {
        String capitalDate = Month.of(Integer.parseInt(absenceMonth)).toString() + "-" + absenceYear;
        capitalDate = capitalDate.substring(0, 1).toUpperCase() + capitalDate.substring(1).toLowerCase();
        dateField.setText(capitalDate);
        nikEmployees.setCellValueFactory(new PropertyValueFactory<AbsenceRecord, String>("nik"));
        nameEmployees.setCellValueFactory(new PropertyValueFactory<AbsenceRecord, String>("name"));
        timePointEmployees.setCellValueFactory(new PropertyValueFactory<AbsenceRecord, String>("subTotalTime"));
        permitPointEmployees.setCellValueFactory(new PropertyValueFactory<AbsenceRecord, String>("subTotalPermit"));
        totalPointEmployees.setCellValueFactory(new PropertyValueFactory<AbsenceRecord, String>("totalPoint"));
        employeesTable.setItems(absenceRecords);
    }
    
}
