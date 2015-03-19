/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import org.cc86.MMC.API.Request;

/**
 *
 * @author tgoerner
 */
public class Dispatcher {
    
    PluginManager pm;
    public Dispatcher(PluginManager p)
    {
        pm=p;
    }
    
    public void handleEvent(Request r)
    {
        
    }
    
    
}
