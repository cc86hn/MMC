/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Plugin;
import org.cc86.MMC.API.Resources;

/**
 *
 * @author tgoerner
 */
public class AudioPlugin implements Plugin{

    @Override
    public void register() {
        AudioProcessor h = new AudioProcessor();
        //API.getDispatcher().registerOnRequestType("webradio", h); 
        //API.getDispatcher().registerOnRequestType("webradioShortID", h); 
    }

    @Override
    public String getName() {
        return "Radio";
    }

    @Override
    public void shutdown()
    {
    }

    @Override
    public void freeUpResources(Resources... res)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
