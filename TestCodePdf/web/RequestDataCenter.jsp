<%@page contentType="text/html"%>
<jsp:useBean id="membre" scope="session" class="JspServlets.MemberDataCenter" />
<jsp:useBean id="msgGeneral" scope="application" class="JspServlets.message" />
<html>
<head><title>Requête au le Data Center (1)</title></head>
<body>
<H3>Bonjour cher <jsp:getProperty name="membre" property="prenom" />
<jsp:getProperty name="membre" property="nom" /> !
</h3>
<p>Je vous écoute : que cherchez-vous ?"<p>
<form method="POST" action=" http://localhost:8084/TestCodePdf/ControleDataCenter">
<P>Auteur : <input type="text" name="auteur" size=20></P>
<P>Année de publication : <input type="text" name="annee" size=30></P>
<input type="hidden" name="action" value="RequestHandling">
<P><input type="submit" value="action"></P>
</form>
<p><hr>
La pensée du jour : <p><jsp:getProperty name="msgGeneral" property="texteMessage" />
</body>
</html>