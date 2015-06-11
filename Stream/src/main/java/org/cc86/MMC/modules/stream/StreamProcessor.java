/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.stream;

import de.nplusc.izc.tools.baseTools.Tools;
import java.util.HashMap;
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
    public static final String MODE_MIRACAST="miracast";
    public static final String MODE_VNC="vnc";
    public static final String MODE_MP4="mp4";
    public static final String MODE_NONE="nop";
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
            HashMap<String,Object> response=new HashMap<>();
            Packet rsp = new Packet();
            switch(packetData.get("command").toString().toLowerCase())
            {
                case MODE_MIRACAST:
                    //NOP yet
                    
                    response.put("message","Not yet implemented, don't waste your time");
                    response.put("command","miracast");
                    response.put("type","response");
                    
                    break;
                case MODE_VNC:
                    if(set)
                    {
                        target=(String) packetData.get("target_ip");
                        
                        new Thread(()->{
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "kill","-9","omxplayer");
                            //Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "kill","-9","TODO:MIRACAST");
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "xtightvncviewer",target);
                            
                        }).start();
                    }
                    else
                    {   //works cause the mode only gets values from literals
                        
                        if(modeSelected.equals(MODE_VNC))
                        {
                            response.put("target_ip",target);
                            response.put("command","vnc");
                            response.put("type","response");
                        }
                        
                    }     
                    break;
                case MODE_MP4:
                    if(set&&packetData.containsKey("target_ip")&&packetData.get("target_ip")!=null)
                    {
                        target=(String) packetData.get("target_ip");
                        new Thread(()->{
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "kill","-9","xtightvncviewer");
                            //Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "kill","-9","TODO:MIRACAST");
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "omxplayer","udp://:"+target);
                            
                        }).start();
                    }
                    else
                    {   //works cause the mode only gets values from literals
                        
                        if(modeSelected.equals(MODE_VNC))
                        {
                            response.put("target_port",target);
                            response.put("command","vnc");
                            response.put("type","response");
                        }
                        
                    }     
                    break;
            }
            rsp.setData(response);
            h.respondToLinkedClient(rsp);
        }
    }
    
}
