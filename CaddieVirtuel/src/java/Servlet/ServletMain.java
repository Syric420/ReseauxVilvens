/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import database.utilities.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vince
 */
public class ServletMain extends HttpServlet {
    
    BeanBD BeanBD;
    
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //Connexion à la base de données
        BeanBD = new BeanBD();
        BeanBD.setTypeBD("mysql");
        BeanBD.connect();
    }
    
    @Override
    public void destroy() { }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
       
        ServletContext sc = getServletContext();
        String Jsp[] = request.getParameterValues("Jsp"); 
        ResultSet r;
        String str, q;
        
        String NewUser;
        
        String Login;
        String donnee[][];
        RequestDispatcher rd;
        if(Jsp == null)
        {
            response.sendRedirect("JSPLogin.jsp");
            return;           
        }

        switch(Jsp[0])
        {
            case "JSPLogin":
                NewUser = request.getParameter("NewUser");
                if(NewUser != null)
                if(NewUser.equals("ON"))
                {
                    BeanBD.addUser(request.getParameterValues("user")[0], request.getParameterValues("password")[0]);
                }
                String usr[] = request.getParameterValues("user");
                String pwd[] = request.getParameterValues("password");

                if(pwd[0].equals(BeanBD.findPassword(usr[0])))
                {
                    System.out.println("OK");
                    int j=0;
                    session.setAttribute("logon.isDone", "OK");
                    Login = usr[0];
                    q = "select idVols,Destination,NombreDePlaces,HeureArrivee,HeureDepart,Paye from volsreserves natural join (vols) where utilisateur ='" + Login + "';" ;
                    donnee=BeanBD.selectVols(q); 
                    rd = sc.getRequestDispatcher("/JSPInit.jsp");
                    sc.log("-- Tentative de redirection sur JSPInit.jsp");
                    request.setAttribute("donnee", donnee);
                    request.setAttribute("Login", Login);
                    request.setAttribute("line", BeanBD.getLine());
                    request.setAttribute("col", BeanBD.getColonne());
                    rd.forward(request, response);
                }
                else
                {
                    rd = sc.getRequestDispatcher("/JSPLogin.jsp?msg=" +
                    URLEncoder.encode("La combinaison de votre identifiant/mot de passe est incorrecte !"));
                    sc.log("-- Tentative de redirection sur JSPInit.jsp");
                    session = request.getSession(true);
                    session.invalidate();
                    Object obj = sc.getAttribute("membre");
                    if (obj != null) sc.removeAttribute("membre");
                    rd.forward(request, response);
                }
                break;
                case "JSPInit":
                    
                    str = request.getParameter("pushedbutton");
                    q = "select * from vols;";
                    donnee=BeanBD.selectVols(q);                   
                    rd = sc.getRequestDispatcher("/JSPCaddie.jsp");
                    sc.log("-- Tentative de redirection sur JSPCaddie.jsp");
                    request.setAttribute("donnee", donnee);
                    request.setAttribute("line", BeanBD.getLine());
                    request.setAttribute("col", BeanBD.getColonne());
                    rd.forward(request, response);
                    break;
                case "JSPCaddie":
 
                    BeanBD.connect();
                    str = request.getParameter("pushedbutton");
                    q = "select * from vols where idVols = '" + str + "';" ;
                    donnee=BeanBD.selectVols(q);
                    rd = sc.getRequestDispatcher("/JSPReserve.jsp");
                    sc.log("-- Tentative de redirection sur JSPReserve.jsp");
                    request.setAttribute("donnee", donnee);
                    request.setAttribute("line", BeanBD.getLine());
                    request.setAttribute("col", BeanBD.getColonne());
                    rd.forward(request, response);

                    break;
                    
                case "JSPReserve":
                    System.out.println("Reserve Billet : " + request.getParameter("cbNbre"));
                    str = request.getParameter("idVols");
                    System.out.println("Reserve Billet : " + str);
                    q = "select PlacesRestantes from vols where idVols = '" + str + "';" ;
                    int nbreDemande = Integer.parseInt(request.getParameter("cbNbre"));
                    int NbreMax = BeanBD.selectInt(q);
                    int tmp = NbreMax - nbreDemande;
                    if(tmp>0 && nbreDemande != 0)
                    {
                        Login = request.getParameter("Login");
                        System.out.println("Places restantes : " + tmp);
                        String update="UPDATE vols SET `PlacesRestantes`='" + tmp + "' WHERE `idVols`='"+ str + "';";
                        String Insert = "INSERT INTO volsreserves  (`idVolsReserves`, `Utilisateur`, `idVols`, `NombreDePlaces`,`Paye`) VALUES ('"+ Login + str + tmp + "', '" + Login + "', '"+ str + "', '" + nbreDemande + "', '0' );";
                        if(!BeanBD.reserveVols(update,Insert))
                        {
                            update="UPDATE vols SET `PlacesRestantes`='" + NbreMax + "' WHERE `idVols`='"+ str + "';";
                            BeanBD.Update(update);
                        }
                    }
                    Login = request.getParameter("Login");
                    q = "select idVols,Destination,NombreDePlaces,HeureArrivee,HeureDepart,Paye from volsreserves natural join (vols) where utilisateur ='" + Login + "';" ;
                    donnee=BeanBD.selectVols(q);
                    rd = sc.getRequestDispatcher("/JSPInit.jsp");
                    sc.log("-- Tentative de redirection sur JSPInit.jsp");
                    request.setAttribute("donnee", donnee);
                    request.setAttribute("line", BeanBD.getLine());
                    request.setAttribute("col", BeanBD.getColonne());
                    rd.forward(request, response);
                    break;
                case "PAYE":
                    
                    
                    Login = request.getParameter("Login");
                    System.out.println("Paye:" + Login);
                    q = "select idVolsReserves,Destination,NombreDePlaces,HeureArrivee,HeureDepart from volsreserves natural join (vols) where utilisateur ='" + Login + "' and paye ='false';" ;
                    donnee=BeanBD.selectVols(q); 
                    rd = sc.getRequestDispatcher("/JSPPay.jsp");
                    sc.log("-- Tentative de redirection sur JSPPay.jsp");
                    request.setAttribute("donnee", donnee);
                    request.setAttribute("Login", Login);
                    request.setAttribute("line", BeanBD.getLine());
                    request.setAttribute("col", BeanBD.getColonne());
                    rd.forward(request, response);
                    break;
                               
                case "JSPPay":
                    
                       
                    String Log = request.getParameter("Login");
                    str = request.getParameter("pushedbutton");
                    System.out.println(str);
                    String temp [] = str.split(";");
                    if(temp[0].equals("CANCEL"))
                    {
                        String requete ="select PlacesRestantes from vols where idVols =(select idVols from volsreserves where idVOlsReserves = '" + temp[1] + "');";
                        tmp = BeanBD.selectInt(requete);
                        requete ="select NombreDePlaces from volsreserves where idVOlsReserves = '" + temp[1] + "';";
                        int tmp2 = BeanBD.selectInt(requete);
                        System.out.println("Nombre de places du vols : " + tmp + "Nombre annulé :" + tmp2);
                        int var= tmp+ tmp2;
                        String update="update vols set PlacesRestantes = '"+ var + "' where idVols =(select idVols from volsreserves where idVOlsReserves = '" + temp[1] + "');";
                        System.out.println(update);
                        BeanBD.Update(update);
                        update="delete from volsreserves where idVolsReserves ='"+ temp[1] + "';";
                        System.out.println(update);
                        BeanBD.payeVols(update);

                    }
                    else if(temp[0].equals("CONFIRM"))
                    {
                        String update="UPDATE volsreserves SET `Paye`='1' WHERE `idVolsReserves`='"+ temp[1] + "';";
                        System.out.println(update);
                        BeanBD.payeVols(update);
                    }

                    q = "select idVols,Destination,NombreDePlaces,HeureArrivee,HeureDepart,Paye from volsreserves natural join (vols) where utilisateur ='" + Log + "';" ;
                    donnee=BeanBD.selectVols(q);
                    rd = sc.getRequestDispatcher("/JSPInit.jsp");
                    sc.log("-- Tentative de redirection sur JSPInit.jsp");
                    request.setAttribute("donnee", donnee);
                    request.setAttribute("Login", Log);
                    request.setAttribute("line", BeanBD.getLine());
                    request.setAttribute("col", BeanBD.getColonne());
                    rd.forward(request, response);
                    break;
                    case "JSPPayAll":
                    
                       
                    Login = request.getParameter("Login");
                    str = request.getParameter("pushedbutton");
                    
                    String var="UPDATE volsreserves SET `Paye`='1' where Utilisateur = '" + Login + "' and Paye != '1'";
                    System.out.println(var);
                    BeanBD.payeVols(var);
                    
                    q = "select idVols,Destination,NombreDePlaces,HeureArrivee,HeureDepart,Paye  from volsreserves natural join (vols) where utilisateur ='" + Login + "';" ;
                    donnee=BeanBD.selectVols(q);
                    rd = sc.getRequestDispatcher("/JSPInit.jsp");
                    sc.log("-- Tentative de redirection sur JSPInit.jsp");
                    request.setAttribute("donnee", donnee);
                    request.setAttribute("Login", Login);
                    request.setAttribute("line", BeanBD.getLine());
                    request.setAttribute("col", BeanBD.getColonne());
                    rd.forward(request, response);
                    break;
                    default :
                    Object existe = session.getAttribute("logon.isDone");
                    if (existe==null)
                    {
                        response.sendRedirect("JSPLogin.jsp");
                        return;
                    }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet by Vincent and Thibault";
    }// </editor-fold>

}
