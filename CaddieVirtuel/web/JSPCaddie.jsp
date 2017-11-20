<%-- 
    Document   : JSPCaddie
    Created on : 10 nov. 2017, 16:24:28
    Author     : tibha
--%>

<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="database.utilities.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns:h="http://java.sun.com/jsf/html" xmlns:f="http://java.sun.com/jsf/core">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            int timeout = session.getMaxInactiveInterval();
            response.setHeader("Refresh", timeout + "; URL = JSPLogin.jsp");
        %>
        <h1>Vols disponibles</h1>
        
<form action="http://localhost:8084/CaddieVirtuel/ServletMain" method="POST">
    <input type="hidden" id="Jsp" name="Jsp" value="JSPCaddie"/> 
    <%String Login = request.getParameter("Login");%>
    <input type="hidden" id="Login" name="Login" value="<%=Login %>"/>
  <input type="hidden" id="pushedbutton" name="pushedbutton" value="0"/>
          <% 
            //String data = request.getParameter("donnee");
            String data [][]= (String [][]) request.getAttribute("donnee");
            int line= (int) request.getAttribute("line");
            int col= (int) request.getAttribute("col");
        %>

<table name="table1" id ="table1" width="59%" border="1" style="border-collapse: collapse; ">
        <tr>
            <%
            String str = "";
            for(int i = 1; i<=col;i++)
               { %>
                <th>
                <%= data[0][i-1]%>
                </th>
           <% 
                    if(i==col)
                    {
                    %>
                        <th>
                            Réservations
                        </th>
                    <%
                    }
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
                    if(i==1)
                        str=data[l][i-1];
            %>
                <td>
                <%= data[l][i-1]%>
 
                </td>
                
           <% 
                    if(i==col)
                    {
                    %>
                        <td>
                            <input type="submit" name="<%="Reserve" + line%> " value="<%="Reserve"%>" onClick="document.getElementById('pushedbutton').value='<%= str%>';" </input> 
                        </td>
                    <%
                    }
               }
        l++;
        %>                   
        </tr>
        <% 
        }
    %>
</table>
</form>

    </body>
</html>



