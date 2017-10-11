/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientserveursocket;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author tibha
 */
    
public class ThreadServeur extends Thread
{
    private int port;
    private ServerSocket SSocket;
    private Socket CSocket;
    private FenServeurSocket fenetreApplication;
    public ThreadServeur(int p, FenServeurSocket fss)
    {
        port =p; fenetreApplication = fss;
    }
    public void run()
    {
        SSocket = null; CSocket = null;
        try
        {
            SSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]");
            System.exit(1);
        }
        System.out.println("Serveur en attente");
        try
        {
        CSocket = SSocket.accept();
        }
        catch (SocketException e)
        {
            System.err.println("Accept interrompu ! ? [" + e + "]");
        }
        catch (IOException e)
        {
            System.err.println("Erreur d'accept ! ? [" + e + "]");
            System.exit(1);
        }
        try
        {
            System.out.println("Serveur a reçu connexion");
            DataInputStream dis = new DataInputStream(CSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(CSocket.getOutputStream());
            String inNom, outReponse;
            int inQuantite;
            while ( !(inNom = dis.readUTF()).equals("FIN"))
            {
            try
            {
                inQuantite = dis.readInt();
            }
            catch (NumberFormatException e)
            {
                System.err.println("Erreur ! La quantité lue n'est pas un nombre [" + e + "]");
                continue; // Denys-like
            }
            System.out.println("Requête reçue = " + inNom + "(" + inQuantite + ")");
            fenetreApplication.getLNom().setText(inNom);
            fenetreApplication.getLQuantiteDemandee().setText(
            String.valueOf(inQuantite));
            int quantiteRestante = 0;
            if (FenServeurSocket.getNbreActions().containsKey(inNom))
            {
                quantiteRestante = ((Integer)FenServeurSocket.getNbreActions().
                get(inNom)) - inQuantite;
                dos.writeUTF(inNom);
                dos.writeInt(quantiteRestante);
                System.out.println("Serveur a envoyé réponse positive");
            }
            else
            {
                dos.writeUTF(inNom + "INCONNU");
                dos.writeInt(-1);
                System.out.println("Serveur a envoyé réponse négative");
            }
            dos.flush();
            }
            dis.close(); dos.close();
            CSocket.close();
            System.out.println("Client déconnecté");
        }
        catch (IOException e)
        {
            System.err.println("Erreur ! ? [" + e + "]");
        }
    }
    public void doStop()
    {
        try
        {
        SSocket.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur ! ? [" + e + "]");
        }
        System.out.println("Serveur déconnecté");
        stop();
    }
}   

