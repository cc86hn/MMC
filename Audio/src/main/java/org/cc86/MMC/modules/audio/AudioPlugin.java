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

    private AudioProcessor h;
    
    @Override
    public void register() {
        h = new AudioProcessor();
        API.getDispatcher().registerOnRequestType("Playback_DLNA", h); 
        API.getDispatcher().registerOnRequestType("playback_seek", h); 
        API.getDispatcher().registerOnRequestType("playback_status", h); 
        API.getDispatcher().registerOnRequestType("playback_control", h); 
        API.getDispatcher().registerOnRequestType("volume", h);
    }

    @Override
    public String getName() {
        return "AudioControl";
    }

    @Override
    public void shutdown()
    {
        h.shutdown();
    }

    @Override
    public void freeUpResources(Resources... res)
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
