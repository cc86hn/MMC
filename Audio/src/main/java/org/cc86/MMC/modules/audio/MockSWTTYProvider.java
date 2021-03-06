/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tgoerner
 */
public class MockSWTTYProvider implements TTYProvider
{
     private static final Logger l = LogManager.getLogger();
     @Override
     public void uartHandler(final Consumer<Byte[]> out, final BlockingQueue<byte[]> ctrl, final boolean addPrefix)
    {
        new Thread(() ->
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while(true)
            {
                try
                {
                    try
                    {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                   // out.accept(42);
                    //out.accept(51);
                   // out.accept(1);
                   // out.accept(10);
                    
                    //out.accept(br.read());
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }   
        }).start();
        new Thread(() ->
        {
            l.trace("starting reader thread");
            //BufferedReader br = new BufferedReader(new InputStreamReader(ctrl));
            while(true)
            {

                try
                {
                    l.trace("waiting for line...");
                    l.info("UART_SEND:"+Arrays.toString(ctrl.take()));
                } catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }

            }   
        }).start();
    }
}
