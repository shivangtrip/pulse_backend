package com.nk;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DetailsRepository {
    Connection  con = null;
    static int cookieid ;
    Properties props = new Properties();
    InputStream inputStream;
    String propFileName = "config.properties";
    String db_username,db_password,db_url;

    public DetailsRepository ()  {
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                props.load(inputStream);
            }
            else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
             db_username = props.getProperty("DATABASE_USER_NAME");
             db_password = props.getProperty("DATABASE_PASSWORD");
             db_url = props.getProperty("DATABASE_URL");
             Class.forName(props.getProperty("LOAD_DRIVER"));
             con = DriverManager.getConnection(db_url,db_username,db_password);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
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
        String sql = " SELECT * from users where email = '"+user.email+"'";
        String sql1 = " SELECT * FROM users WHERE  email= '" + user.email + "'" +  " AND password = crypt('" + user.password + "',password)";
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
                cookieid = -1;
                rs = st.executeQuery(sql1);
                if(rs.next())
                {cookieid= rs.getInt("userid");}
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