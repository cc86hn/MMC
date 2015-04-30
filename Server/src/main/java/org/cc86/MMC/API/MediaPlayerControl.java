/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.API;

import java.util.ArrayList;
import org.cc86.MMC.server.VLCPlayback;

/**
 *
 * @author tgoerner
 */
public class MediaPlayerControl
{

    private static ArrayList<String> playlist = new ArrayList<>();
    private static String nowPlaying = "";

    //TODO logik!
    private static final VLCPlayback backend = new VLCPlayback();

    public static void playURL(String url, boolean enqueue)
    {
        if (!enqueue)
        {
            playlist.clear();
        }

        playlist.add(url);
        if (playlist.size() == 1)
        {
            backend.newFilePlz();
        }
    }

    public static String getTitleToPlay()
    {
        if (!playlist.isEmpty())
        {
            nowPlaying = playlist.remove(0);
            return nowPlaying;
        }
        nowPlaying = "";
        return null;
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
                
            break;
        }
    }

    public static boolean seekable()
    {
        return false;
    }

    public void seek(int seconds)
    {

    }

    public String getCurrentUrl()
    {
        return null;
    }

}
