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

package eu.flatworld.worldexplorer.boundaries;

import eu.flatworld.worldexplorer.geometry.RectangleX;
import eu.flatworld.worldexplorer.tracking.Track;
import java.io.IOException;
import java.util.ArrayList;

public class BoundariesList {

    private BoundariesListIdx idx;
    private BoundariesListPkg pkg;

    public BoundariesList(String idxFile, String pkgFile) throws IOException {
        idx = new BoundariesListIdx(idxFile);
        pkg = new BoundariesListPkg(pkgFile, idx.getEntries().size());
    }

    public BoundariesListIdx getIdx() {
        return idx;
    }

    public void setIdx(BoundariesListIdx idx) {
        this.idx = idx;
    }

    public BoundariesListPkg getPkg() {
        return pkg;
    }

    public void setPkg(BoundariesListPkg pkg) {
        this.pkg = pkg;
    }

    public ArrayList<Track> getVisibleBoundaries(RectangleX visibleRect) {
        ArrayList<Track> tracks = new ArrayList<Track>();
        for (BoundariesListIdxEntry elem : idx.getEntries()) {
            if (visibleRect.asRectangle2D().intersects(elem.getArea().asRectangle2D()) ||
                    visibleRect.asRectangle2D().contains(elem.getArea().asRectangle2D())) {
                tracks.add(pkg.getEntries().get(idx.getEntries().indexOf(elem)).getTrack());
            }
        }
        return tracks;
    }
}
