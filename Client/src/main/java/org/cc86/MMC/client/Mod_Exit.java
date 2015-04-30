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
public class Mod_Exit implements Module
{
    public void exxit()
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","exit");
        p.setData(data);
        c.sendRequest(p);
    }
    private Connection c;
    @Override
    public void receiveMsgFromServer(Packet msg)
    {
        
    }

    @Override
    public void connect(Connection c)
    {

        
        
        this.c=c;
    }

    @Override
    public List<String> getCommands()
    {
        return Arrays.asList(new String[]{"exit"});
    }
}
