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
    protected InetAddress adresseGroupe;
    protected MulticastSocket socketGroupe;

    public Personne(String identifiant) {
        try {
            
            this.identifiant = identifiant;
            adresseGroupe = InetAddress.getByName("234.5.5.9");
            socketGroupe = new MulticastSocket(5001);
            socketGroupe.joinGroup(adresseGroupe);
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
            
            event=("3;" + event);
            DatagramPacket dtg = new DatagramPacket(event.getBytes(), event.length(),adresseGroupe, 5001);
            socketGroupe.send(dtg);
        } catch (IOException ex) {
            Logger.getLogger(Personne.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}
