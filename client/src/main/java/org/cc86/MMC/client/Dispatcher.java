/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import org.cc86.MMC.Networking.Packet;
import org.cc86.MMC.client.API.Connection;
import org.cc86.MMC.client.API.Module;

/**
 *
 * @author iZc <nplusc.de>
 */
public class Dispatcher
{
    private final Module[] modules={new Mod_Radio(),new Mod_Stream(),new Mod_Jukebox(),new Mod_Stereo()};
    
    public void sendPacketToModule(Packet p)
    {
        String name = (String) p.getData().get("command");
        for (Module module : modules) {
            if(module.getCommands().contains(name))
            {
                System.out.println("Dispatched");
                module.receiveMsgFromServer(p);
                break;
            }
        }
        
    }
    public void startUIs()
    {
        startUIs(false);
    }
    public void startUIs(boolean demomode)
    {
        for (Module module : modules) {
            module.loadUI(demomode);
        }
    }
    
    
    public Module[] getModules()
    {
        return modules;
    }
    public void connect(Connection c)
    {
        
        for (Module module : modules) {
            module.connect(c);
        }
    }
    public void quit()
    {
        for (Module module : modules) {
            module.quit();
        }
    }
    
}
