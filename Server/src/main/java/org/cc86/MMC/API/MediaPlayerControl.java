/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.API;

import java.util.ArrayList;
import java.util.List;
import org.cc86.MMC.server.VLCPlayback;

/**
 *
 * @author tgoerner
 */
public class MediaPlayerControl
{
    
    static
    {
        shouldntbeneededatall();
    }
    
    
    
    private static String nowPlaying = "";
    private static final List<PlaybackListener> listeners = new ArrayList<>();
    //TODO logik!
    private static final VLCPlayback backend = new VLCPlayback();

    private static void shouldntbeneededatall()
    {
        Thread t = new Thread(()->
        {
            synchronized(MediaPlayerControl.class)
            {
                try
                {
                    MediaPlayerControl.class.wait();
                    listeners.forEach((PlaybackListener l)->l.titleFinished(nowPlaying));
                } catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        t.setName("MediaPlayerEVTDispatch");
    }
    
    
    public static void playURL(String url)
    {
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
        synchronized(MediaPlayerControl.class)
        {

            MediaPlayerControl.class.notify();

        }
        
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
        }
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
