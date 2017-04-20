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
    protected ProtocolHandler handler;
    
    protected DataHandler(){};
    
    public void sendGet()
    {
        handler.send_packet(0, ProtocolHandler.SRV_GET, handler.dataHandlers.indexOf(this), null, null);
    }
    
    
    /**
     * Processes the Event packet for the given EventID
     * @param packet Packet of the associated Event
     * @return NACK_RSN or -1 for ACK-ing packet
     */
    public int handleEvent(List<Byte> packet)
    {
        return ProtocolHandler.NACK_UNKNOWN;
    }
}
