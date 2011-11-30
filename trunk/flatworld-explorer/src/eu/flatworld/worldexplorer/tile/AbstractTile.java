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

import java.awt.image.BufferedImage;

public abstract class AbstractTile implements Tile {

    protected BufferedImage image;
    protected int l;
    protected int x;
    protected int y;
    protected int gx;
    protected int gy;

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public int getL() {
        return l;
    }

    @Override
    public void setL(int l) {
        this.l = l;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getGx() {
        return gx;
    }

    @Override
    public void setGx(int gx) {
        this.gx = gx;
    }

    @Override
    public int getGy() {
        return gy;
    }

    @Override
    public void setGy(int gy) {
        this.gy = gy;
    }
}
