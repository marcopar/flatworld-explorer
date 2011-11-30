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

public class WorldExplorer {

    public final static String NAME = "Flatworld Explorer";
    public final static String VERSION = "1.0.3";
    public final static String USER_AGENT = NAME + "/" + VERSION;
    public final static String WORK_DIR = System.getProperty("user.home") + System.getProperty("file.separator") + "flatworld" + System.getProperty("file.separator");
    public final static String LOG_FILE = WORK_DIR + "flatworldexplorer";
    public final static String OPTIONS_FILE = WORK_DIR + "flatworldexplorer.prop";
    public final static String OPTIONS_DEFAULT_DATAPATH = "Data";
    public final static String OPTIONS_DEFAULT_CACHEPATH = WORK_DIR + "Cache";
    public final static int OPTIONS_DEFAULT_CACHESIZE = 0;
    public final static boolean OPTIONS_DEFAULT_SHOWGRID = true;
    public final static boolean OPTIONS_DEFAULT_SHOWCENTER = true;
    public final static boolean OPTIONS_DEFAULT_SHOWBOUNDARIES = false;
    public final static boolean OPTIONS_DEFAULT_SHOWPLACENAMES = false;
}
