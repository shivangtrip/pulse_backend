package com.pulse.dbcp;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DBCPConnectionHelper {
    private final static Logger log = LoggerFactory.getLogger(UrlDBInsert.class);
    public Connection createConnection(){
        try {
            BasicDataSource basicDataSource = DBInit.getInstance().getBasicDataSource();
            return basicDataSource.getConnection();
        }catch(SQLException e){
            log.info("Sql exception");
           throw new RuntimeException(e);


        }


    }
}
