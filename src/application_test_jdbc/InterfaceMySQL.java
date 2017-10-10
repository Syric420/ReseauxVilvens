/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application_test_jdbc;
import database.utilities.BeanConnect;
import java.sql.*;
import java.util.Locale;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vince
 */
public class InterfaceMySQL extends javax.swing.JDialog {
    
    BeanConnect BeanConnect;

    public InterfaceMySQL(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        BeanConnect = new BeanConnect();
        BeanConnect.setTypeBD("MySql");
        BeanConnect.connect();
        //instruc=BeanConnect.getInstruc();      
}
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Envoyer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   
    String Table,buf,requete="",table="";
    boolean count = false;
    Table=jTextField1.getText();
    int i = 0;
    //selection de la requete
    if(Table.indexOf(" ",i) != 1)
    {
        requete=Table.substring(i, Table.indexOf(" ",i));
        i = Table.indexOf(" ",i)+1;
        do
        {   
            buf=Table.substring(i, Table.indexOf(" ",i));
            i = Table.indexOf(" ",i)+1;
            if(buf.equals("count(*)"))
                count = true;
                ;
            if(buf.equals("from"))
                break;
        }while(Table.indexOf(" ",i) != -1);
        //selection table
        if(Table.indexOf(" ",i) != -1)
            table=Table.substring(i,Table.indexOf(" ",i));
        else
            table=Table.substring(i);
    }
    else
        System.out.println("Erreur");
    DefaultTableModel jTableModel;
  
    try
    {
        if(requete.equalsIgnoreCase("update"))
        {
            BeanConnect.getInstruc().executeUpdate(jTextField1.getText()) ;
        }
        else
        {
            BeanConnect.setRs(BeanConnect.getInstruc().executeQuery(jTextField1.getText())) ;
            table=table.toLowerCase(Locale.FRANCE);
            switch(table)
            {    
            case "avion":
                IniTable("avion",count);
                jTableModel = (DefaultTableModel) jTable1.getModel();
                if(count)
                {
                    int countA;
                    BeanConnect.getRs().next();
                    countA=BeanConnect.getRs().getInt("count(*)");
                    Vector Temp = new Vector();
                    Temp.addElement(countA);
                    jTableModel.addRow(Temp);
                }
                else
                {
                    int idAvion,NbPlaces,PoidsMax;
                    String TypeAvion;
                    boolean Check_OK;
                    while (BeanConnect.getRs().next())
                    {
                        idAvion=BeanConnect.getRs().getInt("idAvion");
                        Check_OK=BeanConnect.getRs().getBoolean("Check_OK");
                        TypeAvion=BeanConnect.getRs().getString("TypeAvion");
                        NbPlaces=BeanConnect.getRs().getInt("NbPlaces");
                        PoidsMax=BeanConnect.getRs().getInt("PoidsMax");
                        Vector Temp = new Vector();
                        Temp.addElement(idAvion);
                        Temp.addElement(Check_OK);
                        Temp.addElement(TypeAvion);
                        Temp.addElement(NbPlaces);
                        Temp.addElement(PoidsMax);
                        jTableModel.addRow(Temp);
                    }
                }
                    break;
                case "agents":
                IniTable("agents",count);
                jTableModel = (DefaultTableModel) jTable1.getModel();
                if(count)
                {
                    int countA;
                    BeanConnect.getRs().next();
                    countA=BeanConnect.getRs().getInt("count(*)");
                    Vector Temp = new Vector();
                    Temp.addElement(countA);
                    jTableModel.addRow(Temp);
                }
                else
                {
                    int idAgents;
                    String Role;
                    while (BeanConnect.getRs().next())
                    {
                        idAgents=BeanConnect.getRs().getInt("idAgents");
                        Role=BeanConnect.getRs().getString("Role");
                        Vector Temp = new Vector();
                        Temp.addElement(idAgents);
                        Temp.addElement(Role);
                        jTableModel.addRow(Temp);
                    }
                }
                    break;
                case "bagages":
                IniTable("bagages",count);
                jTableModel = (DefaultTableModel) jTable1.getModel();
                if(count)
                {
                    int countA;
                    BeanConnect.getRs().next();
                    countA=BeanConnect.getRs().getInt("count(*)");
                    Vector Temp = new Vector();
                    Temp.addElement(countA);
                    jTableModel.addRow(Temp);
                }
                else
                {
                    int idBagages,Poids;
                    boolean Valise;
                    while (BeanConnect.getRs().next())
                    {
                        idBagages=BeanConnect.getRs().getInt("idBagages");
                        Valise=BeanConnect.getRs().getBoolean("Valise");
                        Poids=BeanConnect.getRs().getInt("Poids");
                        Vector Temp = new Vector();
                        Temp.addElement(idBagages);
                        Temp.addElement(Valise);
                        Temp.addElement(Poids);
                        jTableModel.addRow(Temp);
                    }
                }
                    break;
                case "billets":
                IniTable("billets",count);
                jTableModel = (DefaultTableModel) jTable1.getModel();
                if(count)
                {
                    int countA;
                    BeanConnect.getRs().next();
                    countA=BeanConnect.getRs().getInt("count(*)");
                    Vector Temp = new Vector();
                    Temp.addElement(countA);
                    jTableModel.addRow(Temp);
                }
                else
                {
                    int idBillets;
                    String Nom,Prenom,Num_id;
                    while (BeanConnect.getRs().next())
                    {
                        idBillets=BeanConnect.getRs().getInt("idBillets");
                        Nom=BeanConnect.getRs().getString("Nom");
                        Prenom=BeanConnect.getRs().getString("Prenom");
                        Num_id=BeanConnect.getRs().getString("Num_id");
                        Vector Temp = new Vector();
                        Temp.addElement(idBillets);
                        Temp.addElement(Nom);
                        Temp.addElement(Prenom);
                        Temp.addElement(Num_id);
                        jTableModel.addRow(Temp);
                    }
                }
                    break;
                case "vols":
                IniTable("vols",count);
                jTableModel = (DefaultTableModel) jTable1.getModel();
                if(count)
                {
                    int countA;
                    BeanConnect.getRs().next();
                    countA=BeanConnect.getRs().getInt("count(*)");
                    Vector Temp = new Vector();
                    Temp.addElement(countA);
                    jTableModel.addRow(Temp);
                }
                else
                {
                    int idVols,AvionUtilise;
                    String Destination,HeureArrivee,HeureDepart;
                    while (BeanConnect.getRs().next())
                    {
                        idVols=BeanConnect.getRs().getInt("idVols");
                        Destination=BeanConnect.getRs().getString("Destination");
                        HeureArrivee=BeanConnect.getRs().getString("HeureArrivee");
                        HeureDepart=BeanConnect.getRs().getString("HeureDepart");
                        AvionUtilise=BeanConnect.getRs().getInt("AvionUtilise");
                        Vector Temp = new Vector();
                        Temp.addElement(idVols);
                        Temp.addElement(Destination);
                        Temp.addElement(HeureArrivee);
                        Temp.addElement(HeureDepart);
                        Temp.addElement(AvionUtilise);
                        jTableModel.addRow(Temp);
                    }
                }
                    break;
            }
        }
        }
    catch (SQLException e) { System.out.println("Erreur SQL : " + e.getMessage()); }
    
    
    }//GEN-LAST:event_jButton1ActionPerformed

    private void IniTable(String Table,boolean count)
    {
        switch(Table)
        {
            case "avion":
                if(count)
                jTable1.setModel(new javax.swing.table.DefaultTableModel
                (
                    new Object [][] {
                    },
                    new String [] {
                        "Nombre de tuples d'avion"
                    }
                ));
                else  
                jTable1.setModel(new javax.swing.table.DefaultTableModel
                (
                    new Object [][] {
                    },
                    new String [] {
                        "idAvion", "Check_OK", "TypeAvion", "NbPlaces","Poids Max"
                    }
                ));
                break;
            case "agents":
                if(count)
                jTable1.setModel(new javax.swing.table.DefaultTableModel
                (
                    new Object [][] {
                    },
                    new String [] {
                        "Nombre de tuples d'agents"
                    }
                ));
                else 
                jTable1.setModel(new javax.swing.table.DefaultTableModel
                (
                    new Object [][] {
                    },
                    new String [] {
                        "idAgents", "Role"
                    }
                ));
                break;
            case "bagages":
                if(count)
                jTable1.setModel(new javax.swing.table.DefaultTableModel
                (
                    new Object [][] {
                    },
                    new String [] {
                        "Nombre de tuples de bagages"
                    }
                ));
                else 
                jTable1.setModel(new javax.swing.table.DefaultTableModel
                (
                    new Object [][] {
                    },
                    new String [] {
                        "idBagages", "Valise", "Poids"
                    }
                ));
                break;
            case "billets":
                if(count)
                jTable1.setModel(new javax.swing.table.DefaultTableModel
                (
                    new Object [][] {
                    },
                    new String [] {
                        "Nombre de tuples de billets"
                    }
                ));
                else 
                jTable1.setModel(new javax.swing.table.DefaultTableModel
                (
                    new Object [][] {
                    },
                    new String [] {
                        "idBillets", "Nom", "Prenom","Num_id"
                    }
                ));
                break;
            case "vols":
                if(count)
                jTable1.setModel(new javax.swing.table.DefaultTableModel
                (
                    new Object [][] {
                    },
                    new String [] {
                        "Nombre de tuples de vols"
                    }
                ));
                else 
                jTable1.setModel(new javax.swing.table.DefaultTableModel
                (
                    new Object [][] {
                    },
                    new String [] {
                        "idVols", "Destination", "HeureArrivee","HeureDepart","AvionUtilise"
                    }
                ));
                break;
        }
    }
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
            java.util.logging.Logger.getLogger(InterfaceMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfaceMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfaceMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfaceMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InterfaceMySQL dialog = new InterfaceMySQL(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
