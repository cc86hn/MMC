/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import java.util.HashMap;
import org.cc86.MMC.API.Processor;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.API.Handler;

/**
 *
 * @author tgoerner
 */
public class Dispatcher {
    private HashMap<String,Processor> dispatcherLogic = new HashMap<>();
    PluginManager pm;
    public Dispatcher(PluginManager p)
    {
        pm=p;
        dispatcherLogic.put("exit", new Exitter());
    }
    
    public synchronized void handleEvent(Packet r,Handler h)
    {
        String lm = (String) r.getData().get("command");
        System.out.println("cmd=="+lm);
        Processor p = dispatcherLogic.get(lm); 
        System.out.println(p);
        if(p!=null)
        {
            System.out.println("Einsteigen bitte");
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
