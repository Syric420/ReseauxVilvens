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
    //private String mdp;
    private Identify msgD;
    private int port_chat;
    private String addresse_chat;

    public MessageLogin() {
        this.user = null;
        //this.mdp = null;
        this.msgD = null;
    }

    public MessageLogin(String user, String mdp, Identify digest) {
        this.user = user;
        //this.mdp = mdp;
        this.msgD = digest;
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

    /**
     * @return the port_chat
     */
    public int getPort_chat() {
        return port_chat;
    }

    /**
     * @param port_chat the port_chat to set
     */
    public void setPort_chat(int port_chat) {
        this.port_chat = port_chat;
    }

    /**
     * @return the addresse_chat
     */
    public String getAddresse_chat() {
        return addresse_chat;
    }

    /**
     * @param addresse_chat the addresse_chat to set
     */
    public void setAddresse_chat(String addresse_chat) {
        this.addresse_chat = addresse_chat;
    }

    /**
     * @return the msgD
     */
    public Identify getMsgD() {
        return msgD;
    }

    /**
     * @param msgD the msgD to set
     */
    public void setMsgD(Identify msgD) {
        this.msgD = msgD;
    }
    
    
}
