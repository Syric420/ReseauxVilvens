/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IACOP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tibha
 */
public class Client extends Personne {

    public Client(String identifiant) {
        super(identifiant);
    }
    
    public void post_Question(String Question)
    {
        //check digest
         try {
             
            String chaine = "1;" + Question;
            DatagramPacket dtg = new DatagramPacket(chaine.getBytes(), chaine.length(),adresseGroupe, 5001);
            socketGroupe.send(dtg);
        } catch (IOException ex) {
            Logger.getLogger(Personne.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
