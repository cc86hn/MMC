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
import org.cc86.MMC.Networking.Packet;
import org.cc86.MMC.client.API.Connection;
import org.cc86.MMC.client.API.Module;
import org.cc86.MMC.client.API.Utilities;

/**
 *
 * @author tgoerner
 */
public class Mod_Stream implements Module
{
    StreamUI ui;
    private static final Logger l = LogManager.getLogger();
    Connection c;
    private Packet streampkt;
    //private int streamPort;
    private enum StreamMode{MP4,VNC,MIRACAST};
    private StreamMode lastMode;
    
    
    @Override
    public void receiveMsgFromServer(Packet msg)
    {
        if(ui==null)
        {
            return;
        }
        if(msg.getData().containsKey("disconnect"))
        {
            l.info("disconnect");
            if(mp4Thread!=null)
            {
                mp4Thread.stopStreaming();
            }
            return;
        }
        if(msg.getData().containsKey("reconnect"))
        {
            reconnectStream();
            return;
        }
        if(msg.getData().containsKey("message"))
        {
            ui.updateStatus((String) msg.getData().get("message"));
            return;
        }
        if(msg.getData().containsKey("streamsource"))
        {
            List<String> source = (List<String>) msg.getData().get("streamsource");
            if(source.get(0).equals(""))
            {
                ui.updateSource("None connected");
                return;
            }
            boolean noport=source.get(1).equals("0");
            
            String src = source.get(0)+(noport?":"+source.get(1):"")+" als "+source.get(2);
            ui.updateSource(src);
        }
    }
    MP4Thread mp4Thread=null;
    
    @Override
    public void loadUI(boolean demomode) 
    {
        if(demomode)
        {
            return;
        }
        l.info("figgdi");
        ui= new StreamUI();
        Menu.getMenu().registerTab("ScreenStream", ui);
    }
    
    private void reconnectStream()
    {
        if(streampkt==null)
        {
            return;
        }
        c.sendRequest(streampkt);
        if(lastMode==StreamMode.MP4)
        {
            new Thread(mp4Thread).start();
        }
    }
    public void streamMP4(boolean fastmode)
    {
        lastMode=StreamMode.MP4;
        if(mp4Thread!=null)
        {
            mp4Thread.stopStreaming();
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
        streampkt=p;
    }
        
    
    public void streamVNC()
    {   
        lastMode=StreamMode.VNC;
        //TODO check for VNC and launch if needed
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","vnc");
        p.setData(data);
        c.sendRequest(p);
        streampkt=p;
    }
    
    public void stopThisStream()
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","stream");
        data.put("disconnect","disconnect");
        p.setData(data);
        c.sendRequest(p);
        
        if(mp4Thread!=null)
        {
            mp4Thread.stopStreaming();
        }
        streampkt=null;
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
        streampkt=null;
    }
    
    @Override
    public void connect(Connection c) {
        this.c=c;
        Utilities.registerOnEvent("stream",c);
    }

    @Override
    public List<String> getCommands()
    {
        return Arrays.asList(new String[]{"vnc","mp4","miracast","stream"});
    }
        
    @Override
    public void quit() 
    {
        stopThisStream();
    }
}
