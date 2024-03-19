/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.classes;

//import controllers.Connect;
//import controllers.connection.Connect;
import controllers.connection.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author USER
 */
public class ReadFile {
    Connect conn = new Connect();
    Statement stat;
    int totalNewEmployees;
    ArrayList<ArrayList<String>> newEmployees = new ArrayList<>();
    
    public void readFile(String fileTitle, String directory) {
        try {            
            FileInputStream fileStream = new FileInputStream(new File(directory+fileTitle));
            Workbook workbook = WorkbookFactory.create(fileStream);
            Sheet sheet = workbook.getSheetAt(0);
            
            DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yyyy").toFormatter();
            
            for(Row row : sheet) {               
                String[] employee = new String[2];//[employee nik, employee name]
                String[] absence = new String[5];//[date, clock in, clock out, entry loc, exit loc]
                for (Cell cell : row) {
                    int cellIndex = cell.getColumnIndex();
                    if (row.getRowNum() > 1) {
                        if (cellIndex == 1) employee[1] = cell.toString();//employee name
                        if (cellIndex == 2) employee[0] = cell.toString();//employee nik
                        if (cellIndex == 0) absence[0] = cell.toString();//date
                        if (cellIndex == 3) absence[1] = cell.toString();//clock in
                        if (cellIndex == 4) absence[2] = cell.toString();//clock out
                        if (cellIndex == 5) absence[3] = cell.toString();//entry location
                        if (cellIndex == 6) absence[4] = cell.toString();//exit location
                    }
                }
                LocalDate lDate = LocalDate.parse(absence[0], formatter);
                int month = lDate.getMonthValue();// 1, 2, 3, 4, ...
                int year = lDate.getYear();//2024, 2023, 2022, ...
                if (checkAbsence(month, year)) {
                    // rencana dibuat ada konfirmasi dialog agar dapat dilakukan export ulang file
                    JOptionPane.showMessageDialog(null, "absensi bulan "+ lDate.getMonth().toString() + " tahun " + year + " telah diupload");
                } else {
                    if (!checkEmployee(employee[0])) {
                        createEmployee(employee);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
    }
    
    private boolean createEmployee(String[] employee) {
        try {
            stat = conn.GetConnection().createStatement();
            String sql = "INSERT INTO employee('nik', 'name') VALUES('"+employee[0]+"', '"+employee[1]+"')";
            if (stat.executeUpdate(sql) != 0) {
                newEmployees.add(new ArrayList<>());
                newEmployees.get(totalNewEmployees).add(employee[0]);
                newEmployees.get(totalNewEmployees).add(employee[1]);
                totalNewEmployees++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
    private boolean checkEmployee(String nik) {
        try {
            stat = conn.GetConnection().createStatement();
            String sql = "SELECT * FROM employee WHERE nik = '"+ nik +"'";
            ResultSet res = stat.executeQuery(sql);
            if (res.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
    private boolean checkAbsence(int month, int year){
        try {
            stat = conn.GetConnection().createStatement();
            String sql = "SELECT id FROM absences WHERE MONTH(date) = '"+month+"' AND YEAR(date) = '"+year+"' ";
            ResultSet res = stat.executeQuery(sql);
            
            if (res.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }   
    
    public static void main(String[] args) {
//        readFile("test absensi feb 2024.xlsx", "E:\\aa-kuliah\\tugas kuliah\\project-akhir-semester\\semester-2\\absensi");
    }
}
