package ServerPayment;

import ServerPayment.*;
import PAYP.RequetePAYP;
import Utilities.Encryption;
import database.utilities.BeanBD;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Vince
 */
public class ThreadClientPay extends Thread {


    private SourceTaches tachesAExecuter;
    private String nom;
    private Runnable tacheEnCours;
    private Socket mySock;
    private BeanBD Bc;
    
    private X509Certificate certif;
    private PrivateKey cléPrivée; 
    private PublicKey cléPublique;
    private SecretKey keyHmac;
    private SecretKey keyCipher;
    
    private X509Certificate certifOperator;
    private PublicKey cléPubliqueOperator;
    public ThreadClientPay(SourceTaches st, String n, Socket s )
    {
        tachesAExecuter = st;
        nom = n;
        mySock = s;
        Bc = new BeanBD();
        Bc.setTypeBD("MySql");
        Bc.connect();
       
    }
    
    public void run()
    {        
        KeyStore ks;
        try {
            Security.addProvider(new BouncyCastleProvider());
            InputStream input = null;
            ks = KeyStore.getInstance("JCEKS");
            input = this.getClass().getResourceAsStream("/Cles/ClesLabo.jceks");
            ks.load(input,"123".toCharArray());
            certif = (X509Certificate)ks.getCertificate("serveur_payment");
            cléPublique = certif.getPublicKey();            
            setCléPrivée((PrivateKey) ks.getKey("serveur_payment", "123".toCharArray()));
            
            certifOperator = (X509Certificate)ks.getCertificate("tour_operator");
            cléPubliqueOperator = certifOperator.getPublicKey();


        } catch (KeyStoreException ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        }

       while (!isInterrupted())
        {
            if(mySock!=null)
           {
                
                ObjectInputStream ois=null;
                RequetePAYP req = null;
                try
                {
                    ois = new ObjectInputStream(mySock.getInputStream());
                    req = (RequetePAYP)ois.readObject();
                    req.setBc(Bc);
                }
                catch (ClassNotFoundException e)
                {
                    System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
                }
                catch (IOException e)
                {
                    System.err.println("Erreur ? [" + e.getMessage() + "]");
                }
                
                Runnable travail = req.createRunnable(mySock,cléPublique,cléPrivée);
                
                if (travail != null)
                {
                    tachesAExecuter.recordTache(travail);
                }
                else System.out.println("Pas de mise en file");

                try
                {
                    tacheEnCours = tachesAExecuter.getTache();
                }
                catch (InterruptedException e)
                {
                    System.out.println("Interruption : " + e.getMessage());
                }
                tacheEnCours.run();
                }
        }
    }

    public Socket getMySock() {
        return mySock;
    }

    public void setMySock(Socket mySock) {
        this.mySock = mySock;
    }

    /**
     * @return the keyHmac
     */
    public SecretKey getKeyHmac() {
        return keyHmac;
    }

    /**
     * @param keyHmac the keyHmac to set
     */
    public void setKeyHmac(SecretKey keyHmac) {
        this.keyHmac = keyHmac;
        System.out.println("setKeyHmac : " + keyHmac.toString());
    }
    /**
     * @return the cléPrivée
     */
    public PrivateKey getCléPrivée() {
        return cléPrivée;
    }

    /**
     * @param cléPrivée the cléPrivée to set
     */
    public void setCléPrivée(PrivateKey cléPrivée) {
        this.cléPrivée = cléPrivée;
    }
    /**
     * @return the keyCipher
     */
    public SecretKey getKeyCipher() {
        return keyCipher;
    }

    /**
     * @param keyCipher the keyCipher to set
     */
    public void setKeyCipher(SecretKey keyCipher) {
        this.keyCipher = keyCipher;
        System.out.println("setKeyCipher : " + keyCipher.toString());
    }

    /**
     * @return the cléPubliqueOperator
     */
    public PublicKey getCléPubliqueOperator() {
        return cléPubliqueOperator;
    }
}
