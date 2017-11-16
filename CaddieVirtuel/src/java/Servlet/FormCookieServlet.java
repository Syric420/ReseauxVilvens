/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tibha
 */
public class FormCookieServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            // recherche des cookies éventuels
        String idDansCookie = null;
        Cookie[] tabCookies = request.getCookies();
        if (tabCookies != null)
            for (int i=0; i<tabCookies.length; i++)
            {
                if ("idSession".equals(tabCookies[i].getName()))
                    idDansCookie = tabCookies[i].getValue();
            }

        String idNewDansCookie=null;
        boolean idExiste;
        if (idDansCookie == null)
        {
            idNewDansCookie = (new Integer((int)(Math.random()*1000))).toString() + "\n";
            Cookie cookieId = new Cookie ("idSession", URLEncoder.encode(idNewDansCookie, "UTF-8"));
            response.addCookie(cookieId);
            
            idExiste=false;
        }
        else idExiste=true;
            response.setContentType("text/text");
        PrintWriter sortie = response.getWriter();
        if (!idExiste)sortie.println("Servlet en action !!! Vous recevez l'identifiant " +
            idNewDansCookie);
        else sortie.println("Servlet en action !!! Vous avez été reconnu pour " +idDansCookie);
    }

        @Override
        public String getServletInfo() {
            return "Short description";
        }// </editor-fold>

}
