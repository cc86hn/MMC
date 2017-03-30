/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client.API;

import java.util.HashMap;
import org.cc86.MMC.Networking.Packet;

/**
 *
 * @author tgoerner
 */
public class Utilities
{
    public static void registerOnEvent(String eventID,Connection c)
    {
        Packet p = new Packet();
        HashMap<String,Object> data = new HashMap<>();
        data.put("type","set");
        data.put("command","event");
        data.put("mode","register");
        data.put("eventID",eventID);
        p.setData(data);
        c.sendRequest(p);
        try
        {
            Thread.sleep(10); //HACK, dont be tooo fast
        } catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }
}
