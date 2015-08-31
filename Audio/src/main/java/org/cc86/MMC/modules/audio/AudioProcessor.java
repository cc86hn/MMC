/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.API.Processor;

/**
 *
 * @author tgoerner
 */
public class AudioProcessor implements Processor
{
    private JukeBox jbx = new JukeBox();
    
    
    @Override
    public void process(Packet r, Handler h)
    {
        String pd = (String) r.getData().get("command");
        if(r.getData().get("type").equals("set"))
        {
            switch(pd)
            {
                case "playback_pool":
                    jbx.updatePool(r, h);
                break;
                case "playback_jukebox":
                    jbx.start_playback(r, h);
                break;
            }
        }
    }
    
    void shutdown()
    {
        
    }
    void freeUpAudio()
    {
        jbx.freeUpAudio();
    }
    void onClientDisconnect(Handler h, boolean graceful)
    {
        jbx.onClientDisconnect(h, graceful);
    }
}
