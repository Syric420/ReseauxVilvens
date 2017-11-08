<%@page contentType="text/html"%>
<html>
<head><title>Authentification sur le Data Center (1)</title></head>
<body>
<p>Vous souhaitez donc entrer dans notre grande famille de chercheurs - félicitations !<p>
<% String nom = request.getParameter("nom");%>
Vous êtes donc : <%=nom%>
Confirmez votre mot de passe :
<form method="POST" action=" http://localhost:8084/TestCodePdf/ControleDataCenter">
<P>Mot de passe : <input type="password" name="motDePasse" size=20></P>
<input type="hidden" name="action" value="AuthentificationInProgress">
<P><input type="submit" value="confirmer"></P>
</form>
</body>
</html>