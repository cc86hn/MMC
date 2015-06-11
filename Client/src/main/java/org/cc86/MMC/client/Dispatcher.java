/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import org.cc86.MMC.API.Packet;
import org.cc86.MMC.client.API.Connection;
import org.cc86.MMC.client.API.Module;

/**
 *
 * @author iZc <nplusc.de>
 */
public class Dispatcher
{
    private final Module[] modules={new Mod_test(),new Mod_Radio(),new Mod_Stream()};
    
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
    
}
