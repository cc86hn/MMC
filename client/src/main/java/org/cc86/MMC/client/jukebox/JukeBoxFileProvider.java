/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client.jukebox;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.nplusc.izc.iZpl.API.shared.InvalidPlayListFileException;
import de.nplusc.izc.iZpl.API.shared.SinglePlayListItem;
import de.nplusc.izc.iZpl.Utils.shared.PLFileIO;
import de.nplusc.izc.tools.IOtools.FileTK;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.client.Mod_Jukebox;

/**
 *
 * @author tgoerner
 */
public class JukeBoxFileProvider implements HttpHandler
{
    //filename zu filemapping
    private HashMap<String, String> fileMapping = new HashMap<>();
    
    private final Mod_Jukebox jbx;

    public JukeBoxFileProvider(Mod_Jukebox jbx)
    {
        this.jbx = jbx;
    }
    
    
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
        fileID=URLDecoder.decode(fileID, "UTF-8");
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
    private final List<String> validPLFexts = Arrays.asList
        ("izpl","m3u");
    
    private void updateList()
    {
        fileMapping.clear();
        List<String> files = new LinkedList<>();
        paths.forEach((s)->
        {
           if(new File(s).isFile()&&validPLFexts.contains(FileTK.getFileExt(s).toLowerCase()))
           {
               files.addAll(getiZplAsPlainList(s));
           }
           else
           {
               files.addAll(Arrays.asList(FileTK.walkDirectoryRecursively(s)));
           }
           
        });
        
        files.stream().parallel().filter((s)->validFileExts.contains(FileTK.getFileExt(s).toLowerCase()))
                .sequential().forEach((s)->fileMapping.put(FileTK.getFileName(s)/*.replaceAll("[^\\x00-\\x7F]", "").replaceAll(" ", "")*/, s));
        jbx.sendOwnPoolpart(Arrays.asList(fileMapping.keySet().toArray(new String[0])));
    }
    
    
    private List<String> getiZplAsPlainList(String path)
    {   
        ArrayList<String> fileZ = new ArrayList<>();
        try
        {
            PLFileIO.parseFullList(path,40,true).forEach((e)->fileZ.add(((SinglePlayListItem)e).getPath()));
            
        } 
        catch (InvalidPlayListFileException ex)
        {
            ex.printStackTrace();
        }
        return fileZ;
    }
    
}
