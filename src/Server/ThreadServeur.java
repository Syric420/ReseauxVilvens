/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import ProtocoleSUM.*;
import java.net.*;
import java.io.*;
import java.util.LinkedList;
/**
 *
 * @author Vince
 */
public class ThreadServeur extends Thread {
    private int port;
    private ConsoleServeur guiApplication;
    private ServerSocket SSocket = null;
    private ThreadClient []thr;
    
    public ThreadServeur(int p, ConsoleServeur fs)
    {
        port = p;  guiApplication = fs;
    }
    public void run()
    {
        try
        {
            SSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]"); System.exit(1);
        }
        // Démarrage du pool de threads
        for (int i=0; i<3; i++) // 3 devrait être constante ou une propriété du fichier de config
        {
            thr[i] = new ThreadClient (new ListeTaches(), "Thread du pool n°" +String.valueOf(i), null, guiApplication);
        }

        // Mise en attente du serveur
        Socket CSocket = null;
        while (!isInterrupted())
        {
            try
            {
                System.out.println("************ Serveur en attente");
                CSocket = SSocket.accept();
                guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#accept#thread serveur");
                //On assigne la socket à un thread Client
                assigneAThread(CSocket);
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]"); System.exit(1);
            }
            
            /*
            ObjectInputStream ois=null;
            RequeteSUM req = null;
            try
            {
                ois = new ObjectInputStream(CSocket.getInputStream());
                req = (RequeteSUM)ois.readObject();
                System.out.println("Requete lue par le serveur, instance de " +req.getClass().getName());
            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
            }
            catch (IOException e)
            {
                System.err.println("Erreur ? [" + e.getMessage() + "]");
            }
            Runnable travail = req.createRunnable(CSocket, guiApplication);
            if (travail != null)
            {
                tachesAExecuter.recordTache(travail);
                System.out.println("Travail mis dans la file");
            }
            else System.out.println("Pas de mise en file");*/
        }
    }
    
    public void assigneAThread(Socket sock)
    {
        int i;
        for(i=0; i<thr.length;i++)
        {
            if(thr[i].getMySock() == null)
            {
                thr[i].setMySock(sock);
                thr[i].start();
            }    
        }
        if(i==thr.length)
        {
            System.out.println("Plus de thread disponible");
            System.exit(0);
        }       
    }
}
