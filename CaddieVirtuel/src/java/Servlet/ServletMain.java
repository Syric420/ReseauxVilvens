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

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
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
        ServletContext sc = getServletContext();
        String action = request.getParameter("button");
        String NewUser;
        
       if (action.equals("S'identifier"))
       {
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
                ResultSet r;
                String Login = usr[0];
                String q = "select idVols,Destination,NombreDePlaces,HeureArrivee,HeureDepart from volsreserves natural join (vols) where utilisateur ='" + Login + "';" ;

                try {

                    r = BeanBD.getInstruc().executeQuery(q) ;
                    ResultSetMetaData metaData = r.getMetaData();

                    int col=metaData.getColumnCount();
                    int  line =0;
                    while(r.next())
                    {
                        line ++;

                    }
                    r.beforeFirst();
                    String donnee[][]= new String[line+1][col];

                    for(int i = 1; i<=metaData.getColumnCount();i++)
                    {
                        donnee[0][i-1]= metaData.getColumnName(i);

                    }

                    line =1;

                    while(r.next())
                    {
                        for(int i = 1; i<=metaData.getColumnCount();i++)
                       {
                           donnee[line][i-1]=r.getString(i);

                       }
                        line++;

                    }
                    
                    
                        HttpSession session = request.getSession(true);
                        session.putValue("logon.isDone", usr[0]);
                        //session.setMaxInactiveInterval(30);
                        
                        System.out.println("Session créee : " + session.getId());
                        RequestDispatcher rd = sc.getRequestDispatcher("/JSPInit.jsp");
                        sc.log("-- Tentative de redirection sur JSPInit.jsp");
                        request.setAttribute("donnee", donnee);
                        request.setAttribute("Login", Login);
                        request.setAttribute("line", line);
                        request.setAttribute("col", col);
                        rd.forward(request, response);
                        
                    } catch (SQLException ex) {
                    Logger.getLogger(TrueFormServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            else
            {
                RequestDispatcher rd = sc.getRequestDispatcher("/index.jsp?msg=" +
                URLEncoder.encode("La combinaison de votre identifiant/mot de passe est incorrecte !"));
                sc.log("-- Tentative de redirection sur JSPInit.jsp");
                rd.forward(request, response);
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
