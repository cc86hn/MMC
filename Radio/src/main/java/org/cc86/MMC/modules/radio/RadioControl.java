/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.radio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Plugin;
import org.cc86.MMC.API.Resources;

/**
 *
 * @author tgoerner
 */
public class RadioControl implements Plugin{
    private static final Logger l = LogManager.getLogger();
    private RadioHandler h;
    
    @Override
    public void register() {
        l.info("REGISTERING RadioPlugin");
        h = new RadioHandler();
        API.getDispatcher().registerOnRequestType("webradio", h); 
        API.getDispatcher().registerOnRequestType("webradioShortID", h); 
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
            h.shitdown();
        }
    }

    @Override
    public void freeUpResources(Resources... res)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onClientDisconnect(Handler h, boolean graceful)
    {
        
    }
}
