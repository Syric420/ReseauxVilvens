/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IACOP;

import java.io.Serializable;
import java.security.MessageDigest;

/**
 *
 * @author Vince
 */
public class MessageLogin implements Serializable{
    public static int LOGIN_Client = 1;
    public static int LOGIN_EMPLOYE = 2;
    
    private int typeMessage;
    private String user;
    private String mdp;
    private MessageDigest digest;

    public MessageLogin() {
        this.user = null;
        this.mdp = null;
        this.digest = null;
    }

    public MessageLogin(String user, String mdp, MessageDigest digest) {
        this.user = user;
        this.mdp = mdp;
        this.digest = digest;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the mdp
     */
    public String getMdp() {
        return mdp;
    }

    /**
     * @param mdp the mdp to set
     */
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    /**
     * @return the digest
     */
    public MessageDigest getDigest() {
        return digest;
    }

    /**
     * @param digest the digest to set
     */
    public void setDigest(MessageDigest digest) {
        this.digest = digest;
    }

    /**
     * @return the typeMessage
     */
    public int getTypeMessage() {
        return typeMessage;
    }

    /**
     * @param typeMessage the typeMessage to set
     */
    public void setTypeMessage(int typeMessage) {
        this.typeMessage = typeMessage;
    }
    
    
}
