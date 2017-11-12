/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

import java.io.File;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Plugin;
import org.cc86.MMC.API.PluginNotReadyException;
import org.cc86.MMC.API.Resources;

/**
 *
 * @author tgoerner
 */
public class AudioPlugin implements Plugin{

    private AudioProcessor h;
    private StereoControl sc;
    private static final Logger l = LogManager.getLogger();
     
    @Override
    public void register() throws PluginNotReadyException {
        l.info("REGISTERING AudioPlugin");
        if(!API.getMockMode()&&!new File("/sys/class/softuart/softuart/data").exists())
        {
            throw new PluginNotReadyException("Softuart driver required, somethind did not install correctly");
        }
        h = new AudioProcessor();
        sc = new StereoControl();
        h.linkStereoControl(sc);
        API.getDispatcher().registerOnRequestType("playback_dlna", h);
        API.getDispatcher().registerOnRequestType("playback_jukebox", h);
        API.getDispatcher().registerOnRequestType("playback_pool", h);
        API.getDispatcher().registerOnRequestType("playback_seek", h); 
        API.getDispatcher().registerOnRequestType("playback_status", h); 
        API.getDispatcher().registerOnRequestType("playback_control", h); 
        API.getDispatcher().registerOnRequestType("volume", sc);
        API.getDispatcher().registerOnRequestType("dev_sync", sc);
        API.getDispatcher().registerOnRequestType("speaker_select", sc);
        API.getDispatcher().registerOnRequestType("device_power", sc);
        API.getDispatcher().registerOnRequestType("src_select", sc);
    }

    @Override
    public String getName() {
        return "AudioControl";
    }

    @Override
    public void shutdown()
    {
        if(h!=null)
        {
            h.shutdown();
        }
    }

    @Override
    public void freeUpResources(Resources... res)
    {
        if(Arrays.asList(res).contains(Resources.AUDIODATA))
        {
            h.freeUpAudio();
        }
    }

    @Override
    public void onClientDisconnect(Handler h, boolean graceful)
    {
        this.h.onClientDisconnect(h, graceful);
    }
    
}
