<%-- 
    Document   : JSPInit
    Created on : 08-nov.-2017, 11:04:46
    Author     : Vince
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Initialisation du Caddie</title>
    </head>
    <body>
        <%
            int timeout = session.getMaxInactiveInterval();
            response.setHeader("Refresh", timeout + "; URL = JSPLogin.jsp");
        %>
        <%
            String ipServ =request.getParameter("IP");
            System.out.println("IP INIT: " + ipServ);
        %>
        
        
        <form action=<%=ipServ%> method="POST">
        <input type="hidden" id="IP" name="IP" value=<%=ipServ%>> 
        <input type="hidden" id="Jsp" name="Jsp" value="JSPInit"/> 
        <%String Login =(String) request.getAttribute("Login");
            if(Login == null)
                Login =request.getParameter("Login");
        %>
        <input type="hidden" id="Login" name="Login" value="<%=Login %>"/>
   
        <% 
            //String data = request.getParameter("donnee");
            String data [][]= (String [][]) request.getAttribute("donnee");
            int line= (int) request.getAttribute("line");
            int col= (int) request.getAttribute("col");
        %>
        
        
<table name="table1" id ="table1" width="59%" border="1" style="border-collapse: collapse; ">
    <%
        %>
        <tr>
            <%
            for(int i = 1; i<=col;i++)
               { 
                   
   %>
                <th>
                <%= data[0][i-1]%>
                </th>
                
           <% 
               }
        %>                   
        </tr>
        <% 
        int l =1;
        while(l<line)
        {
        %>
        <tr>
            <%
            for(int i = 1; i<=col;i++)
               {      
                    if(i == col)
                    {
                        if(data[l][i-1].equals("0"))
                        {                %>      
                        <td>
                            Réservé
                        </td>
                        <%
                        }
                        else
                        {%>      
                        <td>
                            Payé
                        </td>
                        <%}
                    }
                    else
{%>      
                <td>
                <%= data[l][i-1]%>
                </td>
                <%}}
        l++;
        %>                   
        </tr>
        <% 
        }
    %>
</table>

        <input type="submit" value="Acces au catalogue des vols" name="button" />
    </form>


    <form action=<%=ipServ%> method="POST">
        <input type="hidden" id="IP" name="IP" value=<%=ipServ%>> 
        <%String Log =(String) request.getAttribute("Login");
            if(Log == null)
                Log =request.getParameter("Login");
        %>
        <input type="hidden" id="Login" name="Login" value="<%=Log %>"/>  
        <input type="hidden" id="Jsp" name="Jsp" value="PAYE"/>
        <input type="submit" value="Payer" name="button" />
    </form> 
    </body>
</html>
