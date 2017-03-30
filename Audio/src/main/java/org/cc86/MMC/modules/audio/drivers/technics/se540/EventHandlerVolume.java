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
public class EventHandlerVolume
{
    private static final int CMD_VOLUME_VOL = 7;
    private static final int CMD_VOLUME_VOLREL = 8;
    private static final int CMD_VOLUME_MUTE = 9;
    private static final int CMD_VOLUME_BALREL = 10;
    private static final int CMD_VOLUME_BAL = 11;
    
    
    public static  int handleEvent(List<Byte> packet,Integer cmd)
    {
        switch(cmd)
        {
            
            
            case CMD_VOLUME_VOL: 
            {
                int volume = (packet.get(1))&0x7F;
                DriverSe540.getDriver().notifyCoreonVolume(volume);
                break;
            }
            case CMD_VOLUME_BAL:
            {
             
                break;
            }
            case CMD_VOLUME_MUTE:
            {
                break;
            }       
        }
        return -1;
    }
}
