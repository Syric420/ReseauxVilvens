package Server;
import database.utilities.BeanBD;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Vince
 */
public class ThreadServeur extends Thread {
    private int port;
    private ConsoleServeur guiApplication;
    private ServerSocket SSocket = null;
    private int nbThreadsMax;
    private int nbThreads;
    private BeanBD Bc;
    private Socket CSocket;
    
    private static final int GET_FLY = 1;
    private static final int WARN_CHECKIN = 2;
    public static final int CHECK_BAGGAGE = 3;
    public static final int CHOOSE_FLY = 4;
    public static final int GET_PISTE = 5;
    public static final int CHOOSE_PISTE = 6;
    private static final int SUCCESS = 100;
    private static final int FAILED = 101;
    private static final int TAKING_OFF = 7;
    private static final int FLYING = 8;
    
    public ThreadServeur(int p, ConsoleServeur fs, int nb)
    {
        port = p;  guiApplication = fs; nbThreadsMax = nb;
        nbThreads=0;
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
        Bc = new BeanBD();
        Bc.setTypeBD("MySql");
        Bc.connect();
        
        // Mise en attente du serveur
        CSocket = null;
       
        while (!isInterrupted())
        {
            try
            {
                System.out.println("************ Serveur en attente");
                CSocket = getSSocket().accept();
                guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#accept");
                //On crée un thread à la demande
                
                if(nbThreads <nbThreadsMax)
                {
                    nbThreads++;
                    Thread ThreadClient = new Thread(new Runnable()
                    {
                        private DataOutputStream dos;
                        private DataInputStream dis;
                        @Override
                        public void run() {
                            try 
                            {
                                dos = new DataOutputStream(CSocket.getOutputStream());
                                dis = new DataInputStream(CSocket.getInputStream());
                                boolean fini=false;
                                while(fini==false)
                                {
                                    while(dis.available()<0);
                                    
                                    String message = readMessage();
                                    System.out.println("message reçu : "+message);
                                    String[] messageSplit = message.split(";");
                                    int type = Integer.parseInt(messageSplit[0]);
                                    switch(type)
                                    {
                                        case GET_FLY:
                                            //On répond avec tous les vols 
                                            System.out.println("GetFly");
                                            String s = Bc.findVolsDecollage();
                                            
                                            sendMessage(s+"\\");
                                            break;
                                    }
                                }
                                

                            } 
                            catch (IOException ex) {
                                Logger.getLogger(ThreadServeur.class.getName()).log(Level.SEVERE, null, ex);
                                System.exit(0);
                            }
                        }

                        private String readMessage() {
                            StringBuffer message=new StringBuffer();
                            try 
                            {
                                byte b;
                                while ((b=dis.readByte())!= (char)'\\' )
                                {
                                    message.append((char)b);
                                }

                            } 
                            catch (IOException ex) {
                                System.err.println("Erreur IOException : "+ex);
                                return null;
                            }
                            return message.toString().trim();
                        }
                        private void sendMessage(String s)
                        {
                            if(dos!=null)
                            {
                                try {
                                    dos.write(s.trim().getBytes());
                                    dos.flush();
                                } catch (IOException ex) {
                                    System.err.println("Erreur IOException : "+ex);
                                }

                            }
                        }
                    });
                    ThreadClient.start();
                }
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
}