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

package eu.flatworld.worldexplorer.provider;

import eu.flatworld.commons.log.LogX;
import eu.flatworld.worldexplorer.tile.Tile;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.LinkedHashMap;
import java.util.logging.Level;

public class MemoryProvider extends AbstractProvider {

    final static String NAME = "MemoryProvider";
    //in MB
    int minimumFreeMemory;
    LinkedHashMap<String, Tile> pool;

    public MemoryProvider() {
        this(32);
    }

    public MemoryProvider(int minimumFreeMemory) {
        LogX.log(Level.INFO, NAME + " starting");
        this.minimumFreeMemory = minimumFreeMemory;
        pool = new LinkedHashMap<String, Tile>();
    }

    @Override
    public Tile getTile(int id, int x, int y, int l) throws Exception {
        Tile tile = null;
        String s = String.format("%d_%d_%d_%d", id, l, y, x);
        tile = pool.get(s);
        if (tile == null) {
            if (getProvider() != null) {
                LogX.log(Level.FINEST, NAME + " getting from parent");
                memCheck();
                tile = getProvider().getTile(id, x, y, l);
                if (tile != null) {
                    pool.put(s, tile);
                }
            }
        }
        //LogX.log(Level.FINE, NAME + " hit: " + tile);
        return tile;
    }

    void memCheck() {
        MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        long free = mu.getMax() - mu.getUsed();
        long minimum = minimumFreeMemory * 1024 * 1024;
        int size = pool.keySet().size();
        LogX.log(Level.FINE, "memCheck: free=" + free + ",minimum=" + minimum + ",size=" + size);
//        int run = 0;
        while (size > 0 && free < minimum) {
            String k = pool.keySet().iterator().next();
            Tile t = pool.get(k);
            synchronized (t) {
                if (t != null) {
                    Image im = t.getImage();
                    if (im != null) {
                        t.getImage().flush();
                        t.setImage(null);
                    }
                    pool.remove(k);
                }
            }
//            run++;
//            if(run % 5 == 0)
//                System.gc();
            mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            free = mu.getMax() - mu.getUsed();
            size = pool.keySet().size();
            LogX.log(Level.FINE, "memCheck freeing: free=" + free + ",minimum=" + minimum + ",size=" + size);
        }
    }

    @Override
    public void reset() {
        pool.clear();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }
}
