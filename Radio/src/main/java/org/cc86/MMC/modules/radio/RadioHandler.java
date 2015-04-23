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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.MediaPlayerControl;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.API.Processor;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author tgoerner
 */
public class RadioHandler implements Processor{

    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<String,String> shortIdMappings;
    private String currentURL="";
    public RadioHandler()
    {
        new TFile(API.SETTINGSPATH).mkdir();
        TFile radiodata = new TFile(API.SETTINGSPATH+File.separator+"radiolist.yml");
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
        }
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
                    response.put("command","webradio");
                    response.put("type","response");
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
                        MediaPlayerControl.playURL(url, false);
                        currentURL=url;
                    }
                    else
                    {
                        HashMap<String,Object> response=new HashMap<>();
                        response.put("URL",currentURL);
                        response.put("command","webradio");
                        response.put("type","response");
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
    
}
