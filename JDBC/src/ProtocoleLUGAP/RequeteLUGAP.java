/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleLUGAP;
import Server.ConsoleServeur;
import java.io.*;
import java.net.*;
import Server.*;
import Utilities.Identify;
import database.utilities.*;
import java.security.MessageDigest;

/**
 *
 * @author Vince
 */
public class RequeteLUGAP implements Requete, Serializable
{
    //public static int REQUEST_CONNECT = 3;
    public static int REQUEST_UPDATELUG = 1;
    public static int REQUEST_CONNECT = 3;
    public static int REQUEST_VOL = 4;
    public static int REQUEST_DECONNECT = 5;
    public static int REQUEST_LUG=6;
    private byte [] ByteArray;
    private BeanBD Bc;
    private BeanRequete Br;
    private int type;
    private String chargeUtile;
    private Socket socketClient;
    public RequeteLUGAP(int t, String chu)
    {
        type = t; setChargeUtile(chu);
        ByteArray = null;
    }
    public RequeteLUGAP(int t, String chu, Socket s,BeanBD B, BeanRequete R)
    {
        type = t; setChargeUtile(chu); socketClient =s;
        ByteArray = null;
        Bc=B;
        Br=R;
    }
    public Runnable createRunnable (final Socket s, final ConsoleServeur cs)
    {
        if(getType() == REQUEST_CONNECT)
        
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
        else
        if(getType() == REQUEST_LUG)
        {
            return new Runnable()
            {
                public void run()
                {
                    traiterBagages(s, cs);
                }
            };
        }else 
            
        if(getType() == REQUEST_UPDATELUG)
        {
            return new Runnable()
            {
                public void run()
                {
                    updateBagages(cs);
                }
            };
        }else return null;
    }
    private void updateBagages(ConsoleServeur cs)
    {
        //System.out.println("UPDATE" + getChargeUtile());
        Bc.updateLug(getChargeUtile());
        cs.TraceEvenements("Serveur#Effectue un UPDATE");
    }
    private void traiterBagages(Socket sock, ConsoleServeur cs)
    {
        String s;
        s = Bc.findBagages(getChargeUtile());
        ReponseLUGAP rep = new ReponseLUGAP(ReponseLUGAP.LUG_OK,s);
        cs.TraceEvenements("Serveur#Recherche un bagage");
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
    private void traiterVol(Socket sock, ConsoleServeur cs)
    {
        String s;
        
        s = Bc.findVols();
        ReponseLUGAP rep = new ReponseLUGAP(ReponseLUGAP.VOL_OK,s);
        cs.TraceEvenements("Serveur#Recherche un vol#");
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
    private void traiterConnect(Socket sock, ConsoleServeur cs)
    {
        
        String chaine = getChargeUtile();
        String tab []= {};
        //System.out.println("Traiter Connect : " + chaine);
        tab = chaine.split(";");
        for(int i = 0 ; i < tab.length ; i++)
            System.out.println(i + ": " + tab[i]);
        
        
        //System.out.println("Digest : " + getByteArray());
        String s = getBc().findPassword(tab[0]);
        cs.TraceEvenements("Serveur#Login "+tab[0]);
        Identify id = null;
        ReponseLUGAP rep;
        if(s != null)
        {
            id = new Identify();
            long temps = Long.parseLong(tab[1]);
            double alea = Double.parseDouble(tab[2]);
            id.setMd(tab[0],s,temps,alea);
            //byte [] B=null;
            //System.out.println("Hash 1: " + getByteArray() + " Hash 2: " + id.getMd());
            if (MessageDigest.isEqual(getByteArray(), id.getMd()) )
            {
                //System.out.println("Digest OK");
                rep= new ReponseLUGAP(ReponseLUGAP.LOGIN_OK,"LOGIN OK");
                cs.TraceEvenements("Serveur#Login OK pour "+tab[0]);
            }
            else
            {
                rep= new ReponseLUGAP(ReponseLUGAP.LOGIN_FAIL,"LOGIN FAILED");
                cs.TraceEvenements("Serveur#Login OK pour "+tab[0]);
            }
        }
        else
        {
            rep= new ReponseLUGAP(ReponseLUGAP.LOGIN_FAIL,"LOGIN FAILED");
        }
        //System.out.println("rep : " + rep.getChargeUtile());
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
    public BeanBD getBc() {
        return Bc;
    }

    /**
     * @param Bc the Bc to set
     */
    public void setBc(BeanBD Bc) {
        this.Bc = Bc;
    }

    
}
