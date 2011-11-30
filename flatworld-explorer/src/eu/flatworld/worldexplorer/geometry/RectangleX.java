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

package eu.flatworld.worldexplorer.geometry;

import java.awt.geom.Rectangle2D;

public class RectangleX {

    private double longitude;
    private double latitude;
    private double width;
    private double height;
    Rectangle2D.Double java2dobject;

    public RectangleX() {
        this(0, 0, 0, 0);
    }

    public RectangleX(double longitude, double latitude, double width, double height) {
        setLongitude(longitude);
        setLatitude(latitude);
        setWidth(width);
        setHeight(height);
    }

    public Rectangle2D.Double asRectangle2D() {
        if (java2dobject == null) {
            java2dobject = new Rectangle2D.Double(longitude, latitude, width, height);
        }
        return java2dobject;
    }

    @Override
    public String toString() {
        return "RectangleX[(" + longitude + ", " + latitude + "),(" + width + ", " + height + ")]";
    }

    public PointX getP1() {
        return new PointX(longitude, latitude);
    }

    public PointX getP2() {
        return new PointX(longitude + width, latitude + height);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        java2dobject = null;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        java2dobject = null;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        java2dobject = null;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        java2dobject = null;
    }
}
