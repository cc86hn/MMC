/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

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

    private final MediaPlayer mpaccess;
    private final MediaPlayerFactory f;

    public VLCPlayback(String vlcpath)
    {

        f = new MediaPlayerFactory("--no-video-title-show");
        mpaccess = f.newHeadlessMediaPlayer();
        mpaccess.addMediaPlayerEventListener(this);
    }

    public void newFilePlz()
    {
        String url = MediaPlayerControl.getTitleToPlay();
        if(url!=null)
        {
            mpaccess.playMedia(url, "");
        }
    }

    public void addTitle(String path)
    {
        mpaccess.stop();
        mpaccess.playMedia(path);
    }

    public int getLengthInSeconds()
    {
        return (int) (Math.ceil(mpaccess.getLength() / 1000f));
    }

    public int getPosition()
    {
        return (int) (Math.ceil((mpaccess.getLength() / 1000f) * mpaccess.getPosition()));
    }

    public boolean isSeekable()
    {
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
        if (StatusMode.getAudioMode() == StatusMode.FILE)//feature nur bei DLNA-mode verfügbar
        {
            newFilePlz();
        }
    }

    public void play()
    {
        mpaccess.play();
    }

    public void pause()
    {
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

    @Override
    public void scrambledChanged(MediaPlayer mp, int i)
    {
    }

    @Override
    public void elementaryStreamAdded(MediaPlayer mp, int i, int i1)
    {
    }

    @Override
    public void elementaryStreamDeleted(MediaPlayer mp, int i, int i1)
    {
    }

    @Override
    public void elementaryStreamSelected(MediaPlayer mp, int i, int i1)
    {
    }

    @Override
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

    @Override
    public void mediaSubItemTreeAdded(MediaPlayer mp, libvlc_media_t l)
    {
    }

    @Override
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
