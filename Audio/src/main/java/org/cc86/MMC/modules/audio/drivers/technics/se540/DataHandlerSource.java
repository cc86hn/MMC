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
public class DataHandlerSource extends DataHandler
{
    private ProtocolHandler handler;
    
    public static final DataHandlerSource instance = new DataHandlerSource(); 
    private DataHandlerSource(){};
    
    public static DataHandler linkHandler(ProtocolHandler h)
    {
        instance.handler=h;
        return instance;
    }
    
    public void changeSource(int newSource)
    {
        List<Byte> userdata = new ArrayList<>();
        userdata.add(((byte)(((byte)newSource)&((byte)0x3))));
        handler.send_packet(0, ProtocolHandler.SRV_SET, handler.dataHandlers.indexOf(this), userdata, null);
    }
    @Override
    public int handleEvent(List<Byte> packet)
    {
        int source = (packet.get(1))&0x03;
        DriverSe540.getDriver().notifyCoreonSource(source);
        return -1;
    }
}