package application_jiachat;
import java.net.*;
import java.awt.*;
import java.io.*;
import javax.swing.DefaultListModel;
import javax.swing.JList;
public class ThreadReception extends Thread
{
    private String nom;
    private MulticastSocket socketGroupe;
    private JList<String> LMsgRecus;
    
    public ThreadReception (String n, MulticastSocket ms, JList<String> l)
    {
        nom = n; socketGroupe = ms; LMsgRecus = l;
    }
    
    public void run()
    {
        boolean enMarche = true;
        while (enMarche)
        {
            try
            {
                byte[] buf = new byte[1000];
                DatagramPacket dtg = new DatagramPacket(buf, buf.length);
                socketGroupe.receive(dtg);
                DefaultListModel dlm = (DefaultListModel) LMsgRecus.getModel();
                dlm = (DefaultListModel<String>) LMsgRecus.getModel();
                dlm.addElement(new String (buf).trim());
                //LMsgRecus.add(new String (buf).trim());
            }
            catch (IOException e)
            {
                System.out.println("Erreur dans le thread :-( :" + e.getMessage());
                enMarche = false;
            }
        }
    }
}