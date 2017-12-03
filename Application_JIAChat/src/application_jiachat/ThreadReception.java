package application_jiachat;
import IACOP.Verify;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
public class ThreadReception extends Thread
{
    private String nom;
    private MulticastSocket socketGroupe;
    private JList<String> LMsgRecus;
    private boolean client;
    private javax.swing.JComboBox<String> jComboBoxQuestion;
    
    public ThreadReception (String n,boolean bool,MulticastSocket ms, JList<String> l,JComboBox<String> jComboBox)
    {
        nom = n; 
        socketGroupe = ms; 
        LMsgRecus = l;
        client = bool;
        jComboBoxQuestion=jComboBox;
        if(!bool)
        {
            DefaultComboBoxModel dcbm = (DefaultComboBoxModel) jComboBoxQuestion.getModel();
            dcbm.addElement("Post Event");
        }
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
                System.out.println("reception"+new String (buf));
                String str[] = (new String (buf)).trim().split("@");
                DefaultListModel dlm;
                switch (str[0]){
                        case "1":
                            if(!client)
                            {
                                //recoit question
                                Verify ver = new Verify();
                                ver.setMD(str[1]);
                                
                                byte[]tmp;
                                int cpt=0;
                                int debut=0,fin=0;
                                for(int i = 0; i<buf.length;i++)
                                {
                                    if(buf[i]==64)
                                        cpt++;
                                    else
                                        if(cpt>1 && debut ==0)
                                            debut=i;
                                    if(buf[i] ==0)
                                    {
                                        fin=i;
                                        break;
                                    }
                                        
                                }
                                
                                tmp = Arrays.copyOfRange(buf, debut, fin);
                                
                                if(ver.checkDigest(tmp))
                                {
                                    System.out.println("OK");
                                }
                                else 
                                    System.out.println("Not OK");
                                
                                DefaultComboBoxModel dcbm = (DefaultComboBoxModel) jComboBoxQuestion.getModel();
                                dcbm.addElement(str[1]);
                            }
                            dlm = (DefaultListModel) LMsgRecus.getModel();
                            dlm = (DefaultListModel<String>) LMsgRecus.getModel();
                            dlm.addElement(str[1]);
                            break;
                        case "2":
                            if(client)
                            {
                                //recoit réponse
                               
                            }
                            else
                            {
                                //supprime dans cb
                                DefaultComboBoxModel dcbm = (DefaultComboBoxModel) jComboBoxQuestion.getModel();
                                dcbm.removeElement(str[1]);
                            }
                            dlm = (DefaultListModel) LMsgRecus.getModel();
                            dlm = (DefaultListModel<String>) LMsgRecus.getModel();
                            System.out.println("str0 :" + str[0] + "str1 :" + str[1] + "str2 :" + str[2]);
                            dlm.addElement("@"+ str[1] + "  " +str[2]);                            
                            break;
                        case "3":

                            dlm = (DefaultListModel) LMsgRecus.getModel();
                            dlm = (DefaultListModel<String>) LMsgRecus.getModel();
                            dlm.addElement(str[1]);
                            break;
                            
                }

            }
            catch (IOException e)
            {
                System.out.println("Erreur dans le thread :-( :" + e.getMessage());
                enMarche = false;
            }
        }
    }
}