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
    MailService mailService = new MailService();

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


    public boolean signup (String username,String password,String email) {

        String sql = "insert into users (username,password,email,roles) values( ?, crypt(?, gen_salt('bf')), ?,'admin') ";
        System.out.println(sql);
        boolean flag =false;
        try {
            PreparedStatement st = con.prepareStatement(sql);
            System.out.println(username+"   "+email);
            st.setString(1, username);
            st.setString(2, password);
            st.setString(3, email);

           if( st.executeUpdate()>=1)
               flag=true;
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;
    }


    public  int login (UserDetails user) {

        cookieid = 0;
        Statement st = null;
        String sql = " SELECT * from users where email = '"+user.email+"'";
        String sql1 = " SELECT * FROM users WHERE  email= '" + user.email + "'" +  " AND password = crypt('" + user.password + "',password)";
        System.out.println(sql);
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
            if( mailService.sendMail(email)){
            PreparedStatement st = con.prepareStatement(sql);

            st.setString(1, email);
            st.setInt(2, adminId);
            st.executeUpdate();
            }

        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public void addAppInfo (String name){

        String sql = " insert into  apps(appname)  values(?) ";
        System.out.println(sql);

        try {
            PreparedStatement st = con.prepareStatement(sql);

            st.setString(1, name);

             st.executeUpdate();
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

 }
  public void  user_app_relation( String name){

    int appid =getAppId(name);
    int userId = getAppUserId(name);

    String sql = "insert into user_app(user_id,app_id) values (? , ?)";
      try {
              PreparedStatement st = con.prepareStatement(sql);
              st.setInt(1, userId);
              st.setInt(2, appid);
              st.executeUpdate();


      }catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }




}
    public int getAppId( String appname){

        Statement st=null;

        String sql =  "select app_id from apps where appname  =  '" + appname + "'";
        int appid =0;
        try {
            st = con.createStatement();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ResultSet rs = null;
        try {

            rs = st.executeQuery(sql);
            if(rs.next())

                appid = rs.getInt("app_id");



        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

         return appid;
    }
    public int getAppUserId( String username){

        Statement st=null;

        String sql =  "select userid from users where username  =  '" + username +"'";
        int userId =0;
        try {
            st = con.createStatement();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ResultSet rs = null;
        try {

            rs = st.executeQuery(sql);
            if(rs.next())

                userId = rs.getInt("userid");



        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userId;
    }
    public void  addCookie (int userId){

        String sql = "insert into cookies_list(user_id) values (?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, userId);
            st.executeUpdate();


        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public int  getCookie (int userId){
        Statement st = null;
        String sql = "select cookie_id from cookies_list where  user_id = " + userId;
        int cookieId =0;
        try {
            st = con.createStatement();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ResultSet rs = null;
        try {

            rs = st.executeQuery(sql);
            if(rs.next())

                cookieId = rs.getInt(1);

        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       return cookieId;
    }
  public int user_apps(int cookieId){
        Statement st= null;
        int user_id =0,app_id=0;
      String sql= " select user_id from cookies_list  where cookie_id = "+ cookieId;
      try {
          st = con.createStatement();
      } catch (SQLException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
      }
      ResultSet rs = null;
      try {

          rs = st.executeQuery(sql);
          if(rs.next())

              user_id = rs.getInt("user_id");

      }catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      sql= " select app_id from user_app  where user_id = "+ user_id;
      try {
          st = con.createStatement();
      } catch (SQLException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
      }
      try {

          rs = st.executeQuery(sql);
          if(rs.next())

              app_id = rs.getInt("app_id");

      }catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }

        return app_id;
  }

}