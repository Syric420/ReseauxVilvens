<%@page contentType="text/html"%>
<jsp:useBean id="membre" class= "JspServlets.MemberDataCenter" scope="session"/>
<html>
<head><title>Authentification sur le Data Center (2)</title></head>
<body>
<% String password = request.getParameter("motDePasse");%>
Vous êtes donc : <jsp:getProperty name="membre" property="nom" />
avec le mot de passe : <%=password%>
<p>Nous souhaitons quelques renseignements complémentaires<p>
<form method="POST" action=" http://localhost:8084/TestCodePdf/ControleDataCenter">
<P>Prénom : <input type="text" name="prenom" size=20></P>
<P>Votre e-mail : <input type="text" name="eMail" size=30></P>
<input type="hidden" name="action" value="AuthenticateRecorded">
<P><input type="submit" value="confirmer"></P>
</form>
</body>
</html>