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
public class PluginNotReadyException extends Exception
{

    public PluginNotReadyException()
    {
    }

    public PluginNotReadyException(String message)
    {
        super(message);
    }

    public PluginNotReadyException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
}
