/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TICKMAP;
import SEBATRAP.*;
import Server.ConsoleServeur;
import java.io.*;
import java.net.*;
import Server.*;
import Utilities.Encryption;
import Utilities.Identify;
import clientServeurSocket.InterfaceClient;
import database.utilities.*;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

public class RequeteTICKMAP implements Requete, Serializable
{
    public static int REQUEST_CONNECT = 1;
    public static int REQUEST_DECONNECT = 2;   
    public static int REQUEST_BUYTICKETS = 3; 
    public static int REQUEST_CONFIRMATION = 4;
    public static int REQUEST_WEBPAY = 5;
    private byte [] ByteArray;
    private byte [] MessageCrypte;
    private BeanBD Bc;
    private BeanRequete Br;
    private int type;
    private String chargeUtile;
    private Socket socketClient;

    private PublicKey cléPublique = null;
    public RequeteTICKMAP(int t, byte [] message)
    {
        MessageCrypte = message;
        type = t;
        ByteArray = null;
    }
    public RequeteTICKMAP(int t, String chu)
    {
        type = t; setChargeUtile(chu);
        ByteArray = null;
        MessageCrypte = null;
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
        else 
            if(getType() == REQUEST_BUYTICKETS)
        
            return new Runnable()
            {
                public void run()
                {
                    AchatTickets(s, cs);
                }
            };
            else 
            if(getType() == REQUEST_CONFIRMATION)
        
            return new Runnable()
            {
                public void run()
                {
                    Confirm(s, cs);
                }
            };
            else return null;
    }
    /*public Runnable createRunnable (final Socket s, final ConsoleServeur cs)
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
    }*/
    private void Confirm(Socket sock, ConsoleServeur cs)
    {
        System.out.println("CONFIRMATION PAYMENT");
        String [] str;
        str = chargeUtile.split("@");
        String update;
        if(chargeUtile.contains("OK"))
        {
            update = "UPDATE volsreserves SET `Paye`='1' WHERE `idVolsReserves`='"+ str[0] + "';";
        }
        else
            update="UPDATE volsreserves SET `Paye`='0' WHERE `idVolsReserves`='"+ str[0] + "';";        
        System.out.println(update);
        Bc.payeVols(update);
    }
    private void AchatTickets(Socket sock, ConsoleServeur cs)
    {
        try {
            ThreadClient thread = (ThreadClient) Thread.currentThread();
            byte[] messageClair=Encryption.decryptDES(thread.getKeyCipher(), MessageCrypte);
            String str =(String)Encryption.convertFromBytes(messageClair);
            System.out.println(new String(MessageCrypte));
            System.out.println(str);
            
            String [] arrayCol,arrayData;
            arrayCol = str.split("@@");
            int size = 0;

            if(arrayCol.length == 4)
            {
                arrayData = arrayCol[3].split("@");
                size = arrayData.length;
            }    
            String q = "select PlacesRestantes from vols where idVols = '" + str + "';" ;
            int nbreDemande = size + 1;
            int NbreMax = Bc.selectInt(q);
            int tmp = NbreMax - nbreDemande;
            ReponseTICKMAP rep ;
            Encryption crypt = new Encryption();
            String message = "";
            if(tmp>0 && nbreDemande != 0)
            {
                System.out.println("Places restantes : " + tmp);
                String update="UPDATE vols SET `PlacesRestantes`='" + tmp + "' WHERE `idVols`='"+ arrayCol[0] + "';";
                String insert = "INSERT INTO volsreserves  (`idVolsReserves`, `Utilisateur`, `idVols`, `NombreDePlaces`,`Paye`) VALUES ('"+ arrayCol[1] +  arrayCol[0] + tmp + "', '" + arrayCol[1] + "', '"+  arrayCol[0] + "', '" + nbreDemande + "', '0' );";
                String idVolsReserve = arrayCol[1] +  arrayCol[0] + tmp;
                System.out.println(update);
                System.out.println(insert);
                Bc.reserveVols(update,insert);
                int prix = 0;
                
                for(int i=0;i<nbreDemande;i++)
                {
                    message = message + (NbreMax - i -1) + ";";
                    prix = prix + 50;
                }
                message = message + "@";
                message = message + prix + "@" + arrayCol[0] + "@" + idVolsReserve;
                byte[]reqCrypt = Encryption.encryptDES(thread.getKeyCipher(),message);
                
                crypt.setMessage(reqCrypt);

            }
            else
            {
                byte[]reqCrypt = Encryption.encryptDES(thread.getKeyCipher(),"Pas assez de places disponnibles");
                crypt.setMessage(reqCrypt);
            }
            ObjectOutputStream oos;
            try
            {
                oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(crypt); oos.flush();
            }
            catch (IOException e)
            {
                System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
            }
            
            
            ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(sock.getInputStream());
       
            byte []hmacEmploye = (byte[])ois.readObject();
            
            Mac hmac = Mac.getInstance("HMAC-MD5", "BC");
            hmac.init(thread.getKeyHmac());
            System.out.println("Hachage du message");
            byte[] msg = Encryption.convertToBytes(message);
            hmac.update(msg);
            System.out.println("Generation des bytes");
            byte[] hb = hmac.doFinal();
            if (MessageDigest.isEqual(hmacEmploye, hb) )
            {
                System.out.println("Le messsage a été authentifié");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
        } catch (IOException ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
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
