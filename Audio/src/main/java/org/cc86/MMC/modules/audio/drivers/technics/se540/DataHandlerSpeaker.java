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
public class DataHandlerSpeaker extends DataHandler
{
    private final byte AR=0x1;
    private final byte AL=0x2;
    private final byte BR=0x4;
    private final byte BL=0x8;
    
    public static final DataHandlerSpeaker instance = new DataHandlerSpeaker(); 
    private DataHandlerSpeaker(){}
    public static DataHandler getInstance()
    {
        return instance;
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
        int cmdid = ApplicationLayer.dataHandlers.indexOf(this);
        ApplicationLayer.sendPacket(ServiceType.SRV_SET,cmdid , userdata);
    }
}
