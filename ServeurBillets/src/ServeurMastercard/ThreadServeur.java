package ServeurMastercard;

import database.utilities.BeanBD;
import java.net.*;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.*;
/**
 *
 * @author Vince
 */
public class ThreadServeur extends Thread {
    private int port;
    private ConsoleServeur guiApplication;
    private ServerSocket ServerSocket = null;
    private Socket Socket = null;
    private int nbThreads;
    private ThreadClient []thr;
    private BeanBD Bc;
    private SourceTaches tachesAExecuter;
    
    public ThreadServeur(int p, ConsoleServeur fs, int nb)
    {
        port = p;  guiApplication = fs; nbThreads = nb;
        tachesAExecuter = new ListeTaches();
    }
    public void run()
    {
        try
        {
            // 1. Keystore
            KeyStore ServerKs = KeyStore.getInstance("JKS");
            String FICHIER_KEYSTORE = "c:\\makecert\\serveur_keystore";
            char[] PASSWD_KEYSTORE = "azerty".toCharArray();
            FileInputStream ServerFK = new FileInputStream (FICHIER_KEYSTORE);
            ServerKs.load(ServerFK, PASSWD_KEYSTORE);
            // 2. Contexte
            SSLContext SslC = SSLContext.getInstance("SSLv3");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            char[] PASSWD_KEY = "azerty".toCharArray();
            kmf.init(ServerKs, PASSWD_KEY);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ServerKs);
            SslC.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            // 3. Factory
            SSLServerSocketFactory SslSFac= SslC.getServerSocketFactory();
            // 4. Socket
            ServerSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]"); System.exit(1);
        } catch (KeyStoreException ex) {
            Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
        }
        Bc = new BeanBD();
        Bc.setTypeBD("MySql");
        Bc.connect();
         thr = new ThreadClient[nbThreads];
        // Démarrage du pool de threads
        for (int i=0; i<nbThreads; i++)
        {
            thr[i] = new ThreadClient (tachesAExecuter, "Thread du pool n°" +String.valueOf(i), null, guiApplication, Bc);
            thr[i].start();
        }

        // Mise en attente du serveur
        Socket CSocket = null;
        while (!isInterrupted())
        {
            try
            {
                System.out.println("************ Serveur en attente");
               // CSocket = getSSocket().accept();
                //setSslSocket((SSLSocket) getSslSSocket().accept());
                Socket = ServerSocket.accept();
                guiApplication.TraceEvenements(Socket.getRemoteSocketAddress().toString()+"#accept");
                //On assigne la socket à un thread Client
                int i = ChercheThreadDispo();
                System.out.println("Thread dispo = Thread n"+i);
                thr[i].setMySock(Socket);
            }
            catch (IOException e)
            {
                System.out.println("Erreur d'accept ! ? [" + e.getMessage() + "]");
            }
            
        }
    }
    
     public int ChercheThreadDispo()
    {
        int i;
        for(i=0; i<thr.length;i++)
        {
            if(thr[i].getMySock() == null)
            {
                return i;
            }    
        } 
        return -1;
    }


}
