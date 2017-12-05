/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gui;

import IACOP.Identify;
import IACOP.MessageLogin;
import database.utilities.BeanBD;
import database.utilities.ReadProperties;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vince
 */
public class ThreadAccept extends Thread {

    private int port_fly;
    private int port_chat;
    private String addresse_chat;
    private ServerSocket SSocket;
    private Socket CSocket;
    ServIAChat IAChat;
    BeanBD BeanBD;
    
    public ThreadAccept(int p, ServIAChat IAChat) {
        ReadProperties rP ;
        try {
            rP = new ReadProperties("/Gui/Config.properties");
            String s;
            s = new String("PORT_FLY");
            System.out.println(rP.getProp(s));
            port_fly = Integer.parseInt(rP.getProp(s));
            
            s = new String("PORT_CHAT");
            System.out.println(rP.getProp(s));
            port_chat = Integer.parseInt(rP.getProp(s));
            
            s = new String("ADRESSE_CHAT");
            System.out.println(rP.getProp(s));
            addresse_chat = rP.getProp(s);
            
        } catch (IOException ex) {
            Logger.getLogger(ThreadAccept.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.IAChat = IAChat;
        BeanBD = new BeanBD();
        BeanBD.setTypeBD("mysql");
        BeanBD.connect();
    }

    @Override
    public void run() {
        log("Démarrage de le connexion");
        SSocket = null; CSocket = null;
        try
        {
            SSocket = new ServerSocket(port_fly);
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
                boolean logged=false;
                DataInputStream dis = new DataInputStream(CSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(CSocket.getOutputStream());
                ObjectOutputStream oos = new ObjectOutputStream(dos);
                ObjectInputStream ois = new ObjectInputStream(dis);
                while(!logged)
                {
                    MessageLogin message = (MessageLogin)ois.readObject();
                    
                    if(message.getTypeMessage()==1)
                    {
                        
                        if(BeanBD.findTicket(message.getUser()))
                        {
                            logged=true;
                            message.setUser("TICKET OK");
                            message.setAddresse_chat(addresse_chat);
                            message.setPort_chat(port_chat);
                        }
                        else
                        {
                            logged=false;
                            message.setUser("TICKET NOT OK");
                        }
                    }
                    else //if(message.getMdp().equals(BeanBD.findPassword(message.getUser())))
                    {
                        String password=BeanBD.findPassword(message.getUser());
                        Identify log = new Identify();
                        log.setMd(message.getUser(), password, message.getMsgD().getTemps(), message.getMsgD().getAlea());
                        if(MessageDigest.isEqual(log.getMd(), message.getMsgD().getMd()))
                        {
                            System.out.println(message.getUser());
                            message.setUser("PASSWORD OK");
                            message.setPort_chat(port_chat);
                            message.setAddresse_chat(addresse_chat);
                            logged=true;                       
                        }
                        else
                        {
                            message.setUser("PASSWORD NOT OK");
                            logged=false;
                        }
                    }
                    /**/
                    oos.writeObject(message);
                }
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
