<%-- 
    Document   : JSPPay
    Created on : 12 nov. 2017, 10:04:23
    Author     : tibha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Time to pay!</h1>
        <% 
            //String data = request.getParameter("donnee");
            String data [][]= (String [][]) request.getAttribute("donnee");
            int line= (int) request.getAttribute("line");
            int col= (int) request.getAttribute("col");
        %>
        
        
<table name="table1" id ="table1" width="59%" border="1" style="border-collapse: collapse; ">
    <%
        int j=0;
        %>
        <tr>
            <%
            for(int i = 1; i<col;i++)
               { %>
                <th>
                <%= data[0][i]%>
                </th>
           <% 
               }
        %>                   
        </tr>
        <% 
        int l =1;
        while(l<line)
        {
        %>
        <tr>
            <%
            for(int i = 1; i<col;i++)
               { 
            %>
                <td>
                <%= data[l][i]%>
 
                </td>
                
           <% 

               }
        l ++;
        %>                   
        </tr>
        <% 
        }
    %>
</table>
    </body>
</html>
