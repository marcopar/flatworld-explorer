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
import eu.flatworld.worldexplorer.provider.Provider;
import eu.flatworld.worldexplorer.tile.Tile;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;

public class BMNGCacheProvider extends BMNGProvider implements Runnable {

    final static String NAME = "BMNGFilesystemProvider";
    String path;

    public BMNGCacheProvider(String path) {
        this.path = path;
        LogX.log(Level.INFO, NAME + " starting: " + path);

        Thread queueRunner = new Thread(this, this.getClass().getName() + "_queueRunner");
        queueRunner.setDaemon(true);
        queueRunner.start();
    }

    @Override
    public Tile getTile(int id, int x, int y, int l) throws Exception {
        BMNGTile tile = null;
        String s = String.format("%s" + BMNGLayer.FILE_BASE + "/%d/%04d/%04d_%04d.jpg", path, month, year, l, y, y, x);
        String sdir = String.format("%s" + BMNGLayer.FILE_BASE + "/%d/%04d/", path, month, year, l, y);
        new File(sdir).mkdirs();
        File f = new File(s);
        if (f.exists()) {
            tile = new BMNGTile();
            tile.setX(x);
            tile.setY(y);
            tile.setGx(x);
            tile.setGy((int) Math.pow(2, l) * BMNGLayer.SURFACE_HEIGHT - 1 - y);
            tile.setL(l);
            tile.setMonth(month);
            tile.setYear(year);
            LogX.log(Level.FINE, NAME + " queueing: " + tile);
            firePropertyChange(P_IDLE, true, false);
            queueTile(tile);
        } else {
            if (getProvider() != null) {
                LogX.log(Level.FINE, NAME + " getting from parent");
                tile = (BMNGTile) getProvider().getTile(id, x, y, l);
            }
        }
        return tile;
    }

    @Override
    public void run() {
        while (true) {
            while (getQueueSize() != 0) {
                Tile tile = peekTile();
                synchronized (tile) {
                    String s = String.format("%s" + BMNGLayer.FILE_BASE + "/%d/%04d/%04d_%04d.jpg", path, month, year, tile.getL(), tile.getY(), tile.getY(), tile.getX());
                    LogX.log(Level.FINER, s);
                    try {
                        File f = new File(s);
                        if (f.exists()) {
                            BufferedImage bi = ImageIO.read(f);
                            tile.setImage(bi);
                        }

                    } catch (Exception ex) {
                        LogX.log(Level.FINER, "", ex);
                    }
                    LogX.log(Level.FINEST, NAME + " dequeueing: " + tile);
                    unqueueTile(tile);
                }
                firePropertyChange(P_DATAREADY, null, tile);
                Thread.yield();
            }
            firePropertyChange(P_IDLE, false, true);
            try {
                Thread.sleep(QUEUE_SLEEP_TIME);
            } catch (Exception ex) {
            }
            ;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof Provider) {
            if (evt.getPropertyName().equals(Provider.P_DATAREADY)) {
                Tile tile = (Tile) evt.getNewValue();
                synchronized (tile) {
                    LogX.log(Level.FINEST, NAME + " saving: " + tile);
                    String s = String.format("%s" + BMNGLayer.FILE_BASE + "/%d/%04d/%04d_%04d.jpg", path, month, year, tile.getL(), tile.getY(), tile.getY(), tile.getX());
                    File f = new File(s);
                    if (tile != null && tile.getImage() != null) {
                        try {
                            f.mkdirs();
                            ImageIO.write(tile.getImage(), "jpg", f);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            f.delete();
                        }
                    }
                }
            }
        }
        firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }
}
