package controllers;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class AbsenceController implements Initializable {
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
    
    void readFile(String fileTitle, String directory) {
        
        try {            
            FileInputStream fileStream = new FileInputStream(new File(directory+fileTitle));
            Workbook workbook = WorkbookFactory.create(fileStream);
            Sheet sheet = workbook.getSheetAt(0);
            for(Row row : sheet) {                
                for (Cell cell : row) {
                    System.out.println(cell);
                }
            }
            
//            HSSFSheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//            while(rowIterator.hasNext()) {
//                System.out.println("row");
//                Row row = rowIterator.next();
//                Iterator<Cell> cellIterator = row.cellIterator();
//                while (cellIterator.hasNext()) {
//                    System.out.println("col");
//                    Cell cell = cellIterator.next();
//                    int cellIndex = cell.getColumnIndex();
//                    System.out.println(cellIndex);
//                    switch (cellIndex) {
//                        case 1:
//                            System.out.println(cell);
//                            break;
//                        case 2:
//                            System.out.println(cell);
//                            break;
//                        default:
//                            throw new AssertionError();
//                    }
//                }
//            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
    }
    
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
        
        readFile(fileTitle, directory);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

}

