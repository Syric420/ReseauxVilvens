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
        <h3>Bienvenue sur notre site très sérieux !</h3>
        <% 
            String user = request.getParameter("user");
            String password = request.getParameter("password");
        %>
<p>Nous vous saluons :
<%= user %> <%=password%>
    </body>
</html>
