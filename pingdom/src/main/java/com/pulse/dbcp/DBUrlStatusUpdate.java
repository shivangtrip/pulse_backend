package com.pulse.dbcp;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUrlStatusUpdate {
    private final static Logger log = LoggerFactory.getLogger(UrlDBInsert.class);

    private String url;

    public DBUrlStatusUpdate(String url) {
        this.url = url;
    }
    public void deleteDownStatus(){
        Connection conn = null;
        ResultSet resultSet = null;
        try{
            BasicDataSource basicDataSource = DBInit.getInstance().getBasicDataSource();
            conn = basicDataSource.getConnection();

            String sql = "DELETE from URLS_DOWN where url = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,url);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            log.info("Exception dbcp connection");
        }
    }
    public void updateDownStatus(){
        Connection conn = null;
        ResultSet resultSet = null;
        try{
            BasicDataSource basicDataSource = DBInit.getInstance().getBasicDataSource();
            conn = basicDataSource.getConnection();

            String sql = "UPDATE URLS set status='up' where url = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,url);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            log.info("Exception in dbcp connection");
        }
    }
}
