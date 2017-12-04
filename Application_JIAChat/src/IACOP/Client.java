/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IACOP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tibha
 */
public class Client extends Personne {

    public Client(String identifiant,String addresse_chat,int port) {
        super(identifiant,addresse_chat,port);
    }
    
    public void post_Question(String Question)
    {
        //check digest
         try {
             
            String chaine = "1@" + Question +"@";
            Verify ver = new Verify();
            ver.setMD(Question);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            outputStream.write( chaine.getBytes() );
            outputStream.write( ver.getMd());
            
            byte[] var =  outputStream.toByteArray();
            System.out.println("Client"+new String (var));
            DatagramPacket dtg = new DatagramPacket(var , var.length,adresseGroupe, port_chat);
            socketGroupe.send(dtg);
            
        } catch (IOException ex) {
            Logger.getLogger(Personne.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
