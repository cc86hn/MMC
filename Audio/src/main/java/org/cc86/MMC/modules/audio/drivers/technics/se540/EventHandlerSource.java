/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

import java.util.List;

/**
 *
 * @author tgoerner
 */
public class EventHandlerSource
{
    public static void handleEvent(List<Byte> packet,Integer cmd)
    {
        if(cmd==12)
        {
            int source = (packet.get(1))&0x03;
            DriverSe540.getDriver().notifyCoreonSource(source);
        }
    }
}
