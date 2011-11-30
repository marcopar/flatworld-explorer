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

import java.awt.geom.Point2D;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.datum.DefaultEllipsoid;

public class PointX implements Cloneable {

    double longitude;
    double latitude;
    double altitude;
    Point2D.Double java2dobject;

    public PointX() {
        this(0, 0, 0);
    }

    public PointX(double longitude, double latitude) {
        this(longitude, latitude, 0);
    }

    public PointX(double longitude, double latitude, double altitude) {
        setLocation(longitude, latitude, altitude);
    }

    public void setLocation(double longitude, double latitude, double altitude) {
        setLongitude(longitude);
        setLatitude(latitude);
        setAltitude(altitude);
    }

    public void setLocation(double longitude, double latitude) {
        setLocation(longitude, latitude, 0);
    }

    @Override
    public String toString() {
        return "PointX[" + longitude + ", " + latitude + ", " + altitude + "]";
    }

    public double distance(PointX tp) {
        try {
            GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
            gc.setStartingGeographicPoint(getLongitude(), getLatitude());
            gc.setDestinationGeographicPoint(tp.getLongitude(), tp.getLatitude());
            return gc.getOrthodromicDistance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public Object clone() {
        return new PointX(longitude, latitude, altitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointX) {
            PointX tp = (PointX) obj;
            return (getLatitude() == tp.getLatitude() &&
                    getLongitude() == tp.getLongitude() &&
                    getAltitude() == tp.getAltitude());
        }
        return super.equals(obj);
    }

    public double getLongitude() {
        return longitude;
    }

    public double getNormalizedLongitude(double longitude) {
        if (longitude > 180) {
            return longitude - 360;
        }
        if (longitude < -180) {
            return longitude + 360;
        }
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = getNormalizedLongitude(longitude);
        java2dobject = null;
    }

    public double getLatitude() {
        return getNormalizedLatitude(latitude);
    }

    public double getNormalizedLatitude(double latitude) {
        if (latitude > 90) {
            return latitude - 180;
        }
        if (latitude < -90) {
            return latitude + 180;
        }
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        java2dobject = null;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
        java2dobject = null;
    }

    public Point2D.Double asPoint2D() {
        if (java2dobject == null) {
            java2dobject = new Point2D.Double(longitude, latitude);
        }
        return java2dobject;
    }
}
