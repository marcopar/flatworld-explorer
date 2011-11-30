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

public class BoundariesTest {

    public BoundariesTest() {
    }

    public static void main(String args[]) {
        try {
            BoundariesList pl = new BoundariesList("/shared/wwcache/Data/Earth/Boundaries/World/pathlist.idx", "/shared/wwcache/Data/Earth/Boundaries/World/pathlist.pkg");
            System.out.println(pl.getIdx());
            for (BoundariesListIdxEntry elem : pl.getIdx().getEntries()) {
                System.out.println(elem);
            }
            System.out.println(pl.getPkg());
            for (BoundariesListPkgEntry elem : pl.getPkg().getEntries()) {
            // System.out.println(elem);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

