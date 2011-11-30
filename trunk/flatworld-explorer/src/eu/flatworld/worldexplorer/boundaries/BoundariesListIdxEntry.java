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
import eu.flatworld.worldexplorer.geometry.RectangleX;
import java.io.IOException;

public class BoundariesListIdxEntry {
    //String name;
    private RectangleX area;
    //long offset;
    public BoundariesListIdxEntry() {
    }

    void read(LittleEndianDataInputStream dis) throws IOException {
        int n = dis.read() & 0xFF;
        byte b[] = new byte[n];
        dis.readFully(b); //name
        //name = new String(b);
        double west = dis.readDouble();
        double south = dis.readDouble();
        double east = dis.readDouble();
        double north = dis.readDouble();
        //offset = dis.readLong();
        dis.readLong(); //offset

        area = new RectangleX(west, south, Math.abs(east - west), Math.abs(north - south));
//        System.err.println(west + " " + south + " " + east + " " + north);
//        System.err.println(area);
    }

    @Override
    public String toString() {
        return "PathListIdxEntry[area=" + area + "]";
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
    public RectangleX getArea() {
        return area;
    }

    public void setArea(RectangleX area) {
        this.area = area;
    }
}

