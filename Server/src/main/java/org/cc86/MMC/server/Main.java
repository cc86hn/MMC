/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 *
 * @author tgoerner
 */
public class Main {
    public static final Main m=new Main();
    private PluginManager mgr;
    private Dispatcher dispatcher;
    private ServerCore core;
    
    
    public static void main(String[] args) {
        m.bootstrap();
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    
    private void setupLibraries()
    {                                                   
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/home/pi/codestuff/vlc");//TODO LNX_PATH
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
    }
    
    
    
    
    private void bootstrap()
    {
        mgr = new PluginManager();
        dispatcher=new Dispatcher(mgr);
        core=new ServerCore(9264);
        mgr.loadPlugins();
        Thread t = new Thread(()->
        core.bootUp());
        t.setName("TCP listener");
        t.start();
    }
}
