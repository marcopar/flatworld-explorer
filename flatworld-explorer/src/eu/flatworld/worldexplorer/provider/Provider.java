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

package eu.flatworld.worldexplorer.provider;

import eu.flatworld.worldexplorer.tile.Tile;
import java.beans.PropertyChangeListener;

public interface Provider {

    public final static String P_DATAREADY = "provider.dataready";
    public final static String P_IDLE = "provider.idle";
    public final static String P_ERROR = "provider.error";

    public Provider getProvider();

    public void setProvider(Provider provider);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void firePropertyChange(String propertyName, byte oldValue, byte newValue);

    public void firePropertyChange(String propertyName, char oldValue, char newValue);

    public void firePropertyChange(String propertyName, short oldValue, short newValue);

    public void firePropertyChange(String propertyName, long oldValue, long newValue);

    public void firePropertyChange(String propertyName, float oldValue, float newValue);

    public void firePropertyChange(String propertyName, double oldValue, double newValue);

    public Tile getTile(int id, int x, int y, int l) throws Exception;

    public void reset();
}
