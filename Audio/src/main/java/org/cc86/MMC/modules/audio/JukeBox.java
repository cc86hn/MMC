/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.MediaPlayerControl;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.API.PlaybackListener;
import org.cc86.MMC.API.PlaybackMode;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author tgoerner
 */
public class JukeBox implements PlaybackListener
{
    
    private boolean isPlaying=false;
    private static final Logger l = LogManager.getLogger();
    private HashMap<String,String> pool = new HashMap<>();
    private HashMap<Handler,List<String>> userLists = new HashMap<>();
    private List<String[]> queue = new ArrayList<>();
    public JukeBox()
    {
        MediaPlayerControl.registerListener(this);
    }


    
    
    
    
    public void updatePool(Packet p, Handler h)
    {
        userLists.put(h, (List<String>) p.getData().get("pool"));
        processPool();
    }
    
    private void processPool()
    {
        pool.clear();
        userLists.forEach((k,v)->
        {
            final String sip = k.getClientIP();v.forEach((v2)->pool.put(v2, sip+"/"+v2));
        });
        
        Packet evt = new Packet();
        HashMap<String,Object> evtdata = new HashMap<>();
        evtdata.put("command","playback_pool");
        evtdata.put("type","response");
        evtdata.put("pool",pool);
        evt.setData(evtdata);
        API.dispatchEvent(evt);
        //TODO: sync queue with pool to remove nonexistent refs
        List<String[]> killList = new ArrayList<>();
        queue.forEach((qi)->{if(!pool.containsValue(qi[2])){killList.add(qi);}});
        killList.forEach((qi)->queue.remove(qi));
    }
    
    
    public void start_playback(Packet p, Handler h)
    {
        l.info("Playback enqueue");
        l.trace(new Yaml().dump(p));
        HashMap<String,Object> data = p.getData();
        String trkid = (String) data.get("path");
        String[] fp = trkid.split("/");
        boolean enqueue = (boolean) data.get("scheduled");
        l.info("scheduled="+enqueue);
        String streamurl = "http://"+fp[0]+":9265/"+fp[1];
        if(enqueue&&(!queue.isEmpty()||isPlaying))
        {
            l.trace("Enqueueing");
            queue.add(new String[]{fp[1], streamurl,trkid});
        }
        else
        {
            isPlaying=true;
            l.trace("playing");
            MediaPlayerControl.playURL(streamurl);
        }
        sendQueue();
    }
    
    @Override
    public void titleFinished(String title)
    {
        if(queue.size()>=1)
        {
            MediaPlayerControl.playURL(queue.get(0)[1]);
        }
        sendQueue();
    }


    private void sendQueue()
    {
        ArrayList<String> lst = new ArrayList<>();
        queue.forEach((e)->lst.add(e[1]));

        Packet evt = new Packet();
        HashMap<String,Object> evtdata = new HashMap<>();
        evtdata.put("command","playback_jukebox");
        evtdata.put("type","response");
        evtdata.put("list",lst);
        evt.setData(evtdata);
        API.dispatchEvent(evt);
    }
    void freeUpAudio()
    {
        if(isPlaying)
        {
            MediaPlayerControl.control(PlaybackMode.STOP);
        }
    }
    void onClientDisconnect(Handler h, boolean graceful)
    {
        userLists.remove(h);
        processPool();
    }
    
}
