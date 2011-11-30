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

import eu.flatworld.worldexplorer.Config;
import java.util.ArrayList;

public class DefaultProviderChain implements ProviderChain {

    protected Provider memoryProvider;
    protected Provider cacheProvider;
    protected Provider dataProvider;
    protected Provider remoteProvider;

    public DefaultProviderChain() {
    }

    public Provider getMemoryProvider() {
        return memoryProvider;
    }

    public synchronized void setMemoryProvider(Provider memoryProvider) {
        this.memoryProvider = memoryProvider;
        setupChain();
    }

    public Provider getCacheProvider() {
        return cacheProvider;
    }

    public synchronized void setCacheProvider(Provider cacheProvider) {
        this.cacheProvider = cacheProvider;
        setupChain();
    }

    public Provider getDataProvider() {
        return dataProvider;
    }

    public synchronized void setDataProvider(Provider dataProvider) {
        this.dataProvider = dataProvider;
        setupChain();
    }

    public Provider getRemoteProvider() {
        return remoteProvider;
    }

    public synchronized void setRemoteProvider(Provider remoteProvider) {
        this.remoteProvider = remoteProvider;
        setupChain();
    }

    synchronized void setupChain() {
        if (memoryProvider != null) {
            if (cacheProvider != null && Config.getConfig().getCacheSize() > 0) {
                memoryProvider.setProvider(cacheProvider);
            } else if (dataProvider != null) {
                memoryProvider.setProvider(dataProvider);
            } else if (remoteProvider != null) {
                memoryProvider.setProvider(remoteProvider);
            }
        }
        if (cacheProvider != null && Config.getConfig().getCacheSize() > 0) {
            if (memoryProvider != null) {
                memoryProvider.setProvider(cacheProvider);
            }
            if (dataProvider != null) {
                cacheProvider.setProvider(dataProvider);
            } else if (remoteProvider != null) {
                cacheProvider.setProvider(remoteProvider);
            }
        }
        if (dataProvider != null) {
            if (cacheProvider != null && Config.getConfig().getCacheSize() > 0) {
                cacheProvider.setProvider(dataProvider);
            } else if (memoryProvider != null) {
                memoryProvider.setProvider(dataProvider);
            }
            if (remoteProvider != null) {
                dataProvider.setProvider(remoteProvider);
            }
        }
        if (remoteProvider != null) {
            if (dataProvider != null) {
                dataProvider.setProvider(remoteProvider);
            } else if (cacheProvider != null && Config.getConfig().getCacheSize() > 0) {
                cacheProvider.setProvider(remoteProvider);
            } else if (memoryProvider != null) {
                memoryProvider.setProvider(remoteProvider);
            }
        }
    }

    @Override
    public Provider getFirstProvider() {
        if (memoryProvider != null) {
            return memoryProvider;
        }
        if (cacheProvider != null && Config.getConfig().getCacheSize() > 0) {
            return cacheProvider;
        }
        if (dataProvider != null) {
            return dataProvider;
        }
        if (remoteProvider != null) {
            return remoteProvider;
        }
        return null;
    }

    @Override
    public ArrayList<Provider> getProviders() {
        ArrayList<Provider> list = new ArrayList<Provider>();
        if (memoryProvider != null) {
            list.add(memoryProvider);
        }
        if (cacheProvider != null && Config.getConfig().getCacheSize() > 0) {
            list.add(cacheProvider);
        }
        if (dataProvider != null) {
            list.add(dataProvider);
        }
        if (remoteProvider != null) {
            list.add(remoteProvider);
        }
        return list;
    }
}
