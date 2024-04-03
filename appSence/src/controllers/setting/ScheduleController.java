package controllers.setting;

import controllers.connection.Connect;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.setting.Schedule;

public class ScheduleController implements Initializable {
    String sql;
    Connect conn = new Connect();
    Statement stat;
    ObservableList<Schedule> schedules;
    
    @FXML
    private TableView<Schedule> scheduleTable;

    @FXML
    private TableColumn<Schedule, String> startColumn, endColumn, descriptionColumn, actionColumn;

    @FXML
    private TextField startHour, startMinute, endHour, endMinute, description;

    @FXML
    void createSchedule(MouseEvent event) {
        String startTime = startHour.getText() + ":" + startMinute.getText();
        String endTime = endHour.getText() + ":" + endMinute.getText();
        String desc = description.getText();
        sql = "INSERT INTO schedules(start, end, description) VALUES (STR_TO_DATE(?, '%H:%i'), STR_TO_DATE(?, '%H:%i'), ?)";
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, startTime);
            ps.setString(2, endTime);
            ps.setString(3, desc);
            ps.executeUpdate();
            clean();
            showSchedules();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void clean(){
        scheduleTable.setItems(null);
        startHour.setText("");
        startMinute.setText("");
        endHour.setText("");
        endMinute.setText("");
        description.setText("");
    }
    
    private void showSchedules() {
        schedules = FXCollections.observableArrayList();
        sql = "SELECT * FROM schedules";
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                schedules.add(new Schedule(
                        res.getString("id"),
                        res.getString("start"),
                        res.getString("end"),
                        res.getString("description"),
                        this
                ));
            }
            
            startColumn.setCellValueFactory(new PropertyValueFactory<Schedule, String>("start"));
            endColumn.setCellValueFactory(new PropertyValueFactory<Schedule, String>("end"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<Schedule, String>("description"));
            actionColumn.setCellValueFactory(new PropertyValueFactory<Schedule, String>("delete"));
            
            scheduleTable.setItems(schedules);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void delete(String id) {
        sql = "DELETE FROM schedules WHERE id = ?";
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, Integer.valueOf(id));
            ps.executeUpdate();
            clean();
            showSchedules();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        showSchedules();
    }
}
