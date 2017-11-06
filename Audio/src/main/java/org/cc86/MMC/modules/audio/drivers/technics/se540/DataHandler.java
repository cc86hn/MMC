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
public class DataHandler 
{
    
    /**
     * 
     */
    protected ApplicationLayer handler;
    
    protected DataHandler(){};
    
    public void sendGet()
    {
        ApplicationLayer.sendPacket(ServiceType.SRV_GET, ApplicationLayer.dataHandlers.indexOf(this), null);
    }
    
    
    
    /**
     * Processes the Event packet for the given EventID
     * @param userdata Packet of the associated Event
     * @return NACK_RSN or -1 for ACK-ing packet
     */
    public int handleEvent(List<Byte> userdata)
    {
        return TransportLayer.NACK_UNKNOWN;
    }
    
    
    public void handleResponse(List<Byte> userdata)
    {
        
    }
}
