/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cc86.MMC.modules.test;

import java.util.HashMap;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Processor;
import org.cc86.MMC.API.Packet;

/**
 *
 * @author iZc <nplusc.de>
 */
public class TestProcessor implements Processor
{
    private String msg ="";
    @Override
    public void process(Packet r,Handler h)
    {
        HashMap<String,Object> rqd = r.getData();
        if(rqd.containsKey("type")&&rqd.get("type")!=null&&(rqd.get("type")).equals("set"))
        {
            msg="Saved:"+rqd.get("message");
        }
        System.out.println("MSG stored");
        Packet rsp = new Packet();
        HashMap<String,Object> rspns= new HashMap<>();
        rspns.put("message",msg);
        rspns.put("command","Test");
        rspns.put("type","response");
        rsp.setData(rspns);
        h.respondToLinkedClient(rsp);

    }
    
}
