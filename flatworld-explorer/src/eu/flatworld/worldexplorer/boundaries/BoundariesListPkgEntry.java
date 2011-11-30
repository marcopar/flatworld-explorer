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

import eu.flatworld.worldexplorer.LittleEndianDataInputStream;
import eu.flatworld.worldexplorer.geometry.LineX;
import eu.flatworld.worldexplorer.geometry.PointX;
import eu.flatworld.worldexplorer.tracking.Track;
import java.io.IOException;

public class BoundariesListPkgEntry {

    private Track track;

    public BoundariesListPkgEntry() {
        track = new Track();
    }

    void read(LittleEndianDataInputStream dis) throws IOException {
        int n = dis.readInt();
        dis.readByte();
        PointX last = null;
        for (int i = 0; i < n; i++) {
            double latitude = dis.readDouble();
            double longitude = dis.readDouble();
            dis.readShort(); //altitude
            PointX p = new PointX(longitude, latitude, 0);
            if (last == null) {
                last = p;
                continue;
            }
            track.getElements().add(new LineX(last, p));
            last = p;
        }
    }

    @Override
    public String toString() {
        return "[track=" + track + "]";
    }

    public Track getTrack() {
        return track;
    }
}

