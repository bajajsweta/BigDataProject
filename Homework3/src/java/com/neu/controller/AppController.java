/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.controller;

import com.mysql.jdbc.Connection;
import com.neu.model.Movie;
import com.neu.model.Pair;
import com.neu.model.Product;
import com.neu.model.RecommendedProducts;
import com.neu.model.SentimentProducts;
import com.neu.model.itemRecommender;
import com.sun.rowset.internal.Row;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ruchirapatil
 */
public class AppController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   
    
    protected Connection establishConnectionJDBC() 
    {
     Connection connection=null;
     try{
         
         Class.forName("com.mysql.jdbc.Driver");
     }
     catch(ClassNotFoundException e)
     {
       System.out.println("where is your JDBC driver?");
       e.printStackTrace();
       
     }
     
       try{
         
         connection=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/Analysis","root","Nalinirajendra1234");
     }
     catch(SQLException e)
     {
       System.out.println("Connection failed Check output consol?");
       e.printStackTrace();
       
     }
       if(connection!=null)
       {
         System.out.println("Connected Successfully!!");  
         
         
       }
       return connection;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        
        if (action != null) {
            if(action.equalsIgnoreCase("productDetails"))
            {
                request.getSession().setAttribute("userId", request.getParameter("userId"));
              request.setAttribute("task", "showAnalysis");  
            }
             else if(action.equalsIgnoreCase("admingraph"))
            {
                //request.getSession().setAttribute("userId", request.getParameter("userId"));
              request.setAttribute("task", "showGraph");  
            
            }
            
            else if(action.equalsIgnoreCase("admin"))
            {
                
                try {
                    
                    //request.getSession().setAttribute("userId", request.getParameter("userId"));
                    //request.setAttribute("task", "showMarketBasket");
                    
                    String query=null;
                    Connection con=establishConnectionJDBC();
                    
                    Statement stmt = null;
            
                    stmt = con.createStatement();
                    //String sql = "SELECT id, first, last, age FROM Registration";
                    
                    
                    
                    
                    query="select * from BasketRecommendations";
                    // PreparedStatement stmt=con.prepareStatement(query);
                    //stmt.setString(1,"%"+keywd+"%");
                    // ResultSet rs2=stmt.executeQuery();
                    ResultSet rs2 = null;
              
                        rs2 = stmt.executeQuery(query);
                   
                    
                    ArrayList<Pair> rows = new ArrayList<Pair>();
                    try {
                        if(!rs2.next())
                        {
                            System.out.println("where is your JDBC driver?");
                        }
                        else
                        {
                            
                            rs2.beforeFirst();
                            int i=0;
                            while(rs2.next())
                            {
                                // System.out.println("inside");
                                String allSuggestedProducts= rs2.getString("SuggestedProducts");
                                String mainProduct=rs2.getString("pid");
                                String query4="select * from Product where ProductId ='"+mainProduct+"'";
                                Statement stmt3 = con.createStatement();
                                    //stmt1.setString(1,"%"+p+"%");
                                    ResultSet rs4=stmt3.executeQuery(query4);
//                                       if(!rs4.next())
//                    {
//    System.out.println("where is your JDBC driver?");
//                     }
                    rs4.next();
                                    Product p1=new Product ();
                                      p1.setProductName(rs4.getString("ProductName"));
                                    p1.setProductImgUrl(rs4.getString("ProductUrl"));
                                     Pair pair=new Pair();
                                    pair.setP1(p1);
                                    
                                System.out.println("Ruchira"+allSuggestedProducts);
                                String[] productList = allSuggestedProducts.split(" ");
                                ArrayList<Product> p8=new ArrayList<>();
                                for(String p:productList)
                                {
                                    
                                    String query2="select * from Product where ProductId ='"+p+"'";
                                    Statement stmt1 = con.createStatement();
                                    //stmt1.setString(1,"%"+p+"%");
                                    ResultSet rs3=stmt1.executeQuery(query2);
                                    if(!rs3.next())
                                    {
                                            System.out.println("where is your JDBC driver?");
                                    }
                                    else
                                    {
                                        rs3.beforeFirst();
                                    rs3.next();
                                   Product product=new Product();
                                   //product.setProductID(rs3.getString("ProductId"));
                                  product.setProductName(rs3.getString("ProductName"));
                                    product.setProductImgUrl(rs3.getString("ProductUrl"));
                                   // product.setProductPrice(rs3.getString("ProductPrice"));
                                   
                                    // pair.getP2().add(product);
                                    p8.add(product);
                                   
                                    rs3.close();
                                    stmt1.close();
                                }
                                    
                                    
                                    
                                }
                                pair.setP2(p8);
                                rows.add(pair); 
                                rs4.close();
                                    stmt3.close();
                                    i++;
                                            if(i>9)
                                            {
                                                break;
                                                
                                            }
                            }
                            
                        }
                    } catch (SQLException ex) {                        
                        Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    request.setAttribute("task", "showAdminBasket");
                    request.setAttribute("productbasket", rows);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
                }
                  
              
              
            }
             else if (action.equalsIgnoreCase("recomendation"))
            {
                
              try {
                   request.getSession().setAttribute("userId", request.getParameter("userId"));
                    String keywd=(String) request.getSession().getAttribute("userId");
                    //String keywdType=request.getParameter("selectR");
                    //System.out.println("test"+keywdType);
                    String query=null;
                    Connection con=establishConnectionJDBC();
                  
                        query="select pr.ProductId, pr.ProductTitle, sa.Sentiment, sa.AverageRating, sa.NoOfTimes\n" +
", p.ProductUrl ,p.ProductPrice\n" +
"from ProductRecommendations pr\n" +
"inner join SentimentalAnalysis sa \n" +
"on pr.productId = sa.productId	\n" +
"inner join product p\n" +
"on pr.ProductId = p.ProductId\n" +
"where pr.UserId like ?";
                        
                     //query="select * from movies where actress like ?";
                    
                    PreparedStatement stmt=con.prepareStatement(query);
                    stmt.setString(1,"%"+keywd+"%");
                    ResultSet rs2=stmt.executeQuery();
                   
                      ArrayList<Product> rows = new ArrayList<Product>();
                      if(!rs2.next())
                    {
    System.out.println("where is your JDBC driver?");
                     }
                      else
                      {
                          rs2.beforeFirst();
                          int i=0;
                          while(rs2.next())
                          {

                             Product product=new Product();
                           product.setProductID(rs2.getString("ProductID"));
                           product.setProductName(rs2.getString("ProductTitle"));
                           product.setProductImgUrl(rs2.getString("ProductUrl"));
                           product.setProductPrice(rs2.getString("ProductPrice"));
                           product.setSentiment(rs2.getString("Sentiment"));
                           product.setNoOfTimes(rs2.getString("NoOfTimes"));
                           product.setAvg_rating(rs2.getString("AverageRating"));
                            rows.add(product);
                            
                            
i++;
                            
                            if(i>3)
                            {
                                break;
                            }
                            
                          }
                      }
                  
                    request.setAttribute("task", "showReccommendedProduct"); 
                    request.setAttribute("rows", rows); 
                    rs2.close();
                    stmt.close();
                    con.close();
                    
                   
                } catch (SQLException ex) {
                    Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
              
            }
             else if (action.equalsIgnoreCase("allAnalysis"))
            {
                
                request.getSession().setAttribute("productId", request.getParameter("productId"));
              //request.setAttribute("task", "showMarkettAnalysis"); 
              try {
                    String keywd=(String) request.getSession().getAttribute("productId");
                    //String keywdType=request.getParameter("selectR");
                    //System.out.println("test"+keywdType);
                    String query=null;
                    Connection con=establishConnectionJDBC();
                    
                    Statement stmt = con.createStatement();

      //String sql = "SELECT id, first, last, age FROM Registration";
     
                    
                    
                   query="select * from BasketRecommendations where pid='"+keywd+"'";
                   // PreparedStatement stmt=con.prepareStatement(query);
                    //stmt.setString(1,"%"+keywd+"%");
                   // ResultSet rs2=stmt.executeQuery();
                     ResultSet rs2 = stmt.executeQuery(query);
                   
                      ArrayList<Product> rows = new ArrayList<Product>();
                      if(!rs2.next())
                    {
    System.out.println("where is your JDBC driver?");
                     }
                      else
                      {
                          rs2.beforeFirst();
                          while(rs2.next())
                          {
                             // System.out.println("inside");
                        String allSuggestedProducts= rs2.getString("SuggestedProducts");
                        System.out.println("Ruchira"+allSuggestedProducts);
                              String[] productList = allSuggestedProducts.split(" ");
                              for(String p:productList)
                              {
                                    String query2="select * from Product where ProductId ='"+p+"'";
                          Statement stmt1 = con.createStatement();
                    //stmt1.setString(1,"%"+p+"%");
                    ResultSet rs3=stmt1.executeQuery(query2);
                    rs3.next();
                      Product product=new Product();
                           product.setProductID(rs3.getString("ProductId"));
                           product.setProductName(rs3.getString("ProductName"));
                          product.setProductImgUrl(rs3.getString("ProductUrl"));
                          product.setProductPrice(rs3.getString("ProductPrice"));
                           
                            rows.add(product);
                             rs3.close();
                             stmt1.close();
                                  
                              }
                            
                          }
                            
                      }
                  
                    //request.setAttribute("task", "showBasketProduct"); 
                    request.setAttribute("FrequentlyBoughtItems", rows); 
                    query="select * from Product where ProductId ='"+keywd+"'";
                        
                     //query="select * from movies where actress like ?";
                    
                   Statement stmt3=con.createStatement();
                    //stmt3.setString(1,"%"+keywd+"%");
                    ResultSet rs4=stmt3.executeQuery(query);
                    rs4.next();
                    Product p2=new Product();
                    p2.setProductName(rs4.getString("ProductName"));
                    p2.setProductImgUrl(rs4.getString("ProductUrl"));
                    request.setAttribute("mainProduct",p2);
                    rs2.close();
                  
                      rs4.close();
                    stmt.close();
                      stmt3.close();
                    con.close();
                    
                   
                } catch (SQLException ex) {
                    Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        
                
                
                   request.getSession().setAttribute("productId", request.getParameter("productId"));
              try {
                    String keywd=(String) request.getSession().getAttribute("productId");
                   
                    String query=null;
                    Connection con=establishConnectionJDBC();
                  
                        query="select * from itemrecommendation where product='"+keywd+"'";
                        
                     //query="select * from movies where actress like ?";
                    
                    Statement stmt=con.createStatement();
                   // stmt.setString(1, "%" + keywd + "%");
                    ResultSet rs2=stmt.executeQuery(query);
                   
                      ArrayList<itemRecommender> rows2 = new ArrayList<itemRecommender>();
                      if(!rs2.next())
                    {
    System.out.println("where is your JDBC driver?");
                     }
                      else
                      {
                          rs2.beforeFirst();
                          while(rs2.next())
                          {
                             itemRecommender itemRc=new itemRecommender();
                             
                           itemRc.setProductTitle1(rs2.getString("ma1_title"));
                           itemRc.setProducturl1(rs2.getString("ma1_imurl"));
                           itemRc.setProductTitle2(rs2.getString("ma2_title"));
                           itemRc.setProducturl2(rs2.getString("ma2_imurl"));
                           itemRc.setProductTitle3(rs2.getString("ma3_title"));
                           itemRc.setProducturl3(rs2.getString("ma3_imurl"));
                           itemRc.setProductTitle4(rs2.getString("ma4_title"));
                           itemRc.setProducturl4(rs2.getString("ma4_imurl"));
                           itemRc.setProductTitle5(rs2.getString("ma5_title"));
                           itemRc.setProducturl5(rs2.getString("ma5_imurl"));
                           
                            rows2.add(itemRc);
                            
                          }
                      }
                  
                    request.setAttribute("task", "showAnalysis1"); 
                    request.setAttribute("itemrecommendations", rows2); 
                    rs2.close();
                    stmt.close();
                    con.close();
                    
                   
                } catch (SQLException ex) {
                    Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
             else if (action.equalsIgnoreCase("recomendation"))
            {
              try {
                    String keywd=(String) request.getSession().getAttribute("userId");
                    //String keywdType=request.getParameter("selectR");
                    //System.out.println("test"+keywdType);
                    String query=null;
                    Connection con=establishConnectionJDBC();
                  
                        query="select * from ProductRecommendations where UserId like ?";
                        
                     //query="select * from movies where actress like ?";
                    
                    PreparedStatement stmt=con.prepareStatement(query);
                    stmt.setString(1,"%"+keywd+"%");
                    ResultSet rs2=stmt.executeQuery();
                   
                      ArrayList<RecommendedProducts> rows = new ArrayList<RecommendedProducts>();
                      if(!rs2.next())
                    {
    System.out.println("where is your JDBC driver?");
                     }
                      else
                      {
                          rs2.beforeFirst();
                          while(rs2.next())
                          {

                             RecommendedProducts product=new RecommendedProducts();
                           product.setProductID(rs2.getString("ProductID"));
                           product.setProductTitle(rs2.getString("ProductTitle"));
                           //product.setSentiment(rs2.getString("Sentiment"));
                           product.setReviewerID(rs2.getString("UserId"));
                           
                            rows.add(product);
                            
                          }
                      }
                  
                    request.setAttribute("task", "showReccommendedProduct"); 
                    request.setAttribute("rows", rows); 
                    rs2.close();
                    stmt.close();
                    con.close();
                    
                   
                } catch (SQLException ex) {
                    Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
          //  else if (action.equalsIgnoreCase("add"))
//            {
//              request.setAttribute("task", "addMovies");
//            }
//            else if (action.equalsIgnoreCase("browse"))
//                    {
//                       request.setAttribute("task", "browseMovies"); 
//                    }
//            else if(action.equalsIgnoreCase("insertTabel"))
//            {
//                try {
//                    String title=request.getParameter("title");
//                    String actor=request.getParameter("actor");
//                    String actress=request.getParameter("actress");
//                    String genre=request.getParameter("genre");
//                    String year2=request.getParameter("year");
//                    
//                    //String instructorName = request.getParameter("text"+j);
//                
//                title = title.replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", "+").trim();
//                actor = actor.replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", "+").trim();
//                actress = actress.replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", "+").trim();
//                genre = genre.replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", "+").trim();
//                //year2 = year2.replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", "+").trim();
//                if (title.equals("")||actor.equals("")||actress.equals("")||genre.equals("")||year2.equals("") )
//                        {
//                            request.setAttribute("task", "showErrorMessage");
//                        }
//                else
//                {
//
//                    int year=Integer.parseInt(request.getParameter("year"));
//                    
//                    Connection con=establishConnectionJDBC();
//                    String query="INSERT INTO Movies\n" +
//                            "VALUES (?,?,?,?,?)";
//                    PreparedStatement stmt=con.prepareStatement(query);
//                    stmt.setString(1, title);
//                    stmt.setString(2, actor);
//                    stmt.setString(3, actress);
//                    stmt.setString(4, genre);
//                    stmt.setInt(5, year);
//                    stmt.executeUpdate();
//                    request.setAttribute("task", "showInsertionMessage");
//                    stmt.close();
//                    con.close();
//                }
//                } catch (SQLException ex) {
//                    Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            
//        }
//            else if(action.equalsIgnoreCase("searchMovie"))
//            {
//                try {
//                    String keywd=request.getParameter("keyword");
//                    String keywdType=request.getParameter("selectR");
//                    System.out.println("test"+keywdType);
//                    String query=null;
//                    Connection con=establishConnectionJDBC();
//                    if(keywdType.equalsIgnoreCase("title"))
//                    {
//                        query="select * from movies where title like ?";
//                    }
//                    else if(keywdType.equalsIgnoreCase("actor"))
//                        
//                    {
//                        query="select * from movies where actor like ?";
//                        
//                    }
//                    else
//                    {
//                        query="select * from movies where actress like ?";
//                    }
//                    PreparedStatement stmt=con.prepareStatement(query);
//                    stmt.setString(1, "%" + keywd + "%");
//                    ResultSet rs2=stmt.executeQuery();
//                   
//                      ArrayList<Movie> rows = new ArrayList<Movie>();
//                      if(!rs2.next())
//                    {
//    System.out.println("where is your JDBC driver?");
//                     }
//                      else
//                      {
//                          rs2.beforeFirst();
//                          while(rs2.next())
//                          {
//                            Movie movie=new Movie();
//                            movie.setTitle(rs2.getString("title"));
//                            movie.setActor(rs2.getString("actor"));
//                            movie.setActress(rs2.getString("actress"));
//                            movie.setGenre(rs2.getString("genre"));
//                            movie.setYear(rs2.getInt("year"));
//                            rows.add(movie);
//                            
//                          }
//                      }
//                  
//                    request.setAttribute("task", "showMovies"); 
//                    request.setAttribute("rows", rows); 
//                    rs2.close();
//                    stmt.close();
//                    con.close();
//                    
//                   
//                } catch (SQLException ex) {
//                    Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//            }
             RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/homePage.jsp");
            rd.forward(request, response);
        }
        else
        {
               RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/homePage.jsp");
            rd.forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
