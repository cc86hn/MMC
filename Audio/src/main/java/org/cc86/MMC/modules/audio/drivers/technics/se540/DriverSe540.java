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
import org.cc86.MMC.modules.audio.ReconnectionCallback;
import org.cc86.MMC.modules.audio.Source;
import org.cc86.MMC.modules.audio.StereoControl;

/**
 *
 * @author tgoerner
 */
public class DriverSe540 implements Driver
{
    private static final Logger l = LogManager.getLogger();
    
    private static DriverSe540 instance = new DriverSe540();
    private StereoControl c;


    
    private Consumer<Byte[]> uartOut;
    
    private DataHandlerVolumeVol vs = DataHandlerVolumeVol.instance;
    private DataHandlerVolumeVolrel vrs = DataHandlerVolumeVolrel.instance;
    private DataHandlerPower ps = DataHandlerPower.instance;
    private DataHandlerSource ss = DataHandlerSource.instance;
    private DataHandlerSpeaker sps = DataHandlerSpeaker.instance;
    
    private Consumer<Integer> volumeCallback;
    private Consumer<Source> sourceCallback;
    private Consumer<Boolean> powerCallback;
    
    private ReconnectionCallback handler = null;
    
    private DriverSe540()
    {
        
    }
    
    public static DriverSe540 getDriver()
    {
        return instance;
    }

    
    
    @Override
    public void setHandler(ReconnectionCallback handler)
    {
        this.handler = handler;
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
    public boolean isReady()
    {
        return uartOut!=null;
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
    public void receiveUartBytes(Byte[] b)
    {
        TransportLayer.receiveBytes(b);
    }

    @Override
    public void setUartSender(Consumer<Byte[]> dataConsumer)
    {
        uartOut=dataConsumer;
    }
    
    
    
    @Override
    public void sync() {
       //Proto
    }

    @Override
    public void setVolumeRel(byte volumeDelta) {
        vrs.changeVolumeRel(volumeDelta);
    }

    @Override
    public void setSpeker(String[] spk) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        sps.changeSpksel(spk);
    }
    
    
    
    @Override
    public StereoControl getStereoControl()
    {
        return c;
    }

    @Override
    public void setStereoControl(StereoControl c)
    {
        this.c = c;
    }
    
}
