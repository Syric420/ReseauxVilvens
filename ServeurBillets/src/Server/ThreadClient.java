package Server;

import TICKMAP.RequeteTICKMAP;
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
public class ThreadClient extends Thread {


    private SourceTaches tachesAExecuter;
    private String nom;
    private Runnable tacheEnCours;
    private Socket mySock;
    private ConsoleServeur guiApplication;
    private BeanBD Bc;
    
    private X509Certificate certif;
    private PrivateKey cléPrivée; 
    private PublicKey cléPublique;
    private SecretKey keyHmac;
    private SecretKey keyCipher;
    public ThreadClient(SourceTaches st, String n, Socket s, ConsoleServeur fs )
    {
        tachesAExecuter = st;
        nom = n;
        mySock = s;
        guiApplication = fs;
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
            certif = (X509Certificate)ks.getCertificate("serveur");
            cléPublique = certif.getPublicKey();            
            setCléPrivée((PrivateKey) ks.getKey("serveur", "123".toCharArray()));
            /*System.out.println("*** Cle publique recuperee = "+cléPublique.toString());
            System.out.println(" *** Cle privee recuperee = " + cléPrivée.toString());*/

        } catch (KeyStoreException ex) {
            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
        }

       while (!isInterrupted())
        {
            if(mySock!=null)
           {
                
                ObjectInputStream ois=null;
                RequeteTICKMAP req = null;
                try
                {
                    ois = new ObjectInputStream(mySock.getInputStream());
                    req = (RequeteTICKMAP)ois.readObject();
                    req.setBc(Bc);
                    guiApplication.TraceEvenements("Serveur#Requête reçue");
                }
                catch (ClassNotFoundException e)
                {
                    System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
                }
                catch (IOException e)
                {
                    System.err.println("Erreur ? [" + e.getMessage() + "]");
                }
                
                Runnable travail = req.createRunnable(mySock, guiApplication,cléPublique);
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
}
