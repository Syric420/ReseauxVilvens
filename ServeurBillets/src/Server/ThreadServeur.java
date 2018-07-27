package Server;

import TICKMAP.RequeteTICKMAP;
import database.utilities.BeanBD;
import java.net.*;
import java.io.*;
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
public class ThreadServeur extends Thread {
    private int port;
    private ConsoleServeur guiApplication;
    private SourceTaches tachesAExecuter;
    private ServerSocket SSocket = null;
    private int nbThreads;
    private ThreadClient thr;
    private BeanBD Bc;
    private X509Certificate certif;
    private PrivateKey cléPrivée; 
    private PublicKey cléPublique;
    private SecretKey keyHmac;
    private SecretKey keyCipher;
    
    public ThreadServeur(int p, ConsoleServeur fs, int nb)
    {
        port = p;  guiApplication = fs; nbThreads = nb; tachesAExecuter=new ListeTaches();
        
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
        try
        {
            SSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]"); System.exit(1);
        }
        Bc = new BeanBD();
        Bc.setTypeBD("MySql");
        //Bc.connect();
         System.out.println("ThreadServer : nbThreadsClients = "+nbThreads);
        // Démarrage du pool de threads
        for (int i=0; i<nbThreads; i++)
        {
            thr = new ThreadClient (tachesAExecuter,"Thread du pool n°" +String.valueOf(i), null, guiApplication, Bc);
            thr.start();
        }
        
        
        // Mise en attente du serveur
        Socket CSocket;
        while (!isInterrupted())
        {
            try
            {
                CSocket = null;
                System.out.println("************ Serveur en attente");
                CSocket = getSSocket().accept();
                guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#accept");
                //Le client est accepte on enregistre sa tache
                ObjectInputStream ois=null;
                RequeteTICKMAP req = null;
                try
                {
                    ois = new ObjectInputStream(CSocket.getInputStream());
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
                
                Runnable travail = req.createRunnable(CSocket, guiApplication,cléPublique);
                if (travail != null)
                {
                    tachesAExecuter.recordTache(travail);
                }
                else System.out.println("Pas de mise en file");

                
            }
            catch (IOException e)
            {
                System.out.println("Erreur d'accept ! ? [" + e.getMessage() + "]");
            }
            
        }
    }
    

    public ServerSocket getSSocket() {
        return SSocket;
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
}
