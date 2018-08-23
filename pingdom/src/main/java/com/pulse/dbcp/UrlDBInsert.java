package com.pulse.dbcp;

import com.pulse.payload.URLPayload;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlDBInsert {
    private final static Logger log = LoggerFactory.getLogger(UrlDBInsert.class);

    private ArrayList<URLPayload> urlObj;
    public UrlDBInsert(ArrayList<URLPayload> urlList) {
        this.urlObj = urlList;
    }


    public void insertURLSList(){

        Connection connection = null;
        ResultSet resultSet = null;
        try {
            log.info("Entered into the try block");
            BasicDataSource basicDataSource = DBInit.getInstance().getBasicDataSource();
            connection = basicDataSource.getConnection();
            //looping through array of objects of type URLPayload and inserting into database
            for(URLPayload uObj:urlObj) {

                String sql = "insert into URLS(user_id,url,created_at,status,app_id) values(?,?,?,?,?)";
                Date today = new Date();
                Timestamp t = new Timestamp(today.getTime());
                try {

                    PreparedStatement st = connection.prepareStatement(sql);
                    st.setInt(1, uObj.getUserId());
                    st.setString(2, uObj.getUrl());
                    st.setTimestamp(3, t);
                    st.setString(4, uObj.getStatus());
                    st.setInt(5, uObj.getAppId());

                    st.executeUpdate();


                } catch (Exception e) {
                    log.info("Statement3 error");
                }
                //insert to URLS_DOWN table if website is down
                if (uObj.getStatus().equals("down")) {
                    try {
                        String sql1 = "insert into URLS_DOWN(user_id,url,created_at,status,app_id) values(?,?,?,?,?)";
                        {
                            PreparedStatement stDown = connection.prepareStatement(sql1);
                            stDown.setInt(1, uObj.getUserId());
                            stDown.setString(2, uObj.getUrl());
                            stDown.setTimestamp(3, t);
                            stDown.setString(4, uObj.getStatus());
                            stDown.setInt(5, uObj.getAppId());
                            stDown.executeUpdate();

                        }
                    }catch (SQLException e){
                        log.info("Statement 4 error");
                    }
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}