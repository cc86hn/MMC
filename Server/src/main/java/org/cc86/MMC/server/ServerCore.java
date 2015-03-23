/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tgoerner
 */
public class ServerCore 
{
    int port;
    public ServerCore(int pPort)
    {
        port=pPort;
    }
    public void bootUp()
    {
        try 
        {
            ServerSocket serverSocket = new ServerSocket(port);
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
            Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
}
