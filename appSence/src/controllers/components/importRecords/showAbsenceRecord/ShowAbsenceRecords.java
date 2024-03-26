/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.components.importRecords.showAbsenceRecord;

import controllers.connection.Connect;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Month;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.importRecords.AbsenceRecord;

/**
 *
 * @author USER
 */
public class ShowAbsenceRecords {
    String sql, absenceMonth, absenceYear;
    Connect conn = new Connect();
    Statement stat;
    ObservableList<AbsenceRecord> absenceRecords;
    
    public ShowAbsenceRecords(int absenceId, ObservableList<AbsenceRecord> absenceRecords) {
        this.absenceRecords = absenceRecords;
        getAbsenceData(absenceId);
    }
    
    private void getAbsenceData (int id) {
        sql = "SELECT MONTH(absences.date) AS month, YEAR(absences.date) AS year FROM absences WHERE absences.id = " + id;
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                absenceMonth = res.getString("month");
                absenceYear = res.getString("year");
            }
            getRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void getRecords() {
        sql = "SELECT " +
                "    nik, " +
                "    name, " +
//                "    MAX(total_cuti) AS total_cuti, " +
//                "    MAX(total_ijin) AS total_ijin, " +
//                "    MAX(total_alfa) AS total_alfa, " +
//                "    MAX(clock_in_point) AS clock_in_point, " +
//                "    MAX(clock_out_point) AS clock_out_point " +
                "    MAX(total_cuti) + MAX(total_ijin) + MAX(total_alfa) AS sub_total_permit, " +
                "    MAX(clock_in_point) + MAX(clock_out_point) AS sub_total_time, " +
                "    MAX(total_cuti) + MAX(total_ijin) + MAX(total_alfa) + MAX(clock_in_point) + MAX(clock_out_point) AS total_point " + 
                "FROM ( " +
                "    SELECT  " +
                "        e.nik, " +
                "        e.name, " +
                "        COALESCE(COUNT(CASE WHEN p.type = 'cuti' THEN p.id END), 0) AS total_cuti, " +
                "        COALESCE(COUNT(CASE WHEN p.type = 'ijin' THEN p.id END), 0) AS total_ijin, " +
                "        COALESCE(COUNT(CASE WHEN p.type = 'alfa' THEN p.id END), 0) AS total_alfa, " +
                "        NULL AS clock_in_point, " +
                "        NULL AS clock_out_point " +
                "    FROM  " +
                "        employee e " +
                "    LEFT JOIN  " +
                "        permits p ON e.nik = p.nik_employee  " +
                "                AND MONTH(p.date) = " + absenceMonth +
                "                AND YEAR(p.date) = " + absenceYear +
                "    GROUP BY  " +
                "        e.nik " +
                "    UNION ALL " +
                "    SELECT  " +
                "        e.nik, " +
                "        e.name, " +
                "        NULL AS total_cuti, " +
                "        NULL AS total_ijin, " +
                "        NULL AS total_alfa, " +
                "        SUM(da.clock_in_point) AS clock_in_point, " +
                "        SUM(da.clock_out_point) AS clock_out_point " +
                "    FROM  " +
                "        employee e " +
                "    LEFT JOIN  " +
                "        detail_absence da ON e.nik = da.nik_employee " +
                "                AND MONTH(da.absence_date) = 2  " +
                "                AND YEAR(da.absence_date) = 2024 " +
                "    GROUP BY  " +
                "        e.nik " +
                ") AS combined_data " +
                "GROUP BY nik;";        
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {                
                absenceRecords.add(new AbsenceRecord(
                    res.getString("nik"),
                    res.getString("name"),
                    res.getString("sub_total_permit"),
                    res.getString("sub_total_time"),
                    res.getString("total_point")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
