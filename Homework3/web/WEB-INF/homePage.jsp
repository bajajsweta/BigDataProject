<%-- 
    Document   : homePage
    Created on : May 30, 2016, 7:23:05 PM
    Author     : ruchirapatil
--%>

<%@page import="com.neu.model.Movie"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sun.rowset.internal.Row"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://unpkg.com/purecss@0.6.0/build/pure-min.css">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>JSP Page</title>
      
    </head>
    <body>
       <c:choose> 
      <c:when test="${not empty requestScope.task}">
          <c:choose>
              <c:when test="${requestScope.task=='showErrorMessage'}">
                <h1>ERROR!!!</h1><br/> 
            <h1>Wrong Input Format!!!</h1><br/> 
             </c:when>
          
             <c:when test="${requestScope.task=='showGraph'}">
                   <h1>Analysis Graphs</h1>
                 <Table>
                     
                     <tr><img src="Reviewcategory.png" style="max-height: 400px;"</tr><tr><img src="sentiment.png" style="max-height: 400px;"</tr>
                     <tr><img src="review_count.png" style="max-height: 400px;"</tr><tr><img src="Mostnumberofreviews.png" style="max-height: 400px;"</tr>
                     
                     <tr><img src="Graphical_Analysis50.PNG" style="max-height: 1000px;"</tr>
                  </Table>
             </c:when>
            
             <c:when test="${requestScope.task=='showAnalysis'}">

                 <h1> See Product Recommendations For You First!!</h1>

        <form action="AppController" method="get" class="pure-form pure-form-stacked">
   
   
<!--        <select name="action" >
  <option id='option1' value="recomendation" selected="browse">See Product Recommendations For You</option>
   </select>-->
        <input type ="submit" value="GO">
         <input type ="hidden" name="action" value="recomendation">
        </form>
             </c:when>
         
                <c:when test="${requestScope.task==('showSentimentProduct')}">
               <h1>Following is sentimental Analysis</h1><br/>
        <br/>
          <TABLE class="pure-table pure-table-bordered">
              <tr><TD>Product Id</td><td>Product Title</td><td>Sentiment</td><td>User Id</td></tr>
          <c:forEach var="element" items="${requestScope.rows}">

                <TR><TD>${element.productID}</TD>
                 <td>${element.productTitle}</td>
                <td>${element.sentiment}</td>
                 <td>${element.userID}</td></tr>
        </c:forEach>  
            </TABLE>
       <a href="AppController" task='showAnalysis'>Click here to go to Home page</a>
       
        
        </form>
             </c:when>
        
        <c:when test="${requestScope.task==('showAdminBasket')}">
            <h1>Recommendations</h1><br/>
        <br/>
          <TABLE border="1">
               <c:forEach var="element" items="${requestScope.productbasket}">
                   <tr><TD><table><tr>${element.p1.productName}</tr><tr><img src="${element.p1.productImgUrl}"></tr><table></td>
              <td>
          <c:forEach var="element1" items="${element.p2}">
                  <TABLE border="1">
                      <TR><TD>Product Id:${element1.productName}</td><img src="${element1.productImgUrl}"></td></tr>
                        
         
                  </table>
        </c:forEach> 
              </td>

              </tr>
                     </c:forEach> 
            </TABLE>
            
            
            </c:when>
          <c:when test="${requestScope.task==('showReccommendedProduct')}">
               <h1>Recommendations</h1><br/>
        <br/>
          <TABLE class="pure-table pure-table-bordered">
              <tr><TD>Product</td><td>Product Image</td></tr>
          <c:forEach var="element" items="${requestScope.rows}">
                  <TR><TD><b>Product Id:</b>${element.productID}</br><b>Product Name:</b>${element.productName}</br><b>Product Average Rating:</b>${element.avg_rating}</br><font color="blue"><b>Product Sentiment:</b>${element.sentiment}</font></br><b>Product Price:</b>${element.productPrice}</br></TD>
                 <td><img src=${element.productImgUrl}></td>        
                 </tr>
        </c:forEach>  
          </TABLE><br>
        Please Make a selection below!!<br/>
        <br/>
        <form action="AppController" method="get" class="pure-form pure-form-stacked">
            Product Id<input type=text name="productId" value="" required><br/>
<!--          <select name="action" >
                  <option id='option1' value="sentimental" selected='add'>See other User's Sentiments about the product</option>
  
  <option id='option1' value="marketDetails" selected="browse">See What other products were bought with this product </option>
   </select>-->
              <input type ="submit" value="Submit">
              <input type ="hidden" name="action" value="allAnalysis">
             
        </form>
        <a href="AppController">Click here to go to Home page</a>
         </c:when>
          <c:when test="${requestScope.task==('showAnalysis1')}">
          <tr><h2>Product:${requestScope.mainProduct.productName}<img src=${requestScope.mainProduct.productImgUrl}></br></tr><br/>
        <br/>
              <h2>Following Items were frequently bought with this item</h2>
          <TABLE class="pure-table pure-table-bordered">
           
<!--              <tr><td>Main Product:</td><td>${requestScope.mainProduct}</td></tr>-->
              <tr>
                    <td>Product ID</td>
                 <td>Product Name</td>
                  <td>Product Price}</td>
                 <td>Product Img</td>
                </tr>
          <c:forEach var="element" items="${requestScope.FrequentlyBoughtItems}">

                <tr>
                    <td>${element.productID}</td>
                 <td>${element.productName}</td>
                  <td>${element.productPrice}</td>
                 <td><img src=${element.productImgUrl}
                width="100%" height="100%" border="2" alt=""/></td>
                </tr>
                  
        </c:forEach>  
          </TABLE>
              <h2>Similar Products </h2>
          <TABLE class="pure-table pure-table-bordered">
           
<!--              <tr><td>Main Product:</td><td>${requestScope.mainProduct}</td></tr>-->
              <tr>
                  <td>Product 1</td>
                  <td>Product 2</td>
                  <td>Product 3</td>
                  <td>Product 4</td>
                  <td>Product 5</td>
                </tr>
          <c:forEach var="element" items="${requestScope.itemrecommendations}">
                <tr>
                 <td>${element.productTitle1}</br><img src=${element.producturl1}></td>
                 <td>${element.productTitle2}</br><img src=${element.producturl2}></td>
                 <td>${element.productTitle3}</br><img src=${element.producturl3}></td>
                 <td>${element.productTitle4}</br><img src=${element.producturl4}></td>
                 <td>${element.productTitle5}</br><img src=${element.producturl5}></td>
                </tr>                  
        </c:forEach>  
            </TABLE>
         
              <a href="AppController" >Click here to go to Home page</a>
         </c:when>
          </c:choose>
      </c:when>

      
<c:otherwise>

     <h1>Product Recommendation Engine</h1>
        <br/>
        Enter User Id!!<br/>
        <br/>
        <form action="AppController" method="get" class="pure-form pure-form-stacked">
            User Id<input type=text name="userId" value="" required><br/>
        <input type='hidden'  name="action" value="recomendation">
 
        <input type ="submit" value="Submit">
        </form>
        <br>
        <br>
        
        
        <form action="AppController" method="get" class="pure-form pure-form-stacked">
<!--            User Id<input type=text name="userId" value="" required><br/> -->
        <input type='hidden'  name="action" value="admin">
        <input type='text'  name="productName" required>
        <br>
        <input type ="submit" value="Click Here If You are An Admin">
        </form>
        
        
        <form action="AppController" method="get" class="pure-form pure-form-stacked">
<!--            User Id<input type=text name="userId" value="" required><br/> -->
        <input type='hidden'  name="action" value="admingraph">

        <br>
        <input type ="submit" value="Show Graphs">
        </form>
        
          <br/>
        
   
        </c:otherwise>
</c:choose>
 </body>

</html>
