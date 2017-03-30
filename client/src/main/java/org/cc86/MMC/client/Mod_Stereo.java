/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.Networking.Packet;
import org.cc86.MMC.client.API.Connection;
import org.cc86.MMC.client.API.Module;
import org.cc86.MMC.client.API.Utilities;

/**
 *
 * @author tgoerner
 */
public class Mod_Stereo implements Module
{
    private static final Logger l = LogManager.getLogger();
    private Connection c;
    private StereoUIApi ui;
    @Override
    public void receiveMsgFromServer(Packet msg)
    {
        try{
        String mode = (String) msg.getData().get("command");
        switch(mode)
        {
            case "volume":
                int volval = Integer.valueOf(msg.getData().get("value")+"");
                ui.setVolumeSlider(volval);
            break;
            case "device_power":
                ui.setPowerState(msg.getData().get("value").equals("ON"));
            break;
            case "src_select":
            {
                l.trace(msg.getData().get("value")+"");
                ui.setSource(Sources.valueOf(msg.getData().get("value")+""));
            }
            break;
        }
        }catch (Exception e){}
    }

    @Override
    public void connect(Connection c) {
        this.c=c;
        Utilities.registerOnEvent("volume",c);
        Utilities.registerOnEvent("device_power",c);
        Utilities.registerOnEvent("src_select",c);
    }

    @Override
    public List<String> getCommands()
    {
         return Arrays.asList(new String[]{"volume","device_power","src_select","speaker_select"});
    }
    @Override
    public void loadUI(boolean demomode)
    {
        if(demomode)
        {
            l.trace("stereodemo");
            ui=DemoUI.getUI();
        }
        else
        {
            ui=new StereoControl();
        }
        
        ui.afterLoad();
    }
    public void syncDevice()
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","dev_sync");

        p.setData(data);
        c.sendRequest(p);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            //java.util.logging.Logger.getLogger(Mod_Stereo.class.getName()).log(Level.SEVERE, null, ex);
        }c.sendRequest(p);
        
    }
    
    public void speaker_select(String... speakers)
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","speaker_select");
        data.put("speakers",Arrays.asList(speakers));
        p.setData(data);
        c.sendRequest(p);
    }
    /**
     * Volume control
     * @param command 0 to 100 or UP and DOWN 
     */
    public void setVolume(String command)
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","volume");
        data.put("value",command);
        p.setData(data);
        c.sendRequest(p);
    }
    public void setPower(boolean on)
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","device_power");
        data.put("value",on?"ON":"OFF");
        p.setData(data);
        c.sendRequest(p);
    }
    public void changeSrc(Sources s)
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","src_select");
        data.put("value",s+"");
        p.setData(data);
        c.sendRequest(p);
    }
}
