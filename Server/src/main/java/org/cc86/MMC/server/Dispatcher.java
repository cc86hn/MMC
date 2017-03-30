/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.Processor;
import org.cc86.MMC.Networking.Packet;
import org.cc86.MMC.API.Handler;

/**
 *
 * @author tgoerner
 */
public class Dispatcher {
    
    private static final Logger l = LogManager.getLogger();
    private HashMap<String,Processor> dispatcherLogic = new HashMap<>();
    PluginManager pm;
    public Dispatcher(PluginManager p)
    {
        pm=p;
        dispatcherLogic.put("exit", new Exitter());
        dispatcherLogic.put("event", new EventManager());
    }
    
    public synchronized void handleEvent(Packet r,Handler h)
    {
        l.info("Handling event");
        String lm = (String) r.getData().get("command");
        l.info("Command:"+lm);
        Processor p = dispatcherLogic.get(lm); 
        l.trace(p);
        if(p!=null)
        {
            l.trace("Einsteigen bitte");
            p.process(r,h);
        }
        else
        {
            //Errorhandling
        }
    }
    
    public void registerOnRequestType(String requestType,Processor p)
    {
        dispatcherLogic.put(requestType, p);
    }
}
