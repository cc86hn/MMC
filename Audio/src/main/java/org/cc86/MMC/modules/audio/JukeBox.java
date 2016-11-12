/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
    private String currentTrackTitle = "";
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
        API.makeSimpleEvent("playback_pool", "pool", pool);
        List<String[]> killList = new ArrayList<>();
        queue.forEach((qi)->{if(!pool.containsValue(qi[2])){killList.add(qi);}});
        killList.forEach((qi)->queue.remove(qi));
    }
    
    
    public void start_playback(Packet p, Handler h)
    {
        try
        {
            l.info("Playback enqueue");
            l.trace(new Yaml().dump(p));
            HashMap<String,Object> data = p.getData();
            String trkid = (String) data.get("path");
            String[] fp = trkid.split("/");
            boolean enqueue = (boolean) data.get("scheduled");
            l.info("scheduled="+enqueue);
            String streamurl = "http://"+fp[0]+":9265/"+URLEncoder.encode(fp[1], "UTF-8");;
            if(enqueue&&(!queue.isEmpty()||isPlaying))
            {
                l.trace("Enqueueing");
                queue.add(new String[]{fp[1], streamurl,trkid});
            }
            else
            {
                currentTrackTitle = fp[1];
                isPlaying=true;
                l.trace("playing");
                MediaPlayerControl.playURL(streamurl);
            }
            sendQueue();
        } catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void trackStarting(String title)
    {
        Packet evt = new Packet();
        HashMap<String,Object> evtdata = new HashMap<>();
        evtdata.put("command","playback_status");
        evtdata.put("type","response");
        evtdata.put("title",currentTrackTitle);
        evtdata.put("duration",MediaPlayerControl.getLength());
        evtdata.put("time",MediaPlayerControl.getTime());
        evtdata.put("seekable",MediaPlayerControl.seekable());
        evt.setData(evtdata);
        API.dispatchEvent(evt);
    }
    
    
    
    @Override
    public void titleFinished(String title)
    {
        l.trace("FinishTrigger_JBX");
        if(queue.size()>=1)
        {
            MediaPlayerControl.playURL(queue.get(0)[1]);
            currentTrackTitle = queue.get(0)[0];
            queue.remove(0);
        }
        else
        {
            isPlaying=false;
            MediaPlayerControl.control(PlaybackMode.STOP);
            Packet evt = new Packet();
            HashMap<String,Object> evtdata = new HashMap<>();
            evtdata.put("command","playback_status");
            evtdata.put("type","response");
            evtdata.put("title","");
            evtdata.put("duration",0);
            evtdata.put("time",0);
            evtdata.put("seekable",false);
            evt.setData(evtdata);
            API.dispatchEvent(evt);
            
            
        }
        sendQueue();
    }

    public void playback_control(Packet p, Handler h)
    {
        String act = p.getData().get("action")+"";
        l.trace("action://"+act);
        switch(act.toLowerCase())
        {
            case "skip":
                MediaPlayerControl.control(PlaybackMode.SKIP);
            break;
            case "play":
                MediaPlayerControl.control(PlaybackMode.PLAY);
            break;
            case "pause":
                MediaPlayerControl.control(PlaybackMode.PAUSE);
            break;
            case "stop":
                isPlaying=false;
                MediaPlayerControl.control(PlaybackMode.STOP);
            break;
            case "loop":
                MediaPlayerControl.control(PlaybackMode.LOOP);
            break;
        }   
    }
    private void sendQueue()
    {
        ArrayList<String> lst = new ArrayList<>();
        queue.forEach((e)->lst.add(e[1]));
        API.makeSimpleEvent("playback_jukebox", "list", lst);
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
