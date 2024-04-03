package controllers.setting;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.setting.Presence;
import controllers.connection.Connect;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;
import models.setting.Permit;

public class PresenceController implements Initializable {
    String sql;
    Connect conn = new Connect();
    Statement stat;
    ObservableList<Presence> pointRequirements;
    Presence presence;
    
    @FXML
    private TableColumn<Presence, String> startEndColumn, typeColumn, pointColumn, scheduleColumn, actionColumn;

    @FXML
    private TextField startHour, startMinute, endHour, endMinute, point;

    @FXML
    private TableView<Presence> presenceTable;
    
    @FXML
    private ComboBox<String> type, schedule;
    
    @FXML
    private VBox buttonParent;
    
    @FXML
    private Button addButton, updateButton;
    
    @FXML
    private HBox updateButtonsContainer;

    @FXML
    void create(MouseEvent event) {
        String startTime = startHour.getText() + ":" + startMinute.getText();
        String endTime = endHour.getText() + ":" + endMinute.getText();
        String type = this.type.getSelectionModel().getSelectedItem();
        String point = this.point.getText();    
        String id_schedule = this.schedule.getSelectionModel().getSelectedItem().split(" | ")[0];
        sql = "INSERT INTO point_requirements(start, end, type, point, id_schedule) VALUES (STR_TO_DATE(?, '%H:%i'), STR_TO_DATE(?, '%H:%i'), ?, ?, ?)";
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, startTime);
            ps.setString(2, endTime);
            ps.setString(3, type);
            ps.setString(4, point);
            ps.setString(5, id_schedule);
            ps.executeUpdate();
            clean();
            showPresences();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void cancelUpdate() {
        isEdit = false;
        showHideButton();
        setForm(presence);
        clean();
    }

    @FXML
    void update(MouseEvent event) {
        sql = "UPDATE point_requirements SET "
                + "start = STR_TO_DATE(?, '%H:%i'), "
                + "end = STR_TO_DATE(?, '%H:%i'), "
                + "type = ?, point = ?, id_schedule = ? "
                + "WHERE id = ?";
        String start = this.startHour.getText() + ":" + this.startMinute.getText();
        String end = this.endHour.getText() + ":" + this.endMinute.getText();
        String type = this.type.getSelectionModel().getSelectedItem();
        String point = this.point.getText();
        int scheduleId = Integer.valueOf(this.schedule.getSelectionModel().getSelectedItem().split(" | ")[0]);
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, start);
            ps.setString(2, end);
            ps.setString(3, type);
            ps.setInt(4, Integer.parseInt(point));
            ps.setInt(5, scheduleId);
            ps.setInt(6, Integer.parseInt(presence.getId()));
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Update complete");
                showPresences();
                cancelUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void clean(){
        startHour.setText("");
        startMinute.setText("");
        endHour.setText("");
        endMinute.setText("");
        point.setText("");
        type.getSelectionModel().clearSelection();
        schedule.getSelectionModel().clearSelection();
    }
    
    private void showPresences() {
        presenceTable.setItems(null);

        pointRequirements = FXCollections.observableArrayList();
        sql = "SELECT pr.id, LEFT(pr.start, 5) AS pr_start, LEFT(pr.end, 5) AS pr_end, pr.type, pr.point, pr.id_schedule, " +
                "s.description, LEFT(s.start, 5) AS s_start, LEFT(s.end, 5) AS s_end, s.id AS s_id FROM point_requirements pr " +
                "INNER JOIN schedules s " +
                "ON pr.id_schedule = s.id " +
                "ORDER BY pr.id_schedule, pr.type, pr.start ASC";
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                String schedule = res.getString("s_id")+" | "+res.getString("description")+" "+res.getString("s_start")+"-"+res.getString("s_end");
                pointRequirements.add(new Presence(
                        res.getString("id"),
                        res.getString("pr_start"),
                        res.getString("pr_end"),
                        res.getString("type"),
                        res.getString("point"),
                        schedule,
                        this
                ));
            }
            
            startEndColumn.setCellValueFactory(new PropertyValueFactory<Presence, String>("startEnd"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<Presence, String>("type"));
            pointColumn.setCellValueFactory(new PropertyValueFactory<Presence, String>("point"));
            scheduleColumn.setCellValueFactory(new PropertyValueFactory<Presence, String>("schedule"));
            actionColumn.setCellValueFactory(new PropertyValueFactory<Presence, String>("delete"));
            
            presenceTable.setItems(pointRequirements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void delete(String id) {
        sql = "DELETE FROM point_requirements WHERE id = ?";
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, Integer.valueOf(id));
            ps.executeUpdate();
            clean();
            showPresences();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setType() {
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("clock_in");
        types.add("clock_out");
        this.type.getItems().addAll(types);
    }
    
    private void setSchedule() {
        sql = "SELECT "
                + "id, "
                + "LEFT(start, 5) AS start, "
                + "LEFT(end, 5) AS end, "
                + "description "
                + "FROM schedules";
        ObservableList<String> pointReqs = FXCollections.observableArrayList();
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                String point = res.getString("id")+" | "+res.getString("description")+" "+res.getString("start")+"-"+res.getString("end");
                pointReqs.add(point);
            }
            this.schedule.getItems().addAll(pointReqs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    boolean isEdit = false;
    private void showHideButton() {
        buttonParent.getChildren().remove(updateButtonsContainer);
        buttonParent.getChildren().remove(addButton);
        if (!isEdit) {
            buttonParent.getChildren().add(addButton);
        } else {
            buttonParent.getChildren().add(updateButtonsContainer);
        }
    }
    
    private void setForm(Presence presence) {
        startHour.setText(presence.getStart().split(":")[0]);
        startMinute.setText(presence.getStart().split(":")[1]);
        endHour.setText(presence.getEnd().split(":")[0]);
        endMinute.setText(presence.getEnd().split(":")[1]);
        type.setValue(presence.getType());
        point.setText(presence.getPoint());
        schedule.setValue(presence.getSchedule());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        showPresences();
        setType();
        setSchedule();
        showHideButton();
        
        presenceTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                presence = presenceTable.getSelectionModel().getSelectedItem();
                isEdit = true;
                showHideButton();
                setForm(presence);
            }
        });
    }    
}
