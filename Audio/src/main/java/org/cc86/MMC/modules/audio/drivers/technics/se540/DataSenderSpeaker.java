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
 * @author LH
 */
public class DataSenderSpeaker 
{
    private final byte AR=0x1;
    private final byte AL=0x2;
    private final byte BR=0x4;
    private final byte BL=0x8;
    private ProtocolHandler handler;
    
    public DataSenderSpeaker(ProtocolHandler h)
    {
        handler=h;
    }
    
    public void changeSpksel(String[] spksel)
    {
        byte spk=0;
        for (String scf : spksel) {
            switch(scf)
            {
                case "AL":
                    spk|=AL;
                    break;
                case "AR":
                    spk|=AR;
                    break;
                case "BL":
                    spk|=BL;
                    break;
                case "BR":
                    spk|=BR;
                    break;
                
            }
        }
        List<Byte> userdata = new ArrayList<>();
        userdata.add(spk);
        handler.send_packet(0, ProtocolHandler.SRV_SET, ProtocolHandler.CMD_SPEAKER_SPK, userdata, null);
    }
}
