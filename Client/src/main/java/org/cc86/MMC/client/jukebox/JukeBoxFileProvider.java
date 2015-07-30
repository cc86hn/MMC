/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client.jukebox;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.nplusc.izc.tools.IOtools.FileTK;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tgoerner
 */
public class JukeBoxFileProvider implements HttpHandler
{
    //filename zu filemapping
    private HashMap<String, String> fileMapping = new HashMap<>();
    
    private Set<String> paths = new HashSet<>();
    
    //javabeans for SnakeYaml serialize
    public Set<String> getPaths()
    {
        return paths;
    }

    public void setPaths(Set<String> paths)
    {
        this.paths = paths;
    }
    
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
    
    public void addDirectoryForScan(String dir)
    {
        paths.add(dir);
        updateList();
    }
    private final List<String> validFileExts = Arrays.asList
        ("mp3","m4a","mp4","wav","flac","aac","ac3","wma","mod","m4a","ogg","flv");
    
    private void updateList()
    {
        List<String> files = new LinkedList<>();
        paths.forEach((s)->
        {
           if()
           files.addAll(Arrays.asList(FileTK.walkDirectoryRecursively(s)));
        });
        
        files.stream().parallel().filter((s)->validFileExts.contains(s!=null?FileTK.getFileExt(s):""))
                .sequential().forEach((s)->fileMapping.put(FileTK.getFileName(s), s));
    }
    
}
