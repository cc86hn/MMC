/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

/**
 *
 * @author tgoerner
 */
public class Main {
    public static final Main m=new Main();
    
    private Dispatcher dispatcher;
    
    public static void main(String[] args) {
        m.bootstrap();
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    
    
    
    private void bootstrap()
    {
        PluginManager m = new PluginManager();
        dispatcher=new Dispatcher(m);
    }
}
