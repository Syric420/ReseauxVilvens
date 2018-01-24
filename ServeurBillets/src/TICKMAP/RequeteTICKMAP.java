/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TICKMAP;
import Server.ConsoleServeur;
import java.io.*;
import java.net.*;
import Server.*;
import Utilities.Encryption;
import Utilities.Identify;
import database.utilities.*;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;

public class RequeteTICKMAP implements Requete, Serializable
{
    public static int REQUEST_CONNECT = 1;
    public static int REQUEST_DECONNECT = 2;   
    private byte [] ByteArray;
    private BeanBD Bc;
    private BeanRequete Br;
    private int type;
    private String chargeUtile;
    private Socket socketClient;

    private PublicKey cléPublique = null;
    public RequeteTICKMAP(int t, String chu)
    {
        type = t; setChargeUtile(chu);
        ByteArray = null;
    }
    public RequeteTICKMAP(int t, String chu, Socket s,BeanBD B, BeanRequete R)
    {
        type = t; setChargeUtile(chu); socketClient =s;
        ByteArray = null;
        Bc=B;
        Br=R;
    }
    
    public Runnable createRunnable (final Socket s, final ConsoleServeur cs,PublicKey cléPublique)
    {
        if(getType() == REQUEST_CONNECT)
        
            return new Runnable()
            {
                public void run()
                {
                    traiterConnect(s, cs,cléPublique);
                }
            };
        else return null;
    }
        public Runnable createRunnable (final Socket s, final ConsoleServeur cs)
    {
        if(getType() == REQUEST_CONNECT)
        
            return new Runnable()
            {
                public void run()
                {
                    traiterConnect(s, cs,cléPublique);
                }
            };
        else return null;
    }

    private void traiterConnect(Socket sock, ConsoleServeur cs,PublicKey cléPublique)
    {
        //permet d'interagir avec le thread parent
       
        
        
        String chaine = getChargeUtile();
        String tab []= {};
        tab = chaine.split(";");
        for(int i = 0 ; i < tab.length ; i++)
            System.out.println(i + ": " + tab[i]);
        
        String s = getBc().findPassword(tab[0]);
        cs.TraceEvenements("Serveur#Login "+tab[0]);
        Identify id = null;
        ReponseTICKMAP rep;
        if(s != null)
        {
            id = new Identify();
            long temps = Long.parseLong(tab[1]);
            double alea = Double.parseDouble(tab[2]);
            id.setMd(tab[0],s,temps,alea);
            if (MessageDigest.isEqual(getByteArray(), id.getMd()) )
            {
                rep= new ReponseTICKMAP(ReponseTICKMAP.LOGIN_OK,"LOGIN OK"); 
                try {                                      
                    rep.setByteArray(convertToBytes(cléPublique));
                    cs.TraceEvenements("Serveur#Login OK pour "+tab[0]);
                    
                } catch (IOException ex) {
                    Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                rep= new ReponseTICKMAP(ReponseTICKMAP.LOGIN_FAIL,"LOGIN FAILED");
                cs.TraceEvenements("Serveur#Login NOT OK pour "+tab[0]);
            }
        }
        else
        {
            rep= new ReponseTICKMAP(ReponseTICKMAP.LOGIN_FAIL,"LOGIN FAILED");
        }
        
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
        
        if(rep.getChargeUtile().equals("LOGIN OK"))
        {
            handshake(sock);
            //envoit des vols
            rep= new ReponseTICKMAP(ReponseTICKMAP.VOL_LOADED,Bc.findVols());
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

    }
    private void handshake(Socket sock)
    {
        ThreadClient thread = (ThreadClient) Thread.currentThread();
        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(sock.getInputStream());
            
            byte[] tmp = ((Encryption)ois.readObject()).getMessage();
            byte[] messageClair=Encryption.decryptRSA(thread.getCléPrivée(), tmp); 
            SecretKey var =(SecretKey)Encryption.convertFromBytes(messageClair);
            thread.setKeyCipher(var); 
            
            tmp = (byte[])ois.readObject();
            messageClair=Encryption.decryptRSA(thread.getCléPrivée(), tmp);
            thread.setKeyHmac((SecretKey)Encryption.convertFromBytes(messageClair));
            
        } catch (IOException ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public String getChargeUtile() { return chargeUtile; }
    
    public void setChargeUtile(String chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }

    public int getType() {
        return type;
    }

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
    private byte[] convertToBytes(Object object) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutput out = new ObjectOutputStream(bos)) {
        out.writeObject(object);
        return bos.toByteArray();
    } 
    }

    
}
