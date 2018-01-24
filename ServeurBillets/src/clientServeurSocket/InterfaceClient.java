/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientServeurSocket;
import ServerPayment.ThreadClientPay;
import TICKMAP.ReponseTICKMAP;
import TICKMAP.RequeteTICKMAP;
import java.io.*;
import java.net.*;
import Utilities.ReadProperties;
import static java.lang.System.exit;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.swing.table.DefaultTableModel;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
public class InterfaceClient extends javax.swing.JFrame {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public Socket cliSock;
    public String Login;
    private PublicKey cléPubliqueServer;
    private X509Certificate certifPay;
    private PublicKey cléPubliquePayment;
    private X509Certificate certifOperator;
    private PublicKey cléPubliqueOperator;
    private PrivateKey cléPrivéeOperator;
    private SecretKey keyHmac;
    private SecretKey keyCipher;
    int PORT_CHECKIN;
    int PORT_PAYMENT;
    String IP_ADDRESS;
    InterfaceConnexion InterfaceCo;

     
    public InterfaceClient() {
   
        initComponents();
        Conf();
        InterfaceCo = new InterfaceConnexion(this, true,cliSock);
        
        InterfaceCo.setVisible(true);
        if(!InterfaceCo.isLogged())
            exit(0);
        else
        {
            System.out.println("Test");
            ReponseTICKMAP rep = null;
            try
            {
                ois = new ObjectInputStream(cliSock.getInputStream());
                rep = (ReponseTICKMAP)ois.readObject();
            }
            catch (ClassNotFoundException e)
            { 
                System.out.println("--- erreur sur la classe = " + e.getMessage()); 
            } catch (IOException ex) {
                Logger.getLogger(InterfaceClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            iniTable(rep.getChargeUtile());
        }
        
    }
    
        private void Conf()
    {
        ReadProperties rP ;
        try {
            rP = new ReadProperties("/clientServeurSocket/Config.properties");
            IP_ADDRESS = rP.getProp("IP_ADDRESS");
            PORT_CHECKIN = Integer.parseInt(rP.getProp("PORT_CHECKIN"));
            PORT_PAYMENT = Integer.parseInt(rP.getProp("PORT_PAYMENT"));
        } catch (IOException ex) {
            Logger.getLogger(InterfaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Security.addProvider(new BouncyCastleProvider());

        ois=null; oos=null; cliSock = null;
        try
        {
            cliSock = new Socket(IP_ADDRESS, PORT_CHECKIN);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e)
        { System.err.println("Erreur ! Host non trouvé [" + e + "]"); }
        catch (IOException e)
        { System.err.println("Erreur ! Pas de connexion ? [" + e + "]"); }
    
        
        KeyStore ks;
        try {
            Security.addProvider(new BouncyCastleProvider());
            InputStream input = null;
            ks = KeyStore.getInstance("JCEKS");
            input = this.getClass().getResourceAsStream("/Cles/ClesLabo.jceks");
            ks.load(input,"123".toCharArray());
            certifPay = (X509Certificate)ks.getCertificate("serveur_payment");
            cléPubliquePayment = getCertifPay().getPublicKey(); 
            
            
            certifOperator = (X509Certificate)ks.getCertificate("tour_operator");
            cléPubliqueOperator = certifOperator.getPublicKey();
            cléPrivéeOperator = (PrivateKey) ks.getKey("serveur_payment", "123".toCharArray());
            /*System.out.println("*** Cle publique recuperee = "+cléPublique.toString());
            System.out.println(" *** Cle privee recuperee = " + cléPrivée.toString());*/

        } catch (KeyStoreException ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ThreadClientPay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        JLabel_Etat = new javax.swing.JLabel();
        JB_Connecter = new javax.swing.JButton();
        JB_Deconnecter = new javax.swing.JButton();
        jButtonReserve = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("Etat du serveur:");

        JLabel_Etat.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        JLabel_Etat.setText("CONNECTE");

        JB_Connecter.setText("Se connecter");
        JB_Connecter.setEnabled(false);
        JB_Connecter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JB_ConnecterActionPerformed(evt);
            }
        });

        JB_Deconnecter.setText("Se déconnecter");
        JB_Deconnecter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JB_DeconnecterActionPerformed(evt);
            }
        });

        jButtonReserve.setText("Reserver");
        jButtonReserve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReserveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JLabel_Etat))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(JB_Connecter)
                                .addGap(106, 106, 106)
                                .addComponent(JB_Deconnecter))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(308, 308, 308)
                        .addComponent(jButtonReserve)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JLabel_Etat))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JB_Connecter)
                    .addComponent(JB_Deconnecter))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonReserve)
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        String chargeUtile;
        chargeUtile = "InterfaceClient avec socket ="+cliSock.toString()+" se déconnecte";
        RequeteTICKMAP req = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_DECONNECT, chargeUtile);;
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }//GEN-LAST:event_formWindowClosing

    private void JB_DeconnecterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JB_DeconnecterActionPerformed
        String chargeUtile;
        chargeUtile = "InterfaceClient avec socket ="+cliSock.toString()+" se déconnecte";
        RequeteTICKMAP req = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_DECONNECT, chargeUtile);;
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); oos.flush();

        } catch (IOException ex) {
            Logger.getLogger(InterfaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        JB_Connecter.setEnabled(true);
        JB_Deconnecter.setEnabled(false);
        JLabel_Etat.setText("Déconnecté");

        iniTable("");
    }//GEN-LAST:event_JB_DeconnecterActionPerformed

    private void JB_ConnecterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JB_ConnecterActionPerformed
        ois=null; oos=null; cliSock = null;
        try
        {
            cliSock = new Socket(IP_ADDRESS, PORT_CHECKIN);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e)
        { System.err.println("Erreur ! Host non trouvé [" + e + "]"); }
        catch (IOException e)
        { System.err.println("Erreur ! Pas de connexion ? [" + e + "]"); }

        JB_Connecter.setEnabled(false);
        JB_Deconnecter.setEnabled(true);
        JLabel_Etat.setText("Connecté");
        
    }//GEN-LAST:event_JB_ConnecterActionPerformed

    private void jButtonReserveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReserveActionPerformed
        int i =jTable1.getSelectedRow();
        String idVols = jTable1.getValueAt(i, 0).toString();
        ReservationVols res = new ReservationVols(this,true,idVols);
        res.setVisible(true);
    }//GEN-LAST:event_jButtonReserveActionPerformed
    
    private void iniTable(String tab)
    {
        String nomTable[] ={},var[] = {},tuples[];
        if(!tab.equals(""))
        {
            var=tab.split("@");
            nomTable=var[0].split(";");
            jTable1.setModel(new javax.swing.table.DefaultTableModel
                        (
                                new Object [][] {
                                },
                                nomTable

                        )
            {

                @Override
                public boolean isCellEditable(int row, int column) {
                   return column == 0 || column== 1 || column== 2 || column==3 || column==4 || column==5? false : true;
                }
            });
            DefaultTableModel dm= (DefaultTableModel)jTable1.getModel();
            for(int i=1; i <var.length ; i++)
            {
                tuples= var[i].split(";");
                dm.addRow(tuples);
            }
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
            java.util.logging.Logger.getLogger(InterfaceClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfaceClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfaceClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfaceClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfaceClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JB_Connecter;
    private javax.swing.JButton JB_Deconnecter;
    private javax.swing.JLabel JLabel_Etat;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonReserve;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the cléPublique
     */
    public PublicKey getCléPublique() {
        return getCléPubliqueServer();
    }

    /**
     * @param cléPubliqueServer the cléPubliqueServer to set
     */
    public void setCléPublique(PublicKey cléPubliqueServer) {
        this.cléPubliqueServer = cléPubliqueServer;
    }

    /**
     * @return the keyHmac
     */
    public SecretKey getKeyHmac() {
        return keyHmac;
    }

    /**
     * @param keyHmac the keyHmac to set
     */
    public void setKeyHmac(SecretKey keyHmac) {
        this.keyHmac = keyHmac;
    }

    /**
     * @return the keyCipher
     */
    public SecretKey getKeyCipher() {
        return keyCipher;
    }

    /**
     * @param keyCipher the keyCipher to set
     */
    public void setKeyCipher(SecretKey keyCipher) {
        this.keyCipher = keyCipher;
    }

    /**
     * @return the cléPubliquePayment
     */
    public PublicKey getCléPubliquePayment() {
        return cléPubliquePayment;
    }

    /**
     * @return the cléPubliqueServer
     */
    public PublicKey getCléPubliqueServer() {
        return cléPubliqueServer;
    }

    /**
     * @return the certifPay
     */
    public X509Certificate getCertifPay() {
        return certifPay;
    }

    /**
     * @return the cléPubliqueOperator
     */
    public PublicKey getCléPubliqueOperator() {
        return cléPubliqueOperator;
    }

    /**
     * @return the cléPrivéeOperator
     */
    public PrivateKey getCléPrivéeOperator() {
        return cléPrivéeOperator;
    }
}
