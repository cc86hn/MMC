/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tgoerner
 */
public class DataHandlerVolumeVol extends DataHandler
{
    private ProtocolHandler handler;
    public static final DataHandlerVolumeVol instance = new DataHandlerVolumeVol(); 
    private DataHandlerVolumeVol(){}
    public static DataHandler linkHandler(ProtocolHandler h)
    {
        instance.handler=h;
        return instance;
    }
    
    public void changeVolume(int newVolume)
    {
        List<Byte> userdata = new ArrayList<>();
        userdata.add(((byte)(((byte)newVolume)&((byte)0x7f))));
        handler.send_packet(ProtocolHandler.SRV_SET, handler.dataHandlers.indexOf(this), userdata, null);
    }
    @Override
    public int handleEvent(List<Byte> packet)
    {
        int volume = (packet.get(1))&0x7F;
        DriverSe540.getDriver().notifyCoreonVolume(volume);
        return -1;
    }
}
