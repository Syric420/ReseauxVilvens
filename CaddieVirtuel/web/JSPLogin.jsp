<%-- 
    Document   : index
    Created on : 08-nov.-2017, 10:39:27
    Author     : Vince
--%>

<%@page import="database.utilities.ReadProperties"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Connexion</title>
        
    <head>   
    </head>
          
        <body>
            <%
            ReadProperties rP ;
            rP = new ReadProperties("/database/utilities/Config.properties");
            String ipServ = rP.getProp("IP_SERVER");
            System.out.println(ipServ);
            int timeout = session.getMaxInactiveInterval();
            response.setHeader("Refresh", timeout + "; URL = JSPLogin.jsp");
        %>
            <% String msg= request.getParameter("msg");
if (msg!=null) out.println("<H2>" + msg + "</H2><p>");
%>
    
    <form action=<%=ipServ%> method="POST">
        <input type="hidden" id="Jsp" name="Jsp" value="JSPLogin"/>
        <input type="hidden" id="IP" name="IP" value=<%=ipServ%>> 
        <h1>Entrez vos identifiants</h1>
        <p> Identifiant: <input type="text" name="user" value="" size="50" autofocus required /> </p>
        <p> Mot de passe: <input type="password" name="password" value="" size="50" required/> </p>
        <p> Nouvel utilisateur: <input type="checkbox" name="NewUser" value="ON" /> </p>
        <input type="submit" value="S'identifier" name="button" />

    </form>
   </body>     
</html>
