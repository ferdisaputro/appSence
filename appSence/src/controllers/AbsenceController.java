package controllers;

import controllers.classes.importRecords.ProcessDatas;
import controllers.classes.importRecords.ReadFile;
import controllers.components.importRecords.ImportOpenFiles.ShowOpenRecords;
import controllers.components.importRecords.showAbsenceRecord.ShowAbsenceRecords;
import controllers.connection.Connect;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import models.importRecords.AbsenceRecord;
import models.importRecords.AbsenceRecord;
import models.importRecords.OpenRecordsData;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;

public class AbsenceController implements Initializable {
    Connect conn = new Connect();
    Statement stat;
    CompletableFuture<String> realResult;
    ArrayList<ArrayList<String>> absences;
    HashMap<String, String> employees;
    String fileTitle;
    String directory;
    
    @FXML
    private TextField alfaPoint, cutiPoint, ijinPoint, sakitPoint, subTotalPoint, prevTitle;
    @FXML
    private Label permitsPointCard, timingPointCard, fileName, totalPointCard;
    @FXML
    private TableView<?> recordsTable, timeAccuracyTable, tableLists;
    @FXML
    private Button importFile;
    
    //employees record
    @FXML
    private Label dateField;
    @FXML
    private TableView<AbsenceRecord> employeesTable;
    @FXML
    private TableColumn<AbsenceRecord, String> nameEmployees, nikEmployees, timePointEmployees, permitPointEmployees, totalPointEmployees;
    
    //open record id
    @FXML
    private TableView<OpenRecordsData> openRecordTable;
    @FXML
    private TableColumn<OpenRecordsData, String> openRecordId, openRecordTitle, openRecordTimeRange, openRecordAction;


    @FXML
    void importFile() {
        realResult = new CompletableFuture<>();
        absences = new ArrayList<>();
        employees = new HashMap<>();
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        fileTitle = dialog.getFile();
        directory = dialog.getDirectory();
        if (fileTitle == null && directory == null) {
            return;
        }
        dialog.dispose();
        importFile.setText("Choosen file : "+ fileTitle);
        prevTitle.setText(fileTitle.split("\\.")[0]);
    }
    
    @FXML
    void openImportFile() {
        ReadFile read = new ReadFile(fileTitle, directory, realResult, absences, employees);
        read.readFile();
        realResult.thenAccept(result -> {
            if ("success".equals(result)) {
                ProcessDatas process = new ProcessDatas(prevTitle.getText(), absences);
                process.proceed();
            } else if ("failed".equals(result)) {
                System.out.println("Failed insert employee");
            } else if ("cancel".equals(result)) {
                System.out.println("import canceled");
                absences.clear();
                fileName.setText("");
            }
        });
    }
    
    public void setAbsenceRecord() {
        ObservableList<AbsenceRecord> absenceRecords;
        absenceRecords = FXCollections.observableArrayList();
        ShowAbsenceRecords records = new ShowAbsenceRecords(33, absenceRecords);
        records.getRecords();
        records.show(nameEmployees, nikEmployees, timePointEmployees, permitPointEmployees, totalPointEmployees, employeesTable, dateField);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        realResult = new CompletableFuture<>();
//        absences = new ArrayList<>();
//        employees = new HashMap<>();
//        ReadFile read = new ReadFile("test absensi feb 2024.xlsx", "E:\\aa-kuliah\\tugas kuliah\\project-akhir-semester\\semester-2\\absensi", realResult, absences, employees);
//        read.readFile();
//        ProcessDatas process = new ProcessDatas("test ajah", absences);
//        process.proceed();
//        realResult.thenAccept(result -> {
//            if ("success".equals(result)) {
//                ProcessDatas process = new ProcessDatas("test ajah", absences);
//                process.proceed();
//            } else if ("failed".equals(result)) {
//                System.out.println("Failed insert employee");
//            } else if ("cancel".equals(result)) {
//                System.out.println("import canceled");
//                absences.clear();
//                fileName.setText("");
//            }
//        });

        

        ShowOpenRecords openRecord = new ShowOpenRecords(openRecordTable, openRecordId, openRecordTitle, openRecordTimeRange, openRecordAction);
        openRecord.show();
        
        
        setAbsenceRecord();
    }    

}

