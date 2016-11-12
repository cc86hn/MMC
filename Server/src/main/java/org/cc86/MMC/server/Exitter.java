/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import java.util.HashMap;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.API.Processor;

/**
 *
 * @author tgoerner
 */
public class Exitter implements Processor
{

    @Override
    public void process(Packet r, Handler h)
    {
        HashMap<String,Object> packetData=r.getData();
        if(packetData.containsKey("command")&&packetData.get("command")!=null&&((String)packetData.get("command")).equalsIgnoreCase("exit"))
        {
            Main.m.shitdownHandler();
        }
    }
    
}
