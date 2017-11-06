/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

import java.util.ArrayList;
import java.util.List;
import static org.cc86.MMC.modules.audio.drivers.technics.se540.DataHandlerEventAll.instance;

/**
 *
 * @author tgoerner
 */
public class DataHandlerVolumeVol extends DataHandler
{
    public static final DataHandlerVolumeVol instance = new DataHandlerVolumeVol(); 
    private DataHandlerVolumeVol(){}
    public static DataHandler getInstance()
    {
        return instance;
    }
    
    public void changeVolume(int newVolume)
    {
        List<Byte> userdata = new ArrayList<>();
        userdata.add(((byte)(((byte)newVolume)&((byte)0x7f))));
        int cmdid = ApplicationLayer.dataHandlers.indexOf(this);
        ApplicationLayer.sendPacket(ServiceType.SRV_SET,cmdid , userdata);
    }
    @Override
    public int handleEvent(List<Byte> packet)
    {
        int volume = (packet.get(1))&0x7F;
        DriverSe540.getDriver().notifyCoreonVolume(volume);
        return -1;
    }
}
