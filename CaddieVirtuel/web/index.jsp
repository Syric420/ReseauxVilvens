<%-- 
    Document   : index
    Created on : 08-nov.-2017, 10:39:27
    Author     : Vince
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Connexion</title>
        
    <head>
        <body>
    <form action="JSPInit.jsp" method="POST">
        
        <h1>Entrez vos identifiants</h1>
        <p> Identifiant: <input type="text" name="user" value="" size="50" autofocus required /> </p>
        <p> Mot de passe: <input type="password" name="password" value="" size="50" required/> </p>
        <p> Nouvel utilisateur: <input type="checkbox" name="NewUser" value="ON" /> </p>
        <input type="submit" value="S'identifier" name="button" />
    </form>
   </body>     
</html>
