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

import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.datum.DefaultEllipsoid;

public class LineX implements Cloneable {

    PointX p1;
    PointX p2;
    Line2D.Double java2dobject;

    public LineX() {
        this(0, 0, 0, 0, 0, 0);
    }

    public LineX(PointX p1, PointX p2) {
        setP1(p1);
        setP2(p2);
    }

    public LineX(double longitude1, double latitude1, double longitude2, double latitude2) {
        this(longitude1, latitude1, 0, longitude2, latitude2, 0);
    }

    public LineX(double longitude1, double latitude1, double altitude1, double longitude2, double latitude2, double altitude2) {
        this(new PointX(longitude1, latitude1, altitude1), new PointX(longitude2, latitude2, altitude2));
    }

    public double getLength() {
        return p1.distance(p2);
    }

    public double getMedianAltitude() {
        return (p1.getAltitude() + p2.getAltitude()) / 2;
    }

    public PointX getP1() {
        return p1;
    }

    public void setP1(PointX p1) {
        this.p1 = p1;
        java2dobject = null;
    }

    public PointX getP2() {
        return p2;
    }

    public void setP2(PointX p2) {
        this.p2 = p2;
        java2dobject = null;
    }

    public ArrayList<LineX> getGeodeticCurve() {
        return getGeodeticCurve(20);
    }

    public ArrayList<LineX> getGeodeticCurve(int steps) {
        ArrayList<LineX> al = new ArrayList<LineX>();
        try {
            GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
            gc.setStartingGeographicPoint(p1.getLongitude(), p1.getLatitude());
            gc.setDestinationGeographicPoint(p2.getLongitude(), p2.getLatitude());
            PathIterator pi = gc.getGeodeticCurve(steps).getPathIterator(null, 0.0000001f);

            double lastc[] = new double[6];
            while (!pi.isDone()) {
                double c[] = new double[6];
                int type = pi.currentSegment(c);
                switch (type) {
                    case PathIterator.SEG_LINETO:
                        LineX tl = new LineX(new PointX(lastc[0], lastc[1]), new PointX(c[0], c[1]));
                        al.addAll(tl.getBoundedRepresentation());
                        lastc = c;
                        break;
                    case PathIterator.SEG_MOVETO:
                        lastc = c;
                        break;
                    default:
                }
                pi.next();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public Line2D.Double asLine2D() {
        if (java2dobject == null) {
            java2dobject = new Line2D.Double(p1.getLongitude(), p1.getLatitude(), p2.getLongitude(), p2.getLatitude());
        }
        return java2dobject;
    }

    @Override
    public String toString() {
        return "LineX[(" + p1.getLongitude() + ", " + p1.getLatitude() + ", " + p1.getAltitude() + "),(" + p2.getLongitude() + ", " + p2.getLatitude() + ", " + p2.getAltitude() + ")]";
    }

    public ArrayList<LineX> getBoundedRepresentation() {
        double dreal = Math.abs(p1.getLongitude() - p2.getLongitude());
        double dpos = Math.abs(p1.getLongitude() - (p2.getLongitude() + 360));
        double dneg = Math.abs(p1.getLongitude() - (p2.getLongitude() - 360));
        ArrayList<LineX> al = new ArrayList<LineX>();
        if (dreal <= dpos && dreal <= dneg) {
            al.add(this);
        } else {
            if (dpos <= dneg) {
                double lat180 = ((180 - p1.getLongitude()) / (p2.getLongitude() + 360 - p1.getLongitude())) * (p2.getLatitude() - p1.getLatitude()) + p1.getLatitude();
                al.add(new LineX(p1, new PointX(180, lat180)));
                al.add(new LineX(new PointX(-180, lat180), p2));
            } else {
                double lat180 = ((-180 - p1.getLongitude()) / (p2.getLongitude() - 360 - p1.getLongitude())) * (p2.getLatitude() - p1.getLatitude()) + p1.getLatitude();
                al.add(new LineX(p1, new PointX(-180, lat180)));
                al.add(new LineX(new PointX(180, lat180), p2));
            }
        }
        return al;
    }

    @Override
    public Object clone() {
        return new LineX(getP1(), getP2());
    }
}
