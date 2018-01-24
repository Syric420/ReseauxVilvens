/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TICKMAP;

import java.io.*;
import Server.Reponse;
import java.util.Vector;
/**
 *
 * @author Vince
 */
public class ReponseTICKMAP implements Reponse, Serializable
{
    public static int LOGIN_OK = 101;
    public static int LOGIN_FAIL = 102;
    public static int VOL_LOADED = 103;
    
    private int codeRetour;
    private String chargeUtile;
    private byte[] byteArray;
    public ReponseTICKMAP(int c, String chu)
    {
        codeRetour = c; 
        setChargeUtile(chu);
    }
    public ReponseTICKMAP(int c, String chu,byte[] array)
    {
        codeRetour = c; 
        setChargeUtile(chu);
        setByteArray(array);
    }
    
    public int getCode() { return codeRetour; }
    public String getChargeUtile() { return chargeUtile; }
    public void setChargeUtile(String chargeUtile) { this.chargeUtile = chargeUtile; }

    /**
     * @return the byteArray
     */
    public byte[] getByteArray() {
        return byteArray;
    }

    /**
     * @param byteArray the byteArray to set
     */
    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

}
