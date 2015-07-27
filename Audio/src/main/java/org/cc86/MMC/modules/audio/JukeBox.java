/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

import de.nplusc.izc.iZpl.API.shared.InvalidPlayListFileException;
import de.nplusc.izc.iZpl.API.shared.PlayListFile;
import de.nplusc.izc.iZpl.API.shared.RawPlayListFile;
import de.nplusc.izc.iZpl.API.shared.SinglePlayListItem;
import de.nplusc.izc.iZpl.Utils.shared.PLFileIO;
import de.nplusc.izc.tools.IOtools.FileTK;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tgoerner
 */
public class JukeBox
{
    private static final Logger l = LogManager.getLogger();
    
    private HashMap<String,PlayListFile> userLists;
    private String rootPath;
    
    public JukeBox(String rootFilepath)
    {
        try
        {
            userLists = new HashMap<>();
            rootPath = rootFilepath;
            if(new File(rootFilepath).exists())
            {
                RawPlayListFile rootFile = PLFileIO.readSingleList(rootFilepath);
                List<SinglePlayListItem> data = rootFile.getData();
                for (SinglePlayListItem element : data)
                {
                    String id = FileTK.getFileName(element.getPath());
                    List<SinglePlayListItem> playlistdata = PLFileIO.readSingleList(element.getPath()).getData();
                    PlayListFile file = new PlayListFile();
                    file.setEntries(data);
                    file.setPath(element.getPath());
                    file.setCalculatedBasePriority(1);
                    userLists.put(id, new PlayListFile());
                }
            }
        }
        catch (InvalidPlayListFileException ex)
        {
            l.error("The playlistFile ({}) was invalid. This should not happen at all!",rootFilepath);
        }
    }
    
    public void save()
    {
        save(false);
    }   
    private void save(boolean onShutdown)
    {
        List<SinglePlayListItem> masterList = new ArrayList<>();
        for(String id:userLists.keySet())
        {
            PlayListFile p = userLists.get(id);
            PLFileIO.writePLFile(p);
            SinglePlayListItem itm = new SinglePlayListItem(p.getPath(), id, 1, true, false);
            masterList.add(itm);
        }
        
        PlayListFile plf = new PlayListFile();
        plf.setCalculatedBasePriority(1);
        plf.setEntries(masterList);
        plf.setPath(rootPath);
        PLFileIO.writePLFile(plf);
        if(!onShutdown)
        {
            //TODO iZpl::reloadTrigger
        }
    }
    
    public void shutdown()
    {
        save(true);
    }
    
}
