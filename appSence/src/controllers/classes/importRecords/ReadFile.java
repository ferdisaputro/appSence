/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.classes.importRecords;

//import controllers.Connect;
//import controllers.connection.Connect;
import controllers.components.importRecords.ImportOpenFiles.PopupUntrackEmployees;
import controllers.connection.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
    ArrayList<ArrayList<String>> newEmployees;
    String fileTitle;
    String directory;
    //main datas
    HashMap<String, String> employees;
    ArrayList<ArrayList<String>> absences;
    //isAbsenceExist
    boolean absenceExistance = true;
    boolean tglMasuk, nama, nik, jamMasuk, jamKeluar, lokasiMasuk, lokasiKeluar;
    int countAbsences;

    
    CompletableFuture<String> realResult;
    
    public ReadFile(String title, String dir, CompletableFuture<String> realResult, ArrayList<ArrayList<String>> absences, HashMap<String, String> employees) {
        this.fileTitle = title;
        this.directory = dir;
        this.realResult = realResult;
        this.absences = absences;
        this.employees = employees;
    }
    
    public void readFile() {
        this.newEmployees = new ArrayList<>();
        try {            
            FileInputStream fileStream = new FileInputStream(new File(directory + File.separator + fileTitle));
            Workbook workbook = WorkbookFactory.create(fileStream);
            Sheet sheet = workbook.getSheetAt(0);
            countAbsences = 0;

            for(Row row : sheet) {               
//                String[] employee = new String[2];//[employee nik, employee name]
                String[] absence = new String[7];//[date, clock in, clock out, entry loc, exit loc, employee name, employee nik]
                for (Cell cell : row) {
                    int cellIndex = cell.getColumnIndex();
                    if (row.getRowNum() > 1) {
                        if (cellIndex == 0) absence[0] = cell.getStringCellValue();//date
                        if (cellIndex == 3) {
                            if (cell.getCellType() != CellType.BLANK) {
                                String[] stringInTime = cell.toString().split(":");
                                int inHour = Integer.parseInt(stringInTime[0]);
                                int inMinutes = Integer.parseInt(stringInTime[1]);
                                int timeInMinutes = inHour * 60 + inMinutes; //convert clock_in time to minutes
    //                            absence[1] = String.valueOf(timeInMinutes);//clock in
                                absence[1] = cell.getStringCellValue();//clock in
                            } else {
                                absence[1] = null;
                            }
                        }
                        if (cellIndex == 4) {
                            if (cell.getCellType() != CellType.BLANK) {
                                String[] stringOutTime = cell.getStringCellValue().split(":");
                                int outHour = Integer.parseInt(stringOutTime[0]);
                                int outMinutes = Integer.parseInt(stringOutTime[1]);
                                int timeOutMinutes = outHour * 60 + outMinutes; //convert clock_out time to minutes
    //                            absence[2] = String.valueOf(timeOutMinutes);//clock out
                                absence[2] = cell.getStringCellValue();//clock out
                            } else {
                                absence[2] = null;
                            }
                        }
                        if (cellIndex == 5) absence[3] = cell.getStringCellValue();//entry location
                        if (cellIndex == 6) absence[4] = cell.getStringCellValue();//exit location
                        if (cellIndex == 2) absence[5] = cell.getStringCellValue();//employee nik
                        if (cellIndex == 1) absence[6] = cell.getStringCellValue();//employee name
                    }
                }
                if (absence[5] != null) {
                    if (!this.employees.containsKey(absence[5])) {
                        this.employees.put(absence[5], absence[6]);
                    }
                }                
                if (isAbsenceExist(absence)) {
                    // rencana dibuat ada konfirmasi dialog agar dapat dilakukan export ulang file                    
//                    return false;
                    return;
                }
                //this format follows database format
                if (row.getRowNum() > 1) {
                    setAbsences(absence);
                }
            }
//            System.out.println(employees.toString());
            findUntrackEmployees();
            if (newEmployees.size() > 0) {
                showPopup();
            } else {
                realResult.complete("success");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "readFile "+ e);
            e.printStackTrace();
        }
    }
    
    private void setAbsences(String[] absence) {
        try {
            absences.add(new ArrayList<>());
            absences.get(countAbsences).add(absence[5]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = dateFormat.parse(absence[0]);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            absences.get(countAbsences).add(sqlDate.toString());
            absences.get(countAbsences).add(absence[1]);
            absences.get(countAbsences).add(absence[2]);
            absences.get(countAbsences).add(absence[3]);
            absences.get(countAbsences).add(absence[4]);
            countAbsences++;
        } catch (ParseException ex) {
            Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void showPopup() throws IOException {
        Stage stage = new Stage();
        FXMLLoader root = new FXMLLoader(getClass().getResource("/resources/components/popupUntrackEmployees.fxml"));
        Scene scene = new Scene(root.load());
//        stage.setResizable(false);
        PopupUntrackEmployees untrack = root.getController();
        untrack.setNewEmployees(newEmployees);
        untrack.setResult(realResult);
        stage.setScene(scene);
        stage.show();
    }
    
//    private void insertEmployee(String[] employee) {
//        try {
//            stat = conn.GetConnection().createStatement();
//            String sql = "INSERT INTO employee(nik, name) VALUES('"+employee[0]+"', '"+employee[1]+"')";
//            if (stat.executeUpdate(sql) != 0) {
//                return;
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
    
    private boolean findUntrackEmployees() {
        HashMap<String, String> tempEmployees = new HashMap<>(this.employees);
        try {
            stat = conn.GetConnection().createStatement();
            String sql = "SELECT nik FROM employee WHERE nik IN(";
            for (String key : tempEmployees.keySet()) {
                sql += "\""+key+"\",";
            }
            sql = sql.substring(0, sql.length() -1);
            sql += ")";
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {                
                String nik = res.getString("nik");
                if (tempEmployees.containsKey(nik)) {
                    tempEmployees.remove(nik);
                }
            }
            setNewEmployee(tempEmployees);
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
    private void setNewEmployee(HashMap<String, String> tempEmployees) {
        int totalNewEmployees = 0;
        for (Map.Entry<String, String> entry : tempEmployees.entrySet()) {
            newEmployees.add(new ArrayList<>());
            newEmployees.get(totalNewEmployees).add(entry.getKey());
            newEmployees.get(totalNewEmployees).add(entry.getValue());
            totalNewEmployees++;
        }
    }
    
    private boolean isAbsenceExist(String[] absence){
        if (absence[0] != null && absenceExistance == true) {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yyyy").toFormatter();
            LocalDate lDate = LocalDate.parse(absence[0], formatter);
            int month = lDate.getMonthValue();// 1, 2, 3, 4, ...
            int year = lDate.getYear();//2024, 2023, 2022, ...
            try {
                stat = conn.GetConnection().createStatement();
                String sql = "SELECT id FROM absences WHERE MONTH(date) = '"+month+"' AND YEAR(date) = '"+year+"' ";
                ResultSet res = stat.executeQuery(sql);
                if (res.next()) {
                    JOptionPane.showMessageDialog(null, "absensi bulan "+ lDate.getMonth().toString() + " tahun " + year + " telah diupload");
                    return true;
                } else {
                    absenceExistance = false;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return false;
    }   
    
//    private boolean createEmployee(String[] employee) {
//        try {
//            stat = conn.GetConnection().createStatement();
//            String sql = "INSERT INTO employee('nik', 'name') VALUES('"+employee[0]+"', '"+employee[1]+"')";
//            if (stat.executeUpdate(sql) != 0) {
//                newEmployees.add(new ArrayList<>());
//                newEmployees.get(totalNewEmployees).add(employee[0]);
//                newEmployees.get(totalNewEmployees).add(employee[1]);
//                totalNewEmployees++;
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return false;
//    }
}
