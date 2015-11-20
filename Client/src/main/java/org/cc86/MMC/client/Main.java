/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cc86.MMC.client;

import de.nplusc.izc.tools.baseTools.Messagers;
import de.nplusc.izc.tools.baseTools.TimeoutManager;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.cc86.MMC.client.API.Connection;

/**
 *
 * @author iZc <nplusc.de>
 */
public class Main
{

    
    private static final Logger l = LogManager.getLogger();
    //private static TestsUI ui;
    //private static RadioUI radio;
    private static final Dispatcher disp = new Dispatcher();
    private static Connection c;
    private static boolean devmode = false;

    private static String piIP;

    
    public static void main(String[] args)
    {
        //args=new String[]{"--demo"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("d", "demo", false, "Demo UI modus für die H+E in Schuttgart");
        options.addOption("x", "devmode", false, "Allows the Demo mode to be closed");
        try 
        {
            
            CommandLine cl = parser.parse(options, args);
            //Main.setupLogging(cl.hasOption("verbose"));
            setupLogging(true);

            TimeoutManager m = new TimeoutManager(3, ()->Messagers.SingleLineMsg("Serversuche fehlgeschlagen", "OK"));
            m.start();
            String srvr =serverDiscovery();//;//"10.110.12.183";//
            l.info(srvr);
            if(srvr.equals("0.0.0.0"))
            {
                l.error("NO SERVER FOUND");
                System.exit(0);
                
            }
            c=new TCPConnection(srvr, 0xCC86);
            try
            {
                c.connect();
            }
            catch (IOException ex)
            {
                //System.out.println(ex.m);
                l.trace("Ell-Emm-AhhX2");
                l.error("FAILED TO CONNECT");
                System.exit(0);
            }
            piIP=srvr;
            disp.connect(c);
            Runtime.getRuntime().addShutdownHook(new Thread(disp::quit));
            m.disarm();
            
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
            }
            catch (ClassNotFoundException ex)
            {
                java.util.logging.Logger.getLogger(TestsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            catch (InstantiationException ex)
            {
                java.util.logging.Logger.getLogger(TestsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            catch (IllegalAccessException ex)
            {
                java.util.logging.Logger.getLogger(TestsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            catch (javax.swing.UnsupportedLookAndFeelException ex)
            {
                java.util.logging.Logger.getLogger(TestsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
            //</editor-fold>
            
            /* Create and display the form */
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
            {
                l.error("Fehler beim Setzen des LookAndFeels");
            }
            devmode = cl.hasOption("devmode");
            if(cl.hasOption("demo"))
            {
                java.awt.EventQueue.invokeLater(()->
                {
                    //ui = new TestsUI();
                    //ui.setVisible(true);
                    DemoUI.bootUI();
                });
            }

            else
            {
                java.awt.EventQueue.invokeLater(()->
                {
                    //ui = new TestsUI();
                    //ui.setVisible(true);
                    Menu.bootUI();
                });
            }
            
        }
        catch (ParseException ex)
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("client", options);
           // ex.printStackTrace();
        }
        
        
    }
    public static Dispatcher getDispatcher()
    {
        return disp;
    }
    /*
    public static TestsUI getUi()
    {
        return ui;
    }

    public static RadioUI getRadio()
    {
        return radio;
    }*/
    /*packahgeprotected*/ static void serverKillen()
    {
        Mod_Exit x = new Mod_Exit();
        x.connect(c);
        x.exxit();
    }
    
    public static String getServerIP()
    {
        return piIP;
    }
    
    
    
    public static String serverDiscovery()
    {
        String res="0.0.0.0";
        DatagramSocket c;
        // Find the server using UDP broadcast
        try
        {
            //Open a random port to send the package
            c = new DatagramSocket();
            c.setBroadcast(true);

            byte[] sendData = "DISCOVER_MMC_REQUEST".getBytes();

            //Try the 255.255.255.255 first
            try
            {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 0xCC86);
                c.send(sendPacket);
                l.info("Request packet sent to: 255.255.255.255 (DEFAULT)");
            } catch (Exception e)
            {
            }

            // Broadcast the message over all the network interfaces
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements())
            {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp())
                {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
                {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null)
                    {
                        continue;
                    }

                    // Send the broadcast package!
                    try
                    {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
                        c.send(sendPacket);
                    } catch (Exception e)
                    {
                    }

                    l.info("Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
            }

            l.info("Done looping over all network interfaces. Now waiting for a reply!");

            //Wait for a response
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);

            //We have a response
            l.info("Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

            //Check if the message is correct
            String message = new String(receivePacket.getData()).trim();
            if (message.equals("DISCOVER_MMC_RESPONSE"))
            {
                //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                res=(receivePacket.getAddress()+"").substring(1);
            }

            //Close the port!
            c.close();
        } catch (IOException ex)
        {
            
        }
        return res;
    }
    
        private static void setupLogging(boolean verbose)
    {
        
        
        //if(!cl.hasOption("verbose")&&!(System.getProperty("log4j.configurationFile")==null))
        //{
        //    System.setProperty("log4j.configurationFile", "file:///"+jarschiv+"!log4j2NonVerbose.xml");
        //}
        LoggerContext cx = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration config = cx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME); 
        LoggerConfig externalloggerConfig = config.getLoggerConfig("External"); 

        if (verbose)
        {
            
            loggerConfig.setLevel(Level.TRACE);
            externalloggerConfig.setLevel(Level.TRACE);
        }
        else
        {
            loggerConfig.setLevel(Level.INFO);
            externalloggerConfig.setLevel(Level.INFO);
        }
        cx.updateLoggers();
    }
    public static boolean isDevmode()
    {
        return devmode;
    }
          
    
}
