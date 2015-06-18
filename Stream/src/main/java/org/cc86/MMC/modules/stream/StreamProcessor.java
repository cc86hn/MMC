/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.stream;

import de.nplusc.izc.tools.baseTools.Tools;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.API.Processor;

/**
 *
 * @author tgoerner
 */
public class StreamProcessor implements Processor
{
    private static final Logger l = LogManager.getLogger();
    public static final String MODE_MIRACAST="miracast";
    public static final String MODE_VNC="vnc";
    public static final String MODE_MP4="mp4";
    public static final String MODE_STOPALL="stopstream";
    public static final String MODE_NONE="nop";
    private Handler lastConnected;
    private String modeSelected=MODE_NONE;
    private String target="";
    @Override
    public void process(Packet r, final Handler h)
    {
        
        
        
        HashMap<String,Object> packetData = r.getData();
        boolean set=false;
        if(packetData.containsKey("command")&&packetData.get("command")!=null)
        {
            if(packetData.containsKey("type")&&packetData.get("type")!=null&&(packetData.get("type")).equals("set"))
            {
                set=true;
            }
            
            HashMap<String,Object> disconnectPKG=new HashMap<>();
            disconnectPKG.put("disconnect","disconnect");
            disconnectPKG.put("command","stream");
            disconnectPKG.put("type","response");
            Packet disconnector = new Packet();
            disconnector.setData(disconnectPKG);
            h.respondToLinkedClient(disconnector);
            HashMap<String,Object> response=new HashMap<>();
            Packet rsp = new Packet();
            response.put("type","response");
            switch(packetData.get("command").toString().toLowerCase())
            {
                case MODE_STOPALL:
                        Tools.runCmdWithPassthru(IoBuilder.forLogger("External.OMX").buildPrintStream(), "killall","xtightvncviewer");
                        Tools.runCmdWithPassthru(IoBuilder.forLogger("External.OMX").buildPrintStream(), "killall","omxplayer.bin");
                    break;
                case MODE_MIRACAST:
                    //NOP yet
                    
                    response.put("message","Not yet implemented, don't waste your time");
                    response.put("command","miracast");
                    
                    break;
                case MODE_VNC:
                    if(set)
                    {
                        String nTarget = h.getClientIP();
                        if(modeSelected.equals(MODE_VNC))
                        {
                            if(target.equals(nTarget))
                            {
                                break; //NOTHING TO DO
                            }
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "killall","xtightvncviewer");
                        }
                        target=h.getClientIP();
                        modeSelected=MODE_VNC;
                        l.info("target="+target);
                        
                        new Thread(()->{
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "killall","omxplayer.bin");
                            //Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "kill","-9","TODO:MIRACAST");
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "xtightvncviewer",target);
                            
                        }).start();
                    }
                    else
                    {   //works cause the mode only gets values from literals
                        
                        if(modeSelected.equals(MODE_VNC))
                        {
                        response.put("command","stream");
                        response.put("message","connected");
                        }
                        
                    }     
                    break;
                case MODE_MP4:
                    if(set&&packetData.containsKey("target_port")&&packetData.get("target_port")!=null)
                    {
                        target=(String) packetData.get("target_port");
                        new Thread(()->{
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.OMX").buildPrintStream(), "killall","xtightvncviewer");
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.OMX").buildPrintStream(), "killall","omxplayer.bin");
                            //Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "kill","-9","TODO:MIRACAST");
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.OMX").buildPrintStream(), "omxplayer","udp://:"+target);
                            
                        }).start();
                        response.put("command","stream");
                        response.put("message","connected");
                    }
                    else
                    {   //works cause the mode only gets values from literals
                        
                        if(modeSelected.equals(MODE_VNC))
                        {
                            response.put("target_port",target);
                            response.put("command","mp4");
                        }
                    }     
                    break;
            }
            rsp.setData(response);
            h.respondToLinkedClient(rsp);
            lastConnected=h;
        }
    }
    
}
