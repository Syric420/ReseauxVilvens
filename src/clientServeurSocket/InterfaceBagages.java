/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientServeurSocket;

import ProtocoleLUGAP.ReponseLUGAP;
import ProtocoleLUGAP.RequeteLUGAP;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author tibha
 */
public class InterfaceBagages extends javax.swing.JDialog {

    /**
     * Creates new form InterfaceBagages
     */
    private boolean toutEnSoute;
    private Socket cliSock;
    private boolean demarre;
    private String chaine;
    public InterfaceBagages(java.awt.Frame parent, boolean modal,String str,Socket Sock) {
        super(parent, modal);
        initComponents();
        cliSock=Sock;
        demarre=false;
        toutEnSoute=false;
        chaine = str;
        RechercheBagages(chaine);
    }
    private void RechercheBagages(String str)
    {       
        ObjectOutputStream oos =null;
        RequeteLUGAP req = null;
        req = new RequeteLUGAP(RequeteLUGAP.REQUEST_LUG, str);
        try
        {
            oos= new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); 
        }
        ObjectInputStream ois;ois=null;
        ReponseLUGAP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseLUGAP)ois.readObject();
            System.out.println(" *** Reponse reçue : " + rep.getChargeUtile());
        }
        catch (ClassNotFoundException e)
        { 
            System.out.println("--- erreur sur la classe = " + e.getMessage()); 
        }
        catch (IOException e)
        { 
            System.out.println("--- erreur IO = " + e.getMessage()); }
            iniTable(rep.getChargeUtile());
    }
        
    private void iniTable(String tab)
    {
        System.out.println("INITABLE");
        String nomTable[] ={},var[] = {},tuples[];
        var=tab.split("@");
        nomTable=var[0].split(";");
        jTable1.setModel(new javax.swing.table.DefaultTableModel
                    (
                            new Object [][] {
                            },
                            nomTable                           
                    ) {

            @Override
            public boolean isCellEditable(int row, int column) {
               return column == 0 || column== 1 || column== 2 ? false : true;
    }
});
        DefaultTableModel dm= (DefaultTableModel)jTable1.getModel();
        demarre=true;
        for(int i=1; i <var.length ; i++)
        {
            tuples= var[i].split(";");
            switch (tuples[1]){ 
                case"true":
                    tuples[1]="VALISE";
                    break;
                default:
                    tuples[1]="PAS VALISE";           
            }
            dm.addRow(tuples);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Bagages");
        setIconImages(null);
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable1PropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1129, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable1PropertyChange
        // TODO add your handling code here:
       DefaultTableModel dm= (DefaultTableModel)jTable1.getModel();
       if(demarre)
       {
           if(dm.getRowCount()>0)
           {
               
               String test = (String)dm.getValueAt(jTable1.getSelectedRow(), 4);//Chargé en soute
               String test2 = (String)dm.getValueAt(jTable1.getSelectedRow(), 3);//Receptionné
               if(test.equalsIgnoreCase("o") && test2.equalsIgnoreCase("n"))
               {
                    JOptionPane.showMessageDialog(this, "Erreur - ne peut être chargé en soute sans être réceptionné");
                    dm.setValueAt("N", jTable1.getSelectedRow(), 4);
                    ObjectOutputStream oos =null;
                    String str ="";
                    RequeteLUGAP req = null;
                    int row = jTable1.getSelectedRow();
                    int col = 4;
                    str = str + jTable1.getColumnName(0) + ";";
                    str = str + dm.getValueAt(row, 0) + ";";//id
                    str = str + jTable1.getColumnName(col) + ";";
                    str = str + dm.getValueAt(row,col);           
                    System.out.println("STR : "+ str);
                    req = new RequeteLUGAP(RequeteLUGAP.REQUEST_UPDATELUG, str);
                    try
                    {
                        oos= new ObjectOutputStream(cliSock.getOutputStream());
                        oos.writeObject(req); oos.flush();
                    }
                    catch (IOException e)
                    {
                        System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); 
                    }
                }
                for(int i = 3 ; i<6 ; i++)
                {
                    String str = (String)dm.getValueAt(jTable1.getSelectedRow(), i);
                    if(!str.equalsIgnoreCase("o") && !str.equalsIgnoreCase("n"))
                    {
                        if(i==4 && !str.equalsIgnoreCase("r"))
                        {
                          JOptionPane.showMessageDialog(this, "Erreur - ne peut être chargé en soute sans être réceptionné");
                          dm.setValueAt("N", jTable1.getSelectedRow(), i);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this, "Erreur - Caractere invalide");
                            dm.setValueAt("N", jTable1.getSelectedRow(), i);
                        }
                    }
                }
                ObjectOutputStream oos =null;
                String str ="";
                RequeteLUGAP req = null;
                int row = jTable1.getSelectedRow();
                int col = jTable1.getSelectedColumn();
                str = str + jTable1.getColumnName(0) + ";";
                str = str + dm.getValueAt(row, 0) + ";";//id
                str = str + jTable1.getColumnName(col) + ";";
                str = str + dm.getValueAt(row,col);           
                System.out.println("STR : "+ str);
                req = new RequeteLUGAP(RequeteLUGAP.REQUEST_UPDATELUG, str);
                try
                {
                    oos= new ObjectOutputStream(cliSock.getOutputStream());
                    oos.writeObject(req); oos.flush();
                }
                catch (IOException e)
                {
                    System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); 
                }
                //RechercheBagages(chaine);
            }
       }
    }//GEN-LAST:event_jTable1PropertyChange

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        DefaultTableModel dm= (DefaultTableModel)jTable1.getModel();
        String test;
        setToutEnSoute(true);
        int i;
        for(i = 0; i<jTable1.getRowCount();i++)
        {
             test = (String)dm.getValueAt(i, 4);//Chargé en soute
             if(test.equalsIgnoreCase("n"))
             {
                 setToutEnSoute(false);
                 i=jTable1.getRowCount();
                 JOptionPane.showMessageDialog(this, "Tous les bagages doivent être chargés en soute avant de pouvoir fermer la fenêtre");
             }
        }
        if(isToutEnSoute())
            this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfaceBagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfaceBagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfaceBagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfaceBagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InterfaceBagages dialog = new InterfaceBagages(new javax.swing.JFrame(), true,null,null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the toutEnSoute
     */
    public boolean isToutEnSoute() {
        return toutEnSoute;
    }

    /**
     * @param toutEnSoute the toutEnSoute to set
     */
    public void setToutEnSoute(boolean toutEnSoute) {
        this.toutEnSoute = toutEnSoute;
    }
}
