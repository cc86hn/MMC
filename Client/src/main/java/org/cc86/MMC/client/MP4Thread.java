/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import de.nplusc.izc.tools.baseTools.ProcessWatcher;
import de.nplusc.izc.tools.baseTools.Tools;
import org.apache.logging.log4j.io.IoBuilder;

/**
 *
 * @author tgoerner
 */
public class MP4Thread implements Runnable,ProcessWatcher
{
    private int port;
    private boolean fastmode;
    
    public MP4Thread(int port,boolean fm)
    {
        this.port=port;
        fastmode=fm;
    }
    
    
    private Process p;
    public void stopStreaming()
    {
        p.destroy();
    }
    @Override
    public void run()
    {
        if(fastmode)
        {
            //ffmpeg -f dshow -framerate 24 -i video="screen-capture-recorder"  -r 24 -vcodec mpeg4 -q 20 -g 0 -f mpegts udp://192.168.10.228:<streamport>?pkt_size=400?buffer_size=65535
            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.FFMPEG").buildPrintStream(), this, "ffmpeg","-f","dshow","-framerate","24",
                    "-i","video=\"screen-capture-recorder\"","-r","24","-vcodec","mpeg4","-q","20","-g","0",
                    "-f","mpegts","udp://"+Main.getServerIP()+":"+port+"?pkt_size=400?buffer_size=65535");
        }
        else
        {
            //ffmpeg -f dshow -framerate 24 -i video="screen-capture-recorder"  
            //-r 24 -f dshow -audio_buffer_size 50 -i audio="virtual-audio-capturer"
            //-acodec libmp3lame -ab 128k -vcodec mpeg4 -q 20 -g 0 -f 
            //mpegts udp://192.168.10.228:9264?pkt_size=400?buffer_size=65535

            Tools.runCmdWithPassthru(IoBuilder.forLogger("External.FFMPEG").buildPrintStream(), this, "ffmpeg","-f","dshow","-framerate","24","-i","video=\"screen-capture-recorder\"",
                    "-f","-dshow","-audio_buffer_size","50","-i","audio=\"virtual-audio-capturer\"","-acodec","libmp3lame","-ab","256k","-vcodec mp4",
                    "-q","20","-g","0","-f","mpegts","udp://"+Main.getServerIP()+":"+port+"?pkt_size=400?buffer_size=65535");
        }
    }
    @Override
    public void receiveProcess(Process prcs)
    {
        p=prcs;
    }
    
}
