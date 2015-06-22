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
public enum Resources
{
    AUDIODATA,VIDEODATA,BEAMER,STEREO;
    
    private static boolean[] statuses = new boolean[Resources.values().length];
     private static Plugin[] holders = new Plugin[Resources.values().length];
    
    public static boolean getStatusOfRessource(Resources r)
    {
        return statuses[r.ordinal()];
    }
    public static void setResourceStatus(Resources r, boolean status)
    {
        statuses[r.ordinal()]=status;
    }
    
    public Plugin getResourceHolder(Resources r)
    {
        return holders[r.ordinal()];
    }
    
    
}
