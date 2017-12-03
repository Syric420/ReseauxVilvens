/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IACOP;

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
public class Verify {
private MessageDigest md;
private byte[] msgD;
    public Verify() {
         Security.addProvider(new BouncyCastleProvider());
    }
    public byte[] getMd() {
        return msgD;
    }
    public void setMD(String str) { 
        try {
            
            md = MessageDigest.getInstance("SHA-1", "BC");
            md.update(str.getBytes());
            msgD= md.digest();          
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
        Logger.getLogger(Verify.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
    public boolean checkDigest(byte[] b)
    {
        return MessageDigest.isEqual(msgD, b);
    }

    
}
