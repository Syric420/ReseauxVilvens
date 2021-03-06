/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientServeurSocket;

import PAYP.RequetePAYP;
import TICKMAP.RequeteTICKMAP;
import Utilities.Encryption;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import java.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author tibha
 */
public class ReservationVols extends javax.swing.JDialog {

    /**
     * Creates new form Reservation
     */
    private String idVols;
    private java.awt.Frame Parent;
    private String message;
    private int prix;
    String idVolsReserve;
    public ReservationVols(java.awt.Frame parent, boolean modal,String str) {
        super(parent, modal);
        initComponents();
        idVols = str;
        Parent = parent;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonReservation = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldNom = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jTextFieldPrenom = new javax.swing.JTextField();
        jTextFieldCB = new javax.swing.JTextField();
        jTextFieldPasseport = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBoxAcc = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaReponse = new javax.swing.JTextArea();
        jButtonAccept = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButtonReservation.setText("Reservation");
        jButtonReservation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReservationActionPerformed(evt);
            }
        });

        jLabel1.setText("Nombre d'accompagnants");

        jLabel2.setText("Nom : ");

        jLabel6.setText("Prenom : ");

        jLabel7.setText("Carte Bancaire : ");

        jLabel8.setText("Passeport : ");

        jComboBoxAcc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4" }));

        jTextAreaReponse.setEditable(false);
        jTextAreaReponse.setColumns(20);
        jTextAreaReponse.setRows(5);
        jScrollPane1.setViewportView(jTextAreaReponse);

        jButtonAccept.setText("Payer");
        jButtonAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAcceptActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel6))
                                        .addGap(59, 59, 59)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldCB, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldPasseport, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldNom, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel1)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(179, 179, 179)
                                .addComponent(jButtonReservation)))
                        .addGap(86, 86, 86))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jComboBoxAcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonAccept)
                        .addGap(196, 196, 196)))
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(127, 127, 127)
                                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jTextFieldNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jTextFieldPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jTextFieldCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldPasseport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxAcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addGap(18, 18, 18)
                .addComponent(jButtonAccept)
                .addContainerGap(49, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonReservation)
                .addGap(33, 33, 33))
        );

        setBounds(0, 0, 977, 359);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonReservationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReservationActionPerformed
        try {
            String str = (String)jComboBoxAcc.getSelectedItem();
            int nbrPassager = Integer.parseInt(str);
            message = "";
            message = message + idVols + "@@";
            message = message + ((InterfaceClient)Parent).Login + "@@";
            message = message + jTextFieldNom.getText() + ";";
            message = message + jTextFieldPrenom.getText() + ";";
            message = message + jTextFieldCB.getText() + ";";
            message = message + jTextFieldPasseport.getText() + "@@";
            for(int i=0;i<nbrPassager;i++)
            {
                PassagerSupp Pass = new PassagerSupp(this,true);        
                Pass.setVisible(true);
            }
            System.out.println(message);
            byte[]reqCrypt = Encryption.encryptDES(((InterfaceClient)Parent).getKeyCipher(), message);
            RequeteTICKMAP req = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_BUYTICKETS , reqCrypt);
            ObjectOutputStream oos =null;
            try
            {
                oos= new ObjectOutputStream(((InterfaceClient)Parent).cliSock.getOutputStream());
                oos.writeObject(req); oos.flush();
            }
            catch (IOException e)
            {
                System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); 
            }
        ObjectInputStream ois = null;
        int [] placesReservees;
        
        try
        {
            ois = new ObjectInputStream(((InterfaceClient)Parent).cliSock.getInputStream());
            
            byte[] tmp = ((Encryption)ois.readObject()).getMessage();
            byte[] messageClair=Encryption.decryptDES(((InterfaceClient)Parent).getKeyCipher(), tmp); 
            String var =(String)Encryption.convertFromBytes(messageClair);
            System.out.println("Message recu : " + var);
            message = var;
            String [] temp1, temp2;
            temp1 = var.split("@");
            prix = Integer.parseInt(temp1[1]);
            idVolsReserve = temp1[3];
            temp2 = temp1[0].split(";");
            placesReservees = new int[temp2.length];
            jTextAreaReponse.setText("Voici les numeros de sieges attribués :");
            for(int i =0;i<temp2.length;i++)
            {
                placesReservees[i] = Integer.parseInt(temp2[i]);
                if(i == temp2.length -1)
                    jTextAreaReponse.append(temp2[i]);
                else
                    jTextAreaReponse.append(temp2[i] +", ");
            }
            jTextAreaReponse.append("\n Pour un prix total de : " + temp1[1]);
            System.out.println(placesReservees[0] + " " +  prix);
        } catch (IOException ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        } catch (Exception ex) {
            Logger.getLogger(ReservationVols.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButtonReservationActionPerformed

    private void jButtonAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAcceptActionPerformed
        Socket cliSockPay = null;
        try {
            Mac hmac = Mac.getInstance("HMAC-MD5", "BC");
            hmac.init(((InterfaceClient)Parent).getKeyHmac());
            System.out.println("Hachage du message");
            byte[] msg = Encryption.convertToBytes(message);
            hmac.update(msg);
            System.out.println("Generation des bytes");
            byte[] hb = hmac.doFinal();
            ObjectOutputStream oos =null;
            try
            {
                oos= new ObjectOutputStream(((InterfaceClient)Parent).cliSock.getOutputStream());
                oos.writeObject(hb); oos.flush();
            }
            catch (IOException e)
            {
                System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); 
            }
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ReservationVols.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ReservationVols.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ReservationVols.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReservationVols.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        try {
            Security.addProvider(new BouncyCastleProvider());
            cliSockPay = new Socket(((InterfaceClient)Parent).IP_ADDRESS, ((InterfaceClient)Parent).PORT_PAYMENT);
            System.out.println(((InterfaceClient)Parent).IP_ADDRESS + " " + ((InterfaceClient)Parent).PORT_PAYMENT);
            ObjectOutputStream oos =null;
            String message = jTextFieldCB.getText() + "@" + ((InterfaceClient)Parent).Login + "@" + prix + "@" + idVolsReserve + "@CONFIRMED";
            byte[] str = Encryption.convertToBytes(message);
            byte[]reqCrypt = Encryption.encryptRSA(((InterfaceClient)Parent).getCléPubliquePayment(), message);
            RequetePAYP pay = new RequetePAYP(RequetePAYP.REQUEST_PAY,reqCrypt);
            
            Signature s = Signature. getInstance("SHA1withRSA","BC");
            System.out.println("Initialisation de la signature");
            s.initSign(((InterfaceClient)Parent).getCléPrivéeOperator());
            System.out.println("Hachage du message");
            s.update(str);
            System.out.println("Generation des bytes");
            byte[] signature = s.sign();            
            pay.setSignature(signature);
            
            oos= new ObjectOutputStream(cliSockPay.getOutputStream());
            oos.writeObject(pay); oos.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(ReservationVols.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReservationVols.class.getName()).log(Level.SEVERE, null, ex);
        }


        
    }//GEN-LAST:event_jButtonAcceptActionPerformed

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
            java.util.logging.Logger.getLogger(ReservationVols.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReservationVols.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReservationVols.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReservationVols.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ReservationVols dialog = new ReservationVols(new javax.swing.JFrame(), true,"0");
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
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButtonAccept;
    private javax.swing.JButton jButtonReservation;
    private javax.swing.JComboBox<String> jComboBoxAcc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaReponse;
    private javax.swing.JTextField jTextFieldCB;
    private javax.swing.JTextField jTextFieldNom;
    private javax.swing.JTextField jTextFieldPasseport;
    private javax.swing.JTextField jTextFieldPrenom;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = this.message + message;
    }
}
