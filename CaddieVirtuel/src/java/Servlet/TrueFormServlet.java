/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import database.utilities.*;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vince
 */
public class TrueFormServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext sc = getServletContext();
        String action = request.getParameter("button");
        //String test = request.getParameter("table1");
        String NewUser = request.getParameter("NewUser");
        
        BeanBD db = new BeanBD();
        db.setTypeBD("MySql");
        db.connect();
       /*if(NewUser != null)
       if(NewUser.equals("ON"))
       {
           System.out.println("Ajout d'un nouvel utilisateur dans bd");
           db.addUser(request.getParameterValues("user")[0], request.getParameterValues("password")[0]);
       }
       if (action.equals("S'identifier"))
       {
           System.out.println(request.getParameterValues("user"));
           String usr[] = request.getParameterValues("user");
           String pwd[] = request.getParameterValues("password");
           System.out.println(usr[0]);
           System.out.println(pwd[0]);
            

           if(pwd[0].equals(db.findPassword(usr[0])))
           {
                RequestDispatcher rd = sc.getRequestDispatcher("/JSPInit.jsp");
                sc.log("-- Tentative de redirection sur JSPInit.jsp");
                rd.forward(request, response);
           }
           else
           {
                RequestDispatcher rd = sc.getRequestDispatcher("/index.jsp?msg=" +
URLEncoder.encode("La combinaison de votre identifiant/mot de passe est incorrecte !"));
                sc.log("-- Tentative de redirection sur JSPInit.jsp");
                rd.forward(request, response);
           }

       }
       else*/
           System.out.println("TEST : "+request.getParameter("table1"));
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
        return "Short description";
    }// </editor-fold>

}
