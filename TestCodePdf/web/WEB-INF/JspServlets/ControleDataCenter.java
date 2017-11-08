/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
public class ControleDataCenter extends HttpServlet
{
public void init(ServletConfig config) throws ServletException
{
super.init(config);
ServletContext sc = getServletContext();
sc.log("-- démarrage de la servlet ControleDataCenter");
}
public void destroy() { }
protected void processRequest(HttpServletRequest request, HttpServletResponse response)
throws ServletException, java.io.IOException
{
java.io.PrintWriter out = response.getWriter();
ServletContext sc = getServletContext();
message penseeDuJour = new message("A vaincre sans péril, on triomphe sans gloire");
sc.setAttribute("msgGeneral", penseeDuJour);
sc.log("-- passage par la servlet ControleDataCenter");
String action = request.getParameter("action");
sc.log("-- Valeur du paramètre action : " + action);
if (action.equals("Authentification"))
{
setAuthentificationInProgress(request, true);
sc.log("Nom récupéré par paramètre = " + request.getParameter("nom") + " -- ok ?");
MemberDataCenter mdc = new MemberDataCenter(request.getParameter("nom"),
request.getParameter("motDePasse"));
HttpSession session = request.getSession(true);
session.setAttribute("membre", mdc);
RequestDispatcher rd = sc.getRequestDispatcher("/AuthenticateDataCenter.jsp");
sc.log("-- Tentative de redirection sur AuthenticateDataCenter.jsp");
rd.forward(request, response);
}
else
if (action.equals("Terminer"))
{
HttpSession session = request.getSession(true);
session.invalidate();
Object obj = sc.getAttribute("membre");
if (obj != null) sc.removeAttribute("membre");
}
if (!isAuthenticated(request) && !isAuthentificationInProgress(request) )
    {
sc.log("-- Validation introuvable dans la session");
RequestDispatcher rd = sc.getRequestDispatcher("/LoginDataCenter.jsp?msg=" +
URLEncoder.encode("Veuillez d'abord vous authentifier !"));
rd.forward(request, response);
}
else
if (action.equals("AuthentificationInProgress"))
{
HttpSession session = request.getSession(true);
MemberDataCenter mdc = (MemberDataCenter) session.getAttribute("membre");
sc.log("Nom récupéré = " + mdc.getNom());
sc.log("Nouveau mot de passe éventuel : " + request.getParameter("motDePasse"));
mdc.setMotDePasse(request.getParameter("password"));
session.setAttribute("membre", mdc);
RequestDispatcher rd = sc.getRequestDispatcher("/Authenticate2DataCenter.jsp");
sc.log("-- Tentative de redirection sur Authenticate2DataCenter.jsp");
rd.forward(request, response);
}
else
if (action.equals("AuthenticateRecorded"))
{
HttpSession session = request.getSession(true);
sc.log("-- Enregistrement du nouveau membre");
MemberDataCenter mdc = (MemberDataCenter) session.getAttribute("membre");
sc.log("Nom récupéré = " + mdc.getNom());
mdc.setPrenom(request.getParameter("prenom"));
mdc.setEMail(request.getParameter("eMail"));
session.setAttribute("membre", mdc);
setAuthenticated(request, true);
RequestDispatcher rd = sc.getRequestDispatcher("/RequestDataCenter.jsp");
rd.forward(request, response);
}
if (action.equals("Requête"))
{
RequestDispatcher rd = sc.getRequestDispatcher("/RequestDataCenter.jsp");
rd.forward(request, response);
}
if (action.equals("RequestHandling"))
{
if (!isAuthenticated(request))
{
sc.log("-- Validation non terminée");
RequestDispatcher rd = sc.getRequestDispatcher("/LoginDataCenter.jsp?msg=" +
URLEncoder.encode("Veuillez d'abord terminer l'authentification !"));
rd.forward(request, response);
}
else
{
sc.log("-- Traitement de la requête");
RequestDispatcher rd = sc.getRequestDispatcher("/ResponseDataCenter.jsp");
rd.forward(request, response);
}
}
if (action.equals("NewRequest"))
{
String reponse = request.getParameter("reponse");
if (reponse.equals("Oui"))
{
sc.log("-- Vers une nouvelle requête");
RequestDispatcher rd = sc.getRequestDispatcher("/RequestDataCenter.jsp");
rd.forward(request, response);
}
else
    {
sc.log("-- Retour au login");
RequestDispatcher rd = sc.getRequestDispatcher("/SimiliLoginDataCenter.jsp");
rd.forward(request, response);
}
}
out.close();
}
protected void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, java.io.IOException { processRequest(request, response); }
protected void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, java.io.IOException { processRequest(request, response); }
/** Returns a short description of the servlet.
*/
public String getServletInfo() { return "Servlet contrôleur"; }
private boolean isAuthenticated (HttpServletRequest request)
{
HttpSession session = request.getSession(true);
// Object existe = session.getValue("UserValid");
Object existe = session.getAttribute("UserValid");
return existe!=null;
}
private boolean isAuthentificationInProgress (HttpServletRequest request)
{
HttpSession session = request.getSession(true);
// Object existe = session.getValue("UserValid");
Object existe = session.getAttribute("ValidationInProgress");
return existe!=null;
}
private void setAuthenticated (HttpServletRequest request, boolean b)
{
HttpSession session = request.getSession(true);
if (b) session.setAttribute("UserValid", "Ok");
else session.removeAttribute("UserValid");
}
private void setAuthentificationInProgress (HttpServletRequest request, boolean b)
{
HttpSession session = request.getSession(true);
if (b) session.setAttribute("ValidationInProgress", "Ok");
else session.removeAttribute("ValidationInProgress");
}
}
