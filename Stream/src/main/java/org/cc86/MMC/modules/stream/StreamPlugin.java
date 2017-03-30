/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.stream;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.Networking.Packet;
import org.cc86.MMC.API.Plugin;
import org.cc86.MMC.API.Resources;

/**
 *
 * @author tgoerner
 */
public class StreamPlugin implements Plugin{
    
    private StreamProcessor h;
    private static final Logger l = LogManager.getLogger();
    @Override
    public void register() {
        l.info("REGISTERING StreamPlugin");
        h = new StreamProcessor();
        API.getDispatcher().registerOnRequestType("vnc", h); 
        API.getDispatcher().registerOnRequestType("mp4", h);
        API.getDispatcher().registerOnRequestType("stream", h); 
        API.getDispatcher().registerOnRequestType("miracast", h); 
        API.getDispatcher().registerOnRequestType("stopall", h);
    }
    @Override
    public String getName() {
        return "Radio";
    }
    
    @Override
    public void shutdown()
    {
        if(h!=null)
        {
            h.stopStream();
        }
    }

    @Override
    public void freeUpResources(Resources... res)    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onClientDisconnect(Handler h, boolean graceful)

    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","stream");
        data.put("disconnect","disconnect");
        p.setData(data);
        this.h.process(p, h);
    }
}
