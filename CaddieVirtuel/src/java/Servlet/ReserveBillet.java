/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import database.utilities.BeanBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tibha
 */
@WebServlet(name = "ReserveBillet", urlPatterns = {"/ReserveBillet"})
public class ReserveBillet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext sc = getServletContext();
        try {
            
            System.out.println("Reserve Billet : " + request.getParameter("cbNbre"));
            BeanBD o = new BeanBD();
            o.setTypeBD("MySql");
            int j=0;
            o.connect();
            ResultSet r;

            String str = request.getParameter("idVols");
            System.out.println("Reserve Billet : " + str);
            String q = "select PlacesRestantes from vols where idVols = '" + str + "';" ;
            r = o.getInstruc().executeQuery(q) ;
            r.next();
            int nbreDemande = Integer.parseInt(request.getParameter("cbNbre"));
            int NbreMax = Integer.parseInt(r.getString(1));
            if(nbreDemande<NbreMax)
            {
                int tmp = NbreMax - nbreDemande;
                String Login = request.getParameter("Login");
                System.out.println("Places restantes : " + tmp);
                String update="UPDATE vols SET `PlacesRestantes`='" + tmp + "' WHERE `idVols`='"+ str + "';";
                String Insert = "INSERT INTO volsreserves  (`idVolsReserves`, `Client`, `idVols`, `Nombre de places`) VALUES ('"+ Login + str + "', '" + Login + "', '"+ str + "', '" + nbreDemande + "');";
                o.reserveVols(update,Insert);
                
            }
            RequestDispatcher rd = sc.getRequestDispatcher("/JSPInit.jsp");
            sc.log("-- Tentative de redirection sur JSPInit.jsp");
            rd.forward(request, response);
        }   catch (SQLException ex) {
            Logger.getLogger(ReserveBillet.class.getName()).log(Level.SEVERE, null, ex);
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
        return "Short description";
    }// </editor-fold>

}
