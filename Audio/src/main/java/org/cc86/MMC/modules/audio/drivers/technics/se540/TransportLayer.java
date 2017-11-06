/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.CRC;

/**
 *
 * @author iZc <nplusc.de>
 */
public class TransportLayer
{
    
    
    
    
    private static final Logger l = LogManager.getLogger();
    
    
    
    private static final int MAX_PACKET_SIZE=9;
    private static final byte INVERTED_SIZE_MASK = 0x38;
    private static final int INVERTED_SIZE_OFFSET = 3;
    private static final byte BASE_SIZE_MASK = 0x07;
    private static final int BASE_SIZE_LENGTH = 3;
    
    private static final int CNT_BYTE = 0;
    private static final byte CNT_MASK = (byte)0x40;
    private static final int CNT_OFFSET = 6;
    private static final int CNT_LENGTH = 1;
    
    
    private static final int DIR_OFFSET = 7;
    private static final int DIR_LENGTH = 1;
    private static final byte DIR_MASK = (byte)0x80;
    private static final int DIR_VALUE = 0;
    
    
    

    
    private static final int SRV_BYTE = 1;
    private static final int SRV_MASK = 0x07;
    private static final int SRV_LENGTH = 3;
    private static final int SRV_OFFSET = 0;
    
    
    private static final int CMD_BYTE = 1;
    private static final int CMD_MASK = 0xF8;
    private static final int CMD_OFFSET = 3;
    private static final int CMD_LENGTH = 5;
    private static final int USRDATA_REQ_START_BYTE=2;
    
    private static final int REQ_CRC_BYTE = 2;
    private static final int USRDATA_RET_START_BYTE=3;
    
    
    private static final int RET_ST_BYTE = 1;
    private static final int RET_ST_MASK = 0x18;
    private static final int RET_ST_OFFSET = 3;
    private static final int RET_ST_LENGTH = 2;
    
    static final byte NACK_UNKNOWN = 0;
    static final byte NACK_INVALID = 1;
    static final byte NACK_BUSY = 2;
    
    
    private static final int MAX_TRY_COUNT=10;
    
    private static byte cnt=0;
    private static boolean acked=false;
    private static int requestCRC=0;
    
   
    
    
    
    private static final ArrayList<Byte> receivebuffer = new ArrayList<>();
    
    private static int packetsLost=0,completeConnectionsLost=0,
            packetsReceived=0,packetsSent=0;
    
    
    
    @SuppressWarnings("SleepWhileInLoop")
    static void send_request(ServiceType service, int command, List<Byte> userdata) throws InvalidPacketException
    {
        acked=false;
        l.trace("Packet go!");
        
        List<Byte> raw_packet = new ArrayList<>(MAX_PACKET_SIZE);
        for(int i=0;i<MAX_PACKET_SIZE;i++)
        {
            raw_packet.add(i,(byte)0);
        }
        int udsize = userdata.size()+1;
        int udsize_inv = udsize^((1<<BASE_SIZE_LENGTH)-1);
        raw_packet.set(0, (byte)((cnt<<CNT_OFFSET)|(DIR_VALUE<<DIR_OFFSET)|(udsize_inv<<INVERTED_SIZE_OFFSET)|(udsize)));
        raw_packet.set(CMD_BYTE,(byte)0);
        if(CMD_BYTE!=SRV_BYTE)
        {
            raw_packet.set(SRV_BYTE,(byte)0);
        }
        raw_packet.set(CMD_BYTE,(byte)(raw_packet.get(CMD_BYTE)|(command<<CMD_OFFSET)));
        raw_packet.set(SRV_BYTE,(byte)(raw_packet.get(SRV_BYTE)|(service.ordinal()<<SRV_OFFSET)));
        int packetlength = USRDATA_REQ_START_BYTE+userdata.size();
        raw_packet.addAll(USRDATA_REQ_START_BYTE, userdata);
        for(int i=0;i<userdata.size();i++)
        {
            raw_packet.set(i+USRDATA_REQ_START_BYTE, userdata.get(i));
        }
        int crc = CRC.CRC8_START;
        for(int i=0;i<packetlength;i++)
        {
            crc = CRC.crc8((byte)crc,raw_packet.get(i));
        }
        raw_packet.set(packetlength,(byte)crc);
        while(raw_packet.size()>packetlength+1)
        {
            raw_packet.remove(packetlength+1);
        }
        Byte[] rawpkg = raw_packet.toArray(new Byte[0]);
        if(crc==requestCRC)
        {
            return;//anti-doublesend, not intended by protocol
        }
        requestCRC=crc;
        synchronized(TransportLayer.class)
        {
            acked=false;
        }
        int retries=0;
        packetsSent++;
        for(;retries<MAX_TRY_COUNT;retries++)
        {
            DriverSe540.getDriver().sendDataViaUart(rawpkg);
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            
            synchronized(TransportLayer.class)
            {
                if(acked)
                {
                    break;
                }
            }
            
        }
        if(retries==MAX_TRY_COUNT)
        {
            packetsLost++;
            //DO_PING
        }
        else
        {
            cnt++;
            cnt=(byte) (cnt%(1<<CNT_LENGTH));
        }
    }
 
    
    
    
    
    static void sendAck(int req_cnt,byte req_crc)
    {
        List<Byte> raw_packet = new ArrayList<>(4);
        for(int i=0;i<3;i++)
        {
            raw_packet.add((byte)0);
        }
        int udsize=2;
        int udsize_inv = udsize^((1<<BASE_SIZE_LENGTH)-1);
        l.trace("UDSIZE_INV=={}",String.format("%03X",udsize_inv));
        l.trace("REQCRC=={}",String.format("%03X",req_crc));
        raw_packet.set(0, (byte)((cnt<<CNT_OFFSET)|(DIR_VALUE<<DIR_OFFSET)|(udsize_inv<<INVERTED_SIZE_OFFSET)|(udsize)));
        
        raw_packet.set(RET_ST_BYTE,(byte)(raw_packet.get(RET_ST_BYTE)|(ReturnStatus.RET_ST_ACK.ordinal()<<RET_ST_OFFSET)));
        raw_packet.set(SRV_BYTE,(byte)(raw_packet.get(SRV_BYTE)|(ServiceType.SRV_RET.ordinal()<<SRV_OFFSET)));
        
        int crc = CRC.CRC8_START;
        for(int i=0;i<3;i++)
        {
            crc = CRC.crc8((byte)crc,raw_packet.get(i));
        }
        l.trace("CRC_SENT={}",crc);
        raw_packet.add((byte)crc);
        DriverSe540.getDriver().sendDataViaUart(raw_packet.toArray(new Byte[0])); 
        packetsSent++;
    }
    
    
    static void sendNack(int req_cnt,byte req_crc,byte rsn)
    {
        List<Byte> raw_packet = new ArrayList<>(4);
        for(int i=0;i<3;i++)
        {
            raw_packet.add((byte)0);
        }
        int udsize=3;
        int udsize_inv = udsize^((1<<BASE_SIZE_LENGTH)-1);
        l.trace("UDSIZE_INV=={}",String.format("%03X",udsize_inv));
        l.trace("REQCRC=={}",String.format("%03X",req_crc));
        raw_packet.set(0, (byte)((cnt<<CNT_OFFSET)|(DIR_VALUE<<DIR_OFFSET)|(udsize_inv<<INVERTED_SIZE_OFFSET)|(udsize)));
        
        raw_packet.set(RET_ST_BYTE,(byte)(raw_packet.get(RET_ST_BYTE)|(ReturnStatus.RET_ST_NACK.ordinal()<<RET_ST_OFFSET)));
        raw_packet.set(SRV_BYTE,(byte)(raw_packet.get(SRV_BYTE)|(ServiceType.SRV_RET.ordinal()<<SRV_OFFSET)));
        
        raw_packet.add(rsn);
        
        int crc = CRC.CRC8_START;
        for(int i=0;i<4;i++)
        {
            crc = CRC.crc8((byte)crc,raw_packet.get(i));
        }
        l.trace("CRC_SENT={}",crc);
        raw_packet.add((byte)crc);
        DriverSe540.getDriver().sendDataViaUart(raw_packet.toArray(new Byte[0])); 
        packetsSent++;
    }
    
    
    public static void receiveBytes(Byte[] b)
    {
        receivebuffer.addAll(Arrays.asList(b));
         //suspected_package = ;
        while(true)
        {
            int listend = MAX_PACKET_SIZE>receivebuffer.size()?receivebuffer.size():MAX_PACKET_SIZE;
            //int ls = receivebuffer.size();
            /*if(ls==0)
            {
                continue;
            }*/
            List<Byte> suspected_package;
            synchronized(receivebuffer)
            {
                suspected_package = new ArrayList<>(((ArrayList<Byte>)receivebuffer.clone()).subList(0, (listend)));
            }
            if(suspected_package.size()<1)
            {
                continue;
            }
            byte hdr = suspected_package.get(0);
            int size_first = ((hdr^0xff)&INVERTED_SIZE_MASK)>>>INVERTED_SIZE_OFFSET;
            int size_last = (hdr&BASE_SIZE_MASK);
            if(size_first!=size_last)
            {
                receivebuffer.remove(0);
                continue;
            }
            int direction = ((hdr&DIR_MASK)>>DIR_OFFSET);
            if(direction==DIR_VALUE)//selbstgespräch erkannt, deshalb ignorieren
            {
                receivebuffer.remove(0);
                return;
            }

            if(suspected_package.size()>size_first+1)
            {
                int crc = CRC.CRC8_START;
                int realsize = size_first+2;
                for(int i=0;i<realsize;i++)
                {
                    crc = CRC.crc8((byte)crc,suspected_package.get(i));
                }
                if(crc==0)//CRC mit gültigem CRC-wert soll immer auf 0 enden
                {
                    l.info("Packet received via UART,Size{},Header{}",realsize,String.format("%03X",suspected_package.get(0)));
                    List<Byte> packet = new ArrayList<>(MAX_PACKET_SIZE);
                    String pkg = String.format("%03X",suspected_package.get(0))+"|";
                    for(int i=0;i<realsize;i++)
                    {
                        pkg+=String.format("%03X",suspected_package.get(i))+"|";
                        packet.add(suspected_package.get(i));
                        receivebuffer.remove(0);
                    }
                    l.trace(pkg);
                    packetsReceived++;
                    packetReceived(packet);
                }
                else
                {
                    receivebuffer.remove(0);
                }
            }
            else
            {
                break; //too short, some bytes not yet received
            }
        }
    }
    
    public static void packetReceived(List<Byte> packet)
    {
        packetsReceived++;
        int hdr=packet.get(0);
        int req_cnt = (hdr&CNT_MASK)>>CNT_OFFSET;
        l.trace("REQ_CNT={}",req_cnt);
        if((hdr&BASE_SIZE_MASK)==0)
        {
            l.info("SYNC RECEIVED");
            
            sendAck(req_cnt,packet.get(REQ_CRC_BYTE));
            //send_response(req_cnt, packet.get(0), RET_ST_ACK, null,true);
            return;
        }
        
        
        
        int srv = (packet.get(SRV_BYTE)&SRV_MASK)>>>SRV_OFFSET;
        //int ret_ST = (packet.get(RET_ST_BYTE)&RET_ST_MASK)>>RET_ST_OFFSET;
        if(srv!=ServiceType.SRV_RET.ordinal())
        {
            if(srv!=ServiceType.SRV_EVT.ordinal())
            {
                sendNack(req_cnt, packet.get(REQ_CRC_BYTE), NACK_UNKNOWN);
            }
            
            //handle_event(packet,hdr);//TO
        }
        else
        {
            int resp_crc=packet.get(REQ_CRC_BYTE);
            if(resp_crc==requestCRC)
            {
                synchronized(TransportLayer.class)
                {
                    acked=true;
                }
            }
            /*
            if(connectionExists)
            {
                if(requestResponseListener!=null)
                {
                    l.info("Request Response gotten");
                    requestResponseListener.accept(packet);
                }
            }
            else
            {
                l.info("Ping ACK'd");
                l.trace(packet.size());
                l.trace("PINGDATA:{}{}",String.format("%03X",packet.get(REQ_CRC_BYTE-1)),String.format("%03X",lastPingCRC));
                if(packet.size()==3&&(packet.get(REQ_CRC_BYTE-1)+(byte)0)==lastPingCRC)
                {
                    l.info("ACK valid");
                    //TODO
                }
            }*/
        }
    }
    
    
}
