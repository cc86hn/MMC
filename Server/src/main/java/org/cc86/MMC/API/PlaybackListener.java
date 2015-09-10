/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.API;

/**
 *
 * @author tgoerner
 */
public interface PlaybackListener
{
    public default void titleFinished(String title){};
    public default void trackStarting(String title){};
}
