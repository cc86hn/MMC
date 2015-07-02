/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.stream;

import de.nplusc.izc.tools.baseTools.Tools;
import org.apache.logging.log4j.io.IoBuilder;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Plugin;
import org.cc86.MMC.API.Resources;

/**
 *
 * @author tgoerner
 */
public class StreamPlugin implements Plugin{
    
    StreamProcessor h;
    
    @Override
    public void register() {
        h = new StreamProcessor();
        API.getDispatcher().registerOnRequestType("vnc", h); 
        API.getDispatcher().registerOnRequestType("mp4", h); 
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
        Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "killall","xtightvncviewer");
        Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "killall","omxplayer.bin");
    }

    @Override
    public void freeUpResources(Resources... res)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
