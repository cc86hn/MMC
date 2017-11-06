/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

import java.util.ArrayList;
import java.util.List;
import static org.cc86.MMC.modules.audio.drivers.technics.se540.DataHandlerEventAll.instance;

/**
 *
 * @author tgoerner
 */
public class DataHandlerVolumeVolrel extends DataHandler
{
    public static final DataHandlerVolumeVolrel instance = new DataHandlerVolumeVolrel(); 
    private DataHandlerVolumeVolrel(){}
    public static DataHandler getInstance()
    {
        return instance;
    }
    
    public void changeVolumeRel(byte newVolume)
    {
        List<Byte> userdata = new ArrayList<>();
        userdata.add(newVolume);
        int cmdid = ApplicationLayer.dataHandlers.indexOf(this);
        ApplicationLayer.sendPacket(ServiceType.SRV_SET,cmdid , userdata);
    }
}
