package Server;

import ProtocoleLUGAP.RequeteLUGAP;
import database.utilities.BeanBD;
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
    private BeanBD Bc;
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
       while (!isInterrupted())
        {
            if(mySock!=null)
           {
                
                ObjectInputStream ois=null;
                RequeteLUGAP req = null;
                try
                {
                    ois = new ObjectInputStream(mySock.getInputStream());
                    req = (RequeteLUGAP)ois.readObject();
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
                
               /* if(req.getType()==RequeteLUGAP.REQUEST_DECONNECT)
                {
                    guiApplication.TraceEvenements(req.getChargeUtile()+"#Requête deconnexion de ");
                    this.setMySock(null);
                }
                else
                {*/
                
                    Runnable travail = req.createRunnable(mySock, guiApplication);
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
           //}
        }
    }

    public Socket getMySock() {
        return mySock;
    }

    public void setMySock(Socket mySock) {
        this.mySock = mySock;
    }
}
