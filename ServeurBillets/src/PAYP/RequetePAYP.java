/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PAYP;
import java.io.*;
import java.net.*;
import Server.*;
import ServerPayment.*;
import Utilities.Encryption;
import Utilities.Identify;
import clientServeurSocket.InterfaceClient;
import database.utilities.*;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

public class RequetePAYP implements ServerPayment.Requete, Serializable
{
    public static int REQUEST_PAY = 1; 
    private byte [] signature;
    private byte [] MessageCrypte;
    private BeanBD Bc;
    private BeanRequete Br;
    private int type;
    private String chargeUtile;
    private Socket socketClient;

    private PublicKey cléPublique = null;
    public RequetePAYP(int t, byte [] message)
    {
        MessageCrypte = message;
        type = t;
        signature = null;
    }
    public RequetePAYP(int t, String chu)
    {
        type = t; setChargeUtile(chu);
        signature = null;
        MessageCrypte = null;
    }
    public RequetePAYP(int t, String chu, Socket s,BeanBD B, BeanRequete R)
    {
        type = t; setChargeUtile(chu); socketClient =s;
        signature = null;
        Bc=B;
        Br=R;
    }
    
    public Runnable createRunnable (final Socket s,PublicKey cléPublique,PrivateKey cléPrivée)
    {
        if(getType() == REQUEST_PAY)
        
            return new Runnable()
            {
                public void run()
                {
                    pay(s,cléPublique,cléPrivée);
                }
            };
        else 
            return null;
    }
    /*public Runnable createRunnable (final Socket s, final ConsoleServeur cs)
    {
        if(getType() == REQUEST_CONNECT)
        
            return new Runnable()
            {
                public void run()
                {
                    traiterConnect(s, cs,cléPublique);
                }
            };
        else return null;
    }*/
    private void pay(final Socket s,PublicKey cléPublique,PrivateKey cléPrivée)
    {
        try {
            ThreadClientPay thread = (ThreadClientPay) Thread.currentThread();
            
            byte[] messageClair=Encryption.decryptRSA(thread.getCléPrivée(), MessageCrypte);
            String var =(String)Encryption.convertFromBytes(messageClair);
            System.out.println("Message decrypté RSA : " + var);
            
            
            Signature sign = Signature.getInstance ("SHA1withRSA", "BC");
            sign.initVerify(cléPublique);
            System.out.println("Hachage du message");
            sign.update(messageClair);
            System.out.println("Verification de la signature construite");
            boolean ok = sign.verify(signature);
            if(ok)
            {
                //envoit num cb
                System.out.println("OK");
                String []str = var.split("@");
                
                var = str[3] +"@OK";
                System.out.println(var);
            }
            else
            {
                System.out.println("fk");
                var = var +"@@FAIL";
            }
                
            
        } catch (IOException ex) {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String getChargeUtile() { return chargeUtile; }
    
    public void setChargeUtile(String chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }

    public int getType() {
        return type;
    }


    /**
     * @return the Bc
     */
    public BeanBD getBc() {
        return Bc;
    }

    /**
     * @param Bc the Bc to set
     */
    public void setBc(BeanBD Bc) {
        this.Bc = Bc;
    }
    private byte[] convertToBytes(Object object) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutput out = new ObjectOutputStream(bos)) {
        out.writeObject(object);
        return bos.toByteArray();
    } 
    }


    @Override
    public Runnable createRunnable(Socket s, PublicKey cléPublique) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the Signature
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * @param Signature the Signature to set
     */
    public void setSignature(byte[] Signature) {
        this.signature = Signature;
    }

    
}
