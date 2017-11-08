<%@page contentType="text/html"%>
<jsp:useBean id="msgGeneral" scope="application" class="JspServlets.message" />
<html>
<head><title>Réponse du Data Center (1)</title></head>
<body>
<H3>Voici les références correspondant à votre demande : </H3><p>0 référence ... :-(<p>
<form method="POST" action=" http://localhost:8084/TestCodePdf/ControleDataCenter">
<P>Autre requête ?
<SELECT Name="reponse">
<OPTION>Oui
<OPTION>Non
</SELECT></P>
<input type="hidden" name="action" value="NewRequest">
<P><input type="submit" value="action"></P>
</form>
<p><hr>
La pensée du jour : <p><jsp:getProperty name="msgGeneral" property="texteMessage" />
</body>
</html>