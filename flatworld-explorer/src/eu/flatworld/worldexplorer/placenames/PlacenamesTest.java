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

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlacenamesTest {

    public static void main(String args[]) {
        PlacenamesList pl = new PlacenamesList();
        File f = new File("/opt/wwcache/Data/Earth/Placenames/");
        File ff[] = f.listFiles();
        for (int i = 0; i < ff.length; i++) {
            File file = ff[i];
            if (file.isFile()) {
                try {
                    pl.read(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        System.err.println("" + pl.getPlacenames().size());
        List<Placename> lp = pl.getPlacenames();
        for (Placename placename : lp) {
            System.err.println(placename.getName() + " " + placename.getLocation());
        }
    }
}
