/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tgoerner
 */
public class DataHandlerEventAll extends DataHandler
{
    private static final Logger l = LogManager.getLogger();
    public static final DataHandlerEventAll instance = new DataHandlerEventAll(); 
    private DataHandlerEventAll(){};
    
    public static DataHandler linkHandler(ProtocolHandler h)
    {
        instance.handler=h;
        return instance;
    }
    
    public void registerEvents()
    {
        List<Byte> userdata = new ArrayList<>();
        userdata.add((byte)0xff);
        userdata.add((byte)0xff);
        userdata.add((byte)0xff);
        userdata.add((byte)0xff);
        int cmdid = handler.dataHandlers.indexOf(this);
        l.trace("prepared EvtAll, cmdid={}",cmdid);
        handler.send_packet(ProtocolHandler.SRV_SET,cmdid , userdata, null);
    }
}

