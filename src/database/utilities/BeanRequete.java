/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.utilities;

/**
 *
 * @author Vince
 */
public class BeanRequete {
    private String buf,requete,table;
    private boolean count;

    public BeanRequete() {
        buf="";
        requete="";
        table="";
        count = false;
    }
    
    public void traiteRequete(String requ)
    {
        int i = 0;
        //selection de la requete
        if(requ.indexOf(" ",i) != 1)
        {
            requete=requ.substring(i, requ.indexOf(" ",i));
            i = requ.indexOf(" ",i)+1;
            do
            {   
                buf=requ.substring(i, requ.indexOf(" ",i));
                i = requ.indexOf(" ",i)+1;
                if(buf.equals("count(*)"))
                    count = true;
                if(buf.equals("from"))
                    break;
            }while(requ.indexOf(" ",i) != -1);
            //selection table
            if(requ.indexOf(" ",i) != -1)
                setTable(requ.substring(i,requ.indexOf(" ",i)));
            else
                setTable(requ.substring(i));
        }
        else
        {
            System.out.println("Erreur");
        }
           
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
}
