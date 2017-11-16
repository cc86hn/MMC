/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tgoerner
 */
public class ServerCore 
{
    private static final Logger l = LogManager.getLogger();
    int port;
    public ServerCore(int pPort)
    {
        port=pPort;
    }
    public void bootUp()
    {
        new Thread(()->{
            /*HACK START*/
            try 
            {
                ServerSocket serverSocket = new ServerSocket(port+1);
                l.info("Ready and listening on port: "+port+1);
                while(true)
                {
                    Socket socket = serverSocket.accept();
                    Thread t = new Thread(()->{
                        try
                        {
                            PrintStream out = new PrintStream(socket.getOutputStream());
                            out.print("PONG");
                        } catch (IOException ex)
                        {
                            ex.printStackTrace();
                        }
                    });
                    t.setName("protocolParser-"+socket.getRemoteSocketAddress());
                    t.start();
                }
            } catch (IOException ex) 
            {
                l.error(ex);
                l.info("Shutting down Hack-Server since it could not boot correctly");
                Main.m.shitdownHandler();
            }  
        },"MesseHack").start();
        /*END HACK!*/
        try 
        {
            ServerSocket serverSocket = new ServerSocket(port);
            l.info("Ready and listening on port: "+port);
            while(true)
            {
                Socket socket = serverSocket.accept();
                Thread t = new Thread(()->{
                   TCPhandler c =  new TCPhandler(socket);
                   c.start();
                });
                t.setName("protocolParser-"+socket.getRemoteSocketAddress());
                t.start();
            }
        } catch (IOException ex) 
        {
            l.error(ex);
            l.info("Shutting down Server since it could not boot correctly");
            Main.m.shitdownHandler();
        }  
    }
}
