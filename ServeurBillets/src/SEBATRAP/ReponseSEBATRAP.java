/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SEBATRAP;

import ServeurMastercard.Reponse;
import java.io.*;
import java.util.Vector;
/**
 *
 * @author Vince
 */
public class ReponseSEBATRAP implements Reponse, Serializable
{
    public static int VERIF_OK = 101;
    public static int VERIF_NOK = 102;
    public static int PAIEMENT_OK = 201;
    public static int PAIEMENT_NOK = 202;
    
    private int codeRetour;
    private String chargeUtile;
    private byte[] byteArray;
    public ReponseSEBATRAP(int c, String chu)
    {
        codeRetour = c; 
        setChargeUtile(chu);
    }
    public ReponseSEBATRAP(int c, String chu,byte[] array)
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
