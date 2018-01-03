package Utilities;

import TICKMAP.RequeteTICKMAP;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Date;
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
   long temps;
   double alea;
   RequeteTICKMAP req;
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
            temps= (new Date()).getTime();
            alea = Math.random();
            //System.out.println("SetMD " + login +";" + password + ";" + temps + ";" + alea);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream bdos = new DataOutputStream(baos);
            bdos.writeLong(temps);
            bdos.writeDouble(alea);

            md.update(baos.toByteArray());
            msgD= md.digest();
            //System.out.println(login + " " + password + " " + msgD);
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Identify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Identify.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        catch (IOException ex) {
                Logger.getLogger(Identify.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        
    public void setMd(String U,String Pass, long T, double A) { 
        try {
            
            md = MessageDigest.getInstance("SHA-1", "BC");
            //System.out.println("SetMD " + U +";" + Pass + ";" + T + ";" + A);
            md.update(U.getBytes());
            md.update(Pass.getBytes());
            temps= T;
            alea = A;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream bdos = new DataOutputStream(baos);
            bdos.writeLong(temps);
            bdos.writeDouble(alea);

            md.update(baos.toByteArray());
            msgD= md.digest();
            //System.out.println(U + " " + Pass + " " + msgD);
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Identify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Identify.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        catch (IOException ex) {
                Logger.getLogger(Identify.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    public RequeteTICKMAP sendLogin()
    {

           //System.out.println("Envoi du message digest");
           String connect = (login + ";" + temps + ";" + alea);
           req= new RequeteTICKMAP(RequeteTICKMAP.REQUEST_CONNECT,connect);
           //System.out.println(req.getType());
           req.setByteArray(getMd());
           return req;
    }
    public void answerLogin(DataInputStream dis)
    {
        
    }
}
