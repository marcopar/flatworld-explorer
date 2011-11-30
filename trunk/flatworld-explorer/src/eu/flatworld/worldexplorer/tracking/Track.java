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

package eu.flatworld.worldexplorer.tracking;

import eu.flatworld.worldexplorer.geometry.LineX;
import eu.flatworld.worldexplorer.geometry.PointX;
import java.awt.Color;
import java.util.ArrayList;

public class Track {

    ArrayList<LineX> elements;
    private Color color;

    public Track() {
        elements = new ArrayList<LineX>();
        color = new Color(255, 255, 0, 150);
    }

    public ArrayList<LineX> getElements() {
        return elements;
    }

    public void setElements(ArrayList<LineX> elements) {
        this.elements = elements;
    }

    public PointX getStartPoint() {
        if (elements.size() == 0) {
            return null;
        }
        return elements.get(0).getP1();
    }

    public PointX getEndPoint() {
        if (elements.size() == 0) {
            return null;
        }
        return elements.get(elements.size() - 1).getP2();
    }

    public double getLength() {
        double res = 0;
        for (LineX elem : elements) {
            res += elem.getLength();
        }
        return res;
    }

    public double getMedianAltitude() {
        double res = 0;
        for (LineX elem : elements) {
            res += (elem.getLength() * elem.getMedianAltitude());
        }
        return res / getLength();
    }

    public double getAscent() {
        double res = 0;
        for (LineX elem : elements) {
            double diff = elem.getP2().getAltitude() - elem.getP1().getAltitude();
            if (diff > 0) {
                res += diff;
            }
        }
        return res;
    }

    public double getDescent() {
        double res = 0;
        for (LineX elem : elements) {
            double diff = elem.getP2().getAltitude() - elem.getP1().getAltitude();
            if (diff < 0) {
                res += diff;
            }
        }
        return res;
    }

    public double getAltitudeVariation() {
        double resp = 0;
        if (elements.size() == 0) {
            return 0;
        }
        return getEndPoint().getAltitude() - getStartPoint().getAltitude();
    }

    public Track getGeodeticCurve() {
        return getGeodeticCurve(20);
    }

    public Track getGeodeticCurve(int steps) {
        ArrayList<LineX> al = new ArrayList<LineX>();
        for (LineX elem : elements) {
            al.addAll(elem.getGeodeticCurve(steps));
        }
        Track track = new Track();
        track.setElements(al);
        return track;
    }

    public static Track getTestTrack() {
        Track t = new Track();
//        t.getElements().add(new TrackLine(new TrackPoint(10, 40), new TrackPoint(20, 50)));
//        t.getElements().add(new TrackLine(new TrackPoint(20, 50), new TrackPoint(90, 40)));
//        t.getElements().add(new TrackLine(new TrackPoint(90, 40), new TrackPoint(170, 10)));
        t.getElements().add(new LineX(new PointX(10, 40), new PointX(11, 41)));
        t.getElements().add(new LineX(new PointX(11, 41), new PointX(170, 10)));
        return t;
    }

    @Override
    public String toString() {
        return "Track[nElements=" + elements.size() + ", length=" + getLength() + "]";
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
