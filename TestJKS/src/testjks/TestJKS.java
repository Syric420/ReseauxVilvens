/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testjks;

import ProtocoleLUGAPM.Bagage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author tibha
 */
public class TestJKS {

    /**
     * @param args the command line arguments
     */;
    public static void main(String[] args) {
        try {
            
            Security.addProvider(new BouncyCastleProvider());
            
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(new FileInputStream("C:\\Users\\tibha\\Desktop\\Programmation Réseau et tech internet\\ClesLabo.jceks"),"123".toCharArray());          
            X509Certificate certif = (X509Certificate)ks.getCertificate("thibvince");
            System.out.println("Type de certificat : " + certif.getType());
            System.out.println("Nom du propriétaire du certificat : " +
            certif.getSubjectDN().getName());
            System.out.println("Recuperation de la cle publique");
            //Public Key
            PublicKey cléPublique = certif.getPublicKey();
            System.out.println("*** Cle publique recuperee = "+cléPublique.toString());
            System.out.println("Dates limites de validité : [" + certif.getNotBefore() + " - " +certif.getNotAfter() + "]");
            //private Key
            PrivateKey cléPrivée;
            cléPrivée = (PrivateKey) ks.getKey("thibvince", "123".toCharArray());
            System.out.println(" *** Cle privee recuperee = " + cléPrivée.toString());                

            String Message = "Code du jour : CVCCDMMM - bye";
            System.out.println("Message a envoyer au serveur : " + Message);
            byte[] message = Message.getBytes();
            
            System.out.println("Instanciation de la signature");
            Signature s = Signature.getInstance("SHA1withRSA","BC");
            System.out.println("Initialisation de la signature");
            s.initSign(cléPrivée);
            System.out.println("Hachage du message");
            s.update(message);
            System.out.println("Generation des bytes");
            byte[] signature = s.sign();
            System.out.println("Termine : signature construite");
            System.out.println("Signature = " + new String(signature));
            System.out.println("\nVérification de la signature");
            String str ="Bonjour je m appelle thibault";
            
            byte [] tmp =  encrypt(cléPublique ,str);
            System.out.println(str);
            System.out.println(new String(tmp));
            byte [] temp = decrypt(cléPrivée,tmp);
            Bagage bag = (Bagage) convertFromBytes(temp);
            System.out.println(bag.toString());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | NoSuchProviderException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(TestJKS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TestJKS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String byteToString(byte [] byteArray)
    {
        return new String(byteArray);
    }
    public byte [] stringToByte( String str)
    {
        return str.getBytes();
    }
    public static byte[] encrypt( PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA","BC");  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        Bagage id = new Bagage("1",1,(float)2,"coucou","lol");
        return cipher.doFinal(convertToBytes(id));  
    }
    
    public static byte[] decrypt(PrivateKey privateKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA","BC");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encrypted);
    }
    private static byte[] convertToBytes(Object object) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutput out = new ObjectOutputStream(bos)) {
        out.writeObject(object);
        return bos.toByteArray();
    } 
    }
    private static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
         ObjectInput in = new ObjectInputStream(bis)) {
        return in.readObject();
    } 
}
}
