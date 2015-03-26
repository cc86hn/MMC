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
        if(packetData.containsKey("command")&&packetData.get("command")!=null)
        {
            boolean isShortIDMapping=false;
            if(packetData.containsKey("type")&&packetData.get("type")!=null&&(packetData.get("type")).equals("set"))
            {

            }
            
            String cmd = (String) packetData.get("command");
            
            if(cmd.equals("webradioShortID"))
            {
                
            }
            else
            {
                if(cmd.equals("webradio"))
                {
                    
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
