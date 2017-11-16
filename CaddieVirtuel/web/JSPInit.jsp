<%-- 
    Document   : JSPInit
    Created on : 08-nov.-2017, 11:04:46
    Author     : Vince
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="http://localhost:8084/CaddieVirtuel/ServletIni" method="POST">
            
        <%String Login =(String) request.getAttribute("Login");
            if(Login == null)
                Login =request.getParameter("Login");
        %>
        <input type="hidden" id="Login" name="Login" value="<%=Login %>"/>   
            
        <% 
            //String data = request.getParameter("donnee");
            String data [][]= (String [][]) request.getAttribute("donnee");
            int line= (int) request.getAttribute("line");
            int col= (int) request.getAttribute("col");
        %>
        
        
<table name="table1" id ="table1" width="59%" border="1" style="border-collapse: collapse; ">
    <%
        %>
        <tr>
            <%
            for(int i = 1; i<=col;i++)
               { 
                   
   %>
                <th>
                <%= data[0][i-1]%>
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
            for(int i = 1; i<=col;i++)
               {                  
            %>
                <td>
                <%= data[l][i-1]%>
                </td>
                <%}
        l++;
        %>                   
        </tr>
        <% 
        }
    %>
</table>

    <input type="submit" value="Acces au catalogue des vols" name="button" />
    </form>
    </body>
</html>
