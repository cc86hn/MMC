/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

/**
 *
 * @author tgoerner
 */
public class InvalidPacketException extends RuntimeException
{

    public InvalidPacketException()
    {
    }

    public InvalidPacketException(String message)
    {
        super(message);
    }

    public InvalidPacketException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidPacketException(Throwable cause)
    {
        super(cause);
    }
    
}
