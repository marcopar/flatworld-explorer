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

package eu.flatworld.worldexplorer.swing;

import eu.flatworld.commons.log.LogX;
import eu.flatworld.worldexplorer.boundaries.BoundariesList;
import eu.flatworld.worldexplorer.layer.DummyLayer;
import eu.flatworld.worldexplorer.layer.Layer;
import eu.flatworld.worldexplorer.provider.FetchAreaSize;
import eu.flatworld.worldexplorer.provider.Provider;
import eu.flatworld.worldexplorer.provider.ProviderChain;
import eu.flatworld.worldexplorer.tile.Tile;
import eu.flatworld.worldexplorer.tools.Hand;
import eu.flatworld.worldexplorer.tools.Tool;
import eu.flatworld.worldexplorer.tracking.Track;
import eu.flatworld.worldexplorer.geometry.LineX;
import eu.flatworld.worldexplorer.geometry.PointX;
import eu.flatworld.worldexplorer.geometry.RectangleX;
import eu.flatworld.worldexplorer.layer.bmng.BMNGLayer;
import eu.flatworld.worldexplorer.placenames.Placename;
import eu.flatworld.worldexplorer.placenames.PlacenamesList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JPanel;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.opengis.referencing.datum.Ellipsoid;

public class MapSurface extends JPanel implements ComponentListener, PropertyChangeListener {

    public final static String P_SURFACE_MOVE = "mapsurface.move";
    public final static String P_SURFACE_ZOOM = "mapsurface.zoom";
    public final static String P_SURFACE_MEASURE = "mapsurface.measure";
    boolean showCenter = true;
    final static Color CENTER_COLOR = new Color(255, 200, 0, 255);
    boolean showGrid = true;
    // grid size in degrees
    int gridSize = 10;
    final static Color GRID_COLOR = new Color(190, 190, 190, 150);
    private boolean showBoundaries = false;
    final static Color BOUNDARIES_COLOR = new Color(255, 200, 0, 200);
    private boolean showPlacenames = false;
    final static Color PLACENAMES_COLOR = new Color(0, 255, 100, 200);
    FetchAreaSize fetchAreaSize = FetchAreaSize.ALL;
    Point2D.Double viewportLocation = new Point2D.Double();
    double zoom = 1;
    int layerNumber;
    Dimension lastDimension;
    ProviderChain providerChain;
    double longitude = 10.9167;
    double latitude = 44.6667;
    Layer layer = new DummyLayer();
    //    TrackManager trackManager;
    BoundariesList boundaries = null;
    public PlacenamesList placenames = null;
    Tool currentTool = Hand.getInstance();
    Ellipsoid ellipsoid = DefaultEllipsoid.WGS84;
    LineX measureLine = null;

    public MapSurface() {
        LogX.log(Level.INFO, "MapSurface starting");

        addComponentListener(this);

        addMouseListener(currentTool);
        addMouseMotionListener(currentTool);
        addMouseWheelListener(currentTool);
        setLayout(null);
        setBackground(new java.awt.Color(0, 0, 0));

    //trackManager = new TrackManager();
    //trackManager.getTracks().add(Track.getTestTrack());
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double nz) {
        //System.out.println(""+nz);
        if (nz < 0.05f) {
            nz = 0.05f;
        }
        double cx = viewportLocation.getX() + getSize().getWidth() / 2;
        double cy = viewportLocation.getY() + getSize().getHeight() / 2;

        cx = cx * nz / zoom;
        cy = cy * nz / zoom;

        viewportLocation.setLocation(cx - getSize().getWidth() / 2, cy - getSize().getHeight() / 2);

        //System.out.println((nz - zoom) + " " + nz);
        double old = zoom;
        zoom = nz;
        //System.out.println(zoom + " " + calculateBestLayer());
        layerNumber = calculateBestLayer();
        firePropertyChange(P_SURFACE_ZOOM, old, zoom);
        //System.out.println(getAperture());
        repaint();
    }

    void paintTiles(Graphics2D g2, ArrayList<Tile> tiles) {
        AffineTransform at = g2.getTransform();
        g2.transform(AffineTransform.getTranslateInstance(-viewportLocation.getX(), -viewportLocation.getY()));

        Iterator<Tile> it = tiles.iterator();
        while (it.hasNext()) {
            Tile mt = it.next();
            if (mt.getImage() != null) {
                AffineTransform ati = g2.getTransform();
                //System.out.println(tiles.size() + " " + mt);
                int l = mt.getL();
                //FIXME fare transform per ogni livello e non per ogni tile
                g2.transform(AffineTransform.getScaleInstance(zoom * (1f / Math.pow(2, l)), zoom * (1f / Math.pow(2, l))));
                mt.draw(g2);
                g2.setTransform(ati);
            } else {

            }
        }
        //System.out.println("***");
        g2.setTransform(at);
    }

    int calculateBestLayer() {
        int n = (int) Math.ceil(Math.log(zoom) / Math.log(2));
        if (n < 0) {
            n = 0;
        }
        if (n > layer.getMaxLayers()) {
            n = layer.getMaxLayers();
        }
        return n;
    }

    int[] getTileAtPoint(double x, double y, int l) {
        int tx = (int) (x / (layer.getTileWidth() / Math.pow(2, l) * zoom));
        int ty = (int) (Math.pow(2, l) * layer.getSurfaceHeight()) - 1 - (int) (y / (layer.getTileHeight() / Math.pow(2, l) * zoom));
        return new int[]{tx, ty};
    }

    void computeLoadArea(int t0[], int t1[]) {
        switch (fetchAreaSize) {
            case SINGLE:
                break;
            case SMALL:
                t0[0] -= 1;
                t1[1] -= 1;
                t1[0] += 1;
                t0[1] += 1;
                break;
            case MEDIUM:
                t0[0] -= 2;
                t1[1] -= 2;
                t1[0] += 2;
                t0[1] += 2;
                break;
            case BIG:
                t0[0] -= 3;
                t1[1] -= 3;
                t1[0] += 3;
                t0[1] += 3;
                break;
        }
    //System.out.println("ccc x from " + t0[0]+ " to " + t1[0] + " y from " + t0[1] + " to " + t1[1]);
    }

    ArrayList<Tile> findTiles(int l) {
        int t0[];
        int t1[];

        if (l == 0 || fetchAreaSize == FetchAreaSize.ALL) {
            t0 = getTileAtPoint(viewportLocation.getX(), viewportLocation.getY(), l);
            t1 = getTileAtPoint(viewportLocation.getX() + getSize().getWidth(), viewportLocation.getY() + getSize().getHeight(), l);
        } else {
            t1 = getTileAtPoint(viewportLocation.getX() + getSize().getWidth() / 2, viewportLocation.getY() + getSize().getHeight() / 2, l);
            t0 = new int[2];
            t0[0] = t1[0];
            t0[1] = t1[1];
            //System.out.println("l=" + l + " x from " + t0[0]+ " to " + t1[0] + " y from " + t0[1] + " to " + t1[1]);
            computeLoadArea(t0, t1);
        }
        //System.out.println("bbb l=" + l + " x from " + t0[0]+ " to " + t1[0] + " y from " + t0[1] + " to " + t1[1]);
        if (t0[0] < 0) {
            t0[0] = 0;
        }
        if (t0[1] < 0) {
            t0[1] = 0;
        }
        if (t0[0] >= Math.pow(2, l) * layer.getSurfaceWidth()) {
            t0[0] = (int) Math.pow(2, l) * layer.getSurfaceWidth() - 1;
        }
        if (t0[1] >= Math.pow(2, l) * layer.getSurfaceHeight()) {
            t0[1] = (int) Math.pow(2, l) * layer.getSurfaceHeight() - 1;
        }
        if (t1[0] < 0) {
            t1[0] = 0;
        }
        if (t1[1] < 0) {
            t1[1] = 0;
        }
        if (t1[0] >= Math.pow(2, l) * layer.getSurfaceWidth()) {
            t1[0] = (int) Math.pow(2, l) * layer.getSurfaceWidth() - 1;
        }
        if (t1[1] >= Math.pow(2, l) * layer.getSurfaceHeight()) {
            t1[1] = (int) Math.pow(2, l) * layer.getSurfaceHeight() - 1;
        }
        //System.out.println("xxx l=" + l + " x from " + t0[0]+ " to " + t1[0] + " y from " + t0[1] + " to " + t1[1]);
        ArrayList<Tile> al = new ArrayList<Tile>();
        for (int x = t0[0]; x <= t1[0]; x++) {
            for (int y = t1[1]; y <= t0[1]; y++) {
                Tile mt;
                try {
                    if (providerChain != null) {
                        //System.out.println("ii");
                        mt = providerChain.getFirstProvider().getTile(layer.getID(), x, y, l);
                        if (mt != null) {
                            al.add(mt);
                        //mt.addPropertyChangeListener(this);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }

        return al;
    }

    ArrayList<Tile> getTiles() {
        //System.out.println(getSize() + " " + getViewportExtent());
        ArrayList<Tile> al = new ArrayList<Tile>();
        for (int i = 0; i <= layerNumber; i++) {
            al.addAll(findTiles(i));
        }
        //al.addAll(findTiles(layerNumber));
        return al;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        ArrayList<Tile> al = getTiles();
        paintTiles(g2, al);
        if (showGrid) {
            paintGrid(g2);
        }
        if (showCenter) {
            paintCenter(g2);
        }

        paintTracks(g2);

        if (showBoundaries && boundaries != null) {
            paintBoundaries(g2);
        }

        if (showPlacenames && placenames != null) {
            paintPlacenames(g2);
        }
    }

    void paintCenter(Graphics2D g2) {
        g2.setColor(CENTER_COLOR);
        g2.drawLine((int) Math.round(getSize().getWidth() / 2 - 10), (int) Math.round(getSize().getHeight() / 2), (int) Math.round(getSize().getWidth() / 2 + 10), (int) Math.round(getSize().getHeight() / 2));
        g2.drawLine((int) Math.round(getSize().getWidth() / 2), (int) Math.round(getSize().getHeight() / 2 - 10), (int) Math.round(getSize().getWidth() / 2), (int) Math.round(getSize().getHeight() / 2 + 10));
    }

    void paintGrid(Graphics2D g2) {
        AffineTransform at = g2.getTransform();
        g2.transform(AffineTransform.getTranslateInstance(-viewportLocation.getX(), -viewportLocation.getY()));
        g2.transform(AffineTransform.getScaleInstance(zoom, zoom));

        Stroke s = g2.getStroke();
        g2.setStroke(new BasicStroke((float) (1 / zoom)));
        g2.setColor(GRID_COLOR);
        for (int i = -180; i <= 180; i += 10) {
            Point2D.Double p1 = toScreen(i, 90);
            Point2D.Double p2 = toScreen(i, -90);
            g2.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
        }
        for (int i = -90; i <= 90; i += 10) {
            Point2D.Double p1 = toScreen(-180, i);
            Point2D.Double p2 = toScreen(+180, i);
            g2.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
        }
        g2.setTransform(at);
        g2.setStroke(s);
    }

    void paintTracks(Graphics2D g2) {
        AffineTransform at = g2.getTransform();
        g2.transform(AffineTransform.getTranslateInstance(-viewportLocation.getX(), -viewportLocation.getY()));
        g2.transform(AffineTransform.getScaleInstance(zoom, zoom));
        Stroke s = g2.getStroke();
        Object antialias = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //        for (Track track : trackManager.getTracks()) {
//            g2.setColor(track.getColor());
//            g2.setStroke(new BasicStroke((float)(5/zoom), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
//            for (LineX line : track.getElements()) {
//                g2.draw(toScreen(line));
//            }
//            g2.setColor(track.getColor());
//            g2.setStroke(new BasicStroke((float)(5/zoom), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
//            for (LineX line : track.getGeodeticCurve().getElements()) {
//                g2.draw(toScreen(line));
//            }
//        }
        if (measureLine != null) {
            g2.setColor(new Color(0, 255, 255, 150));
            g2.setStroke(new BasicStroke((float) (2 / zoom), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
            for (LineX line : measureLine.getGeodeticCurve()) {
                g2.draw(toScreen(line));
            }
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialias);
        g2.setTransform(at);
        g2.setStroke(s);
    }

    void paintBoundaries(Graphics2D g2) {
        AffineTransform at = g2.getTransform();
        g2.transform(AffineTransform.getTranslateInstance(-viewportLocation.getX(), -viewportLocation.getY()));
        g2.transform(AffineTransform.getScaleInstance(zoom, zoom));
        Stroke s = g2.getStroke();
        Object antialias = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RectangleX rect = getMapExtent();
        //        System.err.println(rect);
        g2.setStroke(new BasicStroke((float) (1 / zoom), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        //        g2.setFont(g2.getFont().deriveFont(10/(float)zoom));
        ArrayList<Track> tracks = boundaries.getVisibleBoundaries(rect);
        for (Track track : tracks) {
            g2.setColor(BOUNDARIES_COLOR);
            long skipped = 0;
            Line2D lastLine = null;
            boolean skipping = false;
            for (int i = 0; i < track.getElements().size(); i++) {
                LineX line = track.getElements().get(i);
                Line2D l2 = toScreen(line);
                if (i == 0 || i == track.getElements().size() - 1) {
                    lastLine = null;
                    skipping = false;
                }

                lastLine = null;

                if (lastLine != null) {
                    if (Math.abs(lastLine.getX2() - l2.getX1()) * zoom < 2 && Math.abs(lastLine.getY2() - l2.getY1()) * zoom < 2) {
                        //skip
                        skipped++;
                        skipping = true;
                    } else {
                        if (skipping) {
                            g2.draw(new Line2D.Double(lastLine.getX2(), lastLine.getY2(), l2.getX2(), l2.getY2()));
                        } else {
                            g2.draw(l2);
                        }
                        lastLine = l2;
                        skipping = false;
                    }
                } else {
                    //                    g2.setColor(Color.CYAN);
//                    g2.drawString("" + i, (float)l2.getX1(), (float)l2.getY1());
//                    g2.setColor(track.getColor());
                    g2.draw(l2);
                    lastLine = l2;
                    skipping = false;
                }
            }
        //            System.err.println(track + ": " + skipped + "/" + track.getElements().size());
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialias);
        g2.setTransform(at);
        g2.setStroke(s);
    }

    void paintPlacenames(Graphics2D g2) {
        AffineTransform at = g2.getTransform();
        g2.transform(AffineTransform.getTranslateInstance(-viewportLocation.getX(), -viewportLocation.getY()));
        g2.transform(AffineTransform.getScaleInstance(zoom, zoom));
        Font f = g2.getFont();
        g2.setColor(PLACENAMES_COLOR);
        g2.setFont(f.deriveFont(Font.BOLD, (float) (12 / zoom)));
        Object antialias = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RectangleX rect = getMapExtent();
        int l = 99;
        if (layer instanceof BMNGLayer) {
            l = layerNumber;
        }
        List<Placename> pl = placenames.getVisiblePlacenames(rect, l);
        for (Placename placename : pl) {
            Point2D.Double p = toScreen(placename.getLocation().getLongitude(), placename.getLocation().getLatitude());
            g2.drawString(placename.getName(), (float) p.getX(), (float) p.getY());
        }
        g2.setFont(f);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialias);
        g2.setTransform(at);
    }

    public Point2D.Double toScreen(double longitude, double latitude) {
        double x = (longitude + 180) * layer.getTileWidth() * layer.getSurfaceWidth() / 360;
        double y = (90 - latitude) * layer.getTileHeight() * layer.getSurfaceHeight() / 180;
        return new Point2D.Double(x, y);
    }

    public PointX toMap(double x, double y) {
        double lon = x * 360 / layer.getTileWidth() / layer.getSurfaceWidth() - 180;
        double lat = -(y * 180 / layer.getTileHeight() / layer.getSurfaceHeight() - 90);
        return new PointX(lon, lat);
    }

    public Line2D.Double toScreen(LineX line) {
        double x1 = (line.getP1().getLongitude() + 180) * layer.getTileWidth() * layer.getSurfaceWidth() / 360;
        double y1 = (90 - line.getP1().getLatitude()) * layer.getTileHeight() * layer.getSurfaceHeight() / 180;
        double x2 = (line.getP2().getLongitude() + 180) * layer.getTileWidth() * layer.getSurfaceWidth() / 360;
        double y2 = (90 - line.getP2().getLatitude()) * layer.getTileHeight() * layer.getSurfaceHeight() / 180;
        return new Line2D.Double(x1, y1, x2, y2);
    }

    public LineX toMap(Line2D.Double line) {
        double longitude1 = line.getP1().getX() * 360 / layer.getTileWidth() / layer.getSurfaceWidth() - 180;
        double latitude1 = -(line.getP1().getY() * 180 / layer.getTileHeight() / layer.getSurfaceHeight() - 90);
        double longitude2 = line.getP2().getX() * 360 / layer.getTileWidth() / layer.getSurfaceWidth() - 180;
        double latitude2 = -(line.getP2().getY() * 180 / layer.getTileHeight() / layer.getSurfaceHeight() - 90);
        return new LineX(longitude1, latitude1, longitude2, latitude2);
    }

    public Rectangle2D getViewportExtent() {
        return new Rectangle2D.Double(viewportLocation.getX(), viewportLocation.getY(), getSize().getWidth(), getSize().getHeight());
    }

    public RectangleX getMapExtent() {
        Rectangle2D r2 = getViewportExtent();
        double x1 = r2.getX() * 360 / layer.getTileWidth() / layer.getSurfaceWidth() - 180 * zoom;
        double y1 = -(r2.getY() * 180 / layer.getTileHeight() / layer.getSurfaceHeight() - 90 * zoom);
        double x2 = (r2.getX() + r2.getWidth()) * 360 / layer.getTileWidth() / layer.getSurfaceWidth() - 180 * zoom;
        double y2 = -((r2.getY() + r2.getHeight()) * 180 / layer.getTileHeight() / layer.getSurfaceHeight() - 90 * zoom);

        x1 /= zoom;
        y1 /= zoom;
        x2 /= zoom;
        y2 /= zoom;

        double w = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);
        //        if(w > 360)
//            w = 360;
//        if(h > 180)
//            h = 180;

        double x = x1;
        double y = y1 - h;
        //        if(x > 180)
//            x = 180;
//        if(x < -180)
//            x = -180;
//        if(y > 90)
//            y = 90;
//        if(y < -90)
//            y = -90;
//        if(w > 360)
//            w = 360;
//        if(h > 180)
//            h = 180;
        RectangleX r = new RectangleX(x, y, w, h);
        return r;
    }

    public ProviderChain getProviderChain() {
        return providerChain;
    }

    public Point2D.Double getCoordinates() {
        Point2D.Double p2 = new Point2D.Double(longitude, latitude);
        return p2;
    }

    public void setCoordinadetes(Point2D.Double coordinates) {
        this.longitude = coordinates.getX();
        this.latitude = coordinates.getY();
        updateViewport();
    }

    void updateViewport() {
        double x = longitude * (layer.getTileWidth() * layer.getSurfaceWidth()) / 360d;
        double y = -latitude * (layer.getTileHeight() * layer.getSurfaceHeight()) / 180d;
        x += (layer.getTileWidth() * layer.getSurfaceWidth()) / 2d;
        y += (layer.getTileHeight() * layer.getSurfaceHeight()) / 2d;
        Point2D.Double old = getCoordinates();
        viewportLocation.setLocation(x * zoom - getSize().getWidth() / 2d, y * zoom - getSize().getHeight() / 2d);
        firePropertyChange(P_SURFACE_MOVE, old, getCoordinates());
        repaint();
    }

    public Point2D.Double getViewportLocation() {
        return viewportLocation;
    }

    public void updateCoordinates() {
        double x = (getSize().getWidth() / 2d + viewportLocation.getX()) / zoom;
        double y = (getSize().getHeight() / 2d + viewportLocation.getY()) / zoom;
        x -= (layer.getTileWidth() * layer.getSurfaceWidth()) / 2d;
        y -= (layer.getTileHeight() * layer.getSurfaceHeight()) / 2d;
        longitude = x * 360d / (layer.getTileWidth() * layer.getSurfaceWidth());
        latitude = -y * 180d / (layer.getTileHeight() * layer.getSurfaceHeight());
    }

    public Point2D.Double getAperture() {
        double x = (layer.getTileWidth() / zoom * 360) / (layer.getTileWidth() * layer.getSurfaceWidth());
        double y = (layer.getTileHeight() / zoom * 180) / (layer.getTileHeight() * layer.getSurfaceHeight());
        return new Point2D.Double(x, y);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof Provider) {
            if (evt.getPropertyName().equals(Provider.P_DATAREADY)) {
                repaint();
            }
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateViewport();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    public Layer getLayer() {
        return layer;
    }

    public void switchToLayer(Layer layer, ProviderChain providerChain, double zoom) {
        this.layer = layer;
        this.zoom = zoom;
        if (providerChain != null) {
            providerChain.getFirstProvider().removePropertyChangeListener(this);
        }
        this.providerChain = providerChain;
        providerChain.getFirstProvider().addPropertyChangeListener(this);
        layerNumber = calculateBestLayer();
        updateViewport();
    }

    public boolean isShowCenter() {
        return showCenter;
    }

    public void setShowCenter(boolean showCenter) {
        this.showCenter = showCenter;
        repaint();
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }

    public Ellipsoid getEllipsoid() {
        return ellipsoid;
    }

    public void setEllipsoid(Ellipsoid ellipsoid) {
        this.ellipsoid = ellipsoid;
    }

    public LineX getMeasureLine() {
        return measureLine;
    }

    public void setMeasureLine(LineX measureLine) {
        this.measureLine = measureLine;
    }

    public boolean isShowBoundaries() {
        return showBoundaries;
    }

    public void setShowBoundaries(boolean showBoundaries) {
        this.showBoundaries = showBoundaries;
        repaint();
    }

    public boolean isShowPlacenames() {
        return showPlacenames;
    }

    public void setShowPlacenames(boolean showPlacenames) {
        this.showPlacenames = showPlacenames;
        repaint();
    }

    public BoundariesList getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(BoundariesList boundaries) {
        this.boundaries = boundaries;
    }

    public PlacenamesList getPlacenames() {
        return placenames;
    }

    public void setPlacenames(PlacenamesList placenames) {
        this.placenames = placenames;
    }
}

