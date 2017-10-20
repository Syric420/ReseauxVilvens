/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import ProtocoleSUM.RequeteSUM;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
    public ThreadClient(SourceTaches st, String n, Socket s, ConsoleServeur fs )
    {
        tachesAExecuter = st;
        nom = n;
        mySock = s;
        guiApplication = fs;
    }
    
    public void run()
    {
        while (!isInterrupted())
        {
            System.out.println("MySock = "+mySock);
            while(mySock!=null)
            {
                
                ObjectInputStream ois=null;
                RequeteSUM req = null;
                try
                {
                    ois = new ObjectInputStream(mySock.getInputStream());
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
                    mySock=null;
                    this.interrupt();
                }
                Runnable travail = req.createRunnable(mySock, guiApplication);
                if (travail != null)
                {
                    tachesAExecuter.recordTache(travail);
                    System.out.println("Travail mis dans la file");
                }
                else System.out.println("Pas de mise en file");

                try
                {
                    System.out.println("Tread client avant get");
                    tacheEnCours = tachesAExecuter.getTache();
                }
                catch (InterruptedException e)
                {
                    System.out.println("Interruption : " + e.getMessage());
                }
                System.out.println("run de tachesencours");
                tacheEnCours.run();
            }
            
        }
    }

    /**
     * @return the mySock
     */
    public Socket getMySock() {
        return mySock;
    }

    /**
     * @param mySock the mySock to set
     */
    public void setMySock(Socket mySock) {
        this.mySock = mySock;
    }
}
