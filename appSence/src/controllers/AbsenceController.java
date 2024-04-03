package controllers;

import controllers.classes.importRecords.ProcessDatas;
import controllers.classes.importRecords.ReadFile;
import controllers.components.detailAbsence.ShowDetailAbsence;
import controllers.components.importRecords.ImportOpenFiles.ShowOpenRecords;
import controllers.components.importRecords.showAbsenceRecord.ShowAbsenceRecords;
import controllers.connection.Connect;
import java.awt.FileDialog;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.importRecords.AbsenceRecord;
import models.importRecords.EmployeeRecordVariable;
import models.importRecords.OpenRecordsData;
import models.importRecords.details.DetailRecord;
import models.importRecords.details.PermitDetail;
import models.importRecords.details.TimeDetail;
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
    EmployeeRecordVariable erv = new EmployeeRecordVariable();
    AbsenceRecord record;
    
//    @FXML
//    private TextField alfaPoint, cutiPoint, ijinPoint, sakitPoint, subTotalPoint;
    @FXML
    private TextField prevTitle;
    @FXML
    private Label fileName;
    
    // employee data
    @FXML
    private TextField nikEmployee, codeEmployee, nameEmployee, workHourEmployee;
    
    //card
    @FXML
    private Label permitPointCard, timingPointCard, totalPointCard;
    
    @FXML
    private TableView<?> tableLists;
    @FXML
    private Button importFile;
    

    //details
        //permits
        @FXML
        private TextField cutiIjinTotalDetail, alfaTotalDetail, totalPointPermitDetail;
        @FXML
        private TableView<PermitDetail> permitTableDetail;
        @FXML
        private TableColumn<PermitDetail, String> permitTypeDetail, permitDateDetail, permitDescDetail;

        //timeAcuracy
        @FXML
        private Label totalPointTimeDetail;
        @FXML
        private Label avgClockIn, avgClockOut, timeAccuracy;
        
        //records
    @FXML
    private TableColumn<DetailRecord, String> dateRecords, 
            clockInPointRecords, clockInTimeRecords, 
            clockOutTimeRecords, clockOutPointRecords, 
            totalPointRecords, entryLocRecords, exitLocRecords;
    @FXML
    private TableView<DetailRecord> recordsTable;
    
    
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
                ProcessDatas process = new ProcessDatas(prevTitle.getText(), absences, erv);
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
    
    public void detailAbsence() {
        ShowDetailAbsence detail = new ShowDetailAbsence(record, erv.getId());
        detail.setPoints(permitPointCard, timingPointCard, totalPointCard);
        detail.showDetailPermit(
                cutiIjinTotalDetail, alfaTotalDetail, totalPointPermitDetail, 
                permitTypeDetail, permitDateDetail, permitDescDetail, permitTableDetail);
        detail.showDetailTime(totalPointTimeDetail, avgClockIn, avgClockOut, timeAccuracy);
        detail.showEmployeeData(nikEmployee, codeEmployee, nameEmployee, workHourEmployee);
        detail.showRecords(dateRecords, 
                clockInPointRecords, clockInTimeRecords, 
                clockOutTimeRecords, clockOutPointRecords, 
                totalPointRecords, entryLocRecords, exitLocRecords, recordsTable);
//        detail.show();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        ShowOpenRecords openRecord = new ShowOpenRecords(openRecordTable, openRecordId, openRecordTitle, openRecordTimeRange, openRecordAction, erv);
        openRecord.show();

        erv.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                showAbsences();
            }

            public void showAbsences() {
                ObservableList<AbsenceRecord> absenceRecords;
                absenceRecords = FXCollections.observableArrayList();
                ShowAbsenceRecords records = new ShowAbsenceRecords(erv.getId(), absenceRecords);
                records.show(nameEmployees, nikEmployees, timePointEmployees, permitPointEmployees, totalPointEmployees, employeesTable, dateField);
                openRecord.show();
            }
        });
        
        
        
        employeesTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                record = employeesTable.getSelectionModel().getSelectedItem();
                if (record.getNik() != null) {
                    detailAbsence();
                }
            }
        });
    }    

}




