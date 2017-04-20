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
public class DataHandlerPower extends DataHandler
{
    
    public static final DataHandlerPower instance = new DataHandlerPower(); 
    private DataHandlerPower(){};
    
    public static DataHandler linkHandler(ProtocolHandler h)
    {
        instance.handler=h;
        return instance;
    }
    
    public void changePower(boolean power)
    {
        List<Byte> userdata = new ArrayList<>();
        userdata.add(((byte)(power?1:0)));
        handler.send_packet(0, ProtocolHandler.SRV_SET, handler.dataHandlers.indexOf(this), userdata, null);
    }
    
    
    @Override
    public int handleEvent(List<Byte> packet)
    {
        boolean power = packet.get(1)!=0;
        DriverSe540.getDriver().notifyCoreonPower(power);
        return -1;
    }
}

