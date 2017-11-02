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
public class DataHandlerVolumeBal extends DataHandler
{
    private ProtocolHandler handler;
    public static final DataHandlerVolumeBal instance = new DataHandlerVolumeBal(); 
    private DataHandlerVolumeBal(){}
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
        return -1;
    }
}
