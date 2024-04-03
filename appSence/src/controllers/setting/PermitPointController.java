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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;
import models.setting.Permit;

public class PermitPointController implements Initializable{
    String sql;
    Connect conn = new Connect();
    Statement stat;
    ObservableList<Permit> permitRequirements;
    Permit permit;
    
    @FXML
    private TextField min, max, point;

    @FXML
    private Button addButton, updateButton;
    
    @FXML
    private TableColumn<Permit, String> minMaxColumn, typeColumn, pointColumn, actionColumn;

    @FXML
    private TableView<Permit> permitTable;

    @FXML
    private ComboBox<String> type;
    
    @FXML
    private VBox buttonParent;
    
    @FXML
    private HBox updateButtonsContainer;
    
    @FXML
    void create(MouseEvent event) {
        String min = this.min.getText();
        String max = this.max.getText();
        String type = this.type.getSelectionModel().getSelectedItem();
        String point = this.point.getText();
        sql = "INSERT INTO permit_point_requirements(min, max, permit_type, point) VALUES (?, ?, ?, ?)";
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, min);
            ps.setString(2, max);
            ps.setString(3, type);
            ps.setString(4, point);
            ps.executeUpdate();
            clean();
            showPermits();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void update() {
        sql = "UPDATE permit_point_requirements SET min = ?, max = ?, permit_type = ?, point = ? WHERE id = ?";
        String min = this.min.getText();
        String max = this.max.getText();
        String type = this.type.getSelectionModel().getSelectedItem();
        String point = this.point.getText();
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, Integer.valueOf(min));
            ps.setInt(2, Integer.valueOf(max));
            ps.setString(3, type);
            ps.setInt(4, Integer.valueOf(point));
            ps.setInt(5, Integer.valueOf(permit.getId()));
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Update complete");
                showPermits();
                cancelUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void cancelUpdate() {
        isEdit = false;
        showHideButton();
        setForm(permit);
        clean();
    }
    
    // show hide button create/update
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
    
    private void setForm(Permit permit) {
        max.setText(permit.getMax());
        min.setText(permit.getMin());
        type.setValue(permit.getPermitType());
        point.setText(permit.getPoint());
    }
    
    private void clean(){
        max.setText("");
        min.setText("");
        type.getSelectionModel().clearSelection();
        point.setText("");
    }
    
    public void delete(String id) {
        sql = "DELETE FROM permit_point_requirements WHERE id = ?";
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, Integer.valueOf(id));
            ps.executeUpdate();
            clean();
            showPermits();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showPermits() {
        permitTable.setItems(null);
        permitRequirements = FXCollections.observableArrayList();
        sql = "SELECT * FROM permit_point_requirements ORDER BY permit_type, min ASC";
        try {
            stat = conn.GetConnection().createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                permitRequirements.add(new Permit(
                        res.getString("id"),
                        res.getString("min"),
                        res.getString("max"),
                        res.getString("permit_type"),
                        res.getString("point"),
                        this
                ));
            }
            
            minMaxColumn.setCellValueFactory(new PropertyValueFactory<Permit, String>("minMax"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<Permit, String>("permitType"));
            pointColumn.setCellValueFactory(new PropertyValueFactory<Permit, String>("point"));
            actionColumn.setCellValueFactory(new PropertyValueFactory<Permit, String>("delete"));
            
            permitTable.setItems(permitRequirements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setType() {
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("cuti/ijin");
        types.add("alfa");
        this.type.getItems().addAll(types);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        setType();
        showPermits();
        showHideButton();
        
        permitTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                permit = permitTable.getSelectionModel().getSelectedItem();
                isEdit = true;
                showHideButton();
                setForm(permit);
            }
        });
    }

}
