/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import de.nplusc.izc.tools.baseTools.Messagers;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.client.API.Module;

/**
 *
 * @author tgoerner
 */
public class DemoUI extends javax.swing.JFrame implements StereoUIApi
{
 private static final Logger l = LogManager.getLogger();

    public DemoUI()
    {
        initComponents();
        //btnMinus.setVisible(false);
        //btnPlus.setVisible(false);
        //sourcesList.setVisible(false);
    }

    private static DemoUI m = new DemoUI();

    public static void bootUI()
    {
        Main.getDispatcher().startUIs(true);
        m.setVisible(true);
        m.addWindowListener( new WindowAdapter() {

            @Override
            public void windowIconified(WindowEvent we) {
                m.setState(JFrame.NORMAL);
            }
        });
        if(!Main.isDevmode())
        {
            m.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
        //l.trace("Ach LEgg mi doch am arsch");
        //m.pGeneral.add()
    }
    public static  DemoUI getUI()
    {
        
        return m;
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

        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnPower = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        volumeSlider = new javax.swing.JSlider();
        btnPlus = new javax.swing.JButton();
        btnMinus = new javax.swing.JButton();
        lblEgg = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        outLabel = new javax.swing.JLabel();
        speakersA = new javax.swing.JButton();
        speakersAB = new javax.swing.JButton();
        speakersB = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        cbxSourceSel = new javax.swing.JComboBox();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        stopPlayback = new javax.swing.JButton();
        highwayToHellPlayback = new javax.swing.JButton();
        shutUpAndDrivePlayback = new javax.swing.JButton();
        sugarRushPlayback = new javax.swing.JButton();
        track4 = new javax.swing.JButton();
        track5 = new javax.swing.JButton();

        jLabel2.setText("jLabel2");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setMaximumSize(new java.awt.Dimension(500, 32767));
        jPanel1.setMinimumSize(new java.awt.Dimension(500, 0));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("An/Aus-Status");

        btnPower.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnPower.setText("Unbekannt");
        btnPower.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPowerActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Eingang");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Lautstärke");

        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setEnabled(false);

        btnPlus.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        btnPlus.setText("+");
        btnPlus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPlusActionPerformed(evt);
            }
        });

        btnMinus.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        btnMinus.setText("-");
        btnMinus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMinusActionPerformed(evt);
            }
        });

        lblEgg.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lblEgg.setText("π");
        lblEgg.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                lblEggMousePressed(evt);
            }
        });

        outLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        outLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        outLabel.setText("Gewählt: UNBEKANNT");

        speakersA.setText("Raum A");
        speakersA.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                speakersAActionPerformed(evt);
            }
        });

        speakersAB.setText("Raum A& B");
        speakersAB.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                speakersABActionPerformed(evt);
            }
        });

        speakersB.setText("Raum B");
        speakersB.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                speakersBActionPerformed(evt);
            }
        });

        cbxSourceSel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tuner", "CD", "Tape", "Phono","Ext" }));
        cbxSourceSel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cbxSourceSelActionPerformed(evt);
            }
        });

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setText("Musik-Wahl");

        stopPlayback.setText("STOP");
        stopPlayback.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                stopPlaybackActionPerformed(evt);
            }
        });

        highwayToHellPlayback.setText("Highway To Hell");
        highwayToHellPlayback.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                highwayToHellPlaybackActionPerformed(evt);
            }
        });

        shutUpAndDrivePlayback.setText("Shut up and Drive");
        shutUpAndDrivePlayback.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                shutUpAndDrivePlaybackActionPerformed(evt);
            }
        });

        sugarRushPlayback.setText("Sugar Rush");
        sugarRushPlayback.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                sugarRushPlaybackActionPerformed(evt);
            }
        });

        track4.setText("<RESERVED>");
        track4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                track4ActionPerformed(evt);
            }
        });

        track5.setText("<RESERVED>");
        track5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                track5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(outLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(volumeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnMinus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(speakersA, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(speakersAB, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(speakersB, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnPlus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(21, 21, 21))
                            .addComponent(lblEgg, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(cbxSourceSel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(5, 5, 5)))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnPower, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)))))
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(highwayToHellPlayback, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(track4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sugarRushPlayback, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(shutUpAndDrivePlayback, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(stopPlayback, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(track5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)))
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(highwayToHellPlayback, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(shutUpAndDrivePlayback, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sugarRushPlayback, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(track4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(track5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(126, 126, 126)
                        .addComponent(stopPlayback, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(btnPower)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(11, 11, 11)
                        .addComponent(outLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxSourceSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel1)
                        .addGap(0, 0, 0)
                        .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPlus, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(speakersA, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(speakersAB, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(speakersB, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblEgg))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator3)
                        .addContainerGap())))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMinusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMinusActionPerformed
    {//GEN-HEADEREND:event_btnMinusActionPerformed
        setVolume("DOWN");
    }//GEN-LAST:event_btnMinusActionPerformed

    private void lblEggMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblEggMousePressed
    {//GEN-HEADEREND:event_lblEggMousePressed
        Messagers.SingleLineMsg("Kein Zugang", "Okay");
    }//GEN-LAST:event_lblEggMousePressed

    private void speakersAActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_speakersAActionPerformed
    {//GEN-HEADEREND:event_speakersAActionPerformed
        setSpeakers("AL","AR");
    }//GEN-LAST:event_speakersAActionPerformed

    private void speakersABActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_speakersABActionPerformed
    {//GEN-HEADEREND:event_speakersABActionPerformed
        setSpeakers("AL","BR");
    }//GEN-LAST:event_speakersABActionPerformed

    private void speakersBActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_speakersBActionPerformed
    {//GEN-HEADEREND:event_speakersBActionPerformed
        setSpeakers("BL","BR");
    }//GEN-LAST:event_speakersBActionPerformed

    private void btnPlusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPlusActionPerformed
    {//GEN-HEADEREND:event_btnPlusActionPerformed
        setVolume("UP");
    }//GEN-LAST:event_btnPlusActionPerformed

    private void btnPowerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPowerActionPerformed
    {//GEN-HEADEREND:event_btnPowerActionPerformed
        changePowerState(btnPower.isSelected());
        btnPower.setSelected(!btnPower.isSelected());
    }//GEN-LAST:event_btnPowerActionPerformed

    private void cbxSourceSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSourceSelActionPerformed
        int src = cbxSourceSel.getSelectedIndex();
        updateSource(Sources.values()[src]);
    }//GEN-LAST:event_cbxSourceSelActionPerformed

    private void highwayToHellPlaybackActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_highwayToHellPlaybackActionPerformed
    {//GEN-HEADEREND:event_highwayToHellPlaybackActionPerformed
        playTrack("DEMO/HIGHWAYTOHELL");

    }//GEN-LAST:event_highwayToHellPlaybackActionPerformed

    private void shutUpAndDrivePlaybackActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_shutUpAndDrivePlaybackActionPerformed
    {//GEN-HEADEREND:event_shutUpAndDrivePlaybackActionPerformed
        playTrack("DEMO/SHUTUPANDDRIVE");
        
    }//GEN-LAST:event_shutUpAndDrivePlaybackActionPerformed

    private void sugarRushPlaybackActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sugarRushPlaybackActionPerformed
    {//GEN-HEADEREND:event_sugarRushPlaybackActionPerformed
        playTrack("DEMO/SUGARRUSH");
    }//GEN-LAST:event_sugarRushPlaybackActionPerformed

    private void track4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_track4ActionPerformed
    {//GEN-HEADEREND:event_track4ActionPerformed
        playTrack("DEMO/SUGARRUSH");
    }//GEN-LAST:event_track4ActionPerformed

    private void track5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_track5ActionPerformed
    {//GEN-HEADEREND:event_track5ActionPerformed
        playTrack("DEMO/SUGARRUSH");
    }//GEN-LAST:event_track5ActionPerformed

    private void stopPlaybackActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_stopPlaybackActionPerformed
    {//GEN-HEADEREND:event_stopPlaybackActionPerformed
        playTrack("DEMO/NONE");
    }//GEN-LAST:event_stopPlaybackActionPerformed

    private void playTrack(String trackID)
    {
        Mod_Jukebox jukebox = getJukebox();
        if(jukebox!=null)
        {
            jukebox.snedTrackPlaybackRequest(trackID, true);
        }
        else
        {
            l.error("Should not happen, undefined jukebox module");
        }
    }
    private Mod_Jukebox getJukebox()
    {
        for(Module module:Main.getDispatcher().getModules())
        {
            if(module instanceof Mod_Jukebox)
            {
                return (Mod_Jukebox) module;
            }
        }
        return null;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(DemoUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(DemoUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(DemoUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(DemoUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() ->
        {
            new DemoUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMinus;
    private javax.swing.JButton btnPlus;
    private javax.swing.JToggleButton btnPower;
    private javax.swing.JComboBox cbxSourceSel;
    private javax.swing.JButton highwayToHellPlayback;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblEgg;
    private javax.swing.JLabel outLabel;
    private javax.swing.JButton shutUpAndDrivePlayback;
    private javax.swing.JButton speakersA;
    private javax.swing.JButton speakersAB;
    private javax.swing.JButton speakersB;
    private javax.swing.JButton stopPlayback;
    private javax.swing.JButton sugarRushPlayback;
    private javax.swing.JButton track4;
    private javax.swing.JButton track5;
    private javax.swing.JSlider volumeSlider;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setVolumeSlider(int volume)
    {
        l.trace("VOLCHANGE:"+volume);
        java.awt.EventQueue.invokeLater(() ->
        {
            
            volumeSlider.setValue(volume);
        });
    }

    @Override
    public void setSource(Sources s)
    {
        l.trace("SRC:"+s);
        java.awt.EventQueue.invokeLater(() ->
        {
            outLabel.setText("Gewählt: "+s);
        });
    }
    public void changePowerState(boolean on)
    {
        Module[] m = Main.getDispatcher().getModules();
        for (Object m1 : m) {
            if(m1 instanceof Mod_Stereo)
            {
                ((Mod_Stereo)m1).setPower(on);
            }
        }
    }
    @Override
    public void setPowerState(boolean on)
    {
        l.trace("PWR:"+on);
        java.awt.EventQueue.invokeLater(() ->
        {
            btnPower.setText(on?"an":"aus");
            btnPower.setSelected(on);
            btnMinus.setEnabled(on);
            btnPlus.setEnabled(on);
            speakersA.setEnabled(on);
            speakersAB.setEnabled(on);
            speakersB.setEnabled(on);
            //sourcesList.setEnabled(on);
            cbxSourceSel.setEnabled(on);
        });
    }
    public void syncDevice()
    {
        Module[] m = Main.getDispatcher().getModules();
        for (Object m1 : m) {
            if(m1 instanceof Mod_Stereo)
            {
                ((Mod_Stereo)m1).syncDevice();
            }
        }
    }
    public void setSpeakers(String... speakerIDs)
    {
        Module[] m = Main.getDispatcher().getModules();
        for (Object m1 : m) {
            if(m1 instanceof Mod_Stereo)
            {
                ((Mod_Stereo)m1).speaker_select(speakerIDs);
            }
        }
    }
    public void setVolume(String target)
    {
        Module[] m = Main.getDispatcher().getModules();
        for (Object m1 : m) {
            if(m1 instanceof Mod_Stereo)
            {
                ((Mod_Stereo)m1).setVolume(target);
            }
        }
    }
    public void updateSource(Sources s)
    {
        Module[] m = Main.getDispatcher().getModules();
        for (Object m1 : m) {
            if(m1 instanceof Mod_Stereo)
            {
                ((Mod_Stereo)m1).changeSrc(s);
            }
        }
    }
    @Override
    public void afterLoad()
    {
        syncDevice();
    }
}
