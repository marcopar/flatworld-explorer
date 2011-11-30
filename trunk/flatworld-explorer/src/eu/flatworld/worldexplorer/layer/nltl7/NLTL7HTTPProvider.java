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
import eu.flatworld.worldexplorer.WorldExplorer;
import eu.flatworld.worldexplorer.tile.Tile;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.InputStream;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class NLTL7HTTPProvider extends NLTL7Provider implements Runnable {

    final static String NAME = "NLTL7HTTPProvider";
    HttpClient client;

    public NLTL7HTTPProvider() {
        LogX.log(Level.INFO, NAME + " starting");
        client = new HttpClient();

        Thread queueRunner = new Thread(this, this.getClass().getName() + "_queueRunner");
        queueRunner.setDaemon(true);
        queueRunner.start();
    }

    @Override
    public Tile getTile(int id, int x, int y, int l) throws Exception {
        Tile tile = null;
        tile = new NLTL7Tile();
        tile.setX(x);
        tile.setY(y);
        tile.setGx(x);
        tile.setGy((int) Math.pow(2, l) * NLTL7Layer.SURFACE_HEIGHT - 1 - y);
        tile.setL(l);
        LogX.log(Level.FINEST, NAME + " queueing: " + tile);
        firePropertyChange(P_IDLE, true, false);
        queueTile(tile);
        return tile;
    }

    @Override
    public void run() {
        while (true) {
            while (getQueueSize() != 0) {
                Tile tile = peekTile();
                synchronized (tile) {
                    String s = String.format(NLTL7Layer.HTTP_BASE, tile.getL(), tile.getX(), tile.getY());
                    LogX.log(Level.FINER, s);
                    GetMethod gm = new GetMethod(s);
                    gm.setRequestHeader("User-Agent", WorldExplorer.USER_AGENT);
                    try {
                        int response = client.executeMethod(gm);
                        LogX.log(Level.FINEST, NAME + " " + response + " " + s);
                        if (response == HttpStatus.SC_OK) {
                            InputStream is = gm.getResponseBodyAsStream();
                            BufferedImage bi = ImageIO.read(is);
                            is.close();
                            if (bi != null) {
                                tile.setImage(bi);
                            }
                        }
                    } catch (Exception ex) {
                        LogX.log(Level.FINER, "", ex);
                    } finally {
                        gm.releaseConnection();
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
        firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }
}
