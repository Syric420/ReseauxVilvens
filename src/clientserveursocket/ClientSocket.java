/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientserveursocket;
import java.io.*;
import java.net.*;
/**
 *
 * @author tibha
 */
public class ClientSocket
{
public static void main(String[] args)
{
    Socket cliSock = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    String outNom="";
    int outQuantite=0;
    try
    {
        cliSock = new Socket("192.168.1.8", 50000);
        System.out.println("Client connecté : " + cliSock.getInetAddress().toString());
    }
    catch (UnknownHostException e)
    { System.err.println("Erreur ! Host non trouvé [" + e + "]"); }
    catch (IOException e)
    { System.err.println("Erreur ! Pas de connexion ? [" + e + "]"); }
    
    try
    {
        dis = new DataInputStream(cliSock.getInputStream());
        dos = new DataOutputStream(cliSock.getOutputStream());
        System.out.println("Flux créés");
        if (cliSock==null || dis==null || dos==null) System.exit(1);
        BufferedReader disClavier = new BufferedReader (
        new InputStreamReader (System.in));
        outNom = disClavier.readLine();
        System.out.println("outNom =" + outNom);
        outQuantite = Integer.parseInt(disClavier.readLine());
        System.out.println("outQuantite =" + outQuantite);
        disClavier.close();
    }
    catch (IOException e)
    { System.err.println("Erreur ! Pas de connexion ? [" + e + "]"); }
    
    String reponse = null;
    
    int inQuantiteRestante = 0;
    System.out.println("outNom = " + outNom + " et outQuantite =" + outQuantite);
    if (!outNom.equals("") && outQuantite>0 && dos!=null && dis!=null)
    {
        System.out.println("Client au travail");
        try
        {
            dos.writeUTF(outNom);
            dos.writeInt(outQuantite);
            reponse = dis.readUTF();
            inQuantiteRestante = dis.readInt();
            System.out.println("Reponse obtenue = " + reponse + " : quantité restante = " +
            inQuantiteRestante);
            dis.close(); dos.close(); cliSock.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]");
        }
    }
}
}