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
        if(pd.equals("playback_pool"))
        {
            jbx.updatePool(r, h);
        }
    }
    
    void shutdown()
    {
        
    }
}
