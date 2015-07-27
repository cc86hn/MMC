/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client.jukebox;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tgoerner
 */
public class JukeBoxFileProvider implements HttpHandler
{
    //fileID zu filemapping
    private HashMap<String, String> fileMapping = new HashMap<>();

    private static final Logger l = LogManager.getLogger();

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        String fileID = exchange.getRequestURI().toString().substring(1);
        l.trace(fileID);

        
        boolean fx = fileMapping.containsKey(fileID);
        File file = new File(fileMapping.get(fileID));
        if (!fx && !file.exists())
        {
            String response = "404 File not found at all";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            return;
        }
        Headers h = exchange.getResponseHeaders();
        h.add("Content-Type", "application/octet-stream");
        byte[] bytearray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(bytearray, 0, bytearray.length);
        exchange.sendResponseHeaders(200, file.length());
        OutputStream os = exchange.getResponseBody();
        os.write(bytearray, 0, bytearray.length);
        os.close();

    }

}
