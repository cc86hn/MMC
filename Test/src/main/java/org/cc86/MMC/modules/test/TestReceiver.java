/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cc86.MMC.modules.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Plugin;
import org.cc86.MMC.API.Resources;

/**
 *
 * @author iZc <nplusc.de>
 */
public class TestReceiver implements Plugin
{
    private static final Logger l = LogManager.getLogger();
    @Override
    public void register()
    {
        
        l.info("REGISTERING TestPlugin");
        API.getDispatcher().registerOnRequestType("Test", new TestProcessor());
    }

    @Override
    public String getName()
    {
        return "Test";
    }

    @Override
    public void shutdown()
    {
    }

    @Override
    public void freeUpResources(Resources... res)
    {
        
    }

    @Override
    public void onClientDisconnect(Handler h, boolean graceful)
    {
        
    }
    
}
