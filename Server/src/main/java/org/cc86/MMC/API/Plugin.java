/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.API;

/**
 *
 * @author tgoerner
 */
public interface Plugin {
    /**
     * gets triggered on pluginload so the plugin can register its available listeners for certain Messages
     */
    public void register();
    /**
     * gets the name of the plugin for the Pluginlist
     * @return Name of the plugin
     */
    public String getName();
    
    
    
    /**
     * Requests the plugins to unblock the given resources if taken
     * @param res 
     */
    public void freeUpResources(Resources... res);
    
    
    
    /**
     * event that runs before the program shutdowns
     */
    public void shutdown();
      
}
