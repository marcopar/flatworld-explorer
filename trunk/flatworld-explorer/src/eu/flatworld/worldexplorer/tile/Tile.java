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

package eu.flatworld.worldexplorer.tile;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public interface Tile {

    public void draw(Graphics2D g2);

    public BufferedImage getImage();

    public void setImage(BufferedImage image);

    public int getL();

    public void setL(int l);

    public int getX();

    public void setX(int x);

    public int getY();

    public void setY(int y);

    public int getGx();

    public void setGx(int gx);

    public int getGy();

    public void setGy(int gy);

    public Rectangle2D getBounds();

    public Rectangle2D getBounds(double zoom);
}
