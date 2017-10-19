package com.scribo.common;

import com.scribo.objects.Term;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

public class Thesaurus {
    private static final Logger LOGGER = Logger.getLogger(Thesaurus.class.getName());

    // class variables
    private final String connPath = "jdbc:mysql://127.0.0.1:3306/scribo?autoReconnect=true&useSSL=false";
    private final String connUser = "root";
    private String connPass = null;

    
    // constructor
    public Thesaurus(String connPass) {
        setConnPass(connPass);
    }

    private void setConnPass(String connPass) {
        this.connPass = connPass;
    }
    
    // connect to MySQL and return thesaurus
    public List BuildThesaurus() {
        ResultSet rs;
        List returnList = new LinkedList();
        String connQuery = "select * from thesaurus where level = 1";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            // connect to msyql database and execute query
            Connection conn = DriverManager.getConnection(this.connPath, this.connUser, this.connPass);
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(connQuery);
            
            // loop through dataset
            while(rs.next()) {
                int id = rs.getInt(1);
                String search = rs.getString(2);
                String replace = rs.getString(3);
                
                Term newTerm = new Term(id, search, replace);
                if(newTerm != null)
                    returnList.add(newTerm);
            }

            // close connection
            conn.close();
        }
        
        catch(Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            System.exit(0);
        }

        int thesaurus_len = returnList.size();
        if (thesaurus_len == 0) {
            LOGGER.log(Level.SEVERE, "No records found to build thesaurus.");
            System.exit(0);
        }
        LOGGER.log(Level.INFO, thesaurus_len + " records found for thesaurus.");

        return returnList;
    }
}