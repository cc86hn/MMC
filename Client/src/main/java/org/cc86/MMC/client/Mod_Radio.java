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
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author tgoerner
 */
public class Mod_Radio implements Module
{
    private Connection c;
    @Override
    public void receiveMsgFromServer(Packet msg)
    {
        HashMap<String,Object> data = msg.getData();
        if(data.get("type").equals("response")&&data.get("command").equals("webradioShortID"))
        {
            //Handling der Mapping-Liste
            new Yaml().dump(data);
            System.out.println("WRSID");
            Main.getRadio().updateList((HashMap<String, String>) data.get("mappings"));
            
            
        }
    }
    
    public void switchToStation(String url)
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","webradio");
        data.put("URL",url);
        p.setData(data);
        c.sendRequest(p);
    }
    
    public void addMappingForID(String shortID,String url)
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","webradioShortID");
        data.put("shortID",shortID);
        data.put("URL",url);
        p.setData(data);
        c.sendRequest(p);
    }
    
    public void requestMappings()
    {
                Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","get");
        data.put("command","webradioShortID");
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
        return Arrays.asList(new String[]{"webradio","webradioShortID"});
    }
    
}
