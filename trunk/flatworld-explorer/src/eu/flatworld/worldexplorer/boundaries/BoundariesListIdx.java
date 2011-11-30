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

import eu.flatworld.commons.log.LogX;
import eu.flatworld.worldexplorer.LittleEndianDataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class BoundariesListIdx {

    private ArrayList<BoundariesListIdxEntry> entries;

    public BoundariesListIdx() {
        entries = new ArrayList<BoundariesListIdxEntry>();
    }

    public BoundariesListIdx(String file) throws IOException {
        entries = new ArrayList<BoundariesListIdxEntry>();
        read(file);
    }

    public void read(String file) throws IOException {
        LogX.log(Level.INFO, "Loading boundaries index file: " + file);
        LittleEndianDataInputStream dis = new LittleEndianDataInputStream(new FileInputStream(file));
        int n = dis.readInt();

        for (int i = 0; i < n; i++) {
            BoundariesListIdxEntry entry = new BoundariesListIdxEntry();
            entry.read(dis);
            entries.add(entry);
        }
    }

    @Override
    public String toString() {
        return "PathListIdx[entries.size=" + entries.size() + "]";
    }

    public ArrayList<BoundariesListIdxEntry> getEntries() {
        return entries;
    }
}

