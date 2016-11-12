/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client.API;

import java.io.IOException;
import org.cc86.MMC.API.Packet;

/**
 *
 * @author iZc <nplusc.de>
 */
public interface Connection
{
    public void sendRequest(Packet request);
    public void connect() throws IOException;
}
