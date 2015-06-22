/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import de.nplusc.izc.tools.IOtools.FileTK;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Plugin;
import org.cc86.MMC.API.Resources;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author tgoerner
 */
public class PluginManager {
    private static final Logger l = LogManager.getLogger();
    private List<Plugin> detectedPlugs = new ArrayList<>();
    
    public void loadPlugins()
    {
        Yaml y = new Yaml();
        String[] jarsInPluginFolder = FileTK.getDirectoryContent(API.PLUGINPATH);
        for (String plugin : jarsInPluginFolder)
        {
            TFile pluginattributes = new TFile(plugin + File.separator + "plugin.yml");
            if (pluginattributes.exists())
            {
                try
                {
                    TFileReader r =new TFileReader(pluginattributes);
                    HashMap<String, Object> plugindata = (HashMap<String, Object>) y.load(r);
                    r.close();
                    Class clazz = new URLClassLoader(new URL[]
                    {
                        new File(plugin).toURI().toURL()
                    }).loadClass((String) plugindata.get("pluginbaseclass"));
                    // holen der Interfaces die die Klasse impementiert
                    Class[] interfaces = clazz.getInterfaces();
                    // Durchlaufen durch die Interfaces der Klasse und nachsehn ob es das passende Plugin implementiert
                    boolean isplugin = false;
                    for (Class intf : interfaces)
                    {
                        if (intf.getName().equals("org.cc86.MMC.API.Plugin"))
                        {
                            isplugin = true;
                            break;
                        }
                    }
                    if (isplugin)
                    {
                        Plugin pluginInstance = (Plugin) clazz.newInstance();
                        detectedPlugs.add(pluginInstance);
                       l.trace(pluginInstance);
                        pluginInstance.register();
                    }
                    
                }
                catch (FileNotFoundException | ClassNotFoundException | MalformedURLException | InstantiationException | IllegalAccessException ex)
                {
                    ex.printStackTrace();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
                
            }
        }
    }

    public void shitdown()
    {
        detectedPlugs.forEach((a)->a.shutdown());
    }
    
    public void freeRessources(Resources... r)
    {
        detectedPlugs.forEach((Plugin p)->p.freeUpResources(r));
    }
    
    
}

