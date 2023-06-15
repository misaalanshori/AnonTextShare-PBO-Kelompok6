/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pw.misa.kk6.views;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import pw.misa.kk6.controllers.DocumentController;

/**
 *
 * @author ASUS
 */
public class MenuLoadDoc extends javax.swing.JFrame {

    DocumentController dc;
    /**
     * Creates new form MenuLoadDoc
     * @param controller
     */
    public MenuLoadDoc(DocumentController controller) {
        initComponents();
        this.dc = controller;
    }

    public JLabel getDokumen() {
        return Dokumen;
    }

    public JButton getHapus() {
        return Hapus;
    }

    public JTextField getIsi() {
        return Isi;
    }

    public JTextField getIsiDokumen() {
        return IsiDokumen;
    }

    public JLabel getIsiTotalAkses() {
        return IsiTotalAkses;
    }

    public JTextField getJudul() {
        return Judul;
    }

    public JButton getKembali() {
        return Kembali;
    }

    public JButton getKirim() {
        return Kirim;
    }

    public JTextField getNama() {
        return Nama;
    }

    public JButton getPerbarui() {
        return Perbarui;
    }

    public JRadioButton getPublic() {
        return Public;
    }

    public JScrollPane getScroolListKomen() {
        return ScroolListKomen;
    }

    public JLabel getTotalAkses() {
        return TotalAkses;
    }

    public JRadioButton getUnlisted() {
        return Unlisted;
    }

    public ButtonGroup getButtonGroup1() {
        return buttonGroup1;
    }

    public JList<String> getjList1() {
        return jList1;
    }

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        Dokumen = new javax.swing.JLabel();
        Hapus = new javax.swing.JButton();
        Kembali = new javax.swing.JButton();
        Judul = new javax.swing.JTextField();
        Perbarui = new javax.swing.JButton();
        TotalAkses = new javax.swing.JLabel();
        IsiTotalAkses = new javax.swing.JLabel();
        Nama = new javax.swing.JTextField();
        Isi = new javax.swing.JTextField();
        Kirim = new javax.swing.JButton();
        IsiDokumen = new javax.swing.JTextField();
        ScroolListKomen = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        Unlisted = new javax.swing.JRadioButton();
        Public = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Dokumen.setText("Dokumen :");
        getContentPane().add(Dokumen, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 9, -1, -1));

        Hapus.setText("Hapus");
        Hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HapusActionPerformed(evt);
            }
        });
        getContentPane().add(Hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(337, 41, 237, -1));

        Kembali.setText("Kembali");
        Kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KembaliActionPerformed(evt);
            }
        });
        getContentPane().add(Kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(337, 6, 237, -1));

        Judul.setText("Judul");
        getContentPane().add(Judul, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 6, 134, -1));

        Perbarui.setText("perbarui");
        Perbarui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PerbaruiActionPerformed(evt);
            }
        });
        getContentPane().add(Perbarui, new org.netbeans.lib.awtextra.AbsoluteConstraints(227, 6, 89, -1));

        TotalAkses.setText("Total Akses :");
        getContentPane().add(TotalAkses, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 44, -1, -1));

        IsiTotalAkses.setText("IsiTotalAkses");
        getContentPane().add(IsiTotalAkses, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 44, -1, -1));

        Nama.setText("Nama ");
        getContentPane().add(Nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(337, 234, 141, -1));

        Isi.setText("Isi");
        getContentPane().add(Isi, new org.netbeans.lib.awtextra.AbsoluteConstraints(334, 265, 240, -1));

        Kirim.setText("Kirim");
        Kirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KirimActionPerformed(evt);
            }
        });
        getContentPane().add(Kirim, new org.netbeans.lib.awtextra.AbsoluteConstraints(496, 234, 78, -1));

        IsiDokumen.setText("Isi");
        getContentPane().add(IsiDokumen, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 76, 310, 211));

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        ScroolListKomen.setViewportView(jList1);

        getContentPane().add(ScroolListKomen, new org.netbeans.lib.awtextra.AbsoluteConstraints(337, 76, 237, -1));

        buttonGroup1.add(Unlisted);
        Unlisted.setText("Unlisted");
        getContentPane().add(Unlisted, new org.netbeans.lib.awtextra.AbsoluteConstraints(183, 42, -1, -1));

        buttonGroup1.add(Public);
        Public.setText("Public");
        getContentPane().add(Public, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 42, -1, -1));

        jPanel1.setBackground(new java.awt.Color(0, 204, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HapusActionPerformed
        dc.deleteDocument();
    }//GEN-LAST:event_HapusActionPerformed

    private void KembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KembaliActionPerformed
        dc.kembali();
//        MainMenu frame2 = new MainMenu();  // Membuat instance frame sebelumnya
//        frame2.setVisible(true);
    }//GEN-LAST:event_KembaliActionPerformed

    private void PerbaruiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PerbaruiActionPerformed
        dc.updateDocument();
    }//GEN-LAST:event_PerbaruiActionPerformed

    private void KirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KirimActionPerformed
        dc.insertComment();
        dc.isiComment();
    }//GEN-LAST:event_KirimActionPerformed

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
            java.util.logging.Logger.getLogger(MenuLoadDoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuLoadDoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuLoadDoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuLoadDoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Dokumen;
    private javax.swing.JButton Hapus;
    private javax.swing.JTextField Isi;
    private javax.swing.JTextField IsiDokumen;
    private javax.swing.JLabel IsiTotalAkses;
    private javax.swing.JTextField Judul;
    private javax.swing.JButton Kembali;
    private javax.swing.JButton Kirim;
    private javax.swing.JTextField Nama;
    private javax.swing.JButton Perbarui;
    private javax.swing.JRadioButton Public;
    private javax.swing.JScrollPane ScroolListKomen;
    private javax.swing.JLabel TotalAkses;
    private javax.swing.JRadioButton Unlisted;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}