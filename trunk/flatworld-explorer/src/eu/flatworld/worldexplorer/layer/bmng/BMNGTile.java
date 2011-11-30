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

import eu.flatworld.worldexplorer.tile.AbstractTile;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class BMNGTile extends AbstractTile {

    int month;
    int year;

    public BMNGTile() {
    }

    @Override
    public void draw(Graphics2D g2) {
        if (image != null) {
            g2.drawImage(image, gx * BMNGLayer.WIDTH, gy * BMNGLayer.HEIGHT, BMNGLayer.WIDTH, BMNGLayer.HEIGHT, null);
        }
//        g2.setColor(Color.RED);
//        g2.drawRect(x * WIDTH, y * HEIGHT, WIDTH, HEIGHT);
//        
//        String str = l + "," + x + "," + (Math.pow(2,l) * 5 - 1 - y);
//        int asc = g2.getFontMetrics().getAscent();
//        int desc = g2.getFontMetrics().getDescent();
//        int sw = g2.getFontMetrics().stringWidth(str);
//        g2.drawString(str, (int)(x * WIDTH), (int)(y * HEIGHT + asc));
//        g2.drawString(str, (int)(x * WIDTH), (int)(y * HEIGHT + HEIGHT - desc));
//        g2.drawString(str, (int)(x * WIDTH + WIDTH - sw), (int)(y * HEIGHT + asc));
//        g2.drawString(str, (int)(x * WIDTH + WIDTH - sw), (int)(y * HEIGHT + HEIGHT - desc));
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void setImage(BufferedImage image) {
        BufferedImage old = image;
        this.image = image;
    //firePropertyChange(P_IMAGE_UPDATED, old, image);
    }

    @Override
    public Rectangle2D getBounds() {
        return getBounds(1);
    }

    @Override
    public Rectangle2D getBounds(double zoom) {
        return new Rectangle2D.Double(gx * BMNGLayer.WIDTH * zoom, gy * BMNGLayer.HEIGHT * zoom, BMNGLayer.WIDTH * zoom, BMNGLayer.HEIGHT * zoom);
    }

    @Override
    public String toString() {
        return "BMNG x=" + x + ", y=" + y + ", l=" + l;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
