package com.pulse.dbcp;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBDetailsRetrieve {
    private final static Logger log = LoggerFactory.getLogger(UrlDBInsert.class);

    private String url;

    public DBDetailsRetrieve(String url) {
        this.url = url;
    }
    public ArrayList<String> retrieveDetails(){
        ArrayList<String> arr = new ArrayList<String>();
        Connection conn = null;
        ResultSet resultSet = null;
        try{
            BasicDataSource basicDataSource = DBInit.getInstance().getBasicDataSource();
            conn = basicDataSource.getConnection();

            String sql = "select email,username from users where userid in (select user_id from URLS where url=\'"+url+"')";
            Statement preparedStatement = conn.createStatement();

            resultSet = preparedStatement.executeQuery(sql);

            while(resultSet.next()){
                arr.add(resultSet.getString("email"));
                arr.add(resultSet.getString("username"));
            }
        }catch(SQLException e){
            log.info("Exception in dbcp connection");
        }
        return arr;
    }

}
