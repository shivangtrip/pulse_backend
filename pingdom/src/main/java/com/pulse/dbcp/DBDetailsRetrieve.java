package com.pulse.dbcp;

import com.pulse.service.MailingDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.dc.pr.PRError;

import java.sql.*;

public class DBDetailsRetrieve {
    private final static Logger log = LoggerFactory.getLogger(UrlDBInsert.class);
    private String RETRIEVE_DETAILS = "SELECT email,username FROM users WHERE userid IN (SELECT user_id FROM URLS WHERE url=?)";

    private String url;

    public DBDetailsRetrieve(String url) {
        this.url = url;
    }
    public MailingDetails retrieveDetails(){
        MailingDetails mailingDetails = new MailingDetails();
        DBCPConnectionHelper dbcpConnectionHelper = new DBCPConnectionHelper();
        Connection conn = null;
        ResultSet resultSet = null;
        try{
            conn = dbcpConnectionHelper.createConnection();


            PreparedStatement preparedStatement = conn.prepareStatement(RETRIEVE_DETAILS);
            preparedStatement.setString(1,url);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                mailingDetails.setEmail(resultSet.getString("email"));
                mailingDetails.setEmail(resultSet.getString("username"));

            }
        }catch(SQLException e){
            log.info("Exception in dbcp connection");
            throw new RuntimeException(e);

        }
        finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return mailingDetails;
    }

}
