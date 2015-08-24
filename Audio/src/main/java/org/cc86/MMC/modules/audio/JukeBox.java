/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;


import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Packet;

/**
 *
 * @author tgoerner
 */
public class JukeBox
{
    private static final Logger l = LogManager.getLogger();
    private HashMap<String,String> pool = new HashMap<>();
    private HashMap<Handler,HashMap<String,String>> userLists = new HashMap<>();

    public void updatePool(Packet p, Handler h)
    {
        userLists.put(h, (HashMap<String, String>) p.getData().get("pool"));
        pool.clear();
        userLists.forEach((k,v)->pool.putAll(v));
        Packet evt = new Packet();
        HashMap<String,Object> evtdata = new HashMap<>();
        evtdata.put("command","playback_pool");
        evtdata.put("type","response");
        evtdata.put("pool",pool);
        evt.setData(evtdata);
        API.dispatchEvent(evt);
    }
}
