/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.client.API.Connection;
import org.cc86.MMC.client.API.Module;

/**
 *
 * @author tgoerner
 */
public class Mod_Stream implements Module
{
    StreamUI ui;
    private static final Logger l = LogManager.getLogger();
    Connection c;
    private boolean ignoreAbort=true;
    @Override
    public void receiveMsgFromServer(Packet msg)
    {

        if(msg.getData().containsKey("disconnect"))
        {
            if(ignoreAbort)
            {
                ignoreAbort=false;
                return;
            }
            l.info("disconnect");
            mp4Thread.stopStreaming();
        }
    }
    MP4Thread mp4Thread=null;
    
    @Override
    public void loadUI() 
    {
        l.info("figgdi");
        ui= new StreamUI();
        Menu.getMenu().registerTab("ScreenStream", ui);
    }
    
    
    public void streamMP4(boolean fastmode)
    {
        if(mp4Thread!=null)
        {
            mp4Thread.stopStreaming();
            ignoreAbort=true;
        }
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        int tport = ((int)(0xCC87+Math.random()*100));//52359-52459 als port
        data.put("type","set");
        data.put("command","mp4");
        data.put("target_port",""+tport);
        data.put("mode",fastmode?"fast":"slow");
        mp4Thread = new MP4Thread(tport, fastmode);
        new Thread(mp4Thread).start();
        p.setData(data);
        c.sendRequest(p);
    }
        
    
    public void streamVNC()
    {       
        //TODO check for VNC and launch if needed
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","vnc");
        p.setData(data);
        c.sendRequest(p);
    }
    public void stopAllStreams()
    {       
        //TODO check for VNC and launch if needed
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","stopall");
        p.setData(data);
        c.sendRequest(p);
    }
    
    @Override
    public void connect(Connection c) {
        this.c=c;
    }

    @Override
    public List<String> getCommands()
    {
        return Arrays.asList(new String[]{"vnc","mp4","miracast","stream"});
    }
        
    @Override
    public void quit() 
    {
        if(mp4Thread!=null)
        {
            mp4Thread.stopStreaming();
        }
    }
}
