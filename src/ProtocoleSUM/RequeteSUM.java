/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleSUM;
import Server.ConsoleServeur;
import java.io.*;
import java.util.*;
import java.net.*;
import Server.*;

/**
 *
 * @author Vince
 */
public class RequeteSUM implements Requete, Serializable
{
    //public static int REQUEST_CONNECT = 3;
    public static int REQUEST_E_MAIL = 1;
    public static int REQUEST_TEMPORARY_KEY = 2;
    public static int REQUEST_CONNECT = 3;
    public static Hashtable tableMails = new Hashtable();
    static
    {
    tableMails.put("Vilvens", "claude.vilvens@prov-liege.be");
    tableMails.put("Charlet", "christophe.charlet@prov-liege.be");
    tableMails.put("Madani", "mounawar.madani@prov-liege.be");
    tableMails.put("Wagner", "jean-marc.wagner@prov-liege.be");
    }
    public static Hashtable tablePwdNoms = new Hashtable();
    static
    {
        tablePwdNoms.put("GrosZZ", "Vilvens");
        tablePwdNoms.put("GrosRouteur", "Charlet");
        tablePwdNoms.put("GrosseVoiture", "Madani");
        tablePwdNoms.put("GrosCerveau", "Wagner");
    }
    private int type;
    private String chargeUtile;
    private Socket socketClient;
    public RequeteSUM(int t, String chu)
    {
        type = t; setChargeUtile(chu);
    }
    public RequeteSUM(int t, String chu, Socket s)
    {
        type = t; setChargeUtile(chu); socketClient =s;
    }
    public Runnable createRunnable (final Socket s, final ConsoleServeur cs)
    {
        /*if(type==REQUEST_CONNECT)
        {
            return new Runnable()
            {
                public void run()
                {
                    traiteClient(s, cs);
                }
            };
        }*/
        if (getType()==REQUEST_E_MAIL)
            return new Runnable()
            {
                public void run()
                {
                    traiteRequeteEMail(s, cs);
                }
            };
        else if (getType()==REQUEST_TEMPORARY_KEY)
            return new Runnable()
            {
                public void run()
                {
                    traiteRequeteKey(s, cs);
                }
            };
        else if(getType() == REQUEST_CONNECT)
        {
            return new Runnable()
            {
                public void run()
                {
                    traiterConnect(s, cs);
                }
            };
        }
        else return null;
    }
    private void traiterConnect(Socket sock, ConsoleServeur cs)
    {
        System.out.println("Traiter Connect");
    }
    private void traiteRequeteEMail(Socket sock, ConsoleServeur cs)
    {
        // Affichage des informations
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        System.out.println("Début de traiteRequete : adresse distante = " + adresseDistante);
        // la charge utile est le nom du client
        String eMail = (String)tableMails.get(getChargeUtile());
        cs.TraceEvenements(adresseDistante+"#Mail de "+ getChargeUtile()+"#"+Thread.currentThread().getName());
        if (eMail != null)
            System.out.println("E-Mail trouvé pour " + getChargeUtile());
        else
        {
            System.out.println("E-Mail non trouvé pour " + getChargeUtile() + " : " + eMail);
            eMail="?@?";
        }
        // Construction d'une réponse
        ReponseSUM rep = new ReponseSUM(ReponseSUM.EMAIL_OK, getChargeUtile() +" : " + eMail);
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            //oos.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }


    private void traiteRequeteKey(Socket sock, ConsoleServeur cs)
    {
    // TO DO ;-) !
    }
    public String getChargeUtile() { return chargeUtile; }
    
    public void setChargeUtile(String chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
}
