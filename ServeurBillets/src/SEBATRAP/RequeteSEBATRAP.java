/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SEBATRAP;
import ServeurMastercard.ConsoleServeur;
import ServeurMastercard.Requete;
import java.io.*;
import java.net.*;
import Utilities.Encryption;
import Utilities.Identify;
import database.utilities.*;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

public class RequeteSEBATRAP implements Requete, Serializable
{
    public static int REQUEST_VERIF = 1;
    public static int REQUEST_PAIEMENT = 2;   
    private byte [] ByteArray;
    private BeanBD Bc;
    private BeanRequete Br;
    private int type;
    private String chargeUtile;
    private Socket socketClient;

    private PublicKey cléPublique = null;
    public RequeteSEBATRAP(int t, String chu)
    {
        type = t; setChargeUtile(chu);
        ByteArray = null;
        
    }
    public RequeteSEBATRAP(int t, String chu, Socket s,BeanBD B, BeanRequete R)
    {
        type = t; setChargeUtile(chu); socketClient =s;
        ByteArray = null;
        Bc=B;
        Br=R;
    }
    
    public Runnable createRunnable (final Socket s, final ConsoleServeur cs)
    {
        if(getType() == REQUEST_VERIF)
        
            return new Runnable()
            {
                public void run()
                {
                    VerificationAlimentation(s, cs);
                }
            };
        else 
            if(getType() == REQUEST_PAIEMENT)
        
            return new Runnable()
            {
                public void run()
                {
                    PaiementEffectif(s, cs);
                }
            };
            else return null;
    }
    
    private void PaiementEffectif(Socket s, ConsoleServeur cs)
    {
        cs.TraceEvenements(s.getInetAddress().toString()+"#Début PaiementEffectif");
        String []str = chargeUtile.split("@");
        String cb = str[0];
        String nom = str[1];
        int prix = Integer.parseInt(str[2]);
        int argentBanque = Bc.findArgent(nom);
        
        argentBanque -= prix;
        System.out.println("Argent banque = "+argentBanque);
        System.out.println("au nom de  = "+nom);
        Bc.setArgent(nom, argentBanque);
        cs.TraceEvenements(s.getInetAddress().toString()+"#Paiement bien effectif");
    }
    
    private void VerificationAlimentation(Socket s, ConsoleServeur cs)
    {
        cs.TraceEvenements(s.getInetAddress().toString()+"#Verification Alimentation");
        String []str = chargeUtile.split("@");
        String cb = str[0];
        String nom = str[1];
        int prix = Integer.parseInt(str[2]);

        int argentBanque = Bc.findArgent(nom);
        ReponseSEBATRAP rep;
        
        if(argentBanque<prix)
        {
            rep = new ReponseSEBATRAP(ReponseSEBATRAP.VERIF_NOK, "Pas assez d'argent");
           cs.TraceEvenements(s.getInetAddress().toString()+"#Verification NOK");
           try {
               ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
               oos.writeObject(rep);
           } catch (IOException ex) {
               Logger.getLogger(RequeteSEBATRAP.class.getName()).log(Level.SEVERE, null, ex);
           }
            
        }
        else
        {
            rep = new ReponseSEBATRAP(ReponseSEBATRAP.VERIF_OK, "OK il a assez d'argent");
            cs.TraceEvenements(s.getInetAddress().toString()+"#Verification OK");
            try {
               ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
               oos.writeObject(rep);
           } catch (IOException ex) {
               Logger.getLogger(RequeteSEBATRAP.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
        
        System.out.println(argentBanque+"<"+prix);
     
            
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
    /*private void AchatTickets(Socket sock, ConsoleServeur cs)
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
            ReponseSEBATRAP rep ;
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
            Logger.getLogger(RequeteSEBATRAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequeteSEBATRAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RequeteSEBATRAP.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
        } catch (IOException ex) {
            Logger.getLogger(RequeteSEBATRAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequeteSEBATRAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RequeteSEBATRAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    /*private void traiterConnect(Socket sock, ConsoleServeur cs)
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
        ReponseSEBATRAP rep;
        if(s != null)
        {
            id = new Identify();
            long temps = Long.parseLong(tab[1]);
            double alea = Double.parseDouble(tab[2]);
            id.setMd(tab[0],s,temps,alea);
            if (MessageDigest.isEqual(getByteArray(), id.getMd()) )
            {
                rep= new ReponseSEBATRAP(ReponseSEBATRAP.LOGIN_OK,"LOGIN OK"); 
                try {                                      
                    rep.setByteArray(convertToBytes(cléPublique));
                    cs.TraceEvenements("Serveur#Login OK pour "+tab[0]);
                    
                } catch (IOException ex) {
                    Logger.getLogger(RequeteSEBATRAP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                rep= new ReponseSEBATRAP(ReponseSEBATRAP.LOGIN_FAIL,"LOGIN FAILED");
                cs.TraceEvenements("Serveur#Login NOT OK pour "+tab[0]);
            }
        }
        else
        {
            rep= new ReponseSEBATRAP(ReponseSEBATRAP.LOGIN_FAIL,"LOGIN FAILED");
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

    }*/
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

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

}
