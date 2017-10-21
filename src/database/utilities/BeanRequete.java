/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.utilities;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vince
 */
public class BeanRequete {
    private String buf,requete,table, where, column;
    private boolean count;
    private BeanConnect bc;

    public BeanRequete() {
        buf="";
        requete="";
        table="";
        count = false;
    }
    
    
    private String []IniNameTable()
    {
        String monVec[]={};
        try {
            ResultSetMetaData rsmd = bc.getRs().getMetaData();
            int nbrCol = rsmd.getColumnCount();
            for(int i=0; i<nbrCol;i++)
            {
                 monVec[i]=rsmd.getColumnName(i);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(BeanRequete.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return monVec;
    }
    
    public int Set()
    {
        
        return -1;
    }
    
    public int Select()
    {
        
        return -1;
    }
    
    public int Count()
    {
        return -1;
    }
    
    
    /**
     * @return the buf
     */
    public String getBuf() {
        return buf;
    }

    /**
     * @param buf the buf to set
     */
    public void setBuf(String buf) {
        this.buf = buf;
    }

    /**
     * @return the requete
     */
    public String getRequete() {
        return requete;
    }

    /**
     * @param requete the requete to set
     */
    public void setRequete(String requete) {
        this.requete = requete;
    }

    /**
     * @return the table
     */
    public String getrequ() {
        return getTable();
    }

    /**
     * @param table the table to set
     */
    public void setrequ(String table) {
        this.setTable(table);
    }

    /**
     * @return the count
     */
    public boolean isCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(boolean count) {
        this.count = count;
    }

    /**
     * @return the table
     */
    public String getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * @return the where
     */
    public String getWhere() {
        return where;
    }

    /**
     * @param where the where to set
     */
    public void setWhere(String where) {
        this.where = where;
    }

    /**
     * @return the bc
     */
    public BeanConnect getBc() {
        return bc;
    }

    /**
     * @param bc the bc to set
     */
    public void setBc(BeanConnect bc) {
        this.bc = bc;
    }
}
