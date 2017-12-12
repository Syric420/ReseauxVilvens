/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IACOP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tibha
 */
public class Personne {
    
    String identifiant;
    protected int port_chat;
    protected InetAddress adresseGroupe;
    protected MulticastSocket socketGroupe;

    public Personne(String identifiant,String addresse_chat,int port) {
        try {
            System.out.println(identifiant+addresse_chat+port);
            this.identifiant = identifiant;
            adresseGroupe = InetAddress.getByName(addresse_chat);
            socketGroupe = new MulticastSocket(port);
            port_chat=port;
            socketGroupe.joinGroup(adresseGroupe);
            //socketGroupe.setInterface(InetAddress.getLocalHost());
        } catch (UnknownHostException ex) {
            Logger.getLogger(Personne.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Personne.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public MulticastSocket getSocketGroupe() {
        return socketGroupe;
    }
    
    
    public void post_Event(String event)
    {
        try {
            
            event=("3@" + event);
            DatagramPacket dtg = new DatagramPacket(event.getBytes(), event.length(),adresseGroupe, port_chat);
            socketGroupe.send(dtg);
        } catch (IOException ex) {
            Logger.getLogger(Personne.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}
