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
public class DataSenderSource
{
    private ProtocolHandler handler;
    
    public DataSenderSource(ProtocolHandler h)
    {
        handler=h;
    }
    
    public void changeSource(int newSource)
    {
        List<Byte> userdata = new ArrayList<>();
        userdata.add(((byte)(((byte)newSource)&((byte)0x3))));
        handler.send_packet(0, ProtocolHandler.SRV_SET, ProtocolHandler.CMD_SOURCE_SRC, userdata, null);
    }
}
