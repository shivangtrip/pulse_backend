package com.nk;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

public class DetailsRepository {
    Connection  con = null;
    static int cookieid ;

    public DetailsRepository()  {
        try {
            //Properties props = new Properties();
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pingdb","nandinik","nandinidb");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public boolean signup (UserDetails a1) {

        String sql = "insert into users (username,password,email,roles) values( ?, crypt(?, gen_salt('bf')), ?,'admin') ";
        System.out.println(sql);
        boolean flag =false;
        try {
            PreparedStatement st = con.prepareStatement(sql);

            st.setString(1, a1.getUsername());
            st.setString(2, a1.getPassword());
            st.setString(3,  a1.getEmail());

           if( st.executeUpdate()>=1)
               flag=true;
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;
    }

    public  int login (UserDetails user) {

        String sql = " SELECT * FROM users WHERE  email= '" + user.email + "'" +  " AND password = crypt('" + user.password + "',password)";
        System.out.println(sql);

        cookieid = 0;
        Statement st = null;
        try {
            st = con.createStatement();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = st.executeQuery(sql);

            System.out.println(rs);
            if(rs.next()){
                cookieid= rs.getInt("userid");

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return cookieid ;
    }
    public  void addUser ( String email,int adminId) {

        String sql = " Insert into invites  values(?,?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);

            st.setString(1, email);
            st.setInt(2, adminId);
            st.executeUpdate();
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }



}