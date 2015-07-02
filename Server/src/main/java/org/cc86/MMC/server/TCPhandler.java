/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Packet;
import org.yaml.snakeyaml.Yaml;


/**
 *
 * @author tgoerner
 */
public class TCPhandler implements Handler{
    private static final org.apache.logging.log4j.Logger l = LogManager.getLogger();
    private final Socket sck;
    private String returnMsg;
    public TCPhandler(Socket s)
    {
        sck=s;
    }
    
    public void start()
    {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(sck.getInputStream()));
            PrintStream out = new PrintStream(sck.getOutputStream());
            Thread t = new Thread(()->{
                while(sck.isConnected())
                {
                    String request = "";
                    try {
                        String ln = r.readLine();
                        while(!ln.equals("---"))
                        {
                            request+=ln+"\n";
                            l.trace(ln);
                            ln=r.readLine();
                        }
                        
                    } catch (IOException ex) {
                        l.warn("Lost connection"); //TODO cleanup
                        break;
                    }
                    Object packet = new Yaml().load(request);
                    if(packet instanceof Packet)
                    {
                        l.info("PACKET received");
                        l.trace(request);
                        API.getDispatcher().handleEvent((Packet)packet,this);
                    }
                    else
                    {
                        //error();
                    }
               }
            });
            t.setName("InputCruncher");
            t.start();
            Thread t2 = new Thread(()->{
                while(sck.isConnected())
                {
                    synchronized(sck)
                    {
                        try {
                            sck.wait();
                        } catch (InterruptedException ex) {
                        }
                        out.println(returnMsg);
                        out.flush();
                        returnMsg="";
                        l.info("MSG sent back");
                    }
                }
            });
            t2.setName("OutputCruncher");
            t2.start();
        } catch (IOException ex) {
            Logger.getLogger(TCPhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @Override
    public void respondToLinkedClient(Packet response) {
         synchronized(sck)
         {
             l.info("ANTWORT");
             returnMsg=new Yaml().dump(response)+"\n---\n";
             sck.notify();
         }
    }

    @Override
    public String getClientIP()
    {
        String internalAddr=sck.getRemoteSocketAddress().toString().substring(1);
        int lastCol=internalAddr.lastIndexOf(":");
        return internalAddr.substring(0,lastCol);  //HACK!
    }
    
    
}
