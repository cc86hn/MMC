/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client.API;

import java.util.List;
import org.cc86.MMC.API.Packet;

/**
 *
 * @author iZc <nplusc.de>
 */
public interface Module
{
    public void receiveMsgFromServer(Packet msg);
    public void connect(Connection c);
    public List<String> getCommands();
    public default void loadUI(){};
    public default void quit(){};
}
