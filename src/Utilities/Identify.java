/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author tibha
 */
public class Identify {
   private String login;
   private String password;
   private MessageDigest md;
   private byte[] msgD;
    public Identify() {
        Security.addProvider(new BouncyCastleProvider());
        login = null;
        password = null;
    }
   
   
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getMd() {
        return msgD;
    }

    public void setMd() {
        
        try {
            md = MessageDigest.getInstance("SHA-1", "BC");
            md.update(login.getBytes());
            md.update(password.getBytes());
            msgD= md.digest();
            System.out.println(login + " " + password + " " + msgD);
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Identify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Identify.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
}
