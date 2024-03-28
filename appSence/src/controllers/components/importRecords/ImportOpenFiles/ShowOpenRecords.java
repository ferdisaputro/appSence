/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.components.importRecords.ImportOpenFiles;

import controllers.connection.Connect;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.importRecords.EmployeeRecordVariable;
import models.importRecords.OpenRecordsData;

/**
 *
 * @author USER
 */
public class ShowOpenRecords {
    TableView<OpenRecordsData> openRecordTable;
    TableColumn<OpenRecordsData, String> openRecordId, openRecordTitle, openRecordTimeRange, openRecordAction;
    Connect conn = new Connect();
    Statement stat;
    String sql;
    ObservableList<OpenRecordsData> recordDatas;
    EmployeeRecordVariable erv;
    
    public ShowOpenRecords (TableView<OpenRecordsData> openRecordTable, 
            TableColumn<OpenRecordsData, String> openRecordId, 
            TableColumn<OpenRecordsData, String> openRecordTitle, 
            TableColumn<OpenRecordsData, String> openRecordTimeRange, 
            TableColumn<OpenRecordsData, String> openRecordAction,
            EmployeeRecordVariable erv) {
        this.openRecordTable = openRecordTable;
        this.openRecordId = openRecordId;
        this.openRecordTitle = openRecordTitle;
        this.openRecordTimeRange = openRecordTimeRange;
        this.openRecordAction = openRecordAction;
        this.erv = erv;
//        this.recordDatas =  FXCollections.observableArrayList();
    }
    
    public void show() {
        getRecords();
    }
    
    private boolean getRecords (){
        recordDatas = FXCollections.observableArrayList();
        try {
            stat = conn.GetConnection().createStatement();
            sql = "SELECT * FROM absences";
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                recordDatas.add(new OpenRecordsData(
                    res.getString("id"),
                    res.getString("title"),
                    res.getString("date"),
                    res.getString("created_at"),
                    this.erv
                ));
            }
            
            openRecordId.setCellValueFactory(new PropertyValueFactory<OpenRecordsData, String>("id"));
            openRecordTitle.setCellValueFactory(new PropertyValueFactory<OpenRecordsData, String>("title"));
            openRecordTimeRange.setCellValueFactory(new PropertyValueFactory<OpenRecordsData, String>("createdAt"));
            openRecordAction.setCellValueFactory(new PropertyValueFactory<OpenRecordsData, String>("openButton"));
            openRecordTable.setItems(recordDatas);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
