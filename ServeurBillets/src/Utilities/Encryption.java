/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 *
 * @author tibha
 */
public class Encryption implements Serializable {
    private byte[]message;
     public static byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos)) 
        {
            out.writeObject(object);
            return bos.toByteArray();
        } 
    }
    public static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bis))
        {
        return in.readObject();
        } 
    }
    
    public static byte[] encryptDES( SecretKey key, Object message) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");  
        cipher.init(Cipher.ENCRYPT_MODE, key);  
        return cipher.doFinal(convertToBytes(message));  
    }
    
    public static byte[] decryptDES(SecretKey key, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");  
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encrypted);
    }
    public static byte[] encryptRSA( PublicKey publicKey, Object message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA","BC");  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(convertToBytes(message));  
    }
    
    public static byte[] decryptRSA(PrivateKey privateKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA","BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encrypted);
    }

    /**
     * @return the message
     */
    public byte[] getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(byte[] message) {
        this.message = message;
    }
}
