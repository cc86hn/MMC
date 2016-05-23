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
     public void uartHandler(final Consumer<Integer> out, final InputStream ctrl, final boolean addPrefix)
    {
        new Thread(() ->
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while(true)
            {
                try
                {
                    out.accept(br.read());
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }   
        }).start();
        new Thread(() ->
        {
            l.trace("starting reader thread");
            BufferedReader br = new BufferedReader(new InputStreamReader(ctrl));
            while(true)
            {
                try
                {
                    l.trace("waiting for line...");
                    l.trace("UART_SEND:"+br.readLine());
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }   
        }).start();
    }
}
