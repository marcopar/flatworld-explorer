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

package eu.flatworld.worldexplorer.placenames;

import eu.flatworld.worldexplorer.LittleEndianDataInputStream;
import eu.flatworld.worldexplorer.geometry.PointX;
import java.io.IOException;

public class Placename {

    private String name;
    private PointX location;
    private int level = 0;

    public Placename() {
    }

    public void read(LittleEndianDataInputStream dis) throws IOException {
        int n = dis.readByte() & 0xFF;
        byte b[] = new byte[n];
        dis.readFully(b);
        name = new String(b);
        double lat = dis.readFloat();
        double lon = dis.readFloat();
        location = new PointX(lon, lat);
        int meta = dis.readInt();
        for (int i = 0; i < meta; i++) {
            n = dis.readInt();
            dis.skipBytes(n);
            n = dis.readInt();
            dis.skipBytes(n);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PointX getLocation() {
        return location;
    }

    public void setLocation(PointX location) {
        this.location = location;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
