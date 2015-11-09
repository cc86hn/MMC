/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.server.VLCPlayback;

/**
 *
 * @author tgoerner
 */
public class MediaPlayerControl
{
     private static final Logger l = LogManager.getLogger();
    protected enum Eventtype{START,STOP;};
    static
    {
        //fugly HACK, fuck you javac for spitting out a error
        threadlauncher();
    }
    
    
    
    private static String nowPlaying = "";
    private static final List<PlaybackListener> listeners = new ArrayList<>();
    //TODO logik!
    private static final VLCPlayback backend = new VLCPlayback();
    private static Eventtype next = Eventtype.STOP;
    private static boolean loop=false;
    private static void threadlauncher()
    {
        l.trace("Launching threads....");
        Thread t = new Thread(()->
        {
            l.trace("EDT of Musix launched....");
            synchronized(MediaPlayerControl.class)
            {
                while(true)
                {
                    try
                    {
                        l.trace("FinishTrigger_EVTThread");
                        MediaPlayerControl.class.wait();
                        switch(next)
                        {
                            case STOP:
                            listeners.forEach((PlaybackListener ls)->ls.titleFinished(nowPlaying));
                            break;
                            case START:
                                listeners.forEach((PlaybackListener ls)->ls.trackStarting(nowPlaying));
                            break;
                        }
                    } catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });
        t.setName("MediaPlayerEVTDispatch");
        t.start();
    }
    
    
    public static void playURL(String url)
    {
        
        synchronized(MediaPlayerControl.class)
        {
            next = Eventtype.START;
            l.trace("FinishTrigger_MainThread");
            MediaPlayerControl.class.notifyAll();
        }
        backend.addTitle(url);
        nowPlaying=url;
    }

    public static void registerListener(PlaybackListener l)
    {
        if(!listeners.contains(l))
        {
            listeners.add(l);
        }
    }
    
    public static void unregisterListener(PlaybackListener l)
    {
        if(listeners.contains(l))
        {
            listeners.remove(l);
        }
    }
    
    
    public static void trackFinishedTriggered()
    {
        if(!loop)
        {
            synchronized(MediaPlayerControl.class)
            {
                next = Eventtype.STOP;
                l.trace("FinishTrigger_MainThread");
                MediaPlayerControl.class.notifyAll();
            }
        }
        else
        {
            playURL(nowPlaying);
        }
    }
    
    public static int getTime()
    {
        return backend.getPosition();
    }
    public static int getLength()
    {
        return backend.getLengthInSeconds();
    }
    
    
    public static void control(PlaybackMode m)
    {
        switch (m)
        {
            case PLAY:
                backend.play();
                break;
            case PAUSE:
                backend.pause();
                break;
            case FASTER:
               
                break;
            case SLOWER:
                break;
            case SKIP:
                backend.skipTitle();
                break;
            case STOP:
                backend.pause();
                break;
            case LOOP:
                loop=!loop;
                break;
        }
        API.makeSimpleEvent("playback_control", "action", (m+"").toLowerCase());
    }

    public static boolean seekable()
    {
        return backend.isSeekable();
    }

    public static  void seek(int seconds)
    {
        backend.seek(seconds);
    }

    public static String getCurrentUrl()
    {
        return nowPlaying;
    }

}
