/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.client.API.Connection;
import org.cc86.MMC.client.API.Module;
import org.cc86.MMC.client.jukebox.JukeBoxFileProvider;
import org.cc86.MMC.client.jukebox.JukeBoxPoolManagment;
import org.cc86.MMC.client.jukebox.JukeBoxUI;

/**
 *
 * @author tgoerner
 */
public class Mod_Jukebox implements Module
{
    private Connection c;
    private JukeBoxFileProvider fp;
    private JukeBoxUI ui;
    private JukeBoxPoolManagment pm;
    @Override
    public void receiveMsgFromServer(Packet msg)
    {
        
    }

    
    @Override
    public void connect(Connection c) {
        try
        {
            this.c=c;
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost",9265), 0);//,InetAddress.getByName("localhost"));
            server.createContext("/", new JukeBoxFileProvider());//nimmt jeden beliebigen namen an
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException ex)
        {
            Logger.getLogger(Mod_Jukebox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    
    @Override
    public List<String> getCommands()
    {
        return new ArrayList<>();
    }
    
    public void loadUI()
    {
        pm=new JukeBoxPoolManagment(this);
        ui=new JukeBoxUI(this);
        Menu.getMenu().registerTab("JukeBox", ui);
        Menu.getMenu().registerTab("JukeBox Lokaler Pool", pm);
    }
}
