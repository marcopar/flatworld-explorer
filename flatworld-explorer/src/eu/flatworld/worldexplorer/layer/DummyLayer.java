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

public class DummyLayer implements Layer {

    public DummyLayer() {
    }

    @Override
    public int getTileWidth() {
        return 10;
    }

    @Override
    public int getTileHeight() {
        return 10;
    }

    @Override
    public int getSurfaceWidth() {
        return 10;
    }

    @Override
    public int getSurfaceHeight() {
        return 10;
    }

    @Override
    public int getMaxLayers() {
        return 1;
    }

    @Override
    public double getZeroLevelAperture() {
        return 0;
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public String getName() {
        return "DummyLayer";
    }

    @Override
    public Provider getCacheProvider() {
        return null;
    }

    @Override
    public Provider getDataProvider() {
        return null;
    }

    @Override
    public Provider getRemoteProvider() {
        return null;
    }

    @Override
    public void reinitProviders() {
    }
}
