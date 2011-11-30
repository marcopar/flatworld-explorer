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

package eu.flatworld.worldexplorer;

import eu.flatworld.commons.properties.PropertiesX;
import java.io.IOException;
import java.util.logging.Level;

public class Config extends PropertiesX {

    static Config instance = null;
    private String dataPath = WorldExplorer.OPTIONS_DEFAULT_DATAPATH;
    private String cachePath = WorldExplorer.OPTIONS_DEFAULT_CACHEPATH;
    private int cacheSize = WorldExplorer.OPTIONS_DEFAULT_CACHESIZE;
    private boolean showGrid = WorldExplorer.OPTIONS_DEFAULT_SHOWGRID;
    private boolean showCenter = WorldExplorer.OPTIONS_DEFAULT_SHOWCENTER;
    private boolean showBoundaries = WorldExplorer.OPTIONS_DEFAULT_SHOWBOUNDARIES;
    private boolean showPlacenames = WorldExplorer.OPTIONS_DEFAULT_SHOWPLACENAMES;
    private String logLevel = Level.INFO.getName();
    private int minimumMemoryLimit = 32;

    private Config() {
        super(WorldExplorer.NAME + " " + WorldExplorer.VERSION, WorldExplorer.OPTIONS_FILE);
    }

    public synchronized static Config getConfig() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    @Override
    public synchronized void store() throws IOException {
        setStringProperty("datapath", dataPath);
        setStringProperty("cachepath", cachePath);
        //setIntegerProperty("cachesize", cacheSize);
        setBooleanProperty("showgrid", showGrid);
        setBooleanProperty("showcenter", showCenter);
        setBooleanProperty("showboundaries", showBoundaries);
        setBooleanProperty("showplacenames", showPlacenames);
        setIntegerProperty("minimummemorylimit", minimumMemoryLimit);
        setStringProperty("loglevel", logLevel);
        super.store();
    }

    @Override
    public synchronized void load() throws IOException {
        super.load();
        dataPath = getStringProperty("datapath", dataPath);
        cachePath = getStringProperty("cachepath", cachePath);
        //cacheSize = getIntegerProperty("cachesize", cacheSize);
        showGrid = getBooleanProperty("showgrid", showGrid);
        showCenter = getBooleanProperty("showcenter", showCenter);
        showBoundaries = getBooleanProperty("showboundaries", showBoundaries);
        showPlacenames = getBooleanProperty("showplacenames", showPlacenames);
        minimumMemoryLimit = getIntegerProperty("minimummemorylimit", minimumMemoryLimit);
        logLevel = getStringProperty("loglevel", logLevel);
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public boolean isShowCenter() {
        return showCenter;
    }

    public void setShowCenter(boolean showCenter) {
        this.showCenter = showCenter;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public int getMinimumMemoryLimit() {
        return minimumMemoryLimit;
    }

    public void setMinimumMemoryLimit(int minimumMemoryLimit) {
        this.minimumMemoryLimit = minimumMemoryLimit;
    }

    public boolean isShowBoundaries() {
        return showBoundaries;
    }

    public void setShowBoundaries(boolean showBoundaries) {
        this.showBoundaries = showBoundaries;
    }

    public boolean isShowPlacenames() {
        return showPlacenames;
    }

    public void setShowPlacenames(boolean showPlacenames) {
        this.showPlacenames = showPlacenames;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
}
