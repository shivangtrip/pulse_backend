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

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

public class DetailsRepository {
    Connection  con = null;


    public DetailsRepository()  {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/userdata","nandinik","nandinidb");
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

        String sql = "insert into users (username,password,email) values( ?, crypt(?, gen_salt('bf')), ?) ";
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

    public  boolean login (UserDetails user) {

        String sql = " SELECT * FROM users WHERE  email= '" + user.email + "'" +  " AND password = crypt('" + user.password + "',password)";
        System.out.println(sql);

        boolean flag = false;
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
            flag= true;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return flag ;
    }
}