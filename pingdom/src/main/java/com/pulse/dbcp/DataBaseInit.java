package com.pulse.dbcp;

import com.pulse.payload.URLPayload;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;


public class DataBaseInit {
    private ArrayList<URLPayload> urlObj;
    public DataBaseInit(ArrayList<URLPayload> urlList) {
        this.urlObj = urlList;
    }


    public void dbConnect(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {

            BasicDataSource bds = DataBaseUtility.getInstance().getBds();
            connection = bds.getConnection();
            //looping through array of objects of type URLPayload and inserting into database
            for(URLPayload uObj:urlObj) {


                String sql = "insert into URLS(user_id,url,created_at,status) values(?,?,?,?)";

                try {
                    Date today = new java.util.Date();
                    Timestamp t =  new Timestamp(today.getTime());
                    PreparedStatement st = connection.prepareStatement(sql);
                    st.setInt(1,1);
                    st.setString(2, uObj.getUrl());
                    st.setTimestamp(3, t);
                    st.setString(4, uObj.getStatus());

                    st.executeUpdate();


                } catch (Exception e) {
                    System.out.println("Statement3 error");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
