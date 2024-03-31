/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.importRecords.details;

import java.text.SimpleDateFormat;
import javafx.beans.property.StringProperty;
import java.sql.Date;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author USER
 */
public class PermitDetail {
    private StringProperty date;
    private StringProperty type;
    private StringProperty description;
    
    public PermitDetail(String date, String type, String description) {
        Date sqlDate = Date.valueOf(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.date = new SimpleStringProperty(dateFormat.format(sqlDate));
        this.type = new SimpleStringProperty(type);
        this.description = new SimpleStringProperty(description);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
