/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import de.nplusc.izc.tools.IOtools.FileTK;
import de.nplusc.izc.tools.baseTools.Detectors;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Plugin;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author tgoerner
 */
public class PluginManager {
    public void loadPlugins()
    {
        Yaml y = new Yaml();
        String[] osdata = Detectors.getSystemClassification();
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
                    
                    List<String> supportedOSes = (List<String>) plugindata.get("supportedoses");
                    List<String> supportedArchs = (List<String>) plugindata.get("supportedarchitectures");
                    if (supportedOSes.contains(osdata[0]) && supportedArchs.contains(osdata[1]))
                    {
                        Class clazz = new URLClassLoader(new URL[]
                        {
                            new File(plugin).toURI().toURL()
                        }).loadClass((String) plugindata.get("pluginbaseclass"));
                        // holen der Interfaces die die Klasse impementiert
                        Class[] interfaces = clazz.getInterfaces();
                        // Durchlaufen durch die Interfaces der Klasse und nachsehn ob es das passende Plugin implementiert
                        boolean isplugin = false;
                        boolean isPlaybackPlugin = false;
                        boolean isUIPlugin = false;
                        boolean isFeaturePlugin=false;
                        for (int i = 0; i < interfaces.length && !isplugin; i++)
                        {
                            if (interfaces[i].getName().equals("de.nplusc.izc.iZpl.API.PlaybackPlugin"))
                            {
                                isplugin = true;
                                isPlaybackPlugin = true;
                                
                            }
                            if (interfaces[i].getName().equals("de.nplusc.izc.iZpl.API.UIPlugin"))
                            {
                                isplugin = true;
                                isUIPlugin = true;
                                
                            }
                            if(interfaces[i].getName().equals("de.nplusc.izc.iZpl.API.FeaturePlugin"))
                            {
                                isplugin=isFeaturePlugin=true;
                            }
                        }
                        if (isplugin)
                        {
                            Plugin pluginInstance = (Plugin) clazz.newInstance();
                        }
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
}
