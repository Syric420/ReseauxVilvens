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
            SSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]"); System.exit(1);
        }
        thr = new ThreadClient[nbThreads];
        // Démarrage du pool de threads
        for (int i=0; i<nbThreads; i++)
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
                int i = ChercheThreadDispo();
                System.out.println("Thread dispo = Thread n"+i);
                thr[i].setMySock(CSocket);
                thr[i].start();
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]"); System.exit(1);
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
