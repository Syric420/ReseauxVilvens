<%-- 
    Document   : JSPReserve
    Created on : 12 nov. 2017, 10:04:23
    Author     : tibha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            int timeout = session.getMaxInactiveInterval();
            response.setHeader("Refresh", timeout + "; URL = JSPLogin.jsp");
        %>
        <h1>Reservation!</h1>
                <%
            String ipServ =request.getParameter("IP");
            System.out.println("IP INIT: " + ipServ);
        %>
        
        <form action=<%=ipServ%> method="POST">
            <input type="hidden" id="IP" name="IP" value=<%=ipServ%>> 
            <input type="hidden" id="Jsp" name="Jsp" value="JSPReserve"/> 
        <%String Login = request.getParameter("Login");%>
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
                    if(i==col)
                    {
                    %>
                        <th>
                            Réservations de places
                        </th>
                    <%
                    }
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
                   
                   if(i==1)
                   {
                     %><input type="hidden" id="idVols" name="idVols" value="<%= data[l][i-1]%>"/>
                      <% 
                   }

                   

            %>
                <td>
                <%= data[l][i-1]%>
                </td>
                <%if(i==col)
                {
                    
                    int var = Integer.parseInt(data[l][i-1]);
%>

                 <td>
                    <select name="cbNbre" id="cbNbre" onChange="combo(this, 'theinput')">
                        <%
                        for(int j = 0; j <= var;j++)
                        {
                            %>
                            <option><%=j%></option>
                            <%
                        }
                            
                        %>
                    </select>  
                <%}%>
                </td>
                
           <% 

               }
        l ++;
        %>                   
        </tr>
        <% 
        }
    %>
</table>
    
        
        <input type="submit" value="Reserver" name="Reserve" />

    </form>
    </body>
</html>
