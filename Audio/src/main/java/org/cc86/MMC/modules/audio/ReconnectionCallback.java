/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

/**
 *
 * @author tgoerner
 */
@FunctionalInterface
public interface ReconnectionCallback
{
    void connectionReestablished();
}
