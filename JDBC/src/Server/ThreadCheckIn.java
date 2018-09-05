/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import database.utilities.BeanBD;
import java.io.*;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vince
 */
public class ThreadCheckIn extends Thread {
    private ServerSocket SocketCheckIn;
    private int PORT_CHECKINC;
    private BeanBD Bc;

    public ThreadCheckIn(int port) {
        PORT_CHECKINC = port;
    }
    
    
    
    @Override
    public void run() {
        Bc = new BeanBD();
        Bc.setTypeBD("MySql");
        Bc.connect();
        String rep;
        boolean fini = false;
        DataInputStream dis=null; DataOutputStream dos=null;
        StringBuffer strClient = new StringBuffer();
        StringBuffer strServeur = new StringBuffer();
        byte b; String idTicket; int nbPassagers;
        String [] split;
        try 
        {
            SocketCheckIn = new ServerSocket(PORT_CHECKINC);
            Socket CSocket = null;
            while(!this.isInterrupted())
            {

                CSocket = null;
                System.out.println("************ Serveur en attente");
                CSocket = SocketCheckIn.accept();
                System.out.println("Connexion CheckIN");
                dis = new DataInputStream(CSocket.getInputStream());
                dos = new DataOutputStream(CSocket.getOutputStream());
                System.out.println("Dis et Dos créés");
                if (SocketCheckIn==null || dis==null || dos==null) 
                {
                    System.err.println("Erreur de connexion");
                    System.exit(1);
                }
                Thread.sleep(1000);
                //On reçoit la requête
                StringBuffer strBuf = new StringBuffer();
                while(dis.available()<=0);//On attend qu'il y ait quelque chose sur le flux

                
                while(dis.available()>0)
                {
                    //System.out.println("Lecture caractère");
                    b=dis.readByte();
                    strBuf.append((char) b);
                }
                System.out.println(strBuf.toString().trim());

                // On récupère les données qui nous intéressent
                split = strBuf.toString().split(";");
                idTicket = split[0]; 
                nbPassagers = Integer.parseInt(split[1]);
                
                System.out.println("idTicket = "+idTicket+"\nNbPassagers = "+ nbPassagers);

                 //On cherche dans la BD si trouvé ou non
                if(Bc.findTicket(idTicket, nbPassagers)==true)
                {
                    
                     rep = "OK";
                }  
                else
                {
                    rep = "NOK";
                }
                    System.out.println("rep = "+rep);
               dos.write(rep.getBytes());
               Thread.sleep(5000);
               CSocket.close();
                
            }
        }
        catch (IOException e)
        {
            System.out.println("Erreur d'accept ! ? [" + e.getMessage() + "]");
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadCheckIn.class.getName()).log(Level.SEVERE, null, ex);
        }     
         
    }
        
            
    public boolean chercheSep(StringBuffer st)
    {
        int i=0;
        while(i<st.length()-1)
        {
            if(st.charAt(i)=='\r')
                if(st.charAt(i+1)=='\n')
                    return true;
        }
        return false;
    }
    
}
