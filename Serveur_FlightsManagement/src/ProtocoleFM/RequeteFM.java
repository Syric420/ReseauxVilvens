/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleFM;
import Server.ConsoleServeur;
import java.io.*;
import java.net.*;
import Server.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Vince
 */
public class RequeteFM implements Requete, Serializable
{
    public static int READ_XML = 1;
    private int type;
    private String chargeUtile;
    private Socket socketClient;
    private Document doc;
    public RequeteFM(int t, String chu,Document document)
    {
        type = t; setChargeUtile(chu);
        doc=document;
    }

    public RequeteFM(int t, String chu, Socket s)
    {
        type = t; setChargeUtile(chu); socketClient =s;
    }
    
    public Runnable createRunnable (final Socket s, final ConsoleServeur cs)
    {
        if(getType() == READ_XML)
        
            return new Runnable()
            {
                public void run()
                {
                    readXML(s, cs);
                }
            };
        else return null;
    }
    private void readXML(final Socket s, final ConsoleServeur cs)
    {
        System.out.println("cc");
        affichageDonnées(doc);
    }
    
    public String getChargeUtile() { return chargeUtile; }
    
    public void setChargeUtile(String chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @return the doc
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * @param doc the doc to set
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }
    public void affichageDonnées (Node noeud)
    {
        if (noeud == null) return; // arrêt de la descente récursive
        String name = noeud.getNodeName();
        String valeur;
        String str[];
        String tmp[];
        String lowcost="",nom="",pays="";
        String ville="",zoneFranche="";
        String date="",time="",prix="";
        switch(name){
            case("compagnie"):
                System.out.println("Compagnie: ");
                valeur = loadNode(noeud);
                //System.out.println("Résultats: " + valeur);
                str = valeur.split(";");
                lowcost="";
                nom="";
                pays="";
                for(int i=0;i<str.length;i++)
                {
                    if(str[i].contains("lowcost"))
                    {
                        tmp = str[i].split(" = ");
                        System.out.println(tmp[0] + " : " + tmp[1]);
                        lowcost = tmp[1];
                    }else if(str[i].contains("nom"))
                    {
                        tmp = str[i].split(" = ");
                        System.out.println(tmp[0] + " : " + tmp[1]);
                        nom = tmp[1];
                    }else if(str[i].contains("pays"))
                    {
                        tmp = str[i].split(" = ");
                        System.out.println(tmp[0] + " : " + tmp[1]);
                        pays = tmp[1];
                    }
                }
                break;
            case("createDestination"):
                System.out.println("createDestination");
                valeur = loadNode(noeud);
                System.out.println("Résultats: " + valeur);
                str = valeur.split(";");
                ville="";
                zoneFranche="";
                pays="";
                for(int i=0;i<str.length;i++)
                {
                    if(str[i].contains("ville"))
                    {
                        tmp = str[i].split(" = ");
                        System.out.println(tmp[0] + " : " + tmp[1]);
                        lowcost = tmp[1];
                    }else if(str[i].contains("zoneFranche"))
                    {
                        tmp = str[i].split(" = ");
                        System.out.println(tmp[0] + " : " + tmp[1]);
                        nom = tmp[1];
                    }else if(str[i].contains("pays"))
                    {
                        tmp = str[i].split(" = ");
                        System.out.println(tmp[0] + " : " + tmp[1]);
                        pays = tmp[1];
                    }
                }
                System.out.println(ville + zoneFranche + pays);
                break;
            case("createFlights"):
                System.out.println("createFlights");
                valeur = loadNode(noeud);
                valeur = valeur.replace("\n\t\t\t", "");
                System.out.println("Résultats: " + valeur.trim());
                str = valeur.split("vol = ;");
                for(int i=0;i<str.length;i++)
                {
                    String var[];
                    var = str[i].split(";");
                    //récupération des données de l'ensemble des vols
                    date="";
                    time="";
                    prix="";
                    for (int j=0; j< var.length;j++)
                    {
                        if(var[j].contains("date"))
                        {
                            tmp = var[j].split(" = ");
                            System.out.println(tmp[0] + " : " + tmp[1]);
                            date = tmp[1];
                        }else if(var[j].contains("time"))
                        {
                            tmp = var[j].split(" = ");
                            System.out.println(tmp[0] + " : " + tmp[1]);
                            time = tmp[1];
                        }else if(var[j].contains("prix"))
                        {
                            tmp = var[j].split(" = ");
                            System.out.println(tmp[0] + " : " + tmp[1]);
                            prix = tmp[1];
                        }
                    }
                    System.out.println(date + time + prix);
                }
                break;
            case("listeVols"):
                
        }
        NodeList enfants = noeud.getChildNodes();
        for (int i=0; i<enfants.getLength(); i++)
        {
        // appel récursif pour chaque enfant
            affichageDonnées (enfants.item(i));
        }
    }
    public String loadNode(Node noeud)
    {
        String data = "";
        if (noeud == null) return "";
        
        //recherches des attributs
        NamedNodeMap attrs = noeud.getAttributes(); 
        if(attrs != null)
            for(int i = 0 ; i<attrs.getLength() ; i++) {
              Attr attribute = (Attr)attrs.item(i);     
              //System.out.println(attribute.getName()+" = "+attribute.getValue());
              data = data + (attribute.getName()+" = "+attribute.getValue()) + ";";
            }
        NodeList enfants = noeud.getChildNodes();
        //recherche des données des enfants. + recherche de leurs enfants potentiels
        for (int i=0; i<enfants.getLength(); i++)
        {
            if(enfants.item(i) != null)
            {
                if(!enfants.item(i).getNodeName().equalsIgnoreCase("#text"))
                {
                    //System.out.print(enfants.item(i).getNodeName() + " = ");
                    data = data + (enfants.item(i).getNodeName() + " = ");
                }  
                NodeList tmp = enfants.item(i).getChildNodes();
                if(tmp.item(0) != null)
                {
                    //System.out.println(tmp.item(0).getNodeValue());
                    data = data + (tmp.item(0).getNodeValue()+ ";");
                } 
                data = data + loadNode (enfants.item(i));
            }
        }
        return data;
    }
    
}
