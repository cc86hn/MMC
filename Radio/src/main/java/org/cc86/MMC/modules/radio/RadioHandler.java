/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.radio;

import de.schlichtherle.truezip.file.TFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.MediaPlayerControl;
import org.cc86.MMC.Networking.Packet;
import org.cc86.MMC.API.PlaybackListener;
import org.cc86.MMC.API.PlaybackMode;
import org.cc86.MMC.API.Processor;
import org.cc86.MMC.API.Resources;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author tgoerner
 */
public class RadioHandler implements Processor,PlaybackListener{

    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<String,String> shortIdMappings;
    private static final org.apache.logging.log4j.Logger l = LogManager.getLogger();
    private String currentURL="";
    private String mappingspath = API.SETTINGSPATH+File.separator+"radiolist.yml";
    private boolean streamIsPlaying=false;
    public RadioHandler()
    {
        new TFile(API.SETTINGSPATH).mkdir();
        TFile radiodata = new TFile(mappingspath);
        if(radiodata.exists())
        {
            try {
                shortIdMappings=(HashMap<String, String>) new Yaml().load(new FileInputStream(radiodata));
            } catch (FileNotFoundException ex) {
                shortIdMappings=new HashMap<>();
                Logger.getLogger(RadioHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            shortIdMappings=new HashMap<>();
            shortIdMappings.put("SWR1", "http://mp3-live.swr.de/swr1bw_m.m3u");
            shortIdMappings.put("Radioton", "http://live.radioton.de/live128");
        }
        MediaPlayerControl.registerListener(this);
    }
    
    @Override
    public void process(Packet r, Handler h) {
        
        HashMap<String,Object> packetData = r.getData();
        boolean set=false;
        if(packetData.containsKey("command")&&packetData.get("command")!=null)
        {
            if(packetData.containsKey("type")&&packetData.get("type")!=null&&(packetData.get("type")).equals("set"))
            {
                set=true;
            }
            
            String cmd = (String) packetData.get("command");
            //		shortID
            //		URL
            if(cmd.equals("webradioShortID"))
            {
                if(set&&packetData.containsKey("URL")&&packetData.get("URL")!=null&&packetData.containsKey("shortID")&&packetData.get("shortID")!=null)
                {
                    String url=(String) packetData.get("URL");
                    String shortID = (String) packetData.get("shortID");
                    if(!shortIdMappings.values().contains(shortID))//schutz gegen url-zinkung von vorhandenen URLs
                    {
                        shortIdMappings.put(url, shortID);
                    }
                }
                else
                {
                    HashMap<String,Object> response=new HashMap<>();
                    response.put("mappings",shortIdMappings);
                    response.put("command","webradioShortID");
                    response.put("type","response");
                    Packet rsp = new Packet();
                    rsp.setData(response);
                    h.respondToLinkedClient(rsp);
                }
            }
            else
            {
                if(cmd.equals("webradio"))
                {
                    if(set&&packetData.containsKey("URL")&&packetData.get("URL")!=null)
                    {
                        String url=(String) packetData.get("URL");
                        if(shortIdMappings.containsKey(url))
                        {
                            url=shortIdMappings.get(url);
                        }
                        MediaPlayerControl.playURL(url);
                        currentURL=url;
                        streamIsPlaying=true;
                        
                        
                    }
                    else
                    {
                        if(set&&packetData.containsKey("STOP")&&packetData.get("STOP")!=null)
                        {
                            if(streamIsPlaying)
                            {
                                MediaPlayerControl.control(PlaybackMode.STOP);
                                l.info("BEEP");
                                currentURL="";
                                streamIsPlaying=false;
                            }
                        }
                        else
                        {
                            HashMap<String,Object> response=new HashMap<>();
                            response.put("URL",currentURL);
                            response.put("command","webradio");
                            response.put("type","response");
                            Packet rsp = new Packet();
                            rsp.setData(response);
                            h.respondToLinkedClient(rsp);
                        }
                        
                    }
                    if(set)
                    {
                        API.makeSimpleEvent("webradio", "URL", currentURL);
                    }
                }
                else
                {
                    //ERROR
                }
            }
        }
        else
        {
            //ERROR
        }
        
        
       

            
        
    }
    /*packageprotected*/ void shitdown()
    {
        try
        {
            new Yaml().dump(shortIdMappings, new FileWriter(new TFile(mappingspath)));
        } catch (IOException ex)
        {
            Logger.getLogger(RadioHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*packageprotected*/void freeRsrcs(Resources... r)
    {
        for (Resources r1 : r)
        {
            if(r1==Resources.AUDIODATA)
            {
                MediaPlayerControl.control(PlaybackMode.STOP);
            }
        }
    }
    
    
}
