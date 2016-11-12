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
public class DataSenderVolume
{
    private ProtocolHandler handler;
    
    public DataSenderVolume(ProtocolHandler h)
    {
        handler=h;
    }
    
    public void changeVolume(int newVolume)
    {
        List<Byte> userdata = new ArrayList<>();
        userdata.add(((byte)(((byte)newVolume)&((byte)0x7f))));
        handler.send_packet(0, ProtocolHandler.SRV_SET, ProtocolHandler.CMD_VOLUME_VOL, userdata, null);
    }
}
