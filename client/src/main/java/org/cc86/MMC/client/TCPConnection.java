/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cc86.MMC.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.Networking.Packet;
import org.cc86.MMC.client.API.Connection;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author iZc <nplusc.de>
 */
public class TCPConnection implements Connection
{
    private static final Logger l = LogManager.getLogger();
    private final int port;
    private final String destination;
    private Socket sck;
    private String returnMsg;
    public TCPConnection(String dest,int pPort)
    {
        port=pPort;
        destination=dest;
    }
    
    public void connect() throws IOException
    {
        sck=new Socket(destination, port);
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(sck.getInputStream()));
            PrintStream out = new PrintStream(sck.getOutputStream());
            Thread t = new Thread(()->{
                while(sck.isConnected())
                {
                    String request = "";
                    try {
                        String ln = r.readLine();
                        l.trace("packet line = "+ln);
                        while(ln!=null&&!ln.equals("---"))
                        {
                            request+=ln+"\n";
                            
                            ln=r.readLine();
                        }
                        if(ln==null)
                        {
                            l.warn("Null-Read");
                            System.exit(0);
                        }
                        
                    } catch (IOException ex) {
                        l.warn("NETZAP");
                        System.exit(0);
                        break;
                    }
                    l.trace(request);
                    Object packet = new Yaml().load(request);
                    if(packet instanceof Packet)
                    {
                        
                        l.trace("PACKET");
                        Main.getDispatcher().sendPacketToModule((Packet)packet);
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
                //System.out.println("Gefangen!");
                while(sck.isConnected())
                {
                    synchronized(destination)
                    {
                        try {
                            destination.wait();
                        } catch (InterruptedException ex) {
                        }
                        System.out.println("MSG sent");
                        l.info("PKT="+returnMsg);
                        out.println(returnMsg);
                        out.flush();
                        returnMsg="";
                    }
                }
                //System.out.println("Flucht ist zwecklos");
            });
            t2.setName("OutputCruncher");
            t2.start();
            
        } 
        catch (IOException ex) 
        {}
    }
    
    
    @Override
    public void sendRequest(Packet request) {
         synchronized(destination)
         {
             System.out.println("POKE");
             returnMsg=new Yaml().dump(request)+"\n---\n";
             destination.notify();
         }
    }
    
    
    
}
