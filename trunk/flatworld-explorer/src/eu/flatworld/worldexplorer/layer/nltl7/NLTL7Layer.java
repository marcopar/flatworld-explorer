/*
   Copyright 2011 marcopar@gmail.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package eu.flatworld.worldexplorer.layer.nltl7;

import eu.flatworld.commons.log.LogX;
import eu.flatworld.commons.properties.PropertiesX;
import eu.flatworld.worldexplorer.Config;
import eu.flatworld.worldexplorer.WorldExplorer;
import eu.flatworld.worldexplorer.layer.Layer;
import eu.flatworld.worldexplorer.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class NLTL7Layer extends PropertiesX implements Layer {

    public static String NAME = "NLTL7";
    public static int ID = 2;
    public static int WIDTH = 512;
    public static int HEIGHT = 512;
    public static int SURFACE_WIDTH = 160;
    public static int SURFACE_HEIGHT = 80;
    public static int MAX_LAYERS = 4;
    public static double ZERO_LEVEL_APERTURE = 2.25d;
    public static String HTTP_BASE = "http://nww.terraserver-usa.com/nwwtile.ashx?T=105&L=%d&X=%d&Y=%d";
    public static String FILE_BASE = "/Earth/NLTL7/NLT Landsat7 (Visible Color)";
    NLTL7HTTPProvider httpProvider;
    NLTL7DataProvider dataProvider;
    NLTL7CacheProvider cacheProvider;

    public NLTL7Layer() {
        super(NAME + " Layer", WorldExplorer.WORK_DIR + NAME + ".prop");
        try {
            if (!new File(WorldExplorer.WORK_DIR + NAME + ".prop").exists()) {
                store();
            } else {
                load();
            }
        } catch (IOException ex) {
            LogX.log(Level.WARNING, "Unable to load " + WorldExplorer.WORK_DIR + NAME + ".prop");
        }
    }

    @Override
    public synchronized void store() throws IOException {
        setIntegerProperty(NAME + "_width", WIDTH);
        setIntegerProperty(NAME + "_height", HEIGHT);
        setIntegerProperty(NAME + "_surfacewidth", SURFACE_WIDTH);
        setIntegerProperty(NAME + "_surfaceheight", SURFACE_HEIGHT);
        setIntegerProperty(NAME + "_maxlayers", MAX_LAYERS);
        setDoubleProperty(NAME + "_zerolevelaperture", ZERO_LEVEL_APERTURE);
        setStringProperty(NAME + "_httpbase", HTTP_BASE);
        setStringProperty(NAME + "_filebase", FILE_BASE);
        super.store();
    }

    @Override
    public synchronized void load() throws IOException {
        super.load();
        WIDTH = getIntegerProperty(NAME + "_width", WIDTH);
        HEIGHT = getIntegerProperty(NAME + "_height", HEIGHT);
        SURFACE_WIDTH = getIntegerProperty(NAME + "_surfacewidth", SURFACE_WIDTH);
        SURFACE_HEIGHT = getIntegerProperty(NAME + "_surfaceheight", SURFACE_HEIGHT);
        MAX_LAYERS = getIntegerProperty(NAME + "_maxlayers", MAX_LAYERS);
        ZERO_LEVEL_APERTURE = getDoubleProperty(NAME + "_zerolevelaperture", ZERO_LEVEL_APERTURE);
        HTTP_BASE = getStringProperty(NAME + "_httpbase", HTTP_BASE);
        FILE_BASE = getStringProperty(NAME + "_filebase", FILE_BASE);
    }

    @Override
    public String getName() {
        return NLTL7Layer.NAME;
    }

    @Override
    public int getTileWidth() {
        return NLTL7Layer.WIDTH;
    }

    @Override
    public int getTileHeight() {
        return NLTL7Layer.HEIGHT;
    }

    @Override
    public int getSurfaceWidth() {
        return NLTL7Layer.SURFACE_WIDTH;
    }

    @Override
    public int getSurfaceHeight() {
        return NLTL7Layer.SURFACE_HEIGHT;
    }

    @Override
    public int getMaxLayers() {
        return NLTL7Layer.MAX_LAYERS;
    }

    @Override
    public double getZeroLevelAperture() {
        return NLTL7Layer.ZERO_LEVEL_APERTURE;
    }

    @Override
    public int getID() {
        return NLTL7Layer.ID;
    }

    @Override
    public Provider getCacheProvider() {
        return cacheProvider;
    }

    @Override
    public Provider getDataProvider() {
        return dataProvider;
    }

    @Override
    public Provider getRemoteProvider() {
        return httpProvider;
    }

    @Override
    public void reinitProviders() {
        if (httpProvider != null) {
            httpProvider.reset();
        }
        httpProvider = new NLTL7HTTPProvider();
        if (dataProvider != null) {
            dataProvider.reset();
        }
        dataProvider = new NLTL7DataProvider(Config.getConfig().getDataPath());
        if (cacheProvider != null) {
            cacheProvider.reset();
        }
        cacheProvider = new NLTL7CacheProvider(Config.getConfig().getCachePath());
    }
}
