/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleXML;
import Server.ConsoleServeur;
import java.io.*;
import java.net.*;
import Server.*;
import Utilities.ReadProperties;
import clientServeurSocket.InterfaceClient;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Vince
 */
public class RequeteXML implements Requete, Serializable
{
    public static int READ_XML = 1;
    private int type;
    private String chargeUtile;
    private Socket socketClient;
    private Document doc;
    private byte[] bFile;
    String valeur;
    String str[];
    String tmp[];
    String lowcost="",nom="",pays="";
    String ville="",zoneFranche="";
    String date="",time="",prix="";
    String compagnie="";
    String nTickets="";
    Class cClass = null;
    Method infoMethodes[];
    ReadProperties rP=null ;
    
    public RequeteXML(int t, String chu,byte[] b)
    {
        type = t; setChargeUtile(chu);
        bFile=b;

    }

    public RequeteXML(int t, String chu, Socket s)
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
        try {
            InputStream iStream = new ByteArrayInputStream(bFile); 
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            doc = db.parse(iStream);
            affichageDonnées(doc);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        String name = noeud.getNodeName();
        if (noeud == null) return; // arrêt de la descente récursive
        try {
            rP = new ReadProperties("/ProtocoleXML/Company.properties");
        } catch (IOException ex) {
            Logger.getLogger(InterfaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        switch(name){
            case("compagnie"):
                System.out.println("Compagnie: ");
                valeur = loadNode(noeud);
                //System.out.println("Résultats: " + valeur);
                
                str = valeur.split(";");
                lowcost="";
                compagnie="";
                pays="";
                //a faire dans la classe dédiée
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
                        compagnie = tmp[1];
                    }else if(str[i].contains("pays"))
                    {
                        tmp = str[i].split(" = ");
                        System.out.println(tmp[0] + " : " + tmp[1]);
                        pays = tmp[1];
                    }
                }
                String temp = rP.getProp(compagnie);
                nTickets = rP.getProp(compagnie+"NumberTickets");
                try {
                    cClass = Class.forName(temp);

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
                }
                {
                    infoMethodes = cClass.getDeclaredMethods();
                    Class infoParametres[] = infoMethodes[2].getParameterTypes();
                    Object parametres[] = new Object[infoParametres.length];
                    for (int i=0; i<infoMethodes.length; i++)
                    {
                        System.out.println("méthode["+i+"] = "+infoMethodes[i]);
                    }
                    try {
                        parametres[0]= new String("TEST");
                        parametres[1]= new String("TEST");
                        Object obj = cClass.newInstance();
                        System.out.println("ICI");
                        System.out.println("méthode[0] = "+infoMethodes[2]);
                        infoMethodes[2].invoke(obj, parametres);
                    } catch (InstantiationException ex) {
                        Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
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
                        ville = tmp[1];
                    }else if(str[i].contains("zoneFranche"))
                    {
                        tmp = str[i].split(" = ");
                        System.out.println(tmp[0] + " : " + tmp[1]);
                        zoneFranche = tmp[1];
                    }else if(str[i].contains("pays"))
                    {
                        tmp = str[i].split(" = ");
                        System.out.println(tmp[0] + " : " + tmp[1]);
                        pays = tmp[1];
                    }
                }
                {
                    infoMethodes = cClass.getDeclaredMethods();
                    int indice = 0;
                    for(indice = 0 ; indice<infoMethodes.length ; indice++)
                    {
                        if(infoMethodes[indice].getName().contains("createDestination"))
                            break;
                    }
                    Class infoParametres[] = infoMethodes[indice].getParameterTypes();
                    Object parametres[] = new Object[infoParametres.length];
                    System.out.println(ville + zoneFranche + pays);
                    try {
                        parametres[0]= new String(ville);
                        parametres[1]= new String(pays);
                        Object obj = cClass.newInstance();
                        infoMethodes[indice].invoke(obj, parametres);
                    } catch (InstantiationException ex) {
                        Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(RequeteXML.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
