package com.testautomation.framework.integration;

import com.testautomation.framework.generic.Generic;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.*;

public class DatabaseIntegration {
    public Connection conn = null;
    public String connString = null;
    public String URL=null;
    public String USERNAME=null;
    public String PASSWORD=null;

    public Connection getConnection(String dbURL) {
        readDatabaseArgs();
        try {
            if (conn == null) {
                if (Generic.readConfigProp("database.Type").equalsIgnoreCase("Oracle")){
                    Class.forName("oracle.jdbc.OracleDriver");
                } else if(Generic.readConfigProp("database.Type").equalsIgnoreCase("Sql Server")){
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
                } else {
                    System.out.println("update correct database type in config.properties file");
                }
                conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public HashMap<Integer, HashMap<String, String>> execute(String sql) {

        ResultSet rs = null;
        HashMap<Integer, HashMap<String, String>> recordSet = new LinkedHashMap<Integer, HashMap<String, String>>();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);
            recordSet = convertRSToHashMap(rs);
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return recordSet;
    }
    public HashMap<Integer, HashMap<String, String>> convertRSToHashMap(ResultSet rs) throws SQLException {
        HashMap<Integer, HashMap<String, String>> recordSet = new LinkedHashMap<Integer, HashMap<String, String>>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int counter = 1;
        while (rs.next()) {
            HashMap<String, String> record = new LinkedHashMap<String, String>();
            int i = 1;
            while (i <= rsmd.getColumnCount()) {
                record.put(rsmd.getColumnName(i), rs.getString(i));
                System.out.println(
                        "Record:" + i + "Column: " + rsmd.getColumnName(i) + "Value" + rs.getString(i));
                i++;
            }
            recordSet.put(counter, record);
            counter++;
        }
        return recordSet;
    }

    public HashMap<Integer, HashMap<String, String>> executeQueryWithTimer(
            String selectStr, String fromStr, String whereStr) throws Exception {

        HashMap<Integer, HashMap<String, String>> recordSet = null;
        long start = System.currentTimeMillis();
        long end = start + 15 * 60 * 1000; // 60 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end) {

            recordSet = this.selectQuery(selectStr, fromStr, whereStr);
            if (recordSet.size() > 0) {
                closeConnection();
                return recordSet;
            }
        }

        closeConnection();
        return recordSet;
    }

    public HashMap<Integer, HashMap<String, String>> selectQuery(
            String selectStr, String fromStr, String whereStr) throws Exception {

        String sql = null;
        HashMap<Integer, HashMap<String, String>> recordSet = null;

        if (whereStr == null) {
            sql = "SELECT " + selectStr + " FROM " + fromStr.trim();
        } else {
            sql = "SELECT " + selectStr + " FROM " + fromStr.trim() + "   WHERE " + whereStr;
        }
        recordSet = execute(sql);

        // if (recordSet.size() == 0)
        //  throw new Exception("Query returned no results. SQL:" + sql);
        return recordSet;
    }
    public void readDatabaseArgs(){
        String[] args=null;
        ClassLoader loader=null;
        try {
            File file = new File(System.getProperty("user.dir")+ File.separator + "src"+File.separator+"test"+File.separator+"resources"+File.separator+"config"+File.separator+"database");
            URL[] urls = {file.toURI().toURL()};
            loader = new URLClassLoader(urls);
        }catch (Exception e){
            e.printStackTrace();
        }
        ResourceBundle rbTestdata = ResourceBundle.getBundle("database", Locale.getDefault(), loader);
        URL = rbTestdata.getString("database.URL");
        USERNAME = rbTestdata.getString("database.UserName");
        PASSWORD = rbTestdata.getString("datanbase.Password");
    }
}
