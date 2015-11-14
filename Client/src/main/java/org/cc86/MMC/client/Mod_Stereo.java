/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import java.util.Arrays;
import java.util.List;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.client.API.Connection;
import org.cc86.MMC.client.API.Module;
import org.cc86.MMC.client.API.Utilities;

/**
 *
 * @author tgoerner
 */
public class Mod_Stereo implements Module
{
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
                ui.setSource(Sources.valueOf(msg.getData().get("value")+""));
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
         return Arrays.asList(new String[]{"volume","device_power","src_select"});
    }
    @Override
    public void loadUI(boolean demomode)
    {
        if(demomode)
        {
            ui=DemoUI.getUI();
        }
        else
        {
            ui=new StereoControl();
        }
    }
}
