/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tgoerner
 */
public class SWTTYProvider implements TTYProvider
{
    //startup sudo pigpiod
    //M 10 W nach /dev/pigpio
    //Output in /dev/pigout
    //M 9 R
private static final Logger l = LogManager.getLogger();
    //cat auf /dev/pigout & /dev/pigerr
    static String[] alphabet = "a#b#c#d#e#f#g#h#i#j#k#l#m#n#o#p#q#r#s#t#u#v#w#x#y#z".split("#");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
       new SWTTYProvider().uartHandler(System.out::println, System.in, true);
    }

    private static void setup(PrintStream pigpiostream)
    {

        pigpiostream.println("M 10 W");
        pigpiostream.println("M 9 R");
        pigpiostream.println("SLRC 9");
        pigpiostream.println("SLRO 9 2400 9");
        writeSerialPacket("\n", pigpiostream); //Workaround für einen doofen PiGPIO-Bug
    }

@Override
    public void uartHandler(final Consumer<String> out, final InputStream ctrl, final boolean addPrefix)
    {
        new Thread(() ->
        {
            OutputStream fos = null;

            try
            {
                //final Socket s = new Socket("127.0.0.1", 8888);
                //fis = new FileInputStream();
                if(!new File("/dev/pigpio").exists())
                {
                    l.error("no running pigpiod, shutting down");
                    System.exit(0);
                }
                fos = new FileOutputStream("/dev/pigpio");
                //fos = s.getOutputStream();/*new InputStreamReader(s.getInputStream()*/

                PrintStream ps = new PrintStream(fos);
                setup(ps);

                new Thread(() ->
                {
                    StringBuffer sb = new StringBuffer();
                    StringBuffer sb2 = new StringBuffer();
                    List<Character> bfr  = new ArrayList<>();
                    BufferedReader br;
                    try
                    {
                       int pl = 0;
                       br = new BufferedReader(new FileReader("/dev/pigout"));
                        while (true)
                        {
                            
                            String in = br.readLine();
                            /*if(!in.startsWith("0"))
                            {
                                System.out.println(in);
                            }//*/
                                //System.out.println("Incoming serial msg");
                            /*if(false&&(!in.equals("0")))
                               l.trace("DEBUG:"+in);*/
                            String[] insplit = in.split(" ");

                            if(insplit.length>1)
                            {
                                for(int i=1;i<insplit.length;i++)
                                {
                                    char chr = (char)Integer.parseInt(insplit[i]);
                                    bfr.add(chr);

                                }
                                //System.out.println();
                            }
                            while(bfr.size()>1) //2er-pärchen rausholen
                            {
                                char val = bfr.remove(0);
                                char par = bfr.remove(0);
                                //l.trace("RAW:("+val+"|"+par+")");
                                int data = (val<<8)+par;
                                if(numberOfSetBits(data)%2==1)
                                {
                                    l.warn("PARITY ERROR");
                                    l.trace(String.format("%8s", Integer.toBinaryString(val)).replace(' ', '0'));
                                    l.trace(String.format("%8s", Integer.toBinaryString(par)).replace(' ', '0'));
                                }
                                sb.append(val);
                                sb2.append(String.format("%8s", Integer.toBinaryString(val)).replace(' ', '0')).
                                        append("(").append(val).append(")").append(" ");
                                pl++;
                                if(val=='\n')//||val=='\r'||pl>=10)
                                {
                                    l.trace("DEBUG:"+sb2);
                                    out.accept((addPrefix?"Response:":"")+sb);
                                    
                                    sb = new StringBuffer();
                                    sb2 = new StringBuffer();
                                    pl=0;
                                }

                            }
                        }
                    } catch (FileNotFoundException ex)
                    {
                        ex.printStackTrace();
                    } catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }).start();//*/
                 startReaderTickThread(ps);
                 BufferedReader bs = new BufferedReader(new InputStreamReader(ctrl));
                while (true)
                {
                    String line = bs.readLine()+"\n";//alphabet[new Random().nextInt(26)] + alphabet[new Random().nextInt(26)] + alphabet[new Random().nextInt(26)] + "\r\n";
                    //System.err.print("OUT:"+line);
                    l.trace("PRESEND:"+line);
                    //ps.println(line);
                    writeSerialPacket(line, ps);
                    //Thread.sleep(1000);
                }
            } 
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                try
                {
                    //fis.close();
                    fos.close();
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressWarnings("SleepWhileInLoop")
    private static void startReaderTickThread(final PrintStream ps)
    {
        new Thread(() ->
        {
            while (true)
            {
                ps.println("SLR 9 20");
                try
                {
                    Thread.sleep(10); //absicht
                } catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

      
    private static void writeSerialPacket(String msg, PrintStream pigpio_out)
    {
        final StringBuffer hexed = new StringBuffer();
        msg.chars().forEach((i)->
        {
            int bits = numberOfSetBits(i);
            
            hexed.append("0x").append(Integer.toHexString(i)).append(" ").append("0x").append(Integer.toHexString(bits%2)).append(" ");
        });
        
        String start = "WVCLR";
        String prepare = "WVCRE";
        
        String msgcmd = "WVAS 10 2400 9 2 0 "+hexed.toString();
        String send = "WVTX 0";
        l.trace("DEBUG:"+msgcmd);
        pigpio_out.println(start);
        pigpio_out.println(msgcmd);
        pigpio_out.println(prepare);
        pigpio_out.println(send);
        
    }
    private static int numberOfSetBits(int i)
    {
         // Java: use >>> instead of >>
         // C or C++: use uint32_t
         i = i - ((i >>> 1) & 0x55555555);
         i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
         return (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >>> 24;
    }
}
