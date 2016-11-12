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
public class DataSenderPower
{
    private ProtocolHandler handler;
    
    public DataSenderPower(ProtocolHandler h)
    {
        handler=h;
    }
    
    public void changePower(boolean power)
    {
        List<Byte> userdata = new ArrayList<>();
        userdata.add(((byte)(power?1:0)));
        handler.send_packet(0, ProtocolHandler.SRV_SET, ProtocolHandler.CMD_POWER_PWR, userdata, null);
    }
}

