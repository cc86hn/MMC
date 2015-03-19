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
    public void handleRequest(Request r);
    public void getName();
    
}
