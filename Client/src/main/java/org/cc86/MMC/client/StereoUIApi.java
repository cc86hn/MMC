/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

/**
 *
 * @author tgoerner
 */
public interface StereoUIApi
{
    public void setVolumeSlider(int volume);
    
    public void setSource(Sources s);
    
    public void setPowerState(boolean on);
}
