/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.DAO;

/**
 *
 * @author ruchirapatil
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;


public class DAO {

    private String driver;
    private String dburl;
    private String dbuser;
    private String dbpassword;
    
    public DAO(){
        driver = "com.mysql.jdbc.Driver";
        dburl = "jdbc:mysql://localhost:3306/AmazonProducts";
        dbuser = "root";
        dbpassword="root";
        
        DbUtils.loadDriver(driver);
    }
    
    public Connection getConnection(){
        Connection conn =null;
        try{
            conn = DriverManager.getConnection(dburl,dbuser,dbpassword);
            
        }catch(SQLException ex){
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE,null,ex);
        }
        return conn;
    }
    public void close(Connection connection) throws SQLException{
        if(connection != null){
            DbUtils.close(connection);
        }
    } 
}
