/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gui;

import IACOP.MessageLogin;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Vince
 */
public class ThreadAccept extends Thread {

    private int port;
    private ServerSocket SSocket;
    private Socket CSocket;
    ServIAChat IAChat;
    
    public ThreadAccept(int p, ServIAChat IAChat) {
        port = p;
        this.IAChat = IAChat;
    }

    @Override
    public void run() {
        log("Démarrage de le connexion");
        SSocket = null; CSocket = null;
        try
        {
            SSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]");
            System.exit(1);
        }
        while(!isInterrupted())
        {
            log("Serveur en attente d'une connexion");
            try
            {
                CSocket = SSocket.accept();
                log("Nouveau client détecté : " +CSocket.getLocalSocketAddress());
                DataInputStream dis = new DataInputStream(CSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(CSocket.getOutputStream());
                ObjectOutputStream oos = new ObjectOutputStream(dos);
                ObjectInputStream ois = new ObjectInputStream(dis);
                
                MessageLogin message = (MessageLogin)ois.readObject();
                System.out.println(message.getUser());
                message.setUser("Ok");
                oos.writeObject(message);
            }
            catch (SocketException e)
            {
                System.err.println("Accept interrompu ! ? [" + e + "]");
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept ! ? [" + e + "]");
                System.exit(1);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ThreadAccept.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void log(String t)
    {
        IAChat.dlm.addElement(t);
    }
    
    public void finConnexion()
    {
        try {
            SSocket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ThreadAccept.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
