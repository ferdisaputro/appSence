/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers.components.importRecords.ImportOpenFiles;

import controllers.connection.Connect;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import models.importRecords.UntrackEmployee;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class PopupUntrackEmployees implements Initializable{
    private ArrayList<ArrayList<String>> newEmployees;
    ObservableList<UntrackEmployee> datas;
    ObservableList<String> schedules;
    boolean result;
    CompletableFuture<String> realResult;
    
    @FXML
    private AnchorPane popupUntrackContainer;
    @FXML
    private TableView<UntrackEmployee> tableLists;
    @FXML
    private TableColumn<UntrackEmployee, String> name, nik, idEmployee, shift;

    public void setNewEmployees(ArrayList<ArrayList<String>> employees) {
        this.newEmployees = employees;
    }
    
    public void setResult(CompletableFuture<String> realResult){
        this.realResult = realResult;
    }
    
//    public void showPopup() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("/resources/components/popupUntrackEmployees.fxml"));
//        Scene scene = new Scene(root);
//        stage.setResizable(false);
//        stage.setScene(scene);
//        stage.show();
//    }

    private void setSchedule() {
        schedules = FXCollections.observableArrayList();
        //make a combobox options
        try {
            Connect cn = new Connect();
            Statement stat = cn.GetConnection().createStatement();
            String sql = "SELECT * FROM schedules";
            ResultSet res = stat.executeQuery(sql);
            while(res.next()){
                String schedule = res.getString("id")+" | "+res.getString("description")+" "+res.getString("start")+"-"+res.getString("end");
                schedules.add(schedule);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "setSchedule "+ e);
        }
    }
    
    private void setTable() {
        datas = FXCollections.observableArrayList();
        for (ArrayList<String> newEmployee : newEmployees) {
            UntrackEmployee employee = new UntrackEmployee(newEmployee.get(0), newEmployee.get(1), schedules);
            datas.add(employee);
        }

//        System.out.println(newEmployees.toString());
        nik.setCellValueFactory(new PropertyValueFactory<UntrackEmployee, String>("nik"));
        name.setCellValueFactory(new PropertyValueFactory<UntrackEmployee, String>("name"));
        idEmployee.setCellValueFactory(new PropertyValueFactory<UntrackEmployee, String>("idEmployee"));
        shift.setCellValueFactory(new PropertyValueFactory<UntrackEmployee, String>("shift"));
        tableLists.setItems(datas);
    }
    
    @FXML
    private void createEmployee(MouseEvent event) {
        ArrayList<ArrayList<String>> employees = new ArrayList<>();
        int countEmployees = 0;
        for (UntrackEmployee data : datas) {
            employees.add(new ArrayList<>());
            employees.get(countEmployees).add(data.getNik());
            
            if (!data.getIdEmployee().getText().isEmpty()) employees.get(countEmployees).add(data.getIdEmployee().getText());
            else employees.get(countEmployees).add(null);
            
            if (!data.getName().getText().isEmpty()) employees.get(countEmployees).add(data.getName().getText());
            else employees.get(countEmployees).add(null);
            
            if (data.getShift().getValue() != null) employees.get(countEmployees).add(data.getShift().getValue().toString().split(" | ")[0]);
            else employees.get(countEmployees).add(null);
            
            countEmployees++;
        }
        if (validateValue(employees)) {
            if (insertEmployees(employees)) {
                JOptionPane.showMessageDialog(null, "New employee are added.");
                Stage stage = (Stage) popupUntrackContainer.getScene().getWindow();
                stage.close();
                realResult.complete("success");
            } else {
                JOptionPane.showMessageDialog(null, "Error while adding employee, try again.");
                realResult.complete("failed");
            }
        }
    }
    
    public boolean getResult() {
        return this.result;
    }
    
    private boolean insertEmployees(ArrayList<ArrayList<String>> employees) {
        try {
            Connect conn = new Connect();
            Statement stat = conn.GetConnection().createStatement();
            String sql = "INSERT INTO employee(nik, employee_code, name, id_schedule) VALUES ";
            for (ArrayList<String> employee : employees) {
                sql += "(\""+employee.get(0)+"\", \""+employee.get(1)+"\", \""+employee.get(2)+"\", \""+employee.get(3)+"\"),";
            }
            sql = sql.substring(0, sql.length()-1);
            stat.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
    private boolean validateValue(ArrayList<ArrayList<String>> employees) {
        String message = "";
        for (ArrayList<String> employee : employees) {
            if (employee.get(3) == null) {
                message += employee.get(2) + ", ";
            }
        }
        if (message == "") return true;
        else message = message.substring(0, message.length() -2);
        
        JOptionPane.showMessageDialog(null, "the shift columns for " + message + " have not been filled in.");
        return false;
    }

    @FXML
    private void cancelCreateEmployee(MouseEvent event) {
        Stage stage = (Stage) popupUntrackContainer.getScene().getWindow();
        stage.close();
        realResult.complete("cancel");
    }
        
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setSchedule();
            setTable();
        });
    }    
    
}
