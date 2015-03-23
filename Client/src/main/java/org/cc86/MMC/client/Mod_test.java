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
 * @author iZc <nplusc.de>
 */
public class Mod_test implements Module
{
    private Connection c;
    @Override
    public void receiveMsgFromServer(Packet msg) {
        HashMap<String,Object> data = msg.getData();
        if(data.get("type").equals("response")&&data.get("command").equals("Test"))
        {
            Main.getUi().updateMEssageLine((String) data.get("message"));
        }
    }
    
    public void sendMessage(String msg)
    {
        System.out.println("waddaFUQ");
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","Test");
        data.put("message",msg);
        p.setData(data);
        c.sendRequest(p);
                
    }
           
    
    @Override
    public void connect(Connection c) {
        this.c=c;
    }

    @Override
    public List<String> getCommands() {
        return Arrays.asList("Test");
    }
    
    
}
