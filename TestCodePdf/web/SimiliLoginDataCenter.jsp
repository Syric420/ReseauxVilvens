<%@page language="java" %>
<%@page contentType="text/html; charset=ISO-8859-1"%>
<%@page info="(c) Claude Vilvens - 8/2004" %>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<jsp:useBean id="msgGeneral" scope="application" class="JspServlets.message" />
<html>
<head><title>Data Center des références</title></head>
<body>
<%! Date maintenant = new Date(); %>
<%! String laDate =
DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL,Locale.FRANCE).
format(maintenant); %>
<!-- Page demandée le <%=laDate %> -->
<P>Vous êtes toujours sur LE site des références scientifiques !<p>
Nous sommes le <%=laDate %> !!!<p>
<form method="POST" action=" http://localhost:8084/TestCodePdf/ControleDataCenter">
<P>Requête (vous êtes déjà authentifié) ou fin ?"<p>
<SELECT Name="action">
<OPTION>Requête
<OPTION>Terminer
</SELECT></P>
<P><input type="submit" value="action"></P>
</form>
<p><hr>
La pensée du jour : <p><jsp:getProperty name="msgGeneral" property="texteMessage" />
</body>
</html>