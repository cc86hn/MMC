/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.stream;

import de.nplusc.izc.tools.baseTools.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.API.Processor;
import org.cc86.MMC.API.Resources;
import org.yaml.snakeyaml.Yaml;

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
    public static final String MODE_STOPALL="stopall";
    public static final String MODE_STREAM="stream";
    public static final String MODE_NONE="nop";
    private boolean audioOn=false;
    private List<Handler> streamSources = new ArrayList<>();
    private boolean autoSwitch=false;
    
    //private Handler lastConnected;
    private String modeSelected=MODE_NONE;
    private String target="";
    
    /*packageprotected*/ void freeResources(Resources... r)
    {
        for (Resources r1 : r)
        {
            if(r1==Resources.VIDEODATA||(audioOn&&r1==Resources.AUDIODATA))
            {
                Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "killall","xtightvncviewer");
                Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "killall","omxplayer.bin");
                autoSwitch=false;
            }
        }
    }
    
    
    
    
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
            String command = packetData.get("command").toString().toLowerCase();
            if(packetData.containsKey("disconnect"))
            {
                if(!streamSources.isEmpty())
                {
                    Handler last = streamSources.get(streamSources.size()-1);
                    
                    if(h.equals(last))
                    {
                        stopStream();
                    }
                    if(streamSources.contains(h))
                    {
                        streamSources.remove(h);
                        l.info("removed stream from "+h.getClientIP()+";\n reconnect = "+(autoSwitch?"true":"false"));
                        l.trace(new Yaml().dump(streamSources));
                    }
                    if(!streamSources.isEmpty()&&autoSwitch)
                    {
                        Handler resumer = streamSources.get(streamSources.size()-1);
                        HashMap<String,Object> reconnectPKG=new HashMap<>();
                        reconnectPKG.put("reconnect","reconnect");
                        reconnectPKG.put("command","stream");
                        reconnectPKG.put("type","response");
                        Packet reconnector = new Packet();
                        reconnector.setData(reconnectPKG);
                        resumer.respondToLinkedClient(reconnector);
                    }
                }
                return;
            }
            
            if(!streamSources.isEmpty())
            {
                Handler hdl = streamSources.get(streamSources.size()-1);
                if(!hdl.equals(h))
                {
                    HashMap<String,Object> disconnectPKG=new HashMap<>();
                    disconnectPKG.put("disconnect","disconnect");
                    disconnectPKG.put("command","stream");
                    disconnectPKG.put("type","response");
                    Packet disconnector = new Packet();
                    disconnector.setData(disconnectPKG);
                    hdl.respondToLinkedClient(disconnector);
                }
            }
            HashMap<String,Object> response=new HashMap<>();
            Packet rsp = new Packet();
            response.put("type","response");
            
            autoSwitch=true;
            if(streamSources.contains(h))
            {
                streamSources.remove(h);
            }
            
            audioOn=false;
            switch(command)
            {
                case MODE_STOPALL:
                    
                       
                        if(!streamSources.isEmpty())
                        {
                            Handler hdl = streamSources.get(streamSources.size()-1);
                            HashMap<String,Object> disconnectPKG=new HashMap<>();
                            disconnectPKG.put("disconnect","disconnect");
                            disconnectPKG.put("command","stream");
                            disconnectPKG.put("type","response");
                            Packet disconnector = new Packet();
                            disconnector.setData(disconnectPKG);
                            hdl.respondToLinkedClient(disconnector);
                        }
                        stopStream();
                        streamSources.clear();
                    break;
                case MODE_MIRACAST:
                    //NOP yet
                    
                    response.put("message","Not yet implemented, don't waste your time");
                    response.put("command","miracast");
                    
                    break;
                    
                case MODE_VNC:
                    if (packetData.containsKey("mode")&&packetData.get("mode").equals("slow"))
                    {
                        API.requestResourcesFree(Resources.AUDIODATA);
                    }
                    if (set)
                    {
                        //String nTarget = h.getClientIP();
                        if(modeSelected.equals(MODE_VNC))
                        {
                            if(!streamSources.isEmpty()&&streamSources.get(streamSources.size()-1).equals(h))
                            {
                                l.info("Useless Reconnect!");
                                break; //NOTHING TO DO
                            }
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "killall","xtightvncviewer");
                        }
                        target=h.getClientIP();
                        modeSelected=MODE_VNC;
                        l.info("target="+target);
                        
                        new Thread(()->{
                            Resources.setResourceStatus(Resources.VIDEODATA, true);
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.IXX").buildPrintStream(), "killall","omxplayer.bin");
                            //Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "kill","-9","TODO:MIRACAST");
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "xtightvncviewer",target);
                            
                        }).start();
                        response.put("command","stream");
                        response.put("message","connected");
                    }
                    else
                    {  
                    }     
                    break;
                case MODE_MP4:
                    if(set&&packetData.containsKey("target_port")&&packetData.get("target_port")!=null)
                    {
                        target=(String) packetData.get("target_port");
                        new Thread(()->{
                            Resources.setResourceStatus(Resources.VIDEODATA, true);
                            if(packetData.get("mode").equals("slow"))
                            {
                                Resources.setResourceStatus(Resources.AUDIODATA, true);
                                audioOn=true;
                            }
                            stopStream();
                            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.OMX").buildPrintStream(), "omxplayer","udp://:"+target);
                        }).start();
                        
                        response.put("command","stream");
                        response.put("message","connected");
                    }
                    else
                    {   //works cause the mode only gets values from literals
                    }     
                    break;
                default:
                    return;
            }
            if(set)
            {
                String[] evtparam;
                String tport = ""+(command.equals(MODE_MP4)?target:0);
                if(streamSources.isEmpty())
                {
                    evtparam = new String[]{"","",""};
                }
                else
                {
                    evtparam = new String[]{h.getClientIP(),tport,command};
                }
                API.makeSimpleEvent("stream", "streamsource", evtparam);
            }
            if(!set)
            {
                String tport = ""+(command.equals(MODE_MP4)?target:0);
                response.put("command","stream");
                if(streamSources.isEmpty())
                {
                    response.put("streamsource",new String[]{"","",""});
                }
                else
                {
                    response.put("streamsource",new String[]{h.getClientIP(),tport,command});
                }
                response.put("streamsource",new String[]{h.getClientIP(),tport,command});
            }
            else
            {
            }
            streamSources.add(h);
            rsp.setData(response);
            h.respondToLinkedClient(rsp);
        }
    }
    
    
    void stopStream()
    {
        Tools.runCmdWithPassthru(IoBuilder.forLogger("External.IXX").buildPrintStream(), "killall","xtightvncviewer");
        Tools.runCmdWithPassthru(IoBuilder.forLogger("External.IXX").buildPrintStream(), "killall","omxplayer.bin");
        //Tools.runCmdWithPassthru(IoBuilder.forLogger("External.VNC").buildPrintStream(), "kill","-9","TODO:MIRACAST")
    }
    
}
