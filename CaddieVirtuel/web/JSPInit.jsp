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
        <form action="http://localhost:8084/CaddieVirtuel/JSPCaddie.jsp" method="POST">
        <% 
            //Il faudra ici rediriger vers une servlet adequate pour cahrger jsp cadie etc
            String user = request.getParameter("user");
            String password = request.getParameter("password");
            
        %>
                <input type="hidden" id="Login" name="Login" value="<%= user %>"/>
                <input type="submit" value="Voir catalogue" name="button" />
        <p>Nous vous saluons :
        <%= user %> <%=password%>
</form>
    </body>
</html>
