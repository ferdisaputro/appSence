/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;


import controllers.classes.MyEmployee.PermitClassTable;
import controllers.classes.MyEmployee.DailyActivityTable;
import controllers.classes.MyEmployee.Employee;
import controllers.classes.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import controllers.connection.Connect;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class EmployeeController implements Initializable {
//    Main    
    String q = null;
    Connection con = null ;
    PreparedStatement ps = null ;
    ResultSet rs = null ;
    boolean edited = false;
    Employee employee;
    Employee selectedEmployee;
    
    
//    init
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{ 
            con = DriverManager.getConnection("jdbc:mysql://localhost/app-sence","root","");
        }catch(SQLException e){
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, e);
        }
        OptionPList();
        setShift();
        selectDay();
        TableKlikUpdate();
        DatatableFC();
        showHiddenButton();
    }
     
//    =====================FORM CONTROL SOURCE CODE=============================
    
    @FXML
    private Button add_Button;
    @FXML
    private HBox updateButtonContainer;
    @FXML
    private VBox ButtonParent;
    @FXML
    private TextField txt_nikE;
    @FXML
    private TextField txt_idE;
    @FXML
    private TextField txt_nameE;
    @FXML
    private TextField txt_findE;
    @FXML
    private ComboBox<String> Option_E;
    @FXML
    private TableView<Employee> table_E;
    @FXML
    private TableColumn<Employee, String> col_idE;
    @FXML
    private TableColumn<Employee, String>col_name;
    @FXML
    private TableColumn<Employee, String>col_nik;
    @FXML
    private TableColumn<Employee, String> PermitAction;
    @FXML
    void btn_addE() {
        Add_Employee();
    }
    
    @FXML
    void btn_findE() {    
        FindEmployee();
    }
    
    @FXML
    void Update() {
        updateEmployee();
    }
    
    @FXML
    void cancel() {
        edited = false;
        showHiddenButton(); 
        setForm(employee);
        clean();
    }
    
    public void clearfields (){
        txt_findE.clear();
        option_P.setValue(null);
        date.setValue(null);
    }
    public void clean(){
        txt_nikE.clear();
        txt_idE.clear();
        txt_nameE.clear();
        Option_E.setValue(null);
    }
            
    private ObservableList<Employee>DatatableFC() {
    ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        txt_findE.setPromptText("Masukkan NIK atau ID Karyawan");
        try{
           q = "SELECT * FROM employee";
           ps = con.prepareStatement(q);
           rs = ps.executeQuery();
           
           while(rs.next()){
                employeeList.add(new Employee(
                        rs.getString("nik"),
                        rs.getString("employee_code"),
                        rs.getString("name"),
                        rs.getInt("id_schedule"),
                        this
                ));
            }
            
            col_nik.setCellValueFactory(new PropertyValueFactory<Employee, String>("nik"));
            col_idE.setCellValueFactory(new PropertyValueFactory<Employee, String>("employee_code"));
            col_name.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
            PermitAction.setCellValueFactory(new PropertyValueFactory<Employee, String>("button"));
           
           table_E.setItems(employeeList);
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
                alert.showAndWait();
        }
        return employeeList;
    }
    
    public void UpdateTable(){
        ObservableList<Employee> newdata = DatatableFC();
        table_E.setItems(newdata);
    }
    
    public void Add_Employee(){
        String nik = txt_nikE.getText();
        String id = txt_idE.getText();
        String nm = txt_nameE.getText(); 
        String shift = Option_E.getValue();
        String[] getid = shift.split("//|");
        String getShift = getid[0].trim();
       if (nik.isEmpty() || id.isEmpty() || nm.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();

        } else {
        try{
            q = "INSERT INTO employee (nik,employee_code,name,id_schedule) VALUES (?,?,?,?)";
            ps = con.prepareStatement(q);
            ps.setString (1,nik);
            ps.setString (2,id);
            ps.setString (3,nm);
            ps.setString(4,getShift);
            
            
            int rowsInserted = ps.executeUpdate();
            if(rowsInserted > 0){
                System.out.println("Succes");
                clearfields();
                UpdateTable();
            }
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
                alert.showAndWait();
        }
       }
    }
    
    private void setShift(){
        ObservableList<String> ShiftList = FXCollections.observableArrayList();
        try{
            q = "SELECT * FROM schedules";
           ps = con.prepareStatement(q);
           rs = ps.executeQuery();
           
           while(rs.next()){
               String shift = rs.getString("id")+" | "+ rs.getString("description")+" "+rs.getString("start")+"-"+rs.getString("end");
               ShiftList.add(shift);
           }
           Option_E.setItems(ShiftList);
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
                alert.showAndWait();
        }
    }
    
    public void FindEmployee(){
        String nik = txt_findE.getText();
        if(nik.length() == 16){
             ObservableList<Employee> findemployee = FXCollections.observableArrayList();
            try{
                q = "SELECT * FROM employee where nik = ?";
                ps = con.prepareStatement(q);
                ps.setString(1, nik );
                rs = ps.executeQuery();
                clearfields();
                while (rs.next()){
                    findemployee.add(new Employee(
                            rs.getString("nik"),
                            rs.getString("employee_code"),
                            rs.getString("name"),
                            rs.getInt("id_schedule"),
                            this
                    
                    ));
                }
                table_E.setItems(findemployee);
            }catch(SQLException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
                alert.showAndWait();
            }
        }else if (nik.length() == 6){
            ObservableList<Employee> findemployee = FXCollections.observableArrayList();
            try{
                q = "SELECT * FROM employee where employee_code = ?";
                ps = con.prepareStatement(q);
                ps.setString(1, nik );
                rs = ps.executeQuery();
                clearfields();
                while (rs.next()){
                    findemployee.add(new Employee(
                            rs.getString("nik"),
                            rs.getString("employee_code"),
                            rs.getString("name"),
                            rs.getInt("id_schedule"),
                            this
                    
                    ));
                }
                table_E.setItems(findemployee);
            }catch(SQLException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
                alert.showAndWait();
        }
        }else{
            UpdateTable();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Sorry, Your NIK or ID is not detected");
                alert.showAndWait();
        }
    }
    
    public void ButtonKlikPermit(Employee emp){
        this.resetDailyRecord();
        txt_nikp.setText(emp.getNik());
        this.selectedEmployee = emp;
        try{
            DatatablePermit(emp.getNik());
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }
        
    public void TableKlikPermit(){
        table_E.setOnMouseClicked(event ->{
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                Employee em = table_E.getSelectionModel().getSelectedItem();
                String nik = em.getNik();
                txt_nikp.setText(nik);
                ObservableList<PermitClassTable> permit = FXCollections.observableArrayList();
                try{
                    DatatablePermit(nik);
                }catch(SQLException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Error occurred: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }
    
    public void updateEmployee(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        q = "UPDATE employee SET "
                + "employee_code = ?,"
                + "name = ? ,"
                + "id_schedule = ? "
                + "WHERE nik = ? ";
        
        String nik = txt_nikE.getText();
        String id = txt_idE.getText();
        String nm = txt_nameE.getText();
        String shift = Option_E.getValue();
        String[] getid = shift.split("//|");
        String getShift = getid[0].trim();
        try{
            ps = con.prepareStatement(q);
            ps.setString(1, id);
            ps.setString(2, nm);
            ps.setInt(3, Integer.parseInt(getShift));
            ps.setString(4, nik);
            if(ps.executeUpdate()> 0){
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Update succes");
                cancel();
                UpdateTable();
                clean();
        }else{
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Update Failed");
            }
        }catch(SQLException e){
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
        }
        alert.showAndWait();
    }
    
    private void setForm(Employee employee){
        txt_idE.setText(employee.getEmployee_code());
        txt_nameE.setText(employee.getName());
        txt_nikE.setText(employee.getNik());
        
        try{
            q = "SELECT * FROM employee where id_schedule =?";
            ps = con.prepareStatement(q);
            rs = ps.executeQuery();
            while(rs.next()){
                Option_E.getItems().add(rs.getString("id_schedule"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    private void showHiddenButton(){
        ButtonParent.getChildren().remove(updateButtonContainer);
        ButtonParent.getChildren().remove(add_Button);
        if(!edited){
            ButtonParent.getChildren().add(add_Button);
        }else{
            ButtonParent.getChildren().add(updateButtonContainer);
        }
    }
    
    public void TableKlikUpdate(){
        table_E.setOnMouseClicked(event ->{
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                employee = table_E.getSelectionModel().getSelectedItem();
                edited = true;
                showHiddenButton();
                setForm(employee);
            }
        });
    }
    
//    =======================PERMITS SOURCE CODE================================
    
    @FXML
    private TableView<PermitClassTable> table_P;
    @FXML
    private TableColumn<PermitClassTable, String> Col_date;
    @FXML
    private TableColumn<PermitClassTable, String> Col_type;
    @FXML
    private TableColumn<PermitClassTable, String> Col_delete;
    @FXML
    private ComboBox<String> option_P;
    @FXML
    private TextField txt_id;
    @FXML
    private TextField txt_nikp;
    @FXML
    private TextArea txt_desc;
    @FXML
    private DatePicker date;
    @FXML
    private Button btn_P;
    @FXML
    void add_Permit() {
        Add_Permits();
    }
    
    public void OptionPList (){
    ObservableList<String> List = FXCollections.observableArrayList(
            "cuti/ijin",
            "alfa"
            );
            option_P.setItems(List);
    }
    
    public void deletePermit (int nik) {
        q = "DELETE FROM permits WHERE id = ?";
        try {
            ps = con.prepareStatement(q);
            ps.setInt(1, nik);
            ps.executeUpdate();
            DatatablePermit(this.selectedEmployee.getNik());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    private ObservableList<PermitClassTable> DatatablePermit(String nik) throws SQLException{
        ObservableList<PermitClassTable> permitlist = FXCollections.observableArrayList();
        try{
           q = "SELECT * FROM permits where nik_employee = ?";
           ps = con.prepareStatement(q);
           ps.setString(1, nik);
           rs = ps.executeQuery();
           
           while(rs.next()){
                permitlist.add(new PermitClassTable(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("type"),
                        this));
            }
            Col_date.setCellValueFactory(new PropertyValueFactory<PermitClassTable, String>("date"));
            Col_type.setCellValueFactory(new PropertyValueFactory<PermitClassTable, String>("type"));
            Col_delete.setCellValueFactory(new PropertyValueFactory<PermitClassTable, String>("delete"));
            
            table_P.getItems().clear();
            table_P.setItems(permitlist);
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
                alert.showAndWait();
        }
        return permitlist;
    }
    
    public void PermitUpdate(String nik){
        try{
        ObservableList<PermitClassTable> newpermit = DatatablePermit(nik);
        table_P.setItems(newpermit);
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
                alert.showAndWait();
        }
    }
    
    public void Add_Permits(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String nik = txt_nikp.getText();
        String type = option_P.getValue();
        String Option = option_P.getValue();
        LocalDate dt = date.getValue();
        String desc = txt_desc.getText();
       if (nik.isEmpty() || type.isEmpty() ) {
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();

        } else {
        try{
            q = "INSERT INTO permits (nik_employee,type,date,description,created_at) VALUES (?,?,?,?,?)";
            ps = con.prepareStatement(q);
            ps.setString (1,nik);
            ps.setString (2,Option);
            ps.setDate (3,java.sql.Date.valueOf(dt));
            ps.setString (4,desc);
            ps.setTimestamp (5,time);
            
            int rowsInserted = ps.executeUpdate();
            if(rowsInserted > 0){
               clearfields();
               PermitUpdate(nik);
            } 
        }catch(SQLException e){
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
                alert.showAndWait();
        }
        }
    }
    
    
    
//    ======================DAILY ACTIVITY SOURCE CODE==========================
    
    @FXML
    private ComboBox<String> Option_3;
    @FXML
    private TableView<DailyActivityTable> table_DA;
    @FXML
    private TableColumn<DailyActivityTable,String > TotalPoint;
    @FXML
    private TableColumn<DailyActivityTable,String > date_DA;
    @FXML
    private TableColumn<DailyActivityTable,String > entry;
    @FXML
    private TableColumn<DailyActivityTable,String > exit;
    @FXML
    private TableColumn<DailyActivityTable,String > time_ci;
    @FXML
    private TableColumn<DailyActivityTable,String > time_co;
    @FXML
    private TableColumn<DailyActivityTable,String > point_ci;
    @FXML
    private TableColumn<DailyActivityTable,String > point_co;
    @FXML
    void show() {
        String selectedDay = Option_3.getValue();
        if(selectedDay != null){
            show_employee(selectedDay);
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("please choose a day");
                alert.showAndWait();
        }
    }
    
    private void resetDailyRecord() {
        table_DA.getItems().clear();
        Option_3.getSelectionModel().clearSelection();
    }
    
    public void show_employee(String selectedDay){
        int bulan = Integer.parseInt(selectedDay.split("-")[1]);
        int tahun = Integer.parseInt(selectedDay.split("-")[0]);
        ObservableList<DailyActivityTable> showemployee = FXCollections.observableArrayList();
        try{
            q = "SELECT * FROM detail_absence where MONTH(absence_date) = ? AND YEAR(absence_date) = ? AND nik_employee = ?";
            ps = con.prepareStatement(q);
            ps.setInt(1, bulan);
            ps.setInt(2, tahun);
            ps.setString(3, this.selectedEmployee.getNik());
            rs = ps.executeQuery();
            
            while (rs.next()){
                showemployee.add(new DailyActivityTable(
                        rs.getDate("absence_date"),
                        rs.getTime("clock_in"),
                        rs.getTime("clock_out"),
                        rs.getInt("clock_in_point"),
                        rs.getInt("clock_out_point"),
                        rs.getString("entry_location"),
                        rs.getString("exit_location")
                ));
            }
            date_DA.setCellValueFactory(new PropertyValueFactory<DailyActivityTable, String>("date"));
            time_ci.setCellValueFactory(new PropertyValueFactory<DailyActivityTable, String>("timeCi"));
            time_co.setCellValueFactory(new PropertyValueFactory<DailyActivityTable, String>("timeCo"));
            point_ci.setCellValueFactory(new PropertyValueFactory<DailyActivityTable, String>("pointCi"));
            point_co.setCellValueFactory(new PropertyValueFactory<DailyActivityTable, String>("pointCo"));
            TotalPoint.setCellValueFactory(new PropertyValueFactory<DailyActivityTable, String>("TotalPoint"));
            entry.setCellValueFactory(new PropertyValueFactory<DailyActivityTable, String>("entry"));
            exit.setCellValueFactory(new PropertyValueFactory<DailyActivityTable, String>("exit"));
            
            table_DA.setItems(showemployee);
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
                alert.showAndWait();
        }
    }
    
    private void selectDay(){
        ObservableList<String> option3List = FXCollections.observableArrayList();
        try{
            q = "SELECT * FROM absences ";
           ps = con.prepareStatement(q);
           rs = ps.executeQuery();
           
           while(rs.next()){
               Date date = rs.getDate("date");
               String dt = date.toString();
               option3List.add(dt);
           }
           Option_3.setItems(option3List);
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred: " + e.getMessage());
                alert.showAndWait();
        }
    }
}
