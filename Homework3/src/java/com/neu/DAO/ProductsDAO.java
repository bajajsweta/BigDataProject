/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.DAO;

import com.neu.model.PurchasedProducts;
import com.neu.model.RecommendedProducts;
import com.neu.model.SentimentProducts;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author ruchirapatil
 */


public class ProductsDAO extends DAO{
Connection conn;

    public List<SentimentProducts> getProductsBySentiment(String uid)
    {
        List<SentimentProducts> result=null;
        try {
            conn = getConnection();
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<List<SentimentProducts>> resultSetHandler = new BeanListHandler<>(SentimentProducts.class);
            String q = "select * from SentimentalAnalysis where userID=?";
            result = runner.query(conn, q, resultSetHandler,uid);
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {    
        	try {
                close(conn);
            } 
        	catch (SQLException ex) {
                Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
         }

        return result;

    }

    public List<RecommendedProducts> getRecommendedProducts(String uid)
    {
        List<RecommendedProducts> result=null;
        try {
            conn = getConnection();
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<List<RecommendedProducts>> resultSetHandler = new BeanListHandler<>(RecommendedProducts.class);
            String q = "select * from ProductRecommendations where ReviewerID=?";
            result = runner.query(conn, q, resultSetHandler,uid);
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {    
        	try {
                close(conn);
            } 
        	catch (SQLException ex) {
                Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
         }



        return result;

    }
    
    public List<PurchasedProducts> getPurchasedProducts(String uid)
    {
        List<PurchasedProducts> result=null;
        try {
            conn = getConnection();
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<List<PurchasedProducts>> resultSetHandler = new BeanListHandler<>(PurchasedProducts.class);
            String q = "select * from ProductsPurchased where ReviewerID=?";
            result = runner.query(conn, q, resultSetHandler,uid);
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {    
        	try {
                close(conn);
            } 
        	catch (SQLException ex) {
                Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
         }



        return result;

    }
}


