/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.Networking.Packet;
import org.cc86.MMC.API.Processor;
import org.cc86.MMC.modules.audio.drivers.technics.se540.DriverSe540;
//import org.cc86.MMC.modules.audio.drivers.technics.se540.ProtocolHandler;

/**
 *
 * @author tgoerner
 */

public class StereoControl implements Processor
{
    //MODE WHAT PARAMS
    //MODE: GET SET
    //response hat immer CHG
    private static final Logger l = LogManager.getLogger();
    private boolean pwr = false;
    private int volume=0;
    private String src = "";
    //private PrintStream serialControl;
    private final List<byte[]> sendQueue = new ArrayList<>();
    private Driver se540 = DriverSe540.getDriver();
    private BlockingQueue<byte[]> serialControl = new LinkedBlockingQueue<>();
    public StereoControl()
    {
        se540.addPowerCallback((power) -> this.powerChanged(power));
        se540.addSourceCallback((source) -> this.sourceChanged(source));
        se540.addVolumeCallback((volume) -> this.volumeChanged(volume));
        se540.setUartSender((data) -> this.sendViaUART(data));
        new Thread(()->
        {
            serialControl.clear();
            //serialControl = new PrintStream(new PipedOutputStream(ttycontrolend));
            if(API.getMockMode())
            {
                new MockSWTTYProvider().uartHandler(this::processUARTLine, serialControl, false);
            }
            else
            {
                new SWTTYProvider().uartHandler(this::processUARTLine, serialControl, false);
            }
            while(true)
            {
                synchronized(sendQueue)
                {
                    try
                    {
                        sendQueue.wait();
                        if(sendQueue.size()>0)
                        {
                            //String sq = new String(sendQueue.remove(0));
                            //l.trace("SENDPREP:"+sq);
                            serialControl.add(sendQueue.remove(0));
                        }
                    }
                    catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    public void processUARTLine(Byte[] recvbyte)
    {
        se540.receiveUartBytes(recvbyte);
    }
    
    @Override
    public void process(Packet r, Handler h)
    {
        boolean set = (r.getData().get("type")+"").equalsIgnoreCase("set");
        String pd = (String) r.getData().get("command");
        switch(pd)
        {
            case "volume":
                if(set)
                {
                    String par = (String) r.getData().get("value");
                    try{
                        if(par.equals("UP")||par.equals("DOWN"))
                        {
                            if(par.equals("UP"))
                            {
                                se540.setVolumeRel((byte)10);
                            }
                            else
                            {
                                se540.setVolumeRel((byte)-10);
                            }
                        }
                        else
                        {
                            int vol = Integer.parseInt(par,10);
                            se540.setVolume(vol);
                        }
                    }
                    catch(NumberFormatException ex)
                    {
                        ex.printStackTrace();
                        //TODO ERROR_NOTIFY
                    }
                }
                else
                {
                    //TODO
                }
            break;
            case "device_power":
                if(set)
                {
                    try{
                        String pwr = ((String) r.getData().get("value"));
                        se540.setPower(pwr.equals("ON"));
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                        //TODO ERROR_NOTIFY
                    }
                }
                else
                {
                    //TODO
                }
            break;
                case "src_select":
                if(set)
                {
                    try{
                        String src = ((String) r.getData().get("value"));
                        switch(src)
                        {
                            case "PHONO":
                                se540.setSource(Source.PHONO);
                                break;
                            case "EXT":
                                se540.setSource(Source.EXTERNAl);
                                break;
                            case "TUNER":
                                se540.setSource(Source.TUNER);
                                break;
                        }
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                        //TODO ERROR_NOTIFY
                    }
                }
                else
                {
                    //TODO
                }
            break;
            case "dev_sync":
                if(set)
                {
                    se540.sync();
                }
                else
                {
                    //TODO
                }
            break;
            case "speaker_select":
                if(set)
                {
                    List<Object> speakers = ((List<Object>) r.getData().get("speakers"));
                    final List<String> spk = new ArrayList<>();
                    speakers.forEach((e)->{spk.add(e+"");});
                    se540.setSpeker(spk.toArray(new String[]{}));
                }
                else
                {
                    //TODO
                }
            break;
        }
    }
    
    public static void volumeChanged(int newvolume)
    {
        Packet evt = new Packet();
        HashMap<String,Object> evtdata = new HashMap<>();
        evtdata.put("command","volume");
        evtdata.put("type","response");
        evtdata.put("value",newvolume);
        evt.setData(evtdata);
        API.dispatchEvent(evt);
    }
    
    public static void sourceChanged(Source newSource)
    {
        //TUNER PHONO EXT CD TAPE
        String source = "";
        switch(newSource)
        {
            case EXTERNAl:
                source="EXT";
                break;
            case PHONO:
                source="PHONO";
                break;
            case TUNER:
                source="TUNER";
                break;
        }
        
        Packet evt = new Packet();
        HashMap<String,Object> evtdata = new HashMap<>();
        evtdata.put("command","src_select");
        evtdata.put("type","response");
        evtdata.put("value",source);
        evt.setData(evtdata);
        API.dispatchEvent(evt);
    }
    
    public static void powerChanged(boolean power)
    {
        Packet evt = new Packet();
        HashMap<String,Object> evtdata = new HashMap<>();
        evtdata.put("command","device_power");
        evtdata.put("type","response");
        evtdata.put("value",power?"ON":"OFF");
        evt.setData(evtdata);
        API.dispatchEvent(evt);
    }
    
    public void sendViaUART(Byte[] packet)
    {
        //workaround für java
        byte[] realpkg = new byte[packet.length];
        for (int i = 0; i < realpkg.length; i++)
        {
            realpkg[i]=packet[i];
        }
        synchronized(sendQueue)
        {
            sendQueue.add(realpkg);
            sendQueue.notify();
        }
    }
}
