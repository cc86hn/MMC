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
//import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Packet;
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
    private static final String RESPONSE_HEADER = "CHG";
    private static final String VOLUME_RESPONSE = "VOL";
    private static final String STATE_RESPONSE = "PWR";
    private static final String SRCSEL_RESPONSE = "SRC";
    private static final String VOLUME_SET_COMMAND = "SET VOL %d\n";
    private static final String VOLUME_UP_COMMAND = "SET VOL UP\n";
    private static final String VOLUME_DOWN_COMMAND = "SET VOL DOWN\n";
    private static final String VOLUME_GET_COMMAND = "GET VOL\n";
    private static final String POWER_SET_COMMAND = "SET PWR %s\n";
    private static final String SOURCE_SET_COMMAND = "SET SRC %s\n";
    private static final String SPEAKER_SET_COMMAND = "SET SPK %s\n";
    private static final String POWER_GET_COMMAND = "GET PWR\n";
    private static final String DEVSYNC_COMMAND = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n";
    private boolean pwr = false;
    private int volume=0;
    private String src = "";
    private PrintStream serialControl;
    private final List<byte[]> sendQueue = new ArrayList<>();
    private Driver se540 = DriverSe540.getDriver();
    public StereoControl()
    {
        se540.addPowerCallback((power) -> this.powerChanged(power));
        se540.addSourceCallback((source) -> this.sourceChanged(source));
        se540.addVolumeCallback((volume) -> this.volumeChanged(volume));
        se540.setUartSender((data) -> this.sendViaUART(data));
        new Thread(()->
        {
            PipedInputStream ttycontrolend = new PipedInputStream(8192);
            try
            {
                serialControl = new PrintStream(new PipedOutputStream(ttycontrolend));
                if(API.getMockMode())
                {
                    new MockSWTTYProvider().uartHandler(this::processUARTLine, ttycontrolend, false);
                }
                else
                {
                    new SWTTYProvider().uartHandler(this::processUARTLine, ttycontrolend, false);
                }

            } catch (IOException ex)
            {
                ex.printStackTrace();
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
                            serialControl.write(sendQueue.remove(0));
                            serialControl.flush();
                        }
                    }
                    catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        l.info("UART pipe internally broken");
                    }
                }
            }
        }).start();
    }
    
    public void processUARTLine(int recvbyte)
    {
        //byte recv = 
        if(recvbyte<0)
        {
            recvbyte+=256;
            recvbyte&=0xff;
        }
        l.trace("received "+recvbyte);
        se540.receiveUartByte((byte)recvbyte);
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
                                //sendViaUART(String.format(VOLUME_UP_COMMAND));
                            }
                            else
                            {
                                //sendViaUART(String.format(VOLUME_DOWN_COMMAND));
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
                    try{
                        //sendViaUART(String.format(DEVSYNC_COMMAND));
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
            case "speaker_select":
                if(set)
                {
                    List<Object> speakers = ((List<Object>) r.getData().get("speakers"));
                    final StringBuilder spklist = new StringBuilder();
                    speakers.forEach((s)->spklist.append(",").append(s));
                    spklist.deleteCharAt(0);
                    l.trace("spklist:"+spklist);
                    //sendViaUART(String.format(SPEAKER_SET_COMMAND,spklist+""));
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
        evtdata.put("command","volume");
        evtdata.put("type","response");
        //evtdata.put("value",newvolume);
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
