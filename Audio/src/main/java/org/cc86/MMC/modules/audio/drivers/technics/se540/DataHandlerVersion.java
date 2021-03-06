/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tgoerner
 */
public class DataHandlerVersion extends DataHandler
{
    private static final int VERSION_MAIN_OFFSET=0;
    private static final int VERSION_MAIN_MASK=0x07;
    private static final int VERSION_SUB_OFFSET=3;
    private static final int VERSION_SUB_MASK=0x0F;
    private static final int VERSION_TEST_OFFSET=7;
    private static final int VERSION_TEST_MASK=0x01;
    public static final DataHandlerVersion instance = new DataHandlerVersion(); 
    private DataHandlerVersion(){}
    public static DataHandler getInstance()
    {
        return instance;
    }
        
    @SuppressWarnings("PointlessBitwiseExpression")
    @Override
    public int handleEvent(List<Byte> packet)
    {
        int communication_version = packet.get(0);
        int program_version = packet.get(1);
        
        int main_version = ((program_version^0xff)&VERSION_MAIN_MASK)>>>VERSION_MAIN_OFFSET;
        int sub_version = ((program_version^0xff)&VERSION_MAIN_MASK)>>>VERSION_SUB_OFFSET;
        boolean test_version = (((program_version^0xff)&VERSION_MAIN_MASK)>>>VERSION_TEST_OFFSET)!=0;
        //TODO Upstream or DEBUG
        return -1;
    }
}
