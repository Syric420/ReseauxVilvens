/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientserveursocket;

/**
 *
 * @author tibha
 */
import java.io.*;
import java.net.*;
import java.util.*;
public class FenServeurSocket extends javax.swing.JFrame
{
    private static Hashtable nbreActions = new Hashtable();
    static
    {
        getNbreActions().put("Vilvens", 1520); getNbreActions().put("Charlet", 5210);
        getNbreActions().put("Madani", 541); getNbreActions().put("Wagner", 1519);
        getNbreActions().put("Mercenier", 15230); getNbreActions().put("Kuty", 300);
    }
    public static Hashtable getNbreActions()
    {
        return nbreActions;
    }
    private int port;
    private ThreadServeur ts;
    public FenServeurSocket(int p)
    {
        initComponents();
        port = p;
    }
    private void initComponents() { }
    private void BArreterActionPerformed(java.awt.event.ActionEvent evt)
    {
        ts.doStop();
        System.exit(0);
    }
    private void BDemarrerActionPerformed(java.awt.event.ActionEvent evt)
    {
        ts = new ThreadServeur(port, this);
        ts.start();
    }
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
        public void run()
        {
            new FenServeurSocket(50000).setVisible(true);
        }
        });
    }
    public javax.swing.JLabel getLNom() { return LNom; }
    public javax.swing.JLabel getLQuantiteDemandee() { return LQuantiteDemandee; }
    private javax.swing.JButton BArreter;
    private javax.swing.JButton BDemarrer;
    private javax.swing.JLabel LAnnonce;
    private javax.swing.JLabel LNom;
    private javax.swing.JLabel LNomA;
    private javax.swing.JLabel LQuantiteA;
    private javax.swing.JLabel LQuantiteDemandee;
    private javax.swing.JButton jButton1;
}
