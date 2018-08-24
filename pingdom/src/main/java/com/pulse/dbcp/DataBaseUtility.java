package com.pulse.dbcp;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataBaseUtility {


    private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    //enter your database name at the end instead of pulse
    private static final String DB_URL = "jdbc:postgresql://localhost/pulse";
    //enter your local database username
    private static final String DB_USER = "db_user";
    //password for your respective db
    private static final String DB_PASSWORD = "db_user_password";

    private static final int CONN_POOL_SIZE = 10;


    private BasicDataSource bds = new BasicDataSource();

    private DataBaseUtility() {
        //Set database driver name
        bds.setDriverClassName(DRIVER_CLASS_NAME);
        //Set database url
        bds.setUrl(DB_URL);
        //Set database user
        bds.setUsername(DB_USER);
        //Set database password
        bds.setPassword(DB_PASSWORD);
        //Set the connection pool size
        bds.setInitialSize(CONN_POOL_SIZE);
    }

    private static class DataSourceHolder {
        private static final DataBaseUtility INSTANCE = new DataBaseUtility();
    }

    public static DataBaseUtility getInstance() {
        return DataSourceHolder.INSTANCE;
    }

    public BasicDataSource getBds() {
        return bds;
    }

    public void setBds(BasicDataSource bds) {
        this.bds = bds;
    }
}


