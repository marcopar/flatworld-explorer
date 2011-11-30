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

import eu.flatworld.commons.log.LogX;
import eu.flatworld.worldexplorer.LittleEndianDataInputStream;
import eu.flatworld.worldexplorer.geometry.RectangleX;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PlacenamesList {

    private List<Placename> placenames;

    public PlacenamesList() {
        placenames = new ArrayList<Placename>();
    }

    public void clear() {
        placenames.clear();
    }

    public void read(File file) throws IOException {
        placenames.addAll(_read(file));
    }

    List<Placename> _read(File file) throws IOException {
        LogX.log(Level.INFO, "Loading placenames file: " + file);
        int level = 0;
        String ss = file.getName().substring(0, 1);
        try {
            level = Integer.parseInt(ss);
        } catch (Exception ex) {
        }
        LittleEndianDataInputStream dis = new LittleEndianDataInputStream(new FileInputStream(file));
        int n = dis.readInt();
        List<Placename> l = new ArrayList<Placename>();
        for (int i = 0; i < n; i++) {
            Placename entry = new Placename();
            entry.read(dis);
            entry.setLevel(level);
            l.add(entry);
        }
        return l;
    }

    public void readAll(String dir) throws IOException {
        File fdir = new File(dir);
        File ff[] = fdir.listFiles();
        for (int i = 0; ff != null && i < ff.length; i++) {
            File f = ff[i];
            if (f.isFile()) {
                try {
                    placenames.addAll(_read(f));
                } catch (IOException ex) {
                    LogX.log(Level.WARNING, "Error reading placenames file: " + f.getAbsolutePath());
                }
            }
        }
    }

    public List<Placename> getPlacenames() {
        return placenames;
    }

    public void setPlacenames(List<Placename> placenames) {
        this.placenames = placenames;
    }

    public List<Placename> getVisiblePlacenames(RectangleX rect, int level) {
        List<Placename> ret = new ArrayList<Placename>();
        for (Placename placename : placenames) {
            //System.err.println(placename.getLevel() + " " + level);
            if (placename.getLevel() <= level && rect.asRectangle2D().contains(placename.getLocation().asPoint2D())) {
                ret.add(placename);
            }
        }
        return ret;
    }
}
