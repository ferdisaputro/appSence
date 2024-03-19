package controllers;

import controllers.classes.ReadFile;
import controllers.connection.Connect;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;


public class AbsenceController implements Initializable {
    Connect conn = new Connect();
    Statement stat;
    
    @FXML
    private TextField alfaPoint;
    @FXML
    private TextField cutiPoint;
    @FXML
    private Label dateField;
    @FXML
    private TableView<?> employeesTable;
    @FXML
    private TextField ijinPoint;
    @FXML
    private Button importFile;
    @FXML
    private ComboBox<?> openRecord;
    @FXML
    private Label permitsPointCard;
    @FXML
    private TableView<?> recordsTable;
    @FXML
    private TextField sakitPoint;
    @FXML
    private TextField subTotalPoint;
    @FXML
    private TableView<?> timeAccuracyTable;
    @FXML
    private Label timingPointCard;
    @FXML
    private Label totalPointCard;
    @FXML
    private Label fileName;
    
    
    //for insert new employees
//    int totalNewEmployees;
//    ArrayList<ArrayList<String>> newEmployees = new ArrayList<>();
//    
//    void readFile(String fileTitle, String directory) {
//        try {            
//            FileInputStream fileStream = new FileInputStream(new File(directory+fileTitle));
//            Workbook workbook = WorkbookFactory.create(fileStream);
//            Sheet sheet = workbook.getSheetAt(0);
//            
//            DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yyyy").toFormatter();
//            
//            for(Row row : sheet) {               
//                String[] employee = new String[2];//[employee nik, employee name]
//                String[] absence = new String[5];//[date, clock in, clock out, entry loc, exit loc]
//                for (Cell cell : row) {
//                    int cellIndex = cell.getColumnIndex();
//                    if (row.getRowNum() > 1) {
//                        if (cellIndex == 1) employee[1] = cell.toString();//employee name
//                        if (cellIndex == 2) employee[0] = cell.toString();//employee nik
//                        if (cellIndex == 0) absence[0] = cell.toString();//date
//                        if (cellIndex == 3) absence[1] = cell.toString();//clock in
//                        if (cellIndex == 4) absence[2] = cell.toString();//clock out
//                        if (cellIndex == 5) absence[3] = cell.toString();//entry location
//                        if (cellIndex == 6) absence[4] = cell.toString();//exit location
//                    }
//                }
//                LocalDate lDate = LocalDate.parse(absence[0], formatter);
//                int month = lDate.getMonthValue();// 1, 2, 3, 4, ...
//                int year = lDate.getYear();//2024, 2023, 2022, ...
//                if (checkAbsence(month, year)) {
//                    // rencana dibuat ada konfirmasi dialog agar dapat dilakukan export ulang file
//                    JOptionPane.showMessageDialog(null, "absensi bulan "+ lDate.getMonth().toString() + " tahun " + year + " telah diupload");
//                } else {
//                    if (!checkEmployee(employee[0])) {
//                        createEmployee(employee);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, e);
//            e.printStackTrace();
//        }
//    }
//    
//    boolean createEmployee(String[] employee) {
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
//    
//    boolean checkEmployee(String nik) {
//        try {
//            stat = conn.GetConnection().createStatement();
//            String sql = "SELECT * FROM employee WHERE nik = '"+ nik +"'";
//            ResultSet res = stat.executeQuery(sql);
//            if (res.next()) {
//                return true;
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return false;
//    }
//    
//    boolean checkAbsence(int month, int year){
//        try {
//            stat = conn.GetConnection().createStatement();
//            String sql = "SELECT id FROM absences WHERE MONTH(date) = '"+month+"' AND YEAR(date) = '"+year+"' ";
//            ResultSet res = stat.executeQuery(sql);
//            
//            if (res.next()) {
//                return true;
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return false;
//    }   
//    
//    
//    
//    
    
    
    
    @FXML
    void importFile() {
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String fileTitle = dialog.getFile();
        String directory = dialog.getDirectory();
        String filePath = directory + File.separator + fileTitle;

        dialog.dispose();
        fileName.setText(fileTitle);
        System.out.println(filePath);
        ReadFile read = new ReadFile();
        read.readFile(fileTitle, directory);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

}

