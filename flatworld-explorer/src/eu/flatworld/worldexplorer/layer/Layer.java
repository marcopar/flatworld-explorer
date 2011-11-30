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

package eu.flatworld.worldexplorer.layer;

import eu.flatworld.worldexplorer.provider.Provider;

public interface Layer {

    public int getTileWidth();

    public int getTileHeight();

    public int getSurfaceWidth();

    public int getSurfaceHeight();

    public int getMaxLayers();

    public double getZeroLevelAperture();

    public int getID();

    public String getName();

    public Provider getCacheProvider();

    public Provider getDataProvider();

    public Provider getRemoteProvider();

    public void reinitProviders();
}
