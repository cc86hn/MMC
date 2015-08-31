/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import de.nplusc.izc.tools.baseTools.Tools;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger l = LogManager.getLogger();
    private RadioUI ui;
    private Connection c;
    private HashMap<String,String> mappings;
    @Override
    public void receiveMsgFromServer(Packet msg)
    {
        if(ui==null)
        {
            return;
        }
        HashMap<String,Object> data = msg.getData();
        if(data.get("type").equals("response")&&data.get("command").equals("webradioShortID"))
        {
            //Handling der Mapping-Liste
            l.trace(new Yaml().dump(data));
            l.trace("WRSID");
            mappings=(HashMap<String, String>) data.get("mappings");
            ui.updateList(mappings);
        }
        if(data.get("type").equals("response")&&data.get("command").equals("webradio"))
        {
            
            String stationID = (String) data.get("URL");
            if(mappings.containsValue(stationID))
            {
                
                stationID=Tools.getKeyByValue(mappings, stationID);
            }
            ui.updateStation(stationID);
            //Handling der Mapping-Liste
            //new Yaml().dump(data);
           // ui.updateList((HashMap<String, String>) data.get("mappings"));
        }
    }
    
    @Override
    public void loadUI() 
    {
        ui= new RadioUI();
        Menu.getMenu().registerTab("Radio", ui);
       /* Thread t = new Thread(()->
        {
            while(true)
            {
                Packet p = new Packet();
                HashMap<String,Object> data = new HashMap<>();
                data.put("type","get");
                data.put("command","webradio");
                p.setData(data);
                c.sendRequest(p);
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException ex)
                {
                }
            }
        });
        t.setName("RadioStationPoller");
        t.start();*/
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
        
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","event");
        data.put("mode","register");
        data.put("eventID","webradio");
        p.setData(data);
        c.sendRequest(p);
        
    }
    
    public void stopCurrentRadioStream()
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","webradio");
        data.put("STOP","STOP");
        p.setData(data);
        c.sendRequest(p);
    }
    @Override
    public List<String> getCommands()
    {
        return Arrays.asList(new String[]{"webradio","webradioShortID"});
    }
    
}
