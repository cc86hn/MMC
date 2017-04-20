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
public class DataHandlerVolumeVolrel extends DataHandler
{
    private ProtocolHandler handler;
    public static final DataHandlerVolumeVolrel instance = new DataHandlerVolumeVolrel(); 
    private DataHandlerVolumeVolrel(){}
    public static DataHandler linkHandler(ProtocolHandler h)
    {
        instance.handler=h;
        return instance;
    }
    
    public void changeVolumeRel(byte newVolume)
    {
        List<Byte> userdata = new ArrayList<>();
        userdata.add(newVolume);
        handler.send_packet(0, ProtocolHandler.SRV_SET, handler.dataHandlers.indexOf(this), userdata, null);
    }
}
