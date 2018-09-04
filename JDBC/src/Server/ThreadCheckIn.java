/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

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

    public ThreadCheckIn(int port) {
        PORT_CHECKINC = port;
    }
    
    
    
    @Override
    public void run() {
        boolean fini = false;
        DataInputStream dis=null; DataOutputStream dos=null;
        StringBuffer strClient = new StringBuffer();
        StringBuffer strServeur = new StringBuffer();
        byte b;
            try 
            {
            SocketCheckIn = new ServerSocket(PORT_CHECKINC);

            Socket CSocket = null;

            System.out.println("************ Serveur en attente");
            CSocket = SocketCheckIn.accept();
            System.out.println("Connexion CheckIN");
            dis = new DataInputStream(CSocket.getInputStream());
            dos = new DataOutputStream(CSocket.getOutputStream());

            if (SocketCheckIn==null || dis==null || dos==null) 
            {
                System.err.println("Erreur de connexion");
                System.exit(1);
            }
            
            //On reçoit la requête
            StringBuffer strBuf = new StringBuffer();
            int i=0;
            while(i<500)
            {
                b=dis.readByte();
                strBuf.append((char) b);
                i++;
            }
            System.out.println(strBuf.toString().trim());
            
            CSocket.close();
            //On envoie la réponse si trouvé ou non
            
            }
            catch (IOException e)
            {
                System.out.println("Erreur d'accept ! ? [" + e.getMessage() + "]");
            }
            
            
    }

            
           /* if (SocketCheckIn==null || dis==null || dos==null) 
            {
                System.err.println("Erreur de connexion");
                System.exit(1);
            }
            
               try {
                    /*String test = "3;aaa\n;123\n";
                    System.out.println("Str serv = "+test);
                    dos.write(test.getBytes());
                    dos.flush();
                    
                    StringBuffer strBuf = new StringBuffer();
                    int i=0;
                    while()
                    {
                        b=dis.readByte();
                        strBuf.append((char) b);
                        i++;
                    }
                    
                    
                    
                        
                    
                    System.out.println(strBuf.toString().trim());
                    while(true);*/
                   
        
            
    public String chercheSep(StringBuffer st)
    {
        String ret="NULL";
        int i=0;
        while(i<st.length())
        {
            if(st.charAt(i)=='\r')
                if(st.charAt(i+1)=='\n')
                    ret = st.substring(0, i+1);
        }
        
        System.out.println("str = "+ret);
        return ret;
    }
    
}
