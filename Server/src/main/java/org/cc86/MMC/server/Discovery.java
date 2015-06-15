/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tgoerner
 */
public class Discovery implements Runnable
{
    private static final Logger l = LogManager.getLogger();
  DatagramSocket socket;

  @Override
  public void run() {
    try {
      //Keep a socket open to listen to all the UDP trafic that is destined for this port
      socket = new DatagramSocket(0xCC86, InetAddress.getByName("0.0.0.0"));
      socket.setBroadcast(true);

      while (true) {
        l.info("Ready to receive broadcast packets!");

        //Receive a packet
        byte[] recvBuf = new byte[15000];
        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
        socket.receive(packet);

        //Packet received
        l.info("Discovery packet received from: " + packet.getAddress().getHostAddress());
        l.info("Packet received; data: " + new String(packet.getData()));

        //See if the packet holds the right command (message)
        String message = new String(packet.getData()).trim();
        if (message.equals("DISCOVER_MMC_REQUEST")) {
          byte[] sendData = "DISCOVER_MMC_RESPONSE".getBytes();

          //Send a response
          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
          socket.send(sendPacket);

          l.info("Sent packet to: " + sendPacket.getAddress().getHostAddress());
        }
      }
    } catch (IOException ex) {
      l.warn("IOEXCEPTION");
    }
  }

  public static Discovery getInstance() {
    return DiscoveryThreadHolder.INSTANCE;
  }

  private static class DiscoveryThreadHolder {

    private static final Discovery INSTANCE = new Discovery();
  }
}
