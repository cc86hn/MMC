/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cc86.MMC.client;

import java.io.IOException;
import org.cc86.MMC.client.API.Connection;

/**
 *
 * @author iZc <nplusc.de>
 */
public class Main
{
    private static UI ui;
    private static RadioUI radio;
    private static StreamUI stream;
    private static final Dispatcher disp = new Dispatcher();
    private static Connection c;
    public static void main(String[] args)
    {
        //TODO PI-DETECTION
        c=new TCPConnection("192.168.10.228", 9264);
        try {
            c.connect();
        } catch (IOException ex) {
            //System.out.println(ex.m);
            System.out.println("Ell-Emm-AhhX2");
        }
        disp.connect(c);
        
        java.awt.EventQueue.invokeLater(()->
        {
             ui = new UI();
            //ui.setVisible(true);
            radio=new RadioUI();
            //radio.setVisible(true);
            stream=new StreamUI();
            stream.setVisible(true);
            
        });
        
        
    }
    public static Dispatcher getDispatcher()
    {
        return disp;
    }
    public static UI getUi()
    {
        return ui;
    }

    public static RadioUI getRadio()
    {
        return radio;
    }
    /*packahgeprotected*/ static void serverKillen()
    {
        Mod_Exit x = new Mod_Exit();
        x.connect(c);
        x.exxit();
    }
}
