/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 *
 * @author tgoerner
 */
public interface TTYProvider
{
     public void uartHandler(final Consumer<Byte[]> out, final BlockingQueue<byte[]> ctrl, final boolean addPrefix);
    
}
