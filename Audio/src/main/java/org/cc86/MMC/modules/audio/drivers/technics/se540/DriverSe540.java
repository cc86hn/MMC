/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.modules.audio.Driver;
import org.cc86.MMC.modules.audio.Source;

/**
 *
 * @author tgoerner
 */
public class DriverSe540 implements Driver
{
    private static final Logger l = LogManager.getLogger();
    
    private static DriverSe540 instance = new DriverSe540();
    private ProtocolHandler h = new ProtocolHandler();
    
    private Consumer<Byte[]> uartOut;
    
    private DataSenderVolume vs = new DataSenderVolume(h);
    private DataSenderPower ps = new DataSenderPower(h);
    private DataSenderSource ss = new DataSenderSource(h);
    
    private Consumer<Integer> volumeCallback;
    private Consumer<Source> sourceCallback;
    private Consumer<Boolean> powerCallback;
    
    private DriverSe540()
    {
        h.startReceive();
    }
    
    public static DriverSe540 getDriver()
    {
        return instance;
    }
    
    
    @Override
    public void setVolume(int volume)
    {
        vs.changeVolume(volume);
    }

    @Override
    public void addVolumeCallback(Consumer<Integer> volumeHandler)
    {
       volumeCallback=volumeHandler;
    }

    @Override
    public void setSource(Source source)
    {
        switch(source)
        {
            case EXTERNAl:
                ss.changeSource(0);
                break;
            case PHONO:
                ss.changeSource(2);
                break;
            case TUNER:
                ss.changeSource(1);
                break;
        }
        
        
    }

    @Override
    public void addSourceCallback(Consumer<Source> sourceHandler)
    {
        sourceCallback=sourceHandler;
    }

    @Override
    public void setPower(boolean power)
    {
        l.trace("PWR chg to{}",power);
        ps.changePower(power);
    }

    @Override
    public void addPowerCallback(Consumer<Boolean> powerHandler)
    {
        powerCallback=powerHandler;
    }
    
    
    
    
    void notifyCoreonSource(int source)
    {
        switch(source)
        {
            case 0:
                sourceCallback.accept(Source.EXTERNAl);
                break;
            case 1:
                sourceCallback.accept(Source.TUNER);
                break;
            case 2:
                sourceCallback.accept(Source.PHONO);
                break;
        }
    }
    
    void notifyCoreonVolume(int volume)
    {
        volumeCallback.accept(volume);
    }
    
    void notifyCoreonPower(boolean state)
    {
        powerCallback.accept(state);
    }

    
    void sendDataViaUart(Byte[] data)
    {
        uartOut.accept(data);
    }
    
    
    @Override
    public void receiveUartByte(byte b)
    {
        h.receiveByte(b);
    }

    @Override
    public void setUartSender(Consumer<Byte[]> dataConsumer)
    {
        uartOut=dataConsumer;
    }
    
}
