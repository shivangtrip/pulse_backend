package com.nk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;

public class DetailsRepository {
    Connection connection =null;
    ResultSet rs =null;
    BasicDataSource basicDataSource = DBInit.getInstance().getBasicDataSource();
    private final static Logger logger = LoggerFactory.getLogger(DetailsRepository.class);

    public int signup(String username, String password, String email) {
        String sql = "insert into users (username,password,email,roles) values( ?, crypt(?, gen_salt('bf')), ?,'admin') returning userid; ";
        int userId = 0;
        try {
            connection = basicDataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            logger.info(username + "   " + email);
            st.setString(1, username);
            st.setString(2, password);
            st.setString(3, email);
            rs = st.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("userid");
            }
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
        }
        return userId;
    }


    public int login(UserDetails user) {

        int userId = 0;
        PreparedStatement statement = null;
        String sql = " SELECT * from users where email = '" + user.email + "'";
        String sql1 = " SELECT * FROM users WHERE  email= '" + user.email + "'" + " AND password = crypt('" + user.password + "',password)";
        try {
            connection = basicDataSource.getConnection();
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();

            System.out.println(rs);
            if (rs.next()) {
                userId = -1;
                statement = connection.prepareStatement(sql1);
                rs = statement.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("userid");
                }
            }

        } catch (SQLException e) {
            logger.error("SQL Exception", e);
        }

        return userId;
    }


    public void addUser(String email, int adminId) {

        String sql = " Insert into invites  values(?,?)";
        try {
            connection = basicDataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, email);
            statement.setInt(2, adminId);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception", e);

        }
    }

    public int addAppInfo(String name) {
        int appId = 0;
        String sql = " insert into  apps(appname)  values(?) returning app_id ";
        try {
            connection = basicDataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, name);
            rs = st.executeQuery();
            if (rs.next())
                appId = rs.getInt("app_id");

        } catch (SQLException e) {
            logger.error("SQL Exception", e);
        }
        return appId;

    }

    public void userAppRelation(int userId,int appId) {


        String sql = "insert into user_app(user_id,app_id) values (? , ?)";
        try {
            connection = basicDataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            st.setInt(2, appId);
            st.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception", e);
        }


    }

    public int addCookie(int userId) {
        int cookieId = 0;
        String sql = "insert into cookies_list(user_id) values (?) returning cookie_id ;";
        try {
            connection = basicDataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            rs = st.executeQuery();
            if (rs.next()) {
                cookieId = rs.getInt("cookie_id");
            }


        } catch (SQLException e) {
            logger.error("SQL Exception", e);
        }
        System.out.println("haha" + cookieId);
        return cookieId;
    }

    public int getCookie(int userId) {

        PreparedStatement statement = null;
        String sql = "select cookie_id from cookies_list where  user_id = " + userId;
        int cookieId = 0;
        try {
            connection = basicDataSource.getConnection();
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            if (rs.next())
                cookieId = rs.getInt(1);
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
        }
        return cookieId;
    }

    public int userApps(int cookieId) {

        PreparedStatement statement = null;
        int appId = 0;
        String sql = " SELECT app_id from user_app where user_id in (select user_id from cookies_list  where cookie_id = " + cookieId + ")";
        try {
            connection = basicDataSource.getConnection();
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            if (rs.next())
                appId = rs.getInt("app_id");
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
        }
        return appId;

    }
}