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
import Utilities.Identify;
import database.utilities.BeanConnect;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vince
 */
public class RequeteSUM implements Requete, Serializable
{
    public static int REQUEST_E_MAIL = 1;
    public static int REQUEST_TEMPORARY_KEY = 2;
    public static int REQUEST_CONNECT = 3;
    public static int REQUEST_DECONNECT = 4;
    public static Hashtable tableMails = new Hashtable();
    private byte [] ByteArray;
    public static int REQUEST_VOL = 5;
    public static Hashtable tableMails = new Hashtable();
    private byte [] ByteArray;
    private BeanConnect Bc;
    private int type;
    private String chargeUtile;
    private Socket socketClient;
    public RequeteSUM(int t, String chu)
    {
        type = t; setChargeUtile(chu);
        ByteArray = null;
    }
    public RequeteSUM(int t, String chu, Socket s,BeanConnect B)
    {
        type = t; setChargeUtile(chu); socketClient =s;
        ByteArray = null;
        Bc=B;
    }
    public Runnable createRunnable (final Socket s, final ConsoleServeur cs)
    {
        if(type==REQUEST_DECONNECT)
        {
            return new Runnable()
            {
                public void run()
                {
                    traiterDeconnect(s, cs);
                }
            };
           
        }
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
        
            return new Runnable()
            {
                public void run()
                {
                    traiterConnect(s, cs);
                }
            };
        else if(getType() == REQUEST_VOL)
        {
            return new Runnable()
            {
                public void run()
                {
                    traiterVol(s, cs);
                }
            };
        }
        else return null;
    }
    
    private void traiterDeconnect(Socket sock, ConsoleServeur cs)
    {
            ReponseSUM rep;
            
            ObjectOutputStream oos;
            System.out.println("Entre dans traiterDeconnect my sock = "+sock);
            /*sock.close();
            sock = null;
            /*try
            {
            System.out.println("Entre dans traiterDeconnect");
            rep = new ReponseSUM(ReponseSUM.LOGOUT_OK);
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(oos);
            }
            catch (IOException e)
            {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
            }*/
      }
        
    private void traiterVol(Socket sock, ConsoleServeur cs)
    {
        String s;
        s = Bc.findVols();
        ReponseSUM rep = new ReponseSUM(ReponseSUM.VOL_OK,s);
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
   
    private void traiterConnect(Socket sock, ConsoleServeur cs)
    {
        
        String chaine = getChargeUtile();
        String tab []= {};
        System.out.println("Traiter Connect : " + chaine);
        tab = chaine.split(";");
        for(int i = 0 ; i < tab.length ; i++)
            System.out.println(i + ": " + tab[i]);
        
        
        System.out.println("Digest : " + getByteArray());
        String s = getBc().findPassword(tab[0]);
        Identify id = null;
        ReponseSUM rep;
        if(s != null)
        {
            id = new Identify();
            long temps = Long.parseLong(tab[1]);
            double alea = Double.parseDouble(tab[2]);
            id.setMd(tab[0],s,temps,alea);
            //byte [] B=null;
            System.out.println("Hash 1: " + getByteArray() + " Hash 2: " + id.getMd());
            if (MessageDigest.isEqual(getByteArray(), id.getMd()) )
            {
                System.out.println("Digest OK");
                rep= new ReponseSUM(ReponseSUM.LOGIN_OK,"LOGIN OK");
            }
            else
                rep= new ReponseSUM(ReponseSUM.LOGIN_FAIL,"LOGIN FAILED");
        }
        else
        {
            rep= new ReponseSUM(ReponseSUM.LOGIN_FAIL,"LOGIN FAILED");
        }
        System.out.println("rep : " + rep.getChargeUtile());
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }

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

    /**
     * @return the ByteArray
     */
    public byte[] getByteArray() {
        return ByteArray;
    }

    /**
     * @param ByteArray the ByteArray to set
     */
    public void setByteArray(byte[] ByteArray) {
        this.ByteArray = ByteArray;
    }

    /**
     * @return the Bc
     */
    public BeanConnect getBc() {
        return Bc;
    }

    /**
     * @param Bc the Bc to set
     */
    public void setBc(BeanConnect Bc) {
        this.Bc = Bc;
    }

    
}
