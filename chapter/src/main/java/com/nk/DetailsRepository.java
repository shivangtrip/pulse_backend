package com.nk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class DetailsRepository {

    private String INSERT_USERS = "INSERT INTO users (username,password,email,roles) VALUES( ?, crypt(?, gen_salt('bf')), ?,'admin') RETURNING userid; ";
    private String CHECK_EMAIL_EXISTS = " SELECT * FROM users WHERE email = ?";
    private String AUTHENTICATE_PASSWORD = " SELECT * FROM users WHERE  email= ?  AND password = crypt(?,password)";
    private String INVITE_USER = " INSERT INTO invites  VALUES(?,?)";
    private String CREATE_APP = " INSERT INTO  apps(appname)  VALUES(?) returning app_id ";
    private String USER_APP_MAPPING = "INSERT INTO user_app(user_id,app_id) VALUES (? , ?)";
    private String SET_COOKIE = "INSERT INTO cookies_list(user_id) VALUES (?) returning cookie_id ";
    private String GET_COOKIE = "SELECT cookie_id FROM cookies_list WHERE  user_id = ? ";
    private String APPS_OF_USER = " SELECT app_id FROM user_app WHERE user_id in (SELECT user_id FROM cookies_list  WHERE cookie_id = ?)";
    private String DELETE_COOKIE = "DELETE FROM cookies_list WHERE cookie_id = ?";

    Connection connection = null;
    ResultSet rs = null;
    PreparedStatement statement = null;
    QueryHelper queryHelper = new QueryHelper();
    private final static Logger logger = LoggerFactory.getLogger(DetailsRepository.class);

    /**
     * for  user signup
     *
     * @param user
     * @return
     */
    public int signup(UserDetails user) {

        int userId = 0;
        try {
            connection = queryHelper.createConnection();
            statement = connection.prepareStatement(INSERT_USERS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            rs = statement.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("userid");
            }
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException(e);
        }
        return userId;
    }


    /**
     * For logging an user into the system
     *
     * @param user
     * @return
     */
    public int login(UserDetails user) {

        int userId = 0;
        try {
            connection = queryHelper.createConnection();
            statement = connection.prepareStatement(CHECK_EMAIL_EXISTS);
            statement.setString(1, user.getEmail());
            rs = statement.executeQuery();
            if (rs.next()) {
                userId = -1;
                statement = connection.prepareStatement(AUTHENTICATE_PASSWORD);
                statement.setString(1, user.getEmail());
                statement.setString(2, user.getPassword());
                rs = statement.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("userid");
                }
            }

        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException(e);
        }

        return userId;
    }

    /**
     * for inviting an user
     *
     * @param email
     * @param adminId
     */

    public void addUser(String email, int adminId) {

        try {
            connection = queryHelper.createConnection();
            statement = connection.prepareStatement(INVITE_USER);
            statement.setString(1, email);
            statement.setInt(2, adminId);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException(e);

        }
    }

    /**
     * for creating app
     *
     * @param name
     * @return
     */
    public int createApp(String name) {
        int appId = 0;

        try {
            connection = queryHelper.createConnection();
            statement = connection.prepareStatement(CREATE_APP);
            statement.setString(1, name);
            rs = statement.executeQuery();
            if (rs.next())
                appId = rs.getInt("app_id");

        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException(e);
        }
        return appId;

    }

    /**
     * for mapping user with app
     *
     * @param userId
     * @param appId
     */

    public void userAppRelation(int userId, int appId) {


        try {
            connection = queryHelper.createConnection();
            statement = connection.prepareStatement(USER_APP_MAPPING);
            statement.setInt(1, userId);
            statement.setInt(2, appId);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException(e);
        }


    }

    /**
     * adding cookie to the user when logged in
     *
     * @param userId
     * @return
     */

    public int addCookie(int userId) {
        int cookieId = 0;

        try {
            connection = queryHelper.createConnection();
            statement = connection.prepareStatement(SET_COOKIE);
            statement.setInt(1, userId);
            rs = statement.executeQuery();
            if (rs.next()) {
                cookieId = rs.getInt("cookie_id");
            }

        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException(e);
        }
        return cookieId;
    }

    /**
     * to return a cookie of user
     *
     * @param userId
     * @return
     */
    public int getCookie(int userId) {


        int cookieId = 0;
        try {
            connection = queryHelper.createConnection();
            statement = connection.prepareStatement(GET_COOKIE);
            statement.setInt(1, userId);
            rs = statement.executeQuery();
            if (rs.next())
                cookieId = rs.getInt(1);
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException(e);
        }
        return cookieId;
    }

    /**
     * return list of user's apps
     *
     * @param cookieId
     * @return
     */

    public int appsOfUser(int cookieId) {

        int appId = 0;

        try {
            connection = queryHelper.createConnection();
            statement = connection.prepareStatement(APPS_OF_USER);
            statement.setInt(1, cookieId);
            rs = statement.executeQuery();
            if (rs.next())
                appId = rs.getInt("app_id");
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException(e);
        }
        return appId;

    }

    /**
     * delete a cookie when user logs out
     *
     * @param cookieId
     */
    public void deleteCookie(int cookieId) {

        try {
            connection = queryHelper.createConnection();
            statement = connection.prepareStatement(DELETE_COOKIE);
            statement.setInt(1, cookieId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException(e);
        }

    }
}