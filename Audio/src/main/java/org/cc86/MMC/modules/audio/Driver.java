/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

import java.util.function.Consumer;

/**
 *
 * @author tgoerner
 */
public interface Driver
{
    /**
     * Sets the volume of the device
     * @param volume Percentage value of the volume
     */
    public void setVolume(int volume);
    public void setVolumeRel(byte volumeDelta);
    
    public void sync();
    
    
    public void setSpeker(String[] spk);
    
    /**
     * Registers a callback for volume updates
     * @param volumeHandler Callback function
     */
    public void addVolumeCallback(Consumer<Integer> volumeHandler);
    
        /**
     * Sets the volume of the device
     * @param source ID of the source
     */
    public void setSource(Source source);
    
    /**
     * Registers a callback for source updates
     * @param sourceHandler Callback function
     */
    public void addSourceCallback(Consumer<Source> sourceHandler); //TODO enum?
    
    /**
     * Sets the volume of the device
     * @param power State of the device, true=on
     */
    public void setPower(boolean power);
    
    /**
     * Registers a callback for power state updates
     * @param powerHandler Callback function
     */
    public void addPowerCallback(Consumer<Boolean> powerHandler);
    
    /**
     * Handler to connect the Uart control stream to the driver
     * @param b Byte to receive
     */
    public void receiveUartByte(byte b);
    
    /**
     * Handler to allow tzhe driver to send Data via uart
     * @param data Byte-array of the data
     */
    public void setUartSender(Consumer<Byte[]> data);
    
}
