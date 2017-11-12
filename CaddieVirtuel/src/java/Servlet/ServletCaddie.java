/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import database.utilities.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
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
 * @author Vince
 */
@WebServlet(name = "ServletCaddie", urlPatterns = {"/ServletCaddie"})
public class ServletCaddie extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
            BeanBD o = new BeanBD();
           o.setTypeBD("MySql");
           int j=0;
           o.connect();
           ResultSet r;
           String str = request.getParameter("pushedbutton");
           System.out.println("STR : " + str);
           String q = "select * from vols where idVols = '" + str + "';" ;
           
           
        try {
            
            r = o.getInstruc().executeQuery(q) ;
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
                   System.out.println(donnee[line][i-1]);
                   
               }
                line++;
            
            }
                RequestDispatcher rd = sc.getRequestDispatcher("/JSPPay.jsp");
                sc.log("-- Tentative de redirection sur JSPPay.jsp");
                request.setAttribute("donnee", donnee);
                request.setAttribute("line", line);
                request.setAttribute("col", col);
                rd.forward(request, response);
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ServletCaddie.class.getName()).log(Level.SEVERE, null, ex);
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
