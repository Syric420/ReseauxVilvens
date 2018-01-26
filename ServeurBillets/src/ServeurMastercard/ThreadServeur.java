package ServeurMastercard;

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
    private SSLServerSocket SslSSocket = null;
    private SSLSocket SslSocket = null;
    private int nbThreads;
    private ThreadClient []thr;
    
    public ThreadServeur(int p, ConsoleServeur fs, int nb)
    {
        port = p;  guiApplication = fs; nbThreads = nb;
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
            SslSSocket = (SSLServerSocket) SslSFac.createServerSocket(port);
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
         thr = new ThreadClient[nbThreads];
        // Démarrage du pool de threads
        for (int i=0; i<nbThreads; i++)
        {
            thr[i] = new ThreadClient (new ListeTaches(), "Thread du pool n°" +String.valueOf(i), null, guiApplication);
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
                setSslSocket((SSLSocket) getSslSSocket().accept());
                guiApplication.TraceEvenements(SslSocket.getRemoteSocketAddress().toString()+"#accept");
                //On assigne la socket à un thread Client
                int i = ChercheThreadDispo();
                System.out.println("Thread dispo = Thread n"+i);
                thr[i].setMySock(SslSocket);
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

    /**
     * @return the SslSSocket
     */
    public SSLServerSocket getSslSSocket() {
        return SslSSocket;
    }

    /**
     * @param SslSSocket the SslSSocket to set
     */
    public void setSslSSocket(SSLServerSocket SslSSocket) {
        this.SslSSocket = SslSSocket;
    }

    /**
     * @return the SslSocket
     */
    public SSLSocket getSslSocket() {
        return SslSocket;
    }

    /**
     * @param SslSocket the SslSocket to set
     */
    public void setSslSocket(SSLSocket SslSocket) {
        this.SslSocket = SslSocket;
    }

}
