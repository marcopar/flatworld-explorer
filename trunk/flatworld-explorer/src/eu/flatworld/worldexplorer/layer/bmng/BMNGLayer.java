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

package eu.flatworld.worldexplorer.layer.bmng;

import eu.flatworld.commons.log.LogX;
import eu.flatworld.commons.properties.PropertiesX;
import eu.flatworld.worldexplorer.Config;
import eu.flatworld.worldexplorer.WorldExplorer;
import eu.flatworld.worldexplorer.layer.Layer;
import eu.flatworld.worldexplorer.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.logging.Level;

public class BMNGLayer extends PropertiesX implements Layer {

    public static String NAME = "BMNG";
    public static int ID = 1;
    public static int WIDTH = 512;
    public static int HEIGHT = 512;
    public static int SURFACE_WIDTH = 10;
    public static int SURFACE_HEIGHT = 5;
    public static int MAX_LAYERS = 4;
    public static double ZERO_LEVEL_APERTURE = 36d;
    public static String HTTP_BASE = "http://worldwind25.arc.nasa.gov/tile/tile.aspx?T=bmng.topo.bathy.%04d%02d&L=%d&X=%d&Y=%d";
    public static String FILE_BASE = "/Earth/BMNG/BMNG (Shaded + Bathymetry) Tiled - %d.%d";
    BMNGHTTPProvider httpProvider;
    BMNGDataProvider dataProvider;
    BMNGCacheProvider cacheProvider;
    public int month = 5;
    public int year = 2004;

    public BMNGLayer() {
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

        reinitProviders();
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
        setIntegerProperty(NAME + "_month", month);
        setIntegerProperty(NAME + "_year", year);
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
        month = getIntegerProperty(NAME + "_month", month);
        year = getIntegerProperty(NAME + "_year", year);
    }

    @Override
    public String getName() {
        return BMNGLayer.NAME;
    }

    @Override
    public int getTileWidth() {
        return BMNGLayer.WIDTH;
    }

    @Override
    public int getTileHeight() {
        return BMNGLayer.HEIGHT;
    }

    @Override
    public int getSurfaceWidth() {
        return BMNGLayer.SURFACE_WIDTH;
    }

    @Override
    public int getSurfaceHeight() {
        return BMNGLayer.SURFACE_HEIGHT;
    }

    @Override
    public int getMaxLayers() {
        return BMNGLayer.MAX_LAYERS;
    }

    @Override
    public double getZeroLevelAperture() {
        return BMNGLayer.ZERO_LEVEL_APERTURE;
    }

    @Override
    public int getID() {
        return BMNGLayer.ID;
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
        GregorianCalendar gc = new GregorianCalendar();
        if (httpProvider != null) {
            httpProvider.reset();
        }
        httpProvider = new BMNGHTTPProvider();
        httpProvider.setMonth(month);
        httpProvider.setYear(year);
        if (dataProvider != null) {
            dataProvider.reset();
        }
        dataProvider = new BMNGDataProvider(Config.getConfig().getDataPath());
        dataProvider.setMonth(month);
        dataProvider.setYear(year);
        if (cacheProvider != null) {
            cacheProvider.reset();
        }
        cacheProvider = new BMNGCacheProvider(Config.getConfig().getCachePath());
        cacheProvider.setMonth(month);
        cacheProvider.setYear(year);
    }
}
