package com.nk;

import org.apache.commons.dbcp2.BasicDataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBInit {

    Properties properties = new Properties();
    InputStream inputStream;
    String propFileName = Constants.FILE_NAME;


    private BasicDataSource basicDataSource = new BasicDataSource();

    private DBInit() {
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException(String.format("property file %s not found in classpath", propFileName));
            }
            //Setting database driver name
            basicDataSource.setDriverClassName(properties.getProperty("DRIVER_CLASS_NAME"));
            //Setting database url
            basicDataSource.setUrl("jdbc:postgresql://"+properties.getProperty("HOST")+":"+properties.getProperty("DATABASE_PORT")+"/"+properties.getProperty("DATABASE_NAME"));
            //Setting database user
            basicDataSource.setUsername(properties.getProperty("DATABASE_USER_NAME"));
            //Setting database password
            basicDataSource.setPassword(properties.getProperty(Constants.DATABASE_PASSWORD));
            //Setting the connection pool size
            basicDataSource.setInitialSize(Integer.valueOf(properties.getProperty("CONN_POOL_SIZE")));
        }catch (IOException e) {
          throw new RuntimeException(e);
        }
    }

    private static class DataSourceHolder {
        private static final DBInit INSTANCE = new DBInit();
    }

    public static DBInit getInstance() {
        return DataSourceHolder.INSTANCE;
    }

    public BasicDataSource getBasicDataSource() {
        return basicDataSource;
    }

    public void setBasicDataSource(BasicDataSource bds) {
        this.basicDataSource = bds;
    }
}



