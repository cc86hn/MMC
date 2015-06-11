/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.client.API.Connection;
import org.cc86.MMC.client.API.Module;

/**
 *
 * @author tgoerner
 */
public class Mod_Stream implements Module
{
    Connection c;
    @Override
    public void receiveMsgFromServer(Packet msg)
    {
       
    }
    
    public void streamMP4()
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","mp4");
        data.put("target_port",""+((int)(9264+Math.random()*100)));//9264-9264 als port
        p.setData(data);
        c.sendRequest(p);
    }
        
    
    public void streamVNC()
    {
        
        //TODO check for VNC and launch if needed
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","vnc");
        p.setData(data);
        c.sendRequest(p);
    }
    
    
    @Override
    public void connect(Connection c) {
        this.c=c;
    }

    @Override
    public List<String> getCommands()
    {
        return Arrays.asList(new String[]{"vnc","mp4","miracast"});
    }
    
}
