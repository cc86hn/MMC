/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client.jukebox;

import de.nplusc.izc.tools.baseTools.Messagers;
import java.io.File;
import jnafilechooser.api.WindowsFolderBrowser;
import org.cc86.MMC.client.Menu;
import org.cc86.MMC.client.Mod_Jukebox;

/**
 *
 * @author tgoerner
 */
public class JukeBoxPoolManagment extends javax.swing.JPanel
{
    private Mod_Jukebox jbx;
    /**
     * Creates new form JukeBoxPoolManagment
     * @param x instance of the underlying mod_jukebox classs
     */
    public JukeBoxPoolManagment(Mod_Jukebox x)
    {
        jbx=x;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnAddFolder = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstFolders = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();

        btnAddFolder.setText("Ordner hinzufügen");
        btnAddFolder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddFolderActionPerformed(evt);
            }
        });

        lstFolders.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstFolders);

        jButton1.setText("Ordner entfernen");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAddFolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(111, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddFolder)
                .addGap(29, 29, 29)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddFolderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddFolderActionPerformed
    {//GEN-HEADEREND:event_btnAddFolderActionPerformed
         new Thread(()->{
            File fldr = new WindowsFolderBrowser().showDialog(Menu.getMenu());
            Messagers.SingleLineMsg(fldr.getAbsolutePath(),"DebugOut");
        }).start();
    }//GEN-LAST:event_btnAddFolderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddFolder;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstFolders;
    // End of variables declaration//GEN-END:variables
}
