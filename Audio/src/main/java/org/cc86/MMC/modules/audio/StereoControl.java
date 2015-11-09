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
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.API.Processor;

/**
 *
 * @author tgoerner
 */

public class StereoControl implements Processor
{
    //MODE WHAT PARAMS
    //MODE: GET SET
    //response hat immer CHG
    
    private static final String RESPONSE_HEADER = "CHG";
    private static final String VOLUME_RESPONSE = "VOL";
    private static final String STATE_RESPONSE = "PWR";
    private static final String VOLUME_SET_COMMAND = "SET VOL %d";
    private static final String VOLUME_GET_COMMAND = "GET VOL";
    private static final String POWER_SET_COMMAND = "SET PWR %d";
    private static final String POWER_GET_COMMAND = "GET PWR";
    private boolean pwr = false;
    private int volume=0;
    
    private PrintStream serialControl;
    public StereoControl()
    {
        PipedInputStream ttycontrolend = new PipedInputStream(8192);
        try
        {
            serialControl = new PrintStream(new PipedOutputStream(ttycontrolend));
            SWTTYProvider.uartHandler(this::processUARTLine, ttycontrolend, false);
            
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void processUARTLine(String line)
    {
        String[] response = line.split(" ");
        if(response.length>=3&&response[0].equals("CHG"))
        {
            String val = response[1];
            switch(val)
            {
                case "VOL":
                    volume = Integer.valueOf(val);
                    API.makeSimpleEvent("volume", "value", val);
                break;
                case "PWR":
                    pwr=val.equals("1");
                    API.makeSimpleEvent("device_power", "value", val);
                break;
            }
        }
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
                    try{
                        int vol = Integer.parseInt((String) r.getData().get("value"),10);
                        serialControl.printf(VOLUME_SET_COMMAND, vol);
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
                        int pwr = Integer.parseInt((String) r.getData().get("value"),10);
                        pwr=Math.max(0, Math.min(1, pwr));//clampen auf 0 oder 1
                        serialControl.printf(POWER_SET_COMMAND, pwr);
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
        }
    }
    
}
