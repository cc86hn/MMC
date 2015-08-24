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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
        private static final Logger l = LogManager.getLogger();
    private Connection c;
    private JukeBoxFileProvider fp;
    private JukeBoxUI ui;
    private JukeBoxPoolManagment pm;
    @Override
    public void receiveMsgFromServer(Packet msg)
    {
        l.info("Jukebox received packet");
        String mode = (String) msg.getData().get("command");
        if(mode.equals("playback_pool"))
        {
            ui.updatePool((HashMap<String, String>) msg.getData().get("pool"));
        }
    }

    
    @Override
    public void connect(Connection c) {
        fp = new JukeBoxFileProvider(this);
        try
        {
            this.c=c;
            Packet p = new Packet();
            HashMap<String,Object> data = new HashMap<>();
            data.put("type","set");
            data.put("command","event");
            data.put("mode","register");
            data.put("eventID","playback_pool");
            p.setData(data);
            c.sendRequest(p);
            
            
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost",9265), 0);//,InetAddress.getByName("localhost"));
            server.createContext("/", fp);//nimmt jeden beliebigen namen an
            server.setExecutor(null); // creates a default executor
            server.start();
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void sendOwnPoolpart(Map<String,String> listdata)
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","playback_pool");
        data.put("pool",listdata);
        p.setData(data);
        c.sendRequest(p);
    }
    
    
    @Override
    public List<String> getCommands()
    {
        return Arrays.asList(new String[]{"playback_pool","playback_DLNA"});
    }
    public void loadUI()
    {
        pm=new JukeBoxPoolManagment(this);
        ui=new JukeBoxUI(this);
        
        Menu.getMenu().registerTab("JukeBox", ui);
        Menu.getMenu().registerTab("JukeBox Lokaler Pool", pm);
    }
    
    public JukeBoxFileProvider getFileProvider()
    {
        return fp;
    }
    
}
