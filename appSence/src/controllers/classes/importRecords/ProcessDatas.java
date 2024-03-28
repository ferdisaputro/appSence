/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.classes.importRecords;

import controllers.connection.Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import models.importRecords.EmployeeRecordVariable;

/**
 *
 * @author USER
 */
public class ProcessDatas {
    ArrayList<ArrayList<String>> absences;
    ArrayList<HashMap<String, String>> updatedAbsences;
    Connect conn = new Connect();
    String prevTitle;
    Statement stat;
    String sql;
    EmployeeRecordVariable erv;
//    ArrayList<ArrayList<Integer>> clockInPoints;
    HashMap<String, HashMap<Integer, ArrayList<HashMap<String, Integer>>>> clockPoints;//  {clock_in={1=[[25, 360, 375, 4, 1], [6, 376, 405, 3, 1], [7, 406, 410, 2, 1], [8, 411, 416, 1, 1], [18, 417, 420, 0, 1]], 
                                                                                 //             2=[[14, 420, 435, 4, 2], [15, 436, 465, 3, 2], [16, 466, 470, 2, 2], [17, 471, 476, 1, 2], [24, 477, 480, 0, 2]]}, 
                                                                                 //   clock_out={1=[[9, null, 900, 0, 1], [10, 901, 915, 1, 1], [11, 916, 930, 2, 1], [12, 931, 945, 3, 1], [13, 946, null, 4, 1]], 
                                                                                 //              2=[[19, null, 960, 0, 2], [20, 961, 975, 1, 2], [21, 976, 990, 2, 2], [22, 991, 1005, 3, 2], [23, 1006, null, 4, 2]]}}
//    HashMap<Integer, ArrayList<ArrayList<Integer>>> clockOutPoints;
//    CompletableFuture<Boolean> insertDetails;
    
    public ProcessDatas(String prevTitle, ArrayList<ArrayList<String>> absences, EmployeeRecordVariable erv) {
        this.absences = absences;
        this.prevTitle = prevTitle;
        this.erv = erv;
//        this.insertDetails = new CompletableFuture<>();
    }
    
    public void proceed() {
//        System.out.println(absences.toString());
        createAbsence();
//        absences.clear();
    }
    
    private void createAbsence() {
        sql = "INSERT INTO absences(title, date, created_at) VALUES (?, ?, ?)";
        try {
            Connection cn = conn.GetConnection();
            PreparedStatement pstmt = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, prevTitle);
            pstmt.setString(2, absences.get(0).get(1));
            pstmt.setString(3, LocalDate.now().toString());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Insertion failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1); // Retrieve the generated ID
                    createDetailAbsence(id);
//                    countPermits(id);
                } else {
                    System.out.println("Insertion failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e);
        }
    }
    
    private void createDetailAbsence(int id) {
        try {
            stat = conn.GetConnection().createStatement();
            sql = "INSERT INTO detail_absence(nik_employee, absence_date, clock_in, clock_out, entry_location, exit_location, id_absences) VALUES ";
            for (ArrayList<String> absence : absences) {
                sql += "(\""+absence.get(0)+"\", "
                        + "\""+absence.get(1)+"\", "
                        + (absence.get(2) != null ? "\"" + absence.get(2) + "\"" : null) + ", "
                        + (absence.get(3) != null ? "\"" + absence.get(3) + "\"" : null) + ", "
                        + (absence.get(4) != null ? "\"" + absence.get(4) + "\"" : null) + ", "
                        + (absence.get(5) != null ? "\"" + absence.get(5) + "\"" : null) + ", "
                        + "\""+id+"\"),";
            }
            sql = sql.substring(0, sql.length()-1);
//            System.out.println(sql);
            if (stat.executeUpdate(sql) > 0){
//                insertDetails.complete(Boolean.TRUE);
                if (getPoints()) {
                    validateDetailAbsences(id);    
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
//            System.out.println(e);
            try {
                stat = conn.GetConnection().createStatement();
                sql = "DELETE FROM absences WHERE id = '"+id+"'";
                stat.executeUpdate(sql);
            } catch (Exception et) {
                et.printStackTrace();
//                System.out.println(et);
            }
        }
    }
    
    private void validateDetailAbsences(int id) {
        updatedAbsences = new ArrayList<>();
        try {
            stat = conn.GetConnection().createStatement();
            sql = "SELECT detail_absence.id, " +
                    "HOUR(detail_absence.clock_in) * 60 + MINUTE(detail_absence.clock_in) AS clock_in, " +
                    "HOUR(detail_absence.clock_out) * 60 + MINUTE(detail_absence.clock_out) AS clock_out, " +
                    "employee.nik, employee.id_schedule " +
                    "FROM absences " +
                    "INNER JOIN detail_absence ON absences.id = detail_absence.id_absences " +
                    "INNER JOIN employee ON detail_absence.nik_employee = employee.nik " +
                    "WHERE absences.id = '"+id+"' ORDER BY detail_absence.id ";
            ResultSet res = stat.executeQuery(sql);
            int updateCount = 0;
            while (res.next()) {                
                String idDetail = res.getString("id");
                Integer clockIn = (res.getString("clock_in") != null)? Integer.valueOf(res.getString("clock_in")) : null;
                Integer clockOut = (res.getString("clock_out") != null)? Integer.valueOf(res.getString("clock_out")) : null;
                String nik = res.getString("nik");
                int idSchedule = Integer.parseInt(res.getString("id_schedule"));
                
                updatedAbsences.add(new HashMap());
                updatedAbsences.get(updateCount).put("id_detail", idDetail);
                updatedAbsences.get(updateCount).put("nik", nik);
                
                checkClockInOut(clockIn, "clock_in", idSchedule, updateCount);
                checkClockInOut(clockOut, "clock_out", idSchedule, updateCount);
                updateCount++;
            }
            
            updateDetailAbsences();
            
            erv.setMyVariable(id);
//            System.out.println(updatedAbsences.toString());
//            for (HashMap<String, String> updatedAbsence : updatedAbsences) {
//                System.out.println(updatedAbsence.toString());
//            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e);
        }
    }
    
    private void updateDetailAbsences() {
        StringBuilder query = new StringBuilder("UPDATE detail_absence SET ");
        StringBuilder clockInPoint = new StringBuilder("clock_in_point = CASE id ");
        StringBuilder clockOutPoint = new StringBuilder("clock_out_point = CASE id ");
        StringBuilder inConditionTemp = new StringBuilder("WHERE id IN(");
        for (HashMap<String, String> updatedAbsence : updatedAbsences) {
            clockInPoint.append("WHEN "+updatedAbsence.get("id_detail")+" THEN "+updatedAbsence.get("clock_in_point")+" ");
            clockOutPoint.append("WHEN "+updatedAbsence.get("id_detail")+" THEN "+updatedAbsence.get("clock_out_point")+" ");
            inConditionTemp.append("'" + updatedAbsence.get("id_detail") + "',");
        }
        clockInPoint.append("ELSE clock_in_point END, ");
        clockOutPoint.append("ELSE clock_out_point END ");
        
        StringBuilder inCondition = new StringBuilder(inConditionTemp.substring(0, inConditionTemp.length() - 1));
        inCondition.append(")");
        query.append(clockInPoint.append(clockOutPoint.append(inCondition)));
        
        try {
            stat = conn.GetConnection().createStatement();
            stat.executeUpdate(query.toString());
            stat.close();
            JOptionPane.showMessageDialog(null, "Data successfully imported");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkClockInOut(Integer clockInOut, String type, int idSchedule, int updateCount) {
        if (clockInOut == null) updatedAbsences.get(updateCount).put(type + "_point", "0");
        else {
            for (Map.Entry<Integer, ArrayList<HashMap<String, Integer>>> clockInOutPoint : clockPoints.get(type).entrySet()) {
                if (clockInOutPoint.getKey() == idSchedule) {
                    //test on clock_out loop
                    if (clockInOut < clockInOutPoint.getValue().get(0).get("start")) 
                        updatedAbsences.get(updateCount).put(type + "_point", 
                            String.valueOf(clockInOutPoint.getValue().get(0).get("point"))
                        );
                    else if (clockInOut > clockInOutPoint.getValue().get(clockInOutPoint.getValue().size()-1).get("end")) 
                        updatedAbsences.get(updateCount).put(type + "_point", 
                            String.valueOf(clockInOutPoint.getValue().get(
                                clockInOutPoint.getValue().size()-1
                            ).get("point"))
                        );
                    else {                                
                        for (HashMap<String, Integer> points : clockInOutPoint.getValue()) {
                            int start = points.get("start");
                            int end = points.get("end");
                            String point = String.valueOf(points.get("point"));
                            if (clockInOut >= start && clockInOut <= end) updatedAbsences.get(updateCount).put(type + "_point", point);
                            
                        }
                    }
                }
            }
        }
    }
    
    private boolean getPoints(){
        clockPoints = new HashMap<>();
        try {
            HashMap<Integer, ArrayList<HashMap<String, Integer>>> clockInTempRes = new HashMap<>();
            HashMap<Integer, ArrayList<HashMap<String, Integer>>> clockOutTempRes = new HashMap<>();
            
            Statement idScheduleStat = conn.GetConnection().createStatement();
            ResultSet idScheduleRes = idScheduleStat.executeQuery("SELECT id FROM schedules");
            while (idScheduleRes.next()) {
                clockInTempRes.put(Integer.valueOf(idScheduleRes.getString("id")), new ArrayList<>());
                clockOutTempRes.put(Integer.valueOf(idScheduleRes.getString("id")), new ArrayList<>());
            }
            clockPoints.put("clock_in", clockInTempRes);
            clockPoints.put("clock_out", clockOutTempRes);
            
            stat = conn.GetConnection().createStatement();
            sql = "SELECT id, point, type, id_schedule, "
                    + "HOUR(start) * 60 + MINUTE(start) AS start, "
                    + "HOUR(end) * 60 + MINUTE(end) AS end "
                    + "FROM point_requirements ORDER BY start ASC";
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                HashMap<String, Integer> tempResult = new HashMap<>();
                int id = Integer.parseInt(res.getString("id"));
                Integer start = (res.getString("start") != null)? Integer.valueOf(res.getString("start")) : null;
                Integer end = (res.getString("end") != null)? Integer.valueOf(res.getString("end")) : null;
                int point = Integer.parseInt(res.getString("point"));
                int idSchedule = Integer.parseInt(res.getString("id_schedule"));
                
                tempResult.put("id", id);
                tempResult.put("start", start);
                tempResult.put("end", end);
                tempResult.put("point", point);
                tempResult.put("id_schedule", idSchedule);
                
                //add to different category
                clockPoints.get(res.getString("type")).get(idSchedule).add(tempResult);
            }
//            validateDetailAbsences(32)
            return true;
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e);
        }
        return false;
    }
    
    //validate permits point
//    private void countPermits(int id){
//        String sql = "SELECT "
//                + "e.nik, "
//                + "COALESCE(COUNT(CASE WHEN p.type = 'cuti' THEN p.id END), 0) AS total_cuti, "
//                + "COALESCE(COUNT(CASE WHEN p.type = 'ijin' THEN p.id END), 0) AS total_ijin, "
//                + "COALESCE(COUNT(CASE WHEN p.type = 'alfa' THEN p.id END), 0) AS total_alfa "
//                + "FROM employee e "
//                + "LEFT JOIN permits p "
//                + "ON e.nik = p.nik_employee "
//                + "AND MONTH(p.date) = 2 AND YEAR(p.date) = 2024 "
//                + "GROUP BY e.nik;";
//    }
}




               
                //coverting start time to minutes
//                if (res.getString("start") != null) {
//                    String[] stringStartTime = res.getString("start").split(":");
//                    int startHour = Integer.parseInt(stringStartTime[0]);
//                    int startMinutes = Integer.parseInt(stringStartTime[1]);
//                    int timeStartMinutes = startHour * 60 + startMinutes;
//                    tempResult.add(timeStartMinutes);
//                } else {
//                    tempResult.add(null);
//                }

                //converting end time to minutes
//                if (res.getString("end") != null) {
//                    String[] stringOutTime = res.getString("end").split(":");
//                    int outHour = Integer.parseInt(stringOutTime[0]);
//                    int outMinutes = Integer.parseInt(stringOutTime[1]);
//                    int timeOutMinutes = outHour * 60 + outMinutes;
//                    tempResult.add(timeOutMinutes);
//                } else {
//                    tempResult.add(null);
//                }
                        
                            //check clock_out_point
//                            if (clockOutPoint.getValue().get(0).get("start") != null && clockOut < clockOutPoint.getValue().get(0).get("start")) {
//                                // Add logic for the first index
//                            } else if (clockOutPoint.getValue().get(clockOutPoint.getValue().size()-1).get("end") != null &&
//                                    clockOut > clockOutPoint.getValue().get(clockOutPoint.getValue().size()-1).get("end")) {
//                                // Add logic for the last index
//                            } else {
//                                for (int i = 0; i < clockOutPoint.getValue().size(); i++) {
//                                    HashMap<String, Integer> points = clockOutPoint.getValue().get(i);
//                                    Integer start = points.get("start");
//                                    Integer end = points.get("end");
//                                    String point = String.valueOf(points.get("point"));
//
//                                    // Check if start is null at the first index and end is null at the last index
//                                    if (i == 0 && start == null) {
//                                        // Logic for start is null at the first index
//                                        if (clockOut <= end) {
//                                            updatedAbsences.get(updateCount).add(point);
//                                        }
//                                    } else if (i == clockOutPoint.getValue().size() - 1 && end == null) {
//                                        // Logic for end is null at the last index
//                                        if (clockOut >= start) {
//                                            updatedAbsences.get(updateCount).add(point);
//                                        }
//                                    } else if (start != null && end != null && clockOut >= start && clockOut <= end) {
//                                        updatedAbsences.get(updateCount).add(point);
//                                    }
//                                }
//                            }