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
import org.cc86.MMC.client.API.Utilities;
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
        if(ui==null)
        {
            return;
        }
        l.info("Jukebox received packet");
        String mode = (String) msg.getData().get("command");
        if(mode.equals("playback_pool"))
        {
            ui.updatePool((HashMap<String, String>) msg.getData().get("pool"));
        }
        if(mode.equals("playback_jukebox"))
        {
            ui.updateList((List<String>) msg.getData().get("list"));
        }
        if(mode.equals("playback_status"))
        {
            ui.updateStatus(msg.getData());
        }
    }
    @Override
    public void connect(Connection c) {
        fp = new JukeBoxFileProvider(this);
        try
        {
            this.c=c;
            
            Utilities.registerOnEvent("playback_pool",c);
            Utilities.registerOnEvent("playback_jukebox",c);
            Utilities.registerOnEvent("playback_status",c);
            Utilities.registerOnEvent("playback_control",c);
            
            HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0",9265), 0);//,InetAddress.getByName("localhost"));
            server.createContext("/", fp);//nimmt jeden beliebigen namen an
            server.setExecutor(null); // creates a default executor
            server.start();
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void sendOwnPoolpart(List<String> listdata)
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","playback_pool");
        data.put("pool",listdata);
        p.setData(data);
        c.sendRequest(p);
    }
    public void snedTrackPlaybackRequest(String trackid,boolean scheduled)
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","playback_jukebox");
        data.put("scheduled",scheduled);
        data.put("path",trackid);
        p.setData(data);
        c.sendRequest(p);
    }
    public void skip()
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","playback_control");
        data.put("action","skip");
        p.setData(data);
        c.sendRequest(p);
    }
    public void loop()
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","playback_control");
        data.put("action","loop");
        p.setData(data);
        c.sendRequest(p);
    }
    
    @Override
    public List<String> getCommands()
    {
        return Arrays.asList(new String[]{"playback_pool","playback_jukebox","playback_status"});
    }
    
    @Override
    public void loadUI(boolean demomode)
    {
        if(demomode)
        {
            return;
        }
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
