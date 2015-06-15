/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 *
 * @author tgoerner
 */
public class Main {
    private static final Logger l = LogManager.getLogger();
    public static final Main m=new Main();
    private PluginManager mgr;
    private Dispatcher dispatcher;
    private ServerCore core;
    
    
    public static void main(String[] args) {
        setupLogging(true);//TODO CMDLINE
        m.bootstrap();
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    
    private  void setupLibraries()
    {                    
        /*String vlcpath="/home/pi/codestuff/vlc";
        if(API.APPDIR.charAt(1)==':')
        {
            vlcpath=API.WINVLC;
        }
         NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcpath);//TODO LNX_PATH
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);*/
    }
    
    
    
    
    private void bootstrap()
    {
        setupLibraries();
        Thread discovery = new Thread(Discovery.getInstance());
        discovery.setName("DISCOVERY");
        discovery.start();
        mgr = new PluginManager();
        dispatcher=new Dispatcher(mgr);
        core=new ServerCore(0xCC86);
        mgr.loadPlugins();
        Thread t = new Thread(()->
        core.bootUp());
        t.setName("TCP listener");
        t.start();
    }
    
    
    
    /*packageprotected*/ void shitdownHandler()
    {
        //Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "killall","xtightvncviewer");
        mgr.shitdown();
        System.exit(0);
    }
    
    
    private static void setupLogging(boolean verbose)
    {
        
        
        //if(!cl.hasOption("verbose")&&!(System.getProperty("log4j.configurationFile")==null))
        //{
        //    System.setProperty("log4j.configurationFile", "file:///"+jarschiv+"!log4j2NonVerbose.xml");
        //}
        LoggerContext cx = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration config = cx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME); 
        LoggerConfig externalloggerConfig = config.getLoggerConfig("External"); 

        if (verbose)
        {
            
            loggerConfig.setLevel(Level.TRACE);
            externalloggerConfig.setLevel(Level.TRACE);
        }
        else
        {
            loggerConfig.setLevel(Level.INFO);
            externalloggerConfig.setLevel(Level.INFO);
        }
        cx.updateLoggers();
    }
}
