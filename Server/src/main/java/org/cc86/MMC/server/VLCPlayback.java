/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.MediaPlayerControl;
import org.cc86.MMC.API.StatusMode;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

/**
 *
 * @author tgoerner
 */
public class VLCPlayback implements MediaPlayerEventListener
{
    private static final Logger l = LogManager.getLogger();
    private final MediaPlayer mpaccess;
    private final MediaPlayerFactory f;
    public VLCPlayback()
    {
        if(!API.getMockMode())
        {
            f = new MediaPlayerFactory("--no-video-title-show");
            mpaccess = f.newHeadlessMediaPlayer();
            mpaccess.addMediaPlayerEventListener(this);
        }
        else{
            f=null;mpaccess=null;
        }
    }

    public void newFilePlz()
    {
        l.info("Track finished");
        MediaPlayerControl.trackFinishedTriggered();
    }

    public void addTitle(String path)
    {
        l.trace("TrackSwitch://"+path);
        if(mpaccess!=null)
        {
            mpaccess.stop();
            mpaccess.playMedia(path);
        }
    }

    public int getLengthInSeconds()
    {
        if(mpaccess==null)
        {
            return 0;
        }
        return (int) (Math.ceil(mpaccess.getLength() / 1000f));
    }

    public int getPosition()
    {
        if(mpaccess==null)
        {
            return 0;
        }
        return (int) (Math.ceil((mpaccess.getLength() / 1000f) * mpaccess.getPosition()));
    }

    public boolean isSeekable()
    {
        if(mpaccess==null)
        {
            return false;
        }
        return mpaccess.isSeekable();
    }

    public void seek(int sekunde)
    {
        if (isSeekable())
        {
            mpaccess.setPosition(((float) sekunde) / ((float) getLengthInSeconds()));
        }
    }

    public void skipTitle()
    {
        l.trace("SkipCore");
        if (StatusMode.getAudioMode() == StatusMode.FILE)//feature nur bei DLNA-mode verfügbar
        {
            newFilePlz();
        }
    }

    public void play()
    {
        if(mpaccess==null)
        {
            return;
        }
        mpaccess.play();
    }

    public void pause()
    {       
        if(mpaccess==null)
        {
            return;
        }
        mpaccess.pause();
    }

    @Override
    public void mediaChanged(MediaPlayer mp, libvlc_media_t l, String string)
    {

    }

    @Override
    public void opening(MediaPlayer mp)
    {

    }

    @Override
    public void buffering(MediaPlayer mp, float f)
    {

    }

    @Override
    public void playing(MediaPlayer mp)
    {

    }

    @Override
    public void paused(MediaPlayer mp)
    {

    }

    @Override
    public void stopped(MediaPlayer mp)
    {

    }

    @Override
    public void forward(MediaPlayer mp)
    {

    }

    @Override
    public void backward(MediaPlayer mp)
    {

    }

    @Override
    public void finished(MediaPlayer mp)
    {
        newFilePlz();
    }

    @Override
    public void timeChanged(MediaPlayer mp, long l)
    {

    }

    @Override
    public void positionChanged(MediaPlayer mp, float f)
    {

    }

    @Override
    public void seekableChanged(MediaPlayer mp, int i)
    {

    }

    @Override
    public void pausableChanged(MediaPlayer mp, int i)
    {

    }

    @Override
    public void titleChanged(MediaPlayer mp, int i)
    {

    }

    @Override
    public void snapshotTaken(MediaPlayer mp, String string)
    {

    }

    @Override
    public void lengthChanged(MediaPlayer mp, long l)
    {
    }

    @Override
    public void videoOutput(MediaPlayer mp, int i)
    {

    }

    public void scrambledChanged(MediaPlayer mp, int i)
    {
    }

    public void elementaryStreamAdded(MediaPlayer mp, int i, int i1)
    {
    }

    public void elementaryStreamDeleted(MediaPlayer mp, int i, int i1)
    {
    }

    public void elementaryStreamSelected(MediaPlayer mp, int i, int i1)
    {
    }

    public void error(MediaPlayer mp)
    {
    }

    @Override
    public void mediaMetaChanged(MediaPlayer mp, int i)
    {
    }

    @Override
    public void mediaSubItemAdded(MediaPlayer mp, libvlc_media_t l)
    {
    }

    @Override
    public void mediaDurationChanged(MediaPlayer mp, long l)
    {
    }

    @Override
    public void mediaParsedChanged(MediaPlayer mp, int i)
    {
    }

    @Override
    public void mediaFreed(MediaPlayer mp)
    {
    }

    @Override
    public void mediaStateChanged(MediaPlayer mp, int i)
    {
    }

    public void mediaSubItemTreeAdded(MediaPlayer mp, libvlc_media_t l)
    {
    }

    public void newMedia(MediaPlayer mp)
    {
    }

    @Override
    public void subItemPlayed(MediaPlayer mp, int i)
    {
    }

    @Override
    public void subItemFinished(MediaPlayer mp, int i)
    {
    }

    @Override
    public void endOfSubItems(MediaPlayer mp)
    {
    }
}
