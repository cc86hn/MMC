/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author iZc <nplusc.de>
 */
public class ApplicationLayer
{
    
    static final List<DataHandler> dataHandlers = Arrays.asList(
            DataHandlerVersion.getInstance(),           //VERSION->VER;NO_EVT
            null,                                       //RESET->RST;NO_EVT
            null,                                       //BOOTLOADER->BOOT;NO_EVT
            null,                                       //EVENT->EVT;NO_EVT
            DataHandlerEventAll.getInstance(),          //EVENT->EVTALL;NO_EVT
            null,                                       //STATISTIC->STAT
            DataHandlerPower.getInstance(),             //POWER->PWR
            DataHandlerVolumeVol.getInstance(),         //VOLUME->VOL
            DataHandlerVolumeVolrel.getInstance(),      //VOLUME->VOLREL;NO_EVT
            DataHandlerVolumeMute.getInstance(),        //VOLUME->MUTE
            DataHandlerVolumeBalrel.getInstance(),      //VOLUME->BALREL;NO_EVT
            DataHandlerVolumeBal.getInstance(),         //VOLUME->BAL
            DataHandlerSource.getInstance(),            //SOURCE->SRC
            DataHandlerSpeaker.getInstance(),           //SPEAKER->SPK
            null,                                       //SPEAKER->SPKCFG
            null                                        //TIME->TIME
        ); 
    
    
    public static void sendPacket(ServiceType st,int cmdid,List<Byte> userdata)
    {
        TransportLayer.send_request(st, cmdid, userdata);
    }
    
    public static void handleResponse(int cmdid,List<Byte> userdata)
    {
        DataHandler h = dataHandlers.get(cmdid);
        if(h!=null)
        {
            h.handleResponse(userdata);
        }
    }
    
}
