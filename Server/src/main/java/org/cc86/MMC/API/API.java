/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.API;

import java.io.File;
import org.cc86.MMC.server.Dispatcher;
import org.cc86.MMC.server.Main;

/**
 *
 * @author tgoerner
 */
public class API {
    
    public static final String APPDIR = new File(API.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getParent();
    
    public static final String PLUGINPATH = APPDIR + File.separator + "plugins";
    public static final String SETTINGSPATH = APPDIR + File.separator + "settings";
    public static final String WINVLC = APPDIR + File.separator + "vlc";
    
    
    
    public static Dispatcher getDispatcher()
    {
        return Main.m.getDispatcher();
    }        
    
    public static void requestResourcesFree(Resources... r)
    {
        Main.m.getPluginmgr().freeRessources(r);
    }
    
    public static void dispatchEvent(Packet p)
    {
        Main.m.getEvtmgr().sendEventToRegisteredClients(p);
    }
}
