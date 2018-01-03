/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testjks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author tibha
 */
public class AES {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            InputStream keystoreStream = new FileInputStream("C:\\Users\\tibha\\Desktop\\Programmation Réseau et tech internet\\ClesLabo.jceks");
            Security.addProvider(new BouncyCastleProvider());
            KeyStore keystore = KeyStore.getInstance("JCEKS");
            keystore.load(keystoreStream, "123".toCharArray());
            //X509Certificate certif = (X509Certificate)keystore.getCertificate("thibvince");
            if (!keystore.containsAlias("sym3")) {
                throw new RuntimeException("Alias for key not found");
            }
            
            SecretKey key = (SecretKey)keystore.getKey("sym3", "123".toCharArray());
            System.out.println("Nom du propriétaire du certificat :" + key.toString());
            /*System.out.println("Nom du propriétaire du certificat : " +
            certif.getSubjectDN().getName());
            System.out.println("Recuperation de la cle publique");
            //Public Key
            PublicKey cléPublique = certif.getPublicKey();
            System.out.println("*** Cle publique recuperee = "+cléPublique.toString());
            System.out.println("Dates limites de validité : [" + certif.getNotBefore() + " - " +certif.getNotAfter() + "]");*/
            String str ="Bonjour je m appelle thibault";
            byte[] vecteurInit = new byte[16];
            SecureRandom sr = new SecureRandom();
            sr.nextBytes(vecteurInit);
            byte [] tmp =  encrypt(key ,str,vecteurInit);
            System.out.println(str);
            System.out.println(new String(tmp));
            byte [] temp = decrypt(key,tmp,vecteurInit);
            System.out.println("");
            System.out.println(new String(temp));
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public static byte[] encrypt( SecretKey key, String message,byte[] vecteurInit) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");  
        cipher.init(Cipher.ENCRYPT_MODE, key/*,new IvParameterSpec(vecteurInit)*/);  
        return cipher.doFinal(message.getBytes());  
    }
    
    public static byte[] decrypt(SecretKey key, byte [] encrypted,byte[] vecteurInit) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");  
        cipher.init(Cipher.DECRYPT_MODE, key/*,new IvParameterSpec(vecteurInit)*/);
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
