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

package eu.flatworld.worldexplorer.tools;

import eu.flatworld.worldexplorer.swing.MapSurface;
import eu.flatworld.worldexplorer.geometry.LineX;
import eu.flatworld.worldexplorer.geometry.PointX;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

public class Hand implements Tool {

    final static float ZOOM_STEP = 0.05f;
    Point pDragStart;
    int dragButton;
    final static Hand instance = new Hand();

    private Hand() {
    }

    public static Tool getInstance() {
        return instance;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!(e.getSource() instanceof MapSurface)) {
            return;
        }
        Point p = e.getPoint();
        MapSurface ms = ((MapSurface) e.getSource());
        if (dragButton == MouseEvent.BUTTON1) {
            Point2D.Double viewport = ms.getViewportLocation();
            viewport.setLocation(viewport.getX() + (pDragStart.x - p.x), viewport.getY() + (pDragStart.y - p.y));
            pDragStart = p;
            Point2D.Double old = ms.getCoordinates();
            ms.updateCoordinates();
            ms.firePropertyChange(MapSurface.P_SURFACE_MOVE, false, true);
            ms.repaint();
        }
        if (dragButton == MouseEvent.BUTTON2) {
            double hh = (pDragStart.getY() - p.getY());
            double nz = -1;
            if (hh > 0) {
                nz = ((MapSurface) e.getSource()).getZoom() + (ZOOM_STEP * ((MapSurface) e.getSource()).getZoom());
            }
            if (hh < 0) {
                nz = ms.getZoom() - (ZOOM_STEP * ms.getZoom());
            }
            if (nz != -1) {
                ms.setZoom(nz);
            }
            pDragStart = p;
        }
        if (dragButton == MouseEvent.BUTTON3) {
            Point2D.Double viewport = ms.getViewportLocation();
            double z = ms.getZoom();
            PointX p1 = ms.toMap((pDragStart.x + viewport.getX()) / z, (pDragStart.y + viewport.getY()) / z);
            PointX p2 = ms.toMap((p.x + viewport.getX()) / z, (p.y + viewport.getY()) / z);

            LineX line = new LineX(p1, p2);
            ms.setMeasureLine(line);
            ms.repaint();
            ms.firePropertyChange(MapSurface.P_SURFACE_MEASURE, -1, line.getLength());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!(e.getSource() instanceof MapSurface)) {
            return;
        }
        Point p = e.getPoint();
        MapSurface ms = ((MapSurface) e.getSource());
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() > 1) {
            ms.setZoom(ms.getZoom() * 2);
        }
        if (e.getButton() == MouseEvent.BUTTON2 && e.getClickCount() > 1) {
            ms.setZoom(Math.pow(2, ms.getLayerNumber()));
        }
        if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() > 1) {
            ms.setZoom(ms.getZoom() / 2);
        }
        if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
            ms.setMeasureLine(null);
            ms.firePropertyChange(MapSurface.P_SURFACE_MEASURE, -1, 0);
            ms.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pDragStart = e.getPoint();
        dragButton = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (!(e.getSource() instanceof MapSurface)) {
            return;
        }
        MapSurface ms = ((MapSurface) e.getSource());

        double hh = e.getWheelRotation();
        double nz = -1;
        nz = ms.getZoom() - (ZOOM_STEP * ms.getZoom()) * hh;

        if (nz != -1) {
            ms.setZoom(nz);
        }
    }
}
