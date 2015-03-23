/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cc86.MMC.modules.test;

import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Plugin;

/**
 *
 * @author iZc <nplusc.de>
 */
public class TestReceiver implements Plugin
{

    @Override
    public void register()
    {
        API.getDispatcher().registerOnRequestType("Test", new TestProcessor());
    }

    @Override
    public String getName()
    {
        return "Test";
    }
    
}
