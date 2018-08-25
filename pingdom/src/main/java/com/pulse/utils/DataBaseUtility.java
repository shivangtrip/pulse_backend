package com.pulse.dbcp;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseUtility {


    Properties properties = new Properties();
    InputStream inputStream;
    String propFileName = "pulse.properties";
    private BasicDataSource bds = new BasicDataSource();

    private DataBaseUtility() {
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            //Set database driver name
            bds.setDriverClassName(properties.getProperty("DRIVER_CLASS_NAME"));
            //Set database url
            bds.setUrl(properties.getProperty("DATABASE_URL"));
            //Set database user
            bds.setUsername(properties.getProperty("DATABASE_USER_NAME"));
            //Set database password
            bds.setPassword(properties.getProperty("DATABASE_PASSWORD"));
            //Set the connection pool size
            bds.setInitialSize(Integer.valueOf(properties.getProperty("CONN_POOL_SIZE")));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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


