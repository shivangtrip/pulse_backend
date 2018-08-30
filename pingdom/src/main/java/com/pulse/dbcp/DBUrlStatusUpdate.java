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
        String sql = "DELETE FROM URLS_DOWN WHERE url = ?";
        DBCPConnectionHelper dbcpConnectionHelper = new DBCPConnectionHelper();
        Connection conn = null;
        ResultSet resultSet = null;
        try{

            conn = dbcpConnectionHelper.createConnection();


            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,url);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            log.info("Exception dbcp connection");
            throw new RuntimeException(e);

        }finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
        }finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
